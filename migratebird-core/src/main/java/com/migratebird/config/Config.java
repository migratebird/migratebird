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

import static com.migratebird.config.MigratebirdProperties.PROPERTY_AUTO_CREATE_MIGRATEBIRD_SCRIPTS_TABLE;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_BACKSLASH_ESCAPING_ENABLED;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_BASELINE_REVISION;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_CHECKSUM_COLUMN_NAME;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_CHECKSUM_COLUMN_SIZE;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_CLEANDB;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_DATABASE_NAMES;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_DATABASE_START;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_DB2_COMMAND;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_DISABLE_CONSTRAINTS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_EXCLUDED_QUALIFIERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_EXECUTED_AT_COLUMN_NAME;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_EXECUTED_AT_COLUMN_SIZE;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_EXECUTED_SCRIPTS_TABLE_NAME;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_FILE_LAST_MODIFIED_AT_COLUMN_NAME;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_FILE_NAME_COLUMN_NAME;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_FILE_NAME_COLUMN_SIZE;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_FROM_SCRATCH_ENABLED;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_IDENTIFIER_QUOTE_STRING;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_IGNORE_CARRIAGE_RETURN_WHEN_CALCULATING_CHECK_SUM;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_INCLUDED_QUALIFIERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_LOWEST_ACCEPTABLE_SEQUENCE_VALUE;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_MAX_NR_CHARS_WHEN_LOGGING_SCRIPT_CONTENT;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PATCH_ALLOWOUTOFSEQUENCEEXECUTION;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_POSTPROCESSINGSCRIPT_DIRNAME;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_QUALIFIERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_ENCODING;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_FILE_EXTENSIONS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_INDEX_REGEXP;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_LOCATIONS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_PARAMETER_FILE;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_PATCH_QUALIFIERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_QUALIFIER_REGEXP;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_TARGETDATABASE_REGEXP;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SQL_PLUS_COMMAND;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_STORED_IDENTIFIER_CASE;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SUCCEEDED_COLUMN_NAME;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_TIMESTAMP_FORMAT;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_UPDATE_SEQUENCES;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_USESCRIPTFILELASTMODIFICATIONDATES;
import static com.migratebird.database.StoredIdentifierCase.LOWER_CASE;
import static com.migratebird.database.StoredIdentifierCase.MIXED_CASE;
import static com.migratebird.database.StoredIdentifierCase.UPPER_CASE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.migratebird.database.DatabaseException;
import com.migratebird.database.StoredIdentifierCase;
import com.migratebird.util.MigrateBirdException;
import com.migratebird.util.ReflectionUtils;

public class Config {

    private Properties properties;

    public Config(Properties properties) {
        super();
        this.properties = properties;
    }

    // TODO change it
    public Properties getProps() {
        return properties;
    }

    public String getBaselineRevision() {
        return getString(PROPERTY_BASELINE_REVISION, null);
    }

    /**
     * Gets the string value for the property with the given name. If no such property is found or the value is empty, the given default value is
     * returned.
     * 
     * @param propertyName The name, not null
     * @param defaultValue The default value
     * @param properties The properties, not null
     * @return The trimmed string value, not null
     */
    private String getString(String propertyName, String defaultValue) {
        String value = getProperty(propertyName);
        if (value == null || "".equals(value.trim())) {
            return defaultValue;
        }
        return value.trim();
    }

    private String getProperty(String propertyName) {
        String value = System.getProperty(propertyName);
        if (value == null) {
            value = properties.getProperty(propertyName);
        }
        return value;
    }

    /**
     * Gets the string value for the property with the given name. If no such property is found or the value is empty, an exception will be raised.
     * 
     * @param propertyName The name, not null
     * @param properties The properties, not null
     * @return The trimmed string value, not null
     */
    public String getString(String propertyName) {
        String value = getProperty(propertyName);
        if (value == null || "".equals(value.trim())) {
            throw new MigrateBirdException("No value found for property " + propertyName);
        }
        return value.trim();
    }

    /**
     * Gets the list of comma separated string values for the property with the given name. If no such property is found or the value is empty, an
     * empty list is returned. Empty elements (",,") will not be added. A space (", ,") is not empty, a "" will be added.
     * 
     * @param propertyName The name, not null
     * @param properties The properties, not null
     * @return The trimmed string list, empty if none found
     */
    public List<String> getStringList(String propertyName) {
        return getStringList(propertyName, false);

    }

