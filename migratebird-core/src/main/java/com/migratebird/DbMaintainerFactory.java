/**
 * Copyright 2014 Turgay Kivrak Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.migratebird;

import com.migratebird.config.FactoryWithDatabase;
import com.migratebird.script.analyzer.ScriptUpdatesFormatter;
import com.migratebird.script.executedscriptinfo.ExecutedScriptInfoSource;
import com.migratebird.script.executedscriptinfo.ScriptIndexes;
import com.migratebird.script.repository.ScriptRepository;
import com.migratebird.script.runner.ScriptRunner;
import com.migratebird.structure.clean.DBCleaner;
import com.migratebird.structure.clear.DBClearer;
import com.migratebird.structure.constraint.ConstraintsDisabler;
import com.migratebird.structure.sequence.SequenceUpdater;

public class DbMaintainerFactory extends FactoryWithDatabase<DbMaintainer> {

    @Override
    public DbMaintainer createInstance() {
        ScriptRepository scriptRepository = factoryWithDatabaseContext.createScriptRepository();

        boolean cleanDbEnabled = getConfiguration().getCleanDb();
        boolean fromScratchEnabled = getConfiguration().getFromScratchEnabled();
        boolean useScriptFileLastModificationDates = getConfiguration().getUseScriptFileLastModificationDates();
        boolean allowOutOfSequenceExecutionOfPatchScripts = getConfiguration().getPatchAllowOutOfSeqExecution();
        boolean disableConstraintsEnabled = getConfiguration().getDisableConstraints();
        boolean updateSequencesEnabled = getConfiguration().getUpdateSequencesEnabled();
        long maxNrOfCharsWhenLoggingScriptContent = getConfiguration().getMaxNumCharsWhenLoggingScriptContent();
        ScriptIndexes baseLineRevision = factoryWithDatabaseContext.getBaselineRevision();

        MainFactory mainFactory = factoryWithDatabaseContext.getMainFactory();
        DBCleaner dbCleaner = mainFactory.createDBCleaner();
        DBClearer dbClearer = mainFactory.createDBClearer();
        ConstraintsDisabler constraintsDisabler = mainFactory.createConstraintsDisabler();
        SequenceUpdater sequenceUpdater = mainFactory.createSequenceUpdater();
        ScriptRunner scriptRunner = mainFactory.createScriptRunner();
        ScriptUpdatesFormatter scriptUpdatesFormatter = createScriptUpdatesFormatter();
        ExecutedScriptInfoSource executedScriptInfoSource = mainFactory.createExecutedScriptInfoSource();

        return new DefaultDbMaintainer(scriptRunner, scriptRepository, executedScriptInfoSource, fromScratchEnabled,
                useScriptFileLastModificationDates, allowOutOfSequenceExecutionOfPatchScripts, cleanDbEnabled, disableConstraintsEnabled,
                updateSequencesEnabled, dbClearer, dbCleaner, constraintsDisabler, sequenceUpdater, scriptUpdatesFormatter, getSqlHandler(),
                maxNrOfCharsWhenLoggingScriptContent, baseLineRevision);
    }

    protected ScriptUpdatesFormatter createScriptUpdatesFormatter() {
        return new ScriptUpdatesFormatter();
    }

}
