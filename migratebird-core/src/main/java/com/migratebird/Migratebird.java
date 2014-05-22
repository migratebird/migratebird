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
        getMainFactory().createDbMaintainer().updateDatabase(true);

    }

    public void updateDatabase(String scriptLocations) {
        if (scriptLocations != null) {
            config.updateScriptLocations(scriptLocations);
        }
        getMainFactory().createDbMaintainer().updateDatabase(false);

    }

    public void markDatabaseAsUpToDate(String scriptLocations) {
        if (scriptLocations != null) {
            config.updateScriptLocations(scriptLocations);
        }
        getMainFactory().createDbMaintainer().markDatabaseAsUpToDate();

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
