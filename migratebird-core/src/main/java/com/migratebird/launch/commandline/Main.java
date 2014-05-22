/**
 * Copyright 2014 Turgay Kivrak Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.migratebird.launch.commandline;

import java.io.PrintStream;

import com.migratebird.Migratebird;
import com.migratebird.config.Config;
import com.migratebird.config.ConfigrationLoader;
import com.migratebird.util.MigrateBirdException;

/**
 * Main class for command line operations of MigrateBird
 */
public class Main {

    public static void main(String[] args) {
        try {
            CommandLineArguments commandLineArguments = new CommandLineArguments(args);
            String customConfigFile = commandLineArguments.getConfigFile();
            Config configuration = loadConfiguration(customConfigFile);
            MigrateBirdOperation operation = commandLineArguments.getOperation();

            Main main = new Main();

            main.executeOperation(operation, configuration, commandLineArguments.getFirstExtraArgument(),
                    commandLineArguments.getSecondExtraArgument());
        } catch (MigrateBirdException e) {
            System.err.println("\n" + e.getMessage());
            printHelpMessage(System.out);
            System.exit(1);
        }
    }

    private static void printHelpMessage(PrintStream out) {
         new HelpMessageStream(out).print();        
    }

    protected static Config loadConfiguration(String customConfigFile) {
        return new ConfigrationLoader().load(customConfigFile);
    }

    /**
     * Executes the given operation using the given configuration.
     * 
     * @param operation The operation that must be executed
     * @param configuration The dbMaintain configuration
     * @param commandLineArguments The command line arguments
     */
    public void executeOperation(MigrateBirdOperation operation, Config configuration, String... options) {

        Migratebird migratebird = new Migratebird(configuration);

        switch (operation) {
            case CREATE_SCRIPT_ARCHIVE:
                if (options == null || options[0] == null) {
                    throw new MigrateBirdException("Archive file name must be specified as extra argument");
                }

                String jarFileName = options[0];
                String scriptLocations = options.length > 1 ? options[1] : null;
                migratebird.createScriptArchieve(jarFileName, scriptLocations);
                break;
            case CHECK_SCRIPT_UPDATES:
                migratebird.checkScriptUpdates(options[0]);
                break;
            case UPDATE_DATABASE:
                migratebird.updateDatabase(options[0]);
                break;
            case MARK_DATABASE_AS_UPTODATE:
                migratebird.markDatabaseAsUpToDate(options[0]);
                break;
            case MARK_ERROR_SCRIPT_PERFORMED:
                migratebird.markErrorScriptAsPerformed();
                break;
            case MARK_ERROR_SCRIPT_REVERTED:
                migratebird.markErrorScriptAsReverted();
                break;
            case CLEAR_DATABASE:
                migratebird.clearDatabase();
                break;
            case CLEAN_DATABASE:
                migratebird.cleanDatabase();
                break;
            case DISABLE_CONSTRAINTS:
                migratebird.disableConstraints();
                break;
            case UPDATE_SEQUENCES:
                migratebird.updateSequences();
                break;
            case UPDATE_SQL:
                migratebird.generateUpdateSql();
                break;
        }
    }
}
