/**
 * Copyright 2014 Turgay Kivrak Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.migratebird.script.executedscriptinfo;

import java.text.DateFormat;
import java.util.Set;

import com.migratebird.config.FactoryWithDatabase;
import com.migratebird.database.Database;
import com.migratebird.script.ScriptFactory;
import com.migratebird.script.executedscriptinfo.impl.DefaultExecutedScriptInfoSource;
import com.migratebird.script.qualifier.Qualifier;

public class ExecutedScriptInfoSourceFactory extends FactoryWithDatabase<ExecutedScriptInfoSource> {

    @Override
    public ExecutedScriptInfoSource createInstance() {
        boolean autoCreateExecutedScriptsTable = getConfiguration().getAutoCreateDatabaseLogTable();

        Database defaultDatabase = getDatabases().getDefaultDatabase();
        String executedScriptsTableName = defaultDatabase.toCorrectCaseIdentifier(getConfiguration().getExecutedScriptsTableName());
        String fileNameColumnName = defaultDatabase.toCorrectCaseIdentifier(getConfiguration().getFileNameColName());
        int fileNameColumnSize = getConfiguration().getFileNameColSize();
        String fileLastModifiedAtColumnName = defaultDatabase.toCorrectCaseIdentifier(getConfiguration().getFileLastModifiedAtColumnName());
        String checksumColumnName = defaultDatabase.toCorrectCaseIdentifier(getConfiguration().getCheckSumColumnName());
        int checksumColumnSize = getConfiguration().getCheckSumColSize();
        String executedAtColumnName = defaultDatabase.toCorrectCaseIdentifier(getConfiguration().getExecutedAtColName());
        int executedAtColumnSize = getConfiguration().getExecutedAtColSize();
        String succeededColumnName = defaultDatabase.toCorrectCaseIdentifier(getConfiguration().getSucceededColName());
        DateFormat timestampFormat = getConfiguration().getTimestampFormat();
        String scriptIndexRegexp = getConfiguration().getScriptIndexRegexp();
        String targetDatabaseRegexp = getConfiguration().getScriptTargetDatabaseRegexp();
        String qualifierRegexp = getConfiguration().getScriptQualifierRegexp();
        Set<Qualifier> registeredQualifiers = factoryWithDatabaseContext.createQualifiers(getConfiguration().getQualifierNames());
        Set<Qualifier> patchQualifiers = factoryWithDatabaseContext.createQualifiers(getConfiguration().getScriptPatchQualifierNames());
        String postProcessingScriptsDirName = getConfiguration().getPostProcessingScriptDirName();
        ScriptIndexes baselineRevision = factoryWithDatabaseContext.getBaselineRevision();

        ScriptFactory scriptFactory = new ScriptFactory(scriptIndexRegexp, targetDatabaseRegexp, qualifierRegexp, registeredQualifiers,
                patchQualifiers, postProcessingScriptsDirName, baselineRevision);
        return new DefaultExecutedScriptInfoSource(autoCreateExecutedScriptsTable, executedScriptsTableName, fileNameColumnName, fileNameColumnSize,
                fileLastModifiedAtColumnName, checksumColumnName, checksumColumnSize, executedAtColumnName, executedAtColumnSize,
                succeededColumnName, timestampFormat, defaultDatabase, getSqlHandler(), scriptFactory);
    }

}