    /**
     * Gets the list of comma separated string values for the property with the given name. If no such property is found or the value is empty, an
     * empty list is returned if not required, else an exception is raised. Empty elements (",,") will not be added. A space (", ,") is not empty, a
     * "" will be added.
     * 
     * @param propertyName The name, not null
     * @param properties The properties, not null
     * @param required If true an exception will be raised when the property is not found or empty
     * @return The trimmed string list, empty or exception if none found
     */
    private List<String> getStringList(String propertyName, boolean required) {
        String values = getProperty(propertyName);
        if (values == null || "".equals(values.trim())) {
            if (required) {
                throw new MigrateBirdException("No value found for property " + propertyName);
            }
            return new ArrayList<String>(0);
        }
        String[] splitValues = StringUtils.split(values, ",");
        List<String> result = new ArrayList<String>(splitValues.length);
        for (String value : splitValues) {
            result.add(value.trim());
        }

        if (required && result.isEmpty()) {
            throw new MigrateBirdException("No value found for property " + propertyName);
        }
        return result;
    }

    public List<String> getDatabaseNames() {
        return getStringList(PROPERTY_DATABASE_NAMES);
    }

    public String getProperty(String databaseName, String propertyNameEnd) {
        if (databaseName != null) {
            String customPropertyName = PROPERTY_DATABASE_START + '.' + databaseName + '.' + propertyNameEnd;
            if (containsProperty(customPropertyName)) {
                return getString(customPropertyName, null);
            }
        }
        String defaultPropertyName = PROPERTY_DATABASE_START + '.' + propertyNameEnd;
        return getString(defaultPropertyName, null);
    }

    /**
     * Gets the int value for the property with the given name. If no such property is found, the value is empty or cannot be converted to a long, an
     * exception will be raised.
     * 
     * @param propertyName The name, not null
     * @return The int value, not null
     */
    private int getInt(String propertyName) {
        String value = getProperty(propertyName);
        if (value == null || "".equals(value.trim())) {
            throw new MigrateBirdException("No value found for property " + propertyName);
        }
        try {
            return Integer.valueOf(value.trim());

        } catch (NumberFormatException e) {
            throw new MigrateBirdException("Value " + value + " for property " + propertyName + " is not a number.");
        }
    }

    /**
     * Checks whether the property with the given name exists in the System or in the given properties.
     * 
     * @param propertyName The property name, not null
     * @param properties The properties if not found in System, not null
     * @return True if the property exitsts
     */
    public boolean containsProperty(String propertyName) {
        return getProperty(propertyName) != null;
    }

    /**
     * Gets the boolean value for the property with the given name. If no such property is found or the value is empty, the given default value is
     * returned.
     * 
     * @param propertyName The name, not null
     * @param defaultValue The default value
     * @param properties The properties, not null
     * @return The boolean value, not null
     */
    public boolean getBoolean(String propertyName, boolean defaultValue) {
        String value = getProperty(propertyName);
        if (value == null || "".equals(value.trim())) {
            return defaultValue;
        }
        return Boolean.valueOf(value.trim());
    }

    public List<String> getListDatabaseProperty(String databaseName, String propertyNameEnd) {
        if (databaseName != null) {
            String customPropertyName = PROPERTY_DATABASE_START + '.' + databaseName + '.' + propertyNameEnd;
            if (containsProperty(customPropertyName)) {
                return getStringList(customPropertyName, false);
            }
        }
        String defaultPropertyName = PROPERTY_DATABASE_START + '.' + propertyNameEnd;
        return getStringList(defaultPropertyName, false);
    }

    public StoredIdentifierCase getCustomStoredIdentifierCase(String databaseDialect) {
        String storedIdentifierCasePropertyValue = getString(PROPERTY_STORED_IDENTIFIER_CASE + "." + databaseDialect, "auto");
        if ("lower_case".equals(storedIdentifierCasePropertyValue)) {
            return LOWER_CASE;
        } else if ("upper_case".equals(storedIdentifierCasePropertyValue)) {
            return UPPER_CASE;
        } else if ("mixed_case".equals(storedIdentifierCasePropertyValue)) {
            return MIXED_CASE;
        } else if ("auto".equals(storedIdentifierCasePropertyValue)) {
            return null;
        }
        throw new DatabaseException("Unable to determine stored identifier case. Unknown value " + storedIdentifierCasePropertyValue
                + " for property " + PROPERTY_STORED_IDENTIFIER_CASE + ". It should be one of lower_case, upper_case, mixed_case or auto.");
    }

    public String getCustomIdentifierQuoteString(String databaseDialect) {
        String identifierQuoteStringPropertyValue = getString(PROPERTY_IDENTIFIER_QUOTE_STRING + '.' + databaseDialect, "auto");
        if ("none".equals(identifierQuoteStringPropertyValue)) {
            return "";
        }
        if ("auto".equals(identifierQuoteStringPropertyValue)) {
            return null;
        }
        return identifierQuoteStringPropertyValue;
    }

