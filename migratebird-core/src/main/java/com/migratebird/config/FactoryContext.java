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
package com.migratebird.config;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.migratebird.MainFactory;
import com.migratebird.script.executedscriptinfo.ScriptIndexes;
import com.migratebird.script.qualifier.Qualifier;
import com.migratebird.script.qualifier.QualifierEvaluator;
import com.migratebird.script.qualifier.impl.IncludeExcludeQualifierEvaluator;
import com.migratebird.script.repository.ScriptLocation;
import com.migratebird.script.repository.ScriptRepository;
import com.migratebird.script.repository.impl.ArchiveScriptLocation;
import com.migratebird.script.repository.impl.FileSystemScriptLocation;
import com.migratebird.util.MigrateBirdException;

/**
*/
public class FactoryContext {

    private Config config;
    private MainFactory mainFactory;


    public FactoryContext(Config configuration, MainFactory mainFactory) {
        this.config = configuration;
        this.mainFactory = mainFactory;
    }


    public Set<Qualifier> createQualifiers(List<String> qualifierNames) {
        Set<Qualifier> qualifiers = new HashSet<Qualifier>(qualifierNames.size());
        for (String qualifierName : qualifierNames) {
            qualifiers.add(new Qualifier(qualifierName));
        }
        return qualifiers;
    }

    public ScriptIndexes getBaselineRevision() {
        String baseLineRevisionString = config.getBaselineRevision();
        if (isBlank(baseLineRevisionString)) {
            return null;
        }
        return new ScriptIndexes(baseLineRevisionString);
    }

    public ScriptRepository createScriptRepository() {
        Set<String> scriptLocationIndicators = config.getScriptLocations();
        if (scriptLocationIndicators.isEmpty()) {
            throw new MigrateBirdException("Unable to find scripts. No script locations specified.");
        }
        Set<ScriptLocation> scriptLocations = new HashSet<ScriptLocation>();
        for (String scriptLocationIndicator : scriptLocationIndicators) {
            scriptLocations.add(createScriptLocation(scriptLocationIndicator));
        }
        QualifierEvaluator qualifierEvaluator = createQualifierEvaluator(scriptLocations);
        return new ScriptRepository(scriptLocations, qualifierEvaluator);
    }


    public ScriptLocation createScriptLocation(String scriptLocation) {
        String scriptEncoding = config.getScriptEncoding();
        String postProcessingScriptDirName = config.getPostProcessingScriptDirName();
        Set<Qualifier> registeredQualifiers = createQualifiers(config.getQualifierNames());
        Set<Qualifier> patchQualifiers = createQualifiers(config.getScriptPatchQualifierNames());
        String scriptIndexRegexp = config.getScriptIndexRegexp();
        String qualifierRegexp = config.getScriptQualifierRegexp();
        String targetDatabaseRegexp = config.getScriptTargetDatabaseRegexp();
        Set<String> scriptFileExtensions = config.getScriptFileExtensions();
        boolean ignoreCarriageReturnsWhenCalculatingCheckSum = config.getIgnoreCarriageReturnsWhenCalculatingCheckSum();
        ScriptIndexes baseLineRevision = getBaselineRevision();

        File scriptLocationFile = new File(scriptLocation);
        if (scriptLocationFile.isFile()) {
            return new ArchiveScriptLocation(scriptLocationFile, scriptEncoding, postProcessingScriptDirName, registeredQualifiers, patchQualifiers, scriptIndexRegexp, qualifierRegexp, targetDatabaseRegexp, scriptFileExtensions, baseLineRevision, ignoreCarriageReturnsWhenCalculatingCheckSum);
        } else {
            return new FileSystemScriptLocation(scriptLocationFile, scriptEncoding, postProcessingScriptDirName, registeredQualifiers, patchQualifiers, scriptIndexRegexp, qualifierRegexp, targetDatabaseRegexp, scriptFileExtensions, baseLineRevision, ignoreCarriageReturnsWhenCalculatingCheckSum);
        }
    }


    protected QualifierEvaluator createQualifierEvaluator(Set<ScriptLocation> scriptLocations) {
        Set<Qualifier> registeredQualifiers = getRegisteredQualifiers(scriptLocations);
        Set<Qualifier> includedQualifiers = createQualifiers(config.getIncludedQualifierNames());
        Set<Qualifier> excludedQualifiers = createQualifiers(config.getExcludedQualifierNames());
        return new IncludeExcludeQualifierEvaluator(registeredQualifiers, includedQualifiers, excludedQualifiers);
    }

    protected Set<Qualifier> getRegisteredQualifiers(Set<ScriptLocation> scriptLocations) {
        Set<Qualifier> registeredQualifiers = createQualifiers(config.getRegisteredQualifierNames());
        for (ScriptLocation scriptLocation : scriptLocations) {
            registeredQualifiers.addAll(scriptLocation.getRegisteredQualifiers());
        }
        return registeredQualifiers;
    }


    public Config getConfiguration() {
        return config;
    }

    public MainFactory getMainFactory() {
        return mainFactory;
    }
}
