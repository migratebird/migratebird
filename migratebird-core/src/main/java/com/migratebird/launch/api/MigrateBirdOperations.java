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
package com.migratebird.launch.api;

import java.net.URL;

import com.migratebird.DbUpdate;
import com.migratebird.MainFactory;
import com.migratebird.config.Config;
import com.migratebird.config.ConfigrationLoader;
import com.migratebird.script.archive.ScriptArchiveCreator;
import com.migratebird.structure.clean.DBCleaner;
import com.migratebird.structure.clear.DBClearer;
import com.migratebird.structure.constraint.ConstraintsDisabler;
import com.migratebird.structure.sequence.SequenceUpdater;
import com.migratebird.util.MigrateBirdException;

/**
 * Class that offers static methods that expose all available MigrateBird operations.
 */
public class MigrateBirdOperations {

    private static final String DB_PROPERTIES = "migratebird.properties";

    /**
     * Creates an archive file containing all scripts in all configured script locations
     * 
     * @param archiveFileName The name of the archive file to create
     */
    public static void createScriptArchive(String archiveFileName) {
        ScriptArchiveCreator scriptArchiveCreator = getMainFactory().createScriptArchiveCreator();
        scriptArchiveCreator.createScriptArchive(archiveFileName);
    }

    /**
     * Updates the database to the latest version.
     */
    public static void updateDatabase() {
        DbUpdate migratebird = getMainFactory().createDbUpdate();
        migratebird.updateDatabase(false);
    }

    /**
     * Marks the database as up-to-date, without executing any script. You can use this operation to prepare an existing database to be managed by
     * MigrateBird, or after having manually fixed a problem.
     */
    public static void markDatabaseAsUptodate() {
        DbUpdate migratebird = getMainFactory().createDbUpdate();
        migratebird.markDatabaseAsUpToDate();
    }

    /**
     * Removes all database items, and empties the MIGRATEBIRD_SCRIPTS table.
     */
    public static void clearDatabase() {
        DBClearer dbClearer = getMainFactory().createDBClearer();
        dbClearer.clearDatabase();
    }

    /**
     * Removes the data of all database tables, except for the MIGRATEBIRD_SCRIPTS table.
     */
    public static void cleanDatabase() {
        DBCleaner dbCleaner = getMainFactory().createDBCleaner();
        dbCleaner.cleanDatabase();
    }

    /**
     * Disables or drops all foreign key and not null constraints.
     */
    public static void disableConstraints() {
        ConstraintsDisabler constraintsDisabler = getMainFactory().createConstraintsDisabler();
        constraintsDisabler.disableConstraints();
    }

    /**
     * Updates all sequences and identity columns to a minimum value.
     */
    public static void updateSequences() {
        SequenceUpdater sequenceUpdater = getMainFactory().createSequenceUpdater();
        sequenceUpdater.updateSequences();
    }

    private static MainFactory getMainFactory() {
        URL propertiesFromClassPath = ClassLoader.getSystemResource(DB_PROPERTIES);
        if (propertiesFromClassPath == null) {
            throw new MigrateBirdException("Could not find properties file " + DB_PROPERTIES + " in classpath");
        }
        Config config = new ConfigrationLoader().loadConfiguration(propertiesFromClassPath);
        return new MainFactory(config);
    }
}