    /**
     * Gets the boolean value for the property with the given name. If no such property is found or the value is empty, an exception will be raised.
     * 
     * @param propertyName The name, not null
     * @param properties The properties, not null
     * @return The boolean value, not null
     */
    public boolean getBoolean(String propertyName) {
        String value = getProperty(propertyName);
        if (value == null || "".equals(value.trim())) {
            throw new MigrateBirdException("No value found for property " + propertyName);
        }
        return Boolean.valueOf(value.trim());
    }

    /**
     * Gets the long value for the property with the given name. If no such property is found, the value is empty or cannot be converted to a long, an
     * exception will be raised.
     * 
     * @param propertyName The name, not null
     * @param properties The properties, not null
     * @return The long value, not null
     */
    public long getLong(String propertyName) {
        String value = getProperty(propertyName);
        if (value == null || "".equals(value.trim())) {
            throw new MigrateBirdException("No value found for property " + propertyName);
        }
        try {
            return Long.valueOf(value.trim());

        } catch (NumberFormatException e) {
            throw new MigrateBirdException("Value " + value + " for property " + propertyName + " is not a number.");
        }
    }

    public Set<String> getScriptLocations() {
        return new HashSet<String>(getStringList(PROPERTY_SCRIPT_LOCATIONS));
    }

    public String getScriptEncoding() {
        return getString(PROPERTY_SCRIPT_ENCODING);
    }

    public String getPostProcessingScriptDirName() {
        return getString(PROPERTY_POSTPROCESSINGSCRIPT_DIRNAME);
    }

    public List<String> getQualifierNames() {
        return getStringList(PROPERTY_QUALIFIERS);
    }

    public List<String> getScriptPatchQualifierNames() {
        return getStringList(PROPERTY_SCRIPT_PATCH_QUALIFIERS);
    }

    public String getScriptIndexRegexp() {
        return getString(PROPERTY_SCRIPT_INDEX_REGEXP);
    }

    public String getScriptQualifierRegexp() {
        return getString(PROPERTY_SCRIPT_QUALIFIER_REGEXP);
    }

    public String getScriptTargetDatabaseRegexp() {
        return getString(PROPERTY_SCRIPT_TARGETDATABASE_REGEXP);
    }

    public Set<String> getScriptFileExtensions() {
        return new HashSet<String>(getStringList(PROPERTY_SCRIPT_FILE_EXTENSIONS));
    }

    public boolean getIgnoreCarriageReturnsWhenCalculatingCheckSum() {
        return getBoolean(PROPERTY_IGNORE_CARRIAGE_RETURN_WHEN_CALCULATING_CHECK_SUM);
    }

    public List<String> getIncludedQualifierNames() {
        return getStringList(PROPERTY_INCLUDED_QUALIFIERS, false);
    }

    public List<String> getExcludedQualifierNames() {
        return getStringList(PROPERTY_EXCLUDED_QUALIFIERS, false);
    }

    public List<String> getRegisteredQualifierNames() {
        return getStringList(PROPERTY_QUALIFIERS);
    }

    public String getExecutedScriptsTableName() {
        return getString(PROPERTY_EXECUTED_SCRIPTS_TABLE_NAME);
    }

    public boolean getBackSlashEscapingEnabled() {
        return getBoolean(PROPERTY_BACKSLASH_ESCAPING_ENABLED);
    }

    public String getScriptParameterFile() {
        return getString(PROPERTY_SCRIPT_PARAMETER_FILE, null);
    }

    public boolean getCleanDb() {
        return getBoolean(PROPERTY_CLEANDB);
    }

    public boolean getFromScratchEnabled() {
        return getBoolean(PROPERTY_FROM_SCRATCH_ENABLED);
    }

    public boolean getUseScriptFileLastModificationDates() {
        return getBoolean(PROPERTY_USESCRIPTFILELASTMODIFICATIONDATES);
    }

    public boolean getPatchAllowOutOfSeqExecution() {
        return getBoolean(PROPERTY_PATCH_ALLOWOUTOFSEQUENCEEXECUTION);
    }

    public boolean getDisableConstraints() {
        return getBoolean(PROPERTY_DISABLE_CONSTRAINTS);
    }

    public boolean getUpdateSequencesEnabled() {
        return getBoolean(PROPERTY_UPDATE_SEQUENCES);
    }

    public long getMaxNumCharsWhenLoggingScriptContent() {
        return getLong(PROPERTY_MAX_NR_CHARS_WHEN_LOGGING_SCRIPT_CONTENT);
    }

