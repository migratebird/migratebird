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