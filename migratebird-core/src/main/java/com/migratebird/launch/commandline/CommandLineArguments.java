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

import com.migratebird.util.MigrateBirdException;


/**
 * Data object that exposes the command line arguments that were passed to Migratebird
 *
*/
public class CommandLineArguments {

    private MigrateBirdOperation operation;
    private String firstExtraArgument, secondExtraArgument;
    private String configFile;

    public CommandLineArguments(String[] commandLineArgs) {
        parseArguments(commandLineArgs);
    }

    //TODO implement command based args! like in LB.
    protected void parseArguments(String[] commandLineArgs) {
        boolean nextArgumentIsConfigFile = false;
        for (String commandLineArg : commandLineArgs) {
            if (nextArgumentIsConfigFile) {
                configFile = commandLineArg;
                continue;
            }
            if ("--configFile".equals(commandLineArg)) {
                nextArgumentIsConfigFile = true;
                continue;
            }
            if (commandLineArg.startsWith("--")) {
                throw new MigrateBirdException("Invalid command line option " + commandLineArg);
            }
            if (operation == null) {
                operation = MigrateBirdOperation.getByName(commandLineArg);
                if (operation == null){
                    throw new MigrateBirdException("Invalid operation " + commandLineArg);
                }
                
                continue;
            }
            if (firstExtraArgument == null) {
                firstExtraArgument = commandLineArg;
                continue;
            }
            if (secondExtraArgument == null) {
                secondExtraArgument = commandLineArg;
                continue;
            }
        }
        if (operation == null) {
            throw new MigrateBirdException("No operation was specified");
        }
    }

    public String getFirstExtraArgument() {
        return firstExtraArgument;
    }

    public String getSecondExtraArgument() {
        return secondExtraArgument;
    }

    public String getConfigFile() {
        return configFile;
    }

    public MigrateBirdOperation getOperation() {
        return operation;
    }

}