    public boolean getAutoCreateDatabaseLogTable() {
        return getBoolean(PROPERTY_AUTO_CREATE_MIGRATEBIRD_SCRIPTS_TABLE);
    }

    public String getFileNameColName() {
        return getString(PROPERTY_FILE_NAME_COLUMN_NAME);
    }

    public String getFileLastModifiedAtColumnName() {
        return getString(PROPERTY_FILE_LAST_MODIFIED_AT_COLUMN_NAME);
    }

    public String getCheckSumColumnName() {
        return getString(PROPERTY_CHECKSUM_COLUMN_NAME);
    }

    public int getFileNameColSize() {
        return getInt(PROPERTY_FILE_NAME_COLUMN_SIZE);
    }

    public int getCheckSumColSize() {
        return getInt(PROPERTY_CHECKSUM_COLUMN_SIZE);
    }

    public int getExecutedAtColSize() {
        return getInt(PROPERTY_EXECUTED_AT_COLUMN_SIZE);
    }

    public String getExecutedAtColName() {
        return getString(PROPERTY_EXECUTED_AT_COLUMN_NAME);
    }

    public String getSucceededColName() {
        return getString(PROPERTY_SUCCEEDED_COLUMN_NAME);
    }

    public DateFormat getTimestampFormat() {
        return new SimpleDateFormat(getString(PROPERTY_TIMESTAMP_FORMAT));
    }

    public boolean isEmpty() {
        return properties == null || properties.isEmpty();
    }

    // TOOD how to override props!
    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public long getLowestAcceptableSequenceValue() {
        return getLong(PROPERTY_LOWEST_ACCEPTABLE_SEQUENCE_VALUE);
    }

    public String getDb2Command() {
        return getString(PROPERTY_DB2_COMMAND);
    }

    public String getSqpPlusCommand() {
        return getString(PROPERTY_SQL_PLUS_COMMAND);
    }

    public void updateScriptLocations(String scriptLocations) {
        properties.put(MigratebirdProperties.PROPERTY_SCRIPT_LOCATIONS, scriptLocations);
    }
    
    
    /**config utils **/

    /**
     * Retrieves the concrete instance of the class with the given type as configured by the given <code>Configuration</code>.
     * Tries to retrieve a specific implementation first (propery key = fully qualified name of the interface
     * type + '.impl.className.' + implementationDiscriminatorValue). If this key does not exist, the generally configured
     * instance is retrieved (same property key without the implementationDiscriminatorValue).
     *
     * @param type          The type of the instance
     * @param configuration The configuration containing the necessary properties for configuring the instance
     * @param implementationDiscriminatorValues
     *                      The values that define which specific implementation class should be used.
     *                      This is typically an environment specific property, like the DBMS that is used.
     * @return The configured class name
     */
    public <T> Class<T> getConfiguredClass(Class<T> type, String... implementationDiscriminatorValues) {
        String className = getConfiguredClassName(type, "implClassName", implementationDiscriminatorValues);
        return ReflectionUtils.getClassWithName(className);
    }

    public <T> Class<T> getFactoryClass(Class<T> type, String... implementationDiscriminatorValues) {
        String className = getConfiguredClassName(type, "factory", implementationDiscriminatorValues);
        return ReflectionUtils.getClassWithName(className);
    }


    /**
     * Retrieves the class name of the concrete instance of the class with the given type as configured by the given <code>Configuration</code>.
     * Tries to retrieve a specific implementation first (propery key = fully qualified name of the interface
     * 'type'.'propertyName'.'implementationDiscriminatorValue'). If this key does not exist, the generally configured
     * instance is retrieved (same property key without the implementationDiscriminatorValue).
     *
     * @param type          The type of the instance
     * @param propertyName  The name to add to the type, not null
     * @param implementationDiscriminatorValues
     *                      The values that define which specific implementation class should be used.
     *                      This is typically an environment specific property, like the DBMS that is used.
     * @return The configured class name, not null
     */
    private String getConfiguredClassName(Class<?> type, String propertyName, String... implementationDiscriminatorValues) {
        String propKey = type.getName() + "." + propertyName;

        // first try specific instance using the given discriminators
        if (implementationDiscriminatorValues != null) {
            String implementationSpecificPropKey = propKey;
            for (String implementationDiscriminatorValue : implementationDiscriminatorValues) {
                implementationSpecificPropKey += '.' + implementationDiscriminatorValue;
            }
            if (containsKey(implementationSpecificPropKey)) {
                return getString(implementationSpecificPropKey);
            }
        }

        // specifig not found, try general configured instance
        if (containsKey(propKey)) {
            return getString(propKey);
        }

        // no configuration found
        throw new MigrateBirdException("Missing configuration for " + propKey);
    }

}
