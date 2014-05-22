/**
 * Copyright 2014 www.migratebird.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.migratebird.config;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.migratebird.util.FileUtils;
import com.migratebird.util.MigrateBirdException;


/**
 * Utility that loads the configuration of MigrateBird.
 *
 */
public class ConfigrationLoader {

    private static Log logger = LogFactory.getLog(ConfigrationLoader.class);

    
    /**
     * Name of the fixed configuration file that contains all defaults
     */
    public static final String DEFAULT_PROPERTIES_FILE_NAME = "migratebird-default.properties";
    
    public static final String MIGRATEBIRD_PROPS_FILE_NAME = "migratebird.properties";
    

    /**
     * Loads all properties as defined by the default configuration.
     *
     * @return the settings, not null
     */
    public Config loadConfiguration() {
        return loadConfiguration((Properties) null);
    }


    /**
     * Loads all properties as defined by the default configuration. Properties defined by the properties
     * file to which the given URL points override the default properties.
     *
     * @param customConfigurationUrl URL that points to the custom configuration, may be null if there is no custom config
     * @return the settings, not null
     */
    public Config loadConfiguration(URL customConfigurationUrl) {
        return loadConfiguration(loadPropertiesFromURL(customConfigurationUrl));
    }

    /**
     * Loads all properties as defined by the default configuration. Properties defined by the properties
     * file to which the given URL points override the default properties.
     *
     * @param customConfigurationFile The custom configuration, may be null if there is no custom config
     * @return the settings, not null
     */
    public Config loadConfiguration(File customConfigurationFile) {
        return loadConfiguration(loadPropertiesFromFile(customConfigurationFile));
    }


    /**
     * Loads all properties as defined by the default configuration. Properties from the given properties
     * object override the default properties.
     *
     * @param customConfiguration custom configuration, may be null if there is no custom config
     * @return the settings, not null
     */
    public Config loadConfiguration(Properties customConfiguration) {
        Properties properties = new Properties();

        properties.putAll(loadDefaultConfiguration());

        if (customConfiguration != null) {
            properties.putAll(customConfiguration);
        }
        return new Config(properties);
    }


    /**
     * Creates and loads the default configuration settings from the {@link #DEFAULT_PROPERTIES_FILE_NAME} file.
     *
     * @return the defaults, not null
     * @throws RuntimeException if the file cannot be found or loaded
     */
    public Properties loadDefaultConfiguration() {
        Properties defaultConfiguration = loadPropertiesFromClasspath(DEFAULT_PROPERTIES_FILE_NAME);
        if (defaultConfiguration == null) {
            throw new MigrateBirdException("Configuration file: " + DEFAULT_PROPERTIES_FILE_NAME + " not found in classpath.");
        }
        return defaultConfiguration;
    }


    protected Properties loadPropertiesFromClasspath(String propertiesFileName) {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
            if (inputStream == null) {
                return null;
            }
            return loadPropertiesFromStream(inputStream);

        } catch (IOException e) {
            throw new MigrateBirdException("Unable to load configuration file: " + propertiesFileName, e);
        } finally {
            closeQuietly(inputStream);
        }
    }

    protected Properties loadPropertiesFromURL(URL propertiesFileUrl) {
        if (propertiesFileUrl == null) {
            return null;
        }
        InputStream urlStream = null;
        try {
            urlStream = propertiesFileUrl.openStream();
            return loadPropertiesFromStream(urlStream);
        } catch (IOException e) {
            throw new MigrateBirdException("Unable to load configuration file " + propertiesFileUrl, e);
        } finally {
            closeQuietly(urlStream);
        }
    }

    protected Properties loadPropertiesFromFile(File propertiesFile) {
        if (propertiesFile == null) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(propertiesFile);
            return loadPropertiesFromStream(inputStream);
        } catch (IOException e) {
            throw new MigrateBirdException("Unable to load configuration file " + propertiesFile, e);
        } finally {
            closeQuietly(inputStream);
        }
    }


    protected Properties loadPropertiesFromStream(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    /**
     * @param fileName The name of the file
     * @return An inputStream giving access to the file with the given name.
     */
    protected static URL getPropertiesAsURL(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new MigrateBirdException("Could not find config file" + MIGRATEBIRD_PROPS_FILE_NAME);
        }
        return FileUtils.getUrl(file);
    }

   

    /**
     * Loads the configuration from custom config file or, if no custom config file was configured, from {@link #MIGRATEBIRD_PROPS_FILE_NAME}, if this file
     * exists. If a custom config file was configured and the config file cannot be found, an error message is printed and execution is ended.
     * 
     * @param customConfigFile Custom config file.
     * @return The configuration as a <code>Properties</code> file
     */
    public  Config load(String customConfigFile) {
        URL propertiesAsURL = null;
        if (customConfigFile == null) {
            if (new File(MIGRATEBIRD_PROPS_FILE_NAME).exists()) {
                propertiesAsURL = getPropertiesAsURL(MIGRATEBIRD_PROPS_FILE_NAME);
                logger.info("Loaded configuration from file " + MIGRATEBIRD_PROPS_FILE_NAME);
            }
        } else {
            propertiesAsURL = getPropertiesAsURL(customConfigFile);
            logger.info("Loaded configuration from file " + customConfigFile);
        }
        return new ConfigrationLoader().loadConfiguration(propertiesAsURL);
    }

}
