package com.migratebird.database;

import java.util.Set;

import javax.sql.DataSource;



public interface Database {

    String getDatabaseName();

    String getSupportedDatabaseDialect();

    String getDefaultSchemaName();

    StoredIdentifierCase getStoredIdentifierCase();

    boolean isQuoted(String schemaName);

    String toCorrectCaseIdentifier(String schemaName);

    DataSource getDataSource();

    Set<String> getTableNames(String schemaName);

    Set<String> getSchemaNames();

    String qualified(String schemaName, String tableName);

    boolean supportsSequences();

    boolean supportsSynonyms();

    DatabaseInfo getDatabaseInfo();

    boolean supportsTypes();

    Set<String> getTypeNames(String schemaName);

    Set<String> getViewNames(String schemaName);

    void dropTable(String schemaName, String tableName);

    void dropView(String schemaName, String viewName);

    boolean supportsMaterializedViews();

    Set<String> getMaterializedViewNames(String schemaName);

    void dropMaterializedView(String schemaName, String materializedViewName);

    Set<String> getSynonymNames(String schemaName);

    void dropSynonym(String schemaName, String synonymName);

    Set<String> getSequenceNames(String schemaName);

    void dropSequence(String schemaName, String sequenceName);

    boolean supportsTriggers();

    Set<String> getTriggerNames(String schemaName);

    void dropTrigger(String schemaName, String triggerName);

    boolean supportsStoredProcedures();

    Set<String> getStoredProcedureNames(String schemaName);

    void dropStoredProcedure(String schemaName, String storedProcedureName);

    void dropType(String schemaName, String typeName);

    boolean supportsRules();

    void dropRule(String schemaName, String ruleName);

    Set<String> getRuleNames(String schemaName);

    Set<String> getColumnNames(String defaultSchemaName, String executedScriptsTableName);

    String getLongDataType();

    String getTextDataType(int fileNameColumnSize);

    String quoted(String string);

    Set<String> getSynonymNames();

    Set<String> getViewNames();

    Set<String> getTableNames();

    Set<String> getMaterializedViewNames();

    Set<String> getSequenceNames();

    void disableValueConstraints(String schemaName);

    void disableReferentialConstraints(String schemaName);

    void incrementIdentityColumnToValue(String schemaName, String tableName, String identityColumnName, long lowestAcceptableSequenceValue);

    Set<String> getIdentityColumnNames(String schemaName, String tableName);

    boolean supportsIdentityColumns();

    long getSequenceValue(String schemaName, String sequenceName);

    void incrementSequenceToValue(String schemaName, String sequenceName, long lowestAcceptableSequenceValue);

}
