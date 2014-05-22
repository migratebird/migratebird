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
package com.migratebird;

import com.migratebird.config.Config;

public class Migratebird {

    private Config config;

    public Migratebird(Config config) {
        this.config = config;
    }

    public void clearDatabase() {
        getMainFactory().createDBClearer().clearDatabase();

    }

    private MainFactory getMainFactory() {
        return new MainFactory(config);
    }

    public void createScriptArchieve(String jarFileName, String scriptLocations) {
        if (scriptLocations != null) {
            config.updateScriptLocations(scriptLocations);
        }
        getMainFactory().createScriptArchiveCreator().createScriptArchive(jarFileName);
    }

    public void checkScriptUpdates(String providedScriptLocation) {
        if (providedScriptLocation != null) {
            config.updateScriptLocations(providedScriptLocation);
        }
        getMainFactory().createDbUpdate().updateDatabase(true);

    }

    public void updateDatabase(String scriptLocations) {
        if (scriptLocations != null) {
            config.updateScriptLocations(scriptLocations);
        }
        getMainFactory().createDbUpdate().updateDatabase(false);

    }

    public void markDatabaseAsUpToDate(String scriptLocations) {
        if (scriptLocations != null) {
            config.updateScriptLocations(scriptLocations);
        }
        getMainFactory().createDbUpdate().markDatabaseAsUpToDate();

    }

    public void markErrorScriptAsPerformed() {
        getMainFactory().createExecutedScriptInfoSource().markErrorScriptsAsSuccessful();

    }

    public void markErrorScriptAsReverted() {
        getMainFactory().createExecutedScriptInfoSource().removeErrorScripts();
    }

    public void cleanDatabase() {
        getMainFactory().createDBCleaner().cleanDatabase();
    }

    public void disableConstraints() {
        getMainFactory().createConstraintsDisabler().disableConstraints();
    }

    public void updateSequences() {
        getMainFactory().createSequenceUpdater().updateSequences();
    }

    public void generateUpdateSql() {
        getMainFactory().createUpdateSqlGenerator().generate();
    }

}
