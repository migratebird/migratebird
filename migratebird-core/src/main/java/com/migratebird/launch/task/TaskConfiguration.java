/**
 * Copyright 2014 Turgay Kivrak
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
package com.migratebird.launch.task;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.join;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_DATABASE_NAMES;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_DATABASE_START;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_DIALECT_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_DRIVERCLASSNAME_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_INCLUDED_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PASSWORD_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCHEMANAMES_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_URL_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_USERNAME_END;
import static com.migratebird.config.MigratebirdProperties.UNNAMED_DATABASE_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.migratebird.database.DatabaseException;

/**
*/
public class TaskConfiguration {

    private Map<String, DataSource> dataSourcesPerDatabaseName = new HashMap<String, DataSource>();
    private Properties configuration;


    public TaskConfiguration(Properties configuration) {
        this.configuration = configuration;
    }

    public void addAllConfiguration(Properties customConfiguration) {
        if (customConfiguration == null) {
            return;
        }
        configuration.putAll(customConfiguration);
    }

    public void addConfigurationIfSet(String propertyName, String propertyValue) {
        if (propertyValue != null) {
            configuration.put(propertyName, propertyValue);
        }
    }

    public void addConfigurationIfSet(String propertyName, Boolean propertyValue) {
        if (propertyValue != null) {
            configuration.put(propertyName, String.valueOf(propertyValue));
        }
    }

    public void addConfigurationIfSet(String propertyName, Long propertyValue) {
        if (propertyValue != null) {
            configuration.put(propertyName, String.valueOf(propertyValue));
        }
    }

    public void addDatabaseConfigurations(List<? extends DbMaintainDatabase> dbMaintainDatabases) {
        List<String> databaseNames = new ArrayList<String>();

        for (DbMaintainDatabase dbMaintainDatabase : dbMaintainDatabases) {
            String name = dbMaintainDatabase.getName();
            if (isBlank(name)) {
                name = UNNAMED_DATABASE_NAME;
                if (databaseNames.contains(name)) {
                    throw new DatabaseException("Invalid database configuration. More than one unnamed database found.");
                }
                // unnamed database is put as first element so that it becomes the default database
                databaseNames.add(0, name);
            } else {
                if (databaseNames.contains(name)) {
                    throw new DatabaseException("Invalid database configuration. More than one database with name " + name + " found.");
                }
                databaseNames.add(name);
            }
            addDatabaseConfiguration(dbMaintainDatabase, name);

            DataSource dataSource = dbMaintainDatabase.getDataSource();
            if (dataSource != null) {
                dataSourcesPerDatabaseName.put(name, dataSource);
            }
        }
        configuration.put(PROPERTY_DATABASE_NAMES, join(databaseNames, ','));
    }

    protected void addDatabaseConfiguration(DbMaintainDatabase taskDatabase, String name) {
        addConfigurationIfSet(PROPERTY_DATABASE_START + '.' + name + '.' + PROPERTY_DIALECT_END, taskDatabase.getDialect());
        addConfigurationIfSet(PROPERTY_DATABASE_START + '.' + name + '.' + PROPERTY_DRIVERCLASSNAME_END, taskDatabase.getDriverClassName());
        addConfigurationIfSet(PROPERTY_DATABASE_START + '.' + name + '.' + PROPERTY_URL_END, taskDatabase.getUrl());
        addConfigurationIfSet(PROPERTY_DATABASE_START + '.' + name + '.' + PROPERTY_USERNAME_END, taskDatabase.getUserName());
        addConfigurationIfSet(PROPERTY_DATABASE_START + '.' + name + '.' + PROPERTY_PASSWORD_END, taskDatabase.getPassword());
        addConfigurationIfSet(PROPERTY_DATABASE_START + '.' + name + '.' + PROPERTY_SCHEMANAMES_END, taskDatabase.getSchemaNames());
        addConfigurationIfSet(PROPERTY_DATABASE_START + '.' + name + '.' + PROPERTY_INCLUDED_END, taskDatabase.isIncluded());
    }

    public Properties getConfiguration() {
        return configuration;
    }

    public Map<String, DataSource> getDataSourcesPerDatabaseName() {
        return dataSourcesPerDatabaseName;
    }
}
