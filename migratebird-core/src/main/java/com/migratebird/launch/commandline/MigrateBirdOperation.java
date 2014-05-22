package com.migratebird.launch.commandline;

/**
 * Enum that defines all MigrateBird operations that can be invoked using this class.
 */
public enum MigrateBirdOperation {

    CREATE_SCRIPT_ARCHIVE("createScriptArchive"),
    CHECK_SCRIPT_UPDATES("checkScriptUpdates"),
    UPDATE_DATABASE("updateDatabase"),
    MARK_ERROR_SCRIPT_PERFORMED("markErrorScriptPerformed"),
    MARK_ERROR_SCRIPT_REVERTED("markErrorScriptReverted"),
    MARK_DATABASE_AS_UPTODATE("markDatabaseAsUpToDate"),
    CLEAR_DATABASE("clearDatabase"),
    CLEAN_DATABASE("cleanDatabase"),
    DISABLE_CONSTRAINTS("disableConstraints"),
    UPDATE_SEQUENCES("updateSequences"),
    UPDATE_SQL("updateSQL");

    private String name;

    private MigrateBirdOperation(String name) {
        this.name = name;
    }

    /**
     * @return The name of the operation, that can be used as first command line argument to invoke an operation
     */
    public String getName() {
        return name;
    }

    /**
     * @param operationName The name of the operation, that can be used as first command line argument to invoke an operation
     * @return The operation identified by the given operation name
     */
    public static MigrateBirdOperation getByName(String name) {
        for (MigrateBirdOperation operation : values()) {
            if (operation.getName().equalsIgnoreCase(name)) {
                return operation;
            }
        }
        return null;
    }
}