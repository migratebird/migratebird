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
package com.migratebird.script.repository;

import com.migratebird.AssertUtils;
import com.migratebird.script.Script;
import com.migratebird.script.ScriptFactory;
import com.migratebird.script.executedscriptinfo.ScriptIndexes;
import org.junit.Test;

import java.util.SortedSet;

import static java.util.Arrays.asList;
import static com.migratebird.util.CollectionUtils.asSet;
import static com.migratebird.util.CollectionUtils.asSortedSet;
import static com.migratebird.util.TestUtils.*;
import static org.junit.Assert.assertTrue;

/**
*/
public class ScriptRepositoryBaselineRevisionTest {

    /* Tested object */
    private ScriptRepository scriptRepository;


    @Test
    public void someScriptsFiltered() {
        scriptRepository = createScriptRepository(new ScriptIndexes("1.2"));

        SortedSet<Script> result = scriptRepository.getIndexedScripts();
        com.migratebird.AssertUtils.assertPropertyLenientEquals("fileName", asList("1_folder/2_script.sql", "2_folder/1_script.sql"), result);
    }


    @Test
    public void allScriptsFiltered() {
        scriptRepository = createScriptRepository(new ScriptIndexes("999"));

        SortedSet<Script> result = scriptRepository.getIndexedScripts();
        assertTrue(result.isEmpty());
    }

    @Test
    public void noScriptsFiltered() {
        scriptRepository = createScriptRepository(new ScriptIndexes("1.0"));

        SortedSet<Script> result = scriptRepository.getIndexedScripts();
        AssertUtils.assertPropertyLenientEquals("fileName", asList("1_folder/1_script.sql", "1_folder/2_script.sql", "2_folder/1_script.sql"), result);
    }

    @Test
    public void repeatableScriptsNotFiltered() {
        scriptRepository = createScriptRepository(new ScriptIndexes("1.0"));

        SortedSet<Script> result = scriptRepository.getRepeatableScripts();
        AssertUtils.assertPropertyLenientEquals("fileName", asList("repeatable/script.sql"), result);
    }

    @Test
    public void postProcessingScriptsNotFiltered() {
        scriptRepository = createScriptRepository(new ScriptIndexes("1.0"));

        SortedSet<Script> result = scriptRepository.getPostProcessingScripts();
        AssertUtils.assertPropertyLenientEquals("fileName", asList("postprocessing/script.sql"), result);
    }


    private ScriptRepository createScriptRepository(ScriptIndexes baseLineRevision) {
        ScriptFactory scriptFactory = createScriptFactory(baseLineRevision);
        Script script11 = scriptFactory.createScriptWithoutContent("1_folder/1_script.sql", 0L, "checksum");
        Script script12 = scriptFactory.createScriptWithoutContent("1_folder/2_script.sql", 0L, "checksum");
        Script script21 = scriptFactory.createScriptWithoutContent("2_folder/1_script.sql", 0L, "checksum");
        Script repeatableScript = scriptFactory.createScriptWithoutContent("repeatable/script.sql", 0L, "checksum");
        Script postProcessingScript = scriptFactory.createScriptWithoutContent("postprocessing/script.sql", 0L, "checksum");
        SortedSet<Script> scripts = asSortedSet(script11, script12, script21, repeatableScript, postProcessingScript);

        ScriptLocation scriptLocation = createArchiveScriptLocation(scripts, baseLineRevision);
        return new ScriptRepository(asSet(scriptLocation), getTrivialQualifierEvaluator());
    }


}