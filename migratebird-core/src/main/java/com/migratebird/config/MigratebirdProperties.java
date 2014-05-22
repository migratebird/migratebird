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

//TODO Split by properties?
public class MigratebirdProperties {

    /**
     * The database name that maven/ant/spring tasks will use if no name was specified
     */
    public static final String UNNAMED_DATABASE_NAME = "<unnamed>";

    /**
     * Logical names for the databases. This property is only required if there's more than one database configured.
     */
    public static final String PROPERTY_DATABASE_NAMES = "databases.names";

    /**
     * Start of all property names that concern the configuration of a database
     */
    public static final String PROPERTY_DATABASE_START = "database";

    /**
     * End of property names that indicate a database driver classname
     */
    public static final String PROPERTY_DRIVERCLASSNAME_END = "driverClassName";
    public static final String PROPERTY_DRIVERCLASSNAME = PROPERTY_DATABASE_START + '.' + PROPERTY_DRIVERCLASSNAME_END;

    /**
     * End of property names that indicate a database url
     */
    public static final String PROPERTY_URL_END = "url";
    public static final String PROPERTY_URL = PROPERTY_DATABASE_START + '.' + PROPERTY_URL_END;

    /**
     * End of property names that indicate a database username
     */
    public static final String PROPERTY_USERNAME_END = "userName";
    public static final String PROPERTY_USERNAME = PROPERTY_DATABASE_START + '.' + PROPERTY_USERNAME_END;

    /**
     * End of property names that indicate a database password
     */
    public static final String PROPERTY_PASSWORD_END = "password";
    public static final String PROPERTY_PASSWORD = PROPERTY_DATABASE_START + '.' + PROPERTY_PASSWORD_END;

    /**
     * End of property names that indicate whether the database is included: i.e. it is active and must be updated
     */
    public static final String PROPERTY_INCLUDED_END = "included";
    public static final String PROPERTY_INCLUDED = PROPERTY_DATABASE_START + '.' + PROPERTY_INCLUDED_END;

    /**
     * Property key of the SQL dialect of the underlying DBMS implementation
     */
    public static final String PROPERTY_DIALECT_END = "dialect";
    public static final String PROPERTY_DIALECT = PROPERTY_DATABASE_START + '.' + PROPERTY_DIALECT_END;

    /**
     * Property key for the database schema names
     */
    public static final String PROPERTY_SCHEMANAMES_END = "schemaNames";
    public static final String PROPERTY_SCHEMANAMES = PROPERTY_DATABASE_START + '.' + PROPERTY_SCHEMANAMES_END;

    /**
     * Property key for the default identifier casing (lower_case, upper_case,
     * mixed_case, auto)
     */
    public static final String PROPERTY_STORED_IDENTIFIER_CASE = "database.storedIndentifierCase";

    /**
     * Property key for the default identifier quote string (empty value for not
     * supported, auto)
     */
    public static final String PROPERTY_IDENTIFIER_QUOTE_STRING = "database.identifierQuoteString";

    /**
     * Property indicating if deleting all data from all tables before updating is enabled
     */
    public static final String PROPERTY_CLEANDB = "migratebird.cleanDb";

    /**
     * Property indicating if updating the database from scratch is enabled
     */
    public static final String PROPERTY_FROM_SCRATCH_ENABLED = "migratebird.fromScratch.enabled";

    /**
     * Property indicating if the database constraints should org disabled after updating the database
     */
    public static final String PROPERTY_DISABLE_CONSTRAINTS = "migratebird.disableConstraints";

    /**
     * Property indicating if the database constraints should org disabled after updating the database
     */
    public static final String PROPERTY_UPDATE_SEQUENCES = "migratebird.updateSequences";

    /**
     * Property for the maximum nr of chars of the script content to log when exception occurs, 0 to not log any script content
     */
    public static final String PROPERTY_MAX_NR_CHARS_WHEN_LOGGING_SCRIPT_CONTENT = "migratebird.maxNrOfCharsWhenLoggingScriptContent";

    /**
     * Property key for the lowest acceptable sequence value
     */
    public static final String PROPERTY_LOWEST_ACCEPTABLE_SEQUENCE_VALUE = "sequenceUpdater.lowestAcceptableSequenceValue";

    /**
     * The key of the property that specifies of which schemas nothing should be dropped
     */
    public static final String PROPERTY_PRESERVE_SCHEMAS = "migratebird.preserve.schemas";

    /**
     * The key of the property that specifies which tables should not be dropped
     */
    public static final String PROPERTY_PRESERVE_TABLES = "migratebird.preserve.tables";

    /**
     * The key of the property that specifies which views should not be dropped
     */
    public static final String PROPERTY_PRESERVE_VIEWS = "migratebird.preserve.views";

    /**
     * The key of the property that specifies which materialized views should not be dropped
     */
    public static final String PROPERTY_PRESERVE_MATERIALIZED_VIEWS = "migratebird.preserve.materializedViews";

    /**
     * The key of the property that specifies which synonyms should not be dropped
     */
    public static final String PROPERTY_PRESERVE_SYNONYMS = "migratebird.preserve.synonyms";

    /**
     * The key of the property that specifies which sequences should not be dropped
     */
    public static final String PROPERTY_PRESERVE_SEQUENCES = "migratebird.preserve.sequences";

    /**
     * The key of the property that specifies which triggers should not be dropped
     */
    public static final String PROPERTY_PRESERVE_TRIGGERS = "migratebird.preserve.triggers";

