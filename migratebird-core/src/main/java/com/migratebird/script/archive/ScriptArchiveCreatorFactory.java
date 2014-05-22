/**
 * Copyright 2014 Turgay Kivrak Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.migratebird.script.archive;

import java.util.HashSet;
import java.util.Set;

import com.migratebird.config.FactoryWithoutDatabase;
import com.migratebird.script.archive.impl.DefaultScriptArchiveCreator;
import com.migratebird.script.executedscriptinfo.ScriptIndexes;
import com.migratebird.script.qualifier.Qualifier;
import com.migratebird.script.repository.ScriptRepository;

public class ScriptArchiveCreatorFactory extends FactoryWithoutDatabase<ScriptArchiveCreator> {

    @Override
    public ScriptArchiveCreator createInstance() {
        ScriptRepository scriptRepository = factoryContext.createScriptRepository();
        String scriptEncoding = getConfiguration().getScriptEncoding();
        String postProcessingScriptDirName = getConfiguration().getPostProcessingScriptDirName();
        Set<Qualifier> registeredQualifiers = factoryContext.createQualifiers(getConfiguration().getQualifierNames());
        Set<Qualifier> patchQualifiers = factoryContext.createQualifiers(getConfiguration().getScriptPatchQualifierNames());
        String scriptIndexRegexp = getConfiguration().getScriptIndexRegexp();
        String qualifierRegexp = getConfiguration().getScriptQualifierRegexp();
        String targetDatabaseRegexp = getConfiguration().getScriptTargetDatabaseRegexp();
        Set<String> scriptFileExtensions = new HashSet<String>(getConfiguration().getScriptFileExtensions());
        ScriptIndexes baselineRevision = factoryContext.getBaselineRevision();
        boolean ignoreCarriageReturnsWhenCalculatingCheckSum = getConfiguration().getIgnoreCarriageReturnsWhenCalculatingCheckSum();

        return new DefaultScriptArchiveCreator(scriptRepository, scriptEncoding, postProcessingScriptDirName, registeredQualifiers, patchQualifiers,
                scriptIndexRegexp, qualifierRegexp, targetDatabaseRegexp, scriptFileExtensions, baselineRevision,
                ignoreCarriageReturnsWhenCalculatingCheckSum);
    }

}
