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

import static com.migratebird.launch.commandline.MigrateBirdOperation.CHECK_SCRIPT_UPDATES;
import static com.migratebird.launch.commandline.MigrateBirdOperation.CLEAN_DATABASE;
import static com.migratebird.launch.commandline.MigrateBirdOperation.CLEAR_DATABASE;
import static com.migratebird.launch.commandline.MigrateBirdOperation.CREATE_SCRIPT_ARCHIVE;
import static com.migratebird.launch.commandline.MigrateBirdOperation.DISABLE_CONSTRAINTS;
import static com.migratebird.launch.commandline.MigrateBirdOperation.MARK_DATABASE_AS_UPTODATE;
import static com.migratebird.launch.commandline.MigrateBirdOperation.MARK_ERROR_SCRIPT_PERFORMED;
import static com.migratebird.launch.commandline.MigrateBirdOperation.MARK_ERROR_SCRIPT_REVERTED;
import static com.migratebird.launch.commandline.MigrateBirdOperation.UPDATE_DATABASE;
import static com.migratebird.launch.commandline.MigrateBirdOperation.UPDATE_SEQUENCES;

import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;

import com.migratebird.config.ConfigrationLoader;
import com.migratebird.config.MigratebirdProperties;


public class HelpMessageStream {
    private PrintStream stream;

    public HelpMessageStream(PrintStream stream) {
        this.stream = stream;
    }

    /**
     * Prints out a help message that explains the usage of this class
     */
    public void print() {
        stream.println("");
        stream.println("Usage:");
        stream.println(" java -jar migratebird.jar <operation> [extra operation arguments] [--config propertiesFile]");
        stream.println("");
        stream.println("");

        stream.println(" --config                     Optional. Provides custom properties file. If omitted,");
        stream.println("                              the file " + ConfigrationLoader.MIGRATEBIRD_PROPS_FILE_NAME + " is expected ");
        stream.println("                              to be available in the execution directory.");
        stream.println(" --help                       Prints this help message");
        stream.println(" --version                    Prints this version information");
        stream.println("");
        stream.println("Available operations are:");
        stream.println();

        stream.println(StringUtils.rightPad(CREATE_SCRIPT_ARCHIVE.getName(), 30, ' ') + "Creates an archive file containing all");
        stream.println("                              configured script locations. Expects file name as second argument.");
        stream.println("                              Optionally, a third argument may be added indicating the scripts archive file or root folder.");
        stream.println("                              This argument overrides the value of the property "
                + MigratebirdProperties.PROPERTY_SCRIPT_LOCATIONS + ".");
        stream.println("");
        stream.println(StringUtils.rightPad(UPDATE_DATABASE.getName(), 30, ' ') + "Updates the database to the latest version.");
        stream.println("                              Optionally, an extra argument may be added indicating the scripts archive file or root folder.");
        stream.println("                              This argument overrides the value of the property "
                + MigratebirdProperties.PROPERTY_SCRIPT_LOCATIONS + ".");
        stream.println("");
        stream.println(StringUtils.rightPad(MARK_ERROR_SCRIPT_PERFORMED.getName(), 30, ' ')
                + "Task that indicates that the failed script was manually performed.");
        stream.println("                              The script will NOT be run again in the next update.");
        stream.println("                              No scripts will be executed by this task.");
        stream.println();
        stream.println(StringUtils.rightPad(MARK_ERROR_SCRIPT_REVERTED.getName(), 30, ' ')
                + "Task that indicates that the failed script was manually reverted.");
        stream.println("                              The script will be run again in the next update.");
        stream.println("                              No scripts will be executed by this task.");
        stream.println();
        stream.println(StringUtils.rightPad(MARK_DATABASE_AS_UPTODATE.getName(), 30, ' ')
                + "Marks the database as up-to-date, without executing any script.");
        stream.println("                              You can use this operation to prepare an existing database to be managed by MigrateBird, ");
        stream.println("                              or after fixing a problem manually.");
        stream.println("                              Optionally, an extra argument may be added indicating the scripts archive file or root folder.");
        stream.println("                              This argument overrides the value of the property "
                + MigratebirdProperties.PROPERTY_SCRIPT_LOCATIONS + ".");
        stream.println();
        stream.println(StringUtils.rightPad(CHECK_SCRIPT_UPDATES.getName(), 30, ' ')
                + "Checks if there are any script updates and prints them out, without executing any script.");
        stream.println();
        stream.println(StringUtils.rightPad(CLEAR_DATABASE.getName(), 30, ' ') + "Removes all database items, and empties the  table.");
        stream.println();
        stream.println(StringUtils.rightPad(CLEAN_DATABASE.getName(), 30, ' ')
                + "Removes the data of all database tables, except for the MIGRATEBIRD_SCRIPTS table.");
        stream.println();
        stream.println(StringUtils.rightPad(DISABLE_CONSTRAINTS.getName(), 30, ' ') + "Disables or drops all foreign key and not null constraints.");
        stream.println();
        stream.println(StringUtils.rightPad(UPDATE_SEQUENCES.getName(), 30, ' ') + "Updates all sequences and identity columns to a minimal value.");
    }
}