    /**
     * The key of the property that specifies which types should not be dropped
     */
    public static final String PROPERTY_PRESERVE_TYPES = "migratebird.preserve.types";

    /**
     * Property key for schemas in which none of the tables should be cleaned
     */
    public static final String PROPERTY_PRESERVE_DATA_SCHEMAS = "migratebird.preserveDataOnly.schemas";

    /**
     * Property key for the tables that should not be cleaned
     */
    public static final String PROPERTY_PRESERVE_DATA_TABLES = "migratebird.preserveDataOnly.tables";

    /* The key of the property that specifies the database table in which the DB version is stored */
    public static final String PROPERTY_EXECUTED_SCRIPTS_TABLE_NAME = "migratebird.executedScriptsTableName";

    /* The key of the property that specifies the column in which the script filenames are stored */
    public static final String PROPERTY_FILE_NAME_COLUMN_NAME = "migratebird.fileNameColumnName";
    public static final String PROPERTY_FILE_NAME_COLUMN_SIZE = "migratebird.fileNameColumnSize";

    /* The key of the property that specifies the column in which the last modification timestamp is stored */
    public static final String PROPERTY_FILE_LAST_MODIFIED_AT_COLUMN_NAME = "migratebird.fileLastModifiedAtColumnName";

    /* The key of the property that specifies the column in which the last modification timestamp is stored */
    public static final String PROPERTY_CHECKSUM_COLUMN_NAME = "migratebird.checksumColumnName";
    public static final String PROPERTY_CHECKSUM_COLUMN_SIZE = "migratebird.checksumColumnSize";

    /* The key of the property that specifies the column in which is stored whether the last update succeeded. */
    public static final String PROPERTY_EXECUTED_AT_COLUMN_NAME = "migratebird.executedAtColumnName";
    public static final String PROPERTY_EXECUTED_AT_COLUMN_SIZE = "migratebird.executedAtColumnSize";

    /* The key of the property that specifies the column in which is stored whether the last update succeeded. */
    public static final String PROPERTY_SUCCEEDED_COLUMN_NAME = "migratebird.succeededColumnName";

    /* The key of the property that specifies whether the executed scripts table should be created automatically. */
    public static final String PROPERTY_AUTO_CREATE_MIGRATEBIRD_SCRIPTS_TABLE = "migratebird.autoCreateMigrateBirdScriptsTable";

    public static final String PROPERTY_TIMESTAMP_FORMAT = "migratebird.timestampFormat";

    /**
     * Property key for the directory in which the script files are located
     */
    public static final String PROPERTY_SCRIPT_LOCATIONS = "migratebird.script.locations";

    /**
     * Property key for the extension of the script files
     */
    public static final String PROPERTY_SCRIPT_FILE_EXTENSIONS = "migratebird.script.fileExtensions";

    public static final String PROPERTY_IGNORE_CARRIAGE_RETURN_WHEN_CALCULATING_CHECK_SUM = "migratebird.script.ignoreCarriageReturnsWhenCalculatingCheckSum";

    /**
     * Property key for the directory in which the code script files are located
     */
    public static final String PROPERTY_POSTPROCESSINGSCRIPT_DIRNAME = "migratebird.postProcessingScript.directoryName";

    /**
     * Property key for the patch indicator. I.e. the keyword to use in the filename to indicate that the script is a patch script.
     */
    public static final String PROPERTY_SCRIPT_PATCH_QUALIFIERS = "migratebird.script.patch.qualifiers";

    /**
     * The key for the property that specifies that patch scripts can be executed out of sequence
     */
    public static final String PROPERTY_PATCH_ALLOWOUTOFSEQUENCEEXECUTION = "migratebird.allowOutOfSequenceExecutionOfPatches";

    public static final String PROPERTY_USESCRIPTFILELASTMODIFICATIONDATES = "migratebird.useScriptFileLastModificationDates";

    public static final String PROPERTY_SCRIPT_ENCODING = "migratebird.script.encoding";

    public static final String PROPERTY_SCRIPT_INDEX_REGEXP = "migratebird.script.index.regexp";
    public static final String PROPERTY_SCRIPT_TARGETDATABASE_REGEXP = "migratebird.script.targetDatabase.regexp";
    public static final String PROPERTY_SCRIPT_QUALIFIER_REGEXP = "migratebird.script.qualifier.regexp";

    public static final String PROPERTY_BACKSLASH_ESCAPING_ENABLED = "migratebird.script.backSlashEscapingEnabled";

    public static final String PROPERTY_QUALIFIERS = "migratebird.qualifiers";

    public static final String PROPERTY_EXCLUDED_QUALIFIERS = "migratebird.excludedQualifiers";

    public static final String PROPERTY_INCLUDED_QUALIFIERS = "migratebird.includedQualifiers";

    public static final String PROPERTY_SQL_PLUS_COMMAND = "migratebird.sqlPlusScriptRunner.sqlPlusCommand";

    public static final String PROPERTY_DB2_COMMAND = "migratebird.db2ScriptRunner.db2Command";

    public static final String PROPERTY_BASELINE_REVISION = "migratebird.baseline.revision";

    public static final String PROPERTY_SCRIPT_PARAMETER_FILE = "migratebird.scriptParameterFile";

    /**
     * Private constructor to prevent instantiation
     */
    private MigratebirdProperties() {
    }
}
