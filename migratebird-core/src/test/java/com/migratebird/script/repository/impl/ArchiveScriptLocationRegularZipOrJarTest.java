/**
 * Copyright 2014 Turgay Kivrak
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
package com.migratebird.script.repository.impl;

import com.migratebird.AssertUtils;
import com.migratebird.script.Script;
import com.migratebird.script.qualifier.Qualifier;
import com.migratebird.util.MigrateBirdException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.SortedSet;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static junit.framework.Assert.assertTrue;
import static com.migratebird.util.CollectionUtils.asSet;

/**
*/
public class ArchiveScriptLocationRegularZipOrJarTest {

    private String zipFileLocation;

    @Before
    public void init() throws Exception {
        zipFileLocation = new File(getClass().getResource("test-scripts.zip").toURI()).getPath();
    }

    @Test
    public void readFromRegularZip() {
        File zipFile = new File(zipFileLocation);
        ArchiveScriptLocation archiveScriptLocation = createArchiveScriptLocationFromFile(zipFile);

        SortedSet<Script> scripts = archiveScriptLocation.getScripts();
        AssertUtils.assertPropertyLenientEquals("fileName", asList("scripts/01_script.sql", "scripts/02_script.sql", "otherScripts/03_script.sql"), scripts);
    }

    @Test
    public void rootPathSpecified() {
        File zipFile = new File(zipFileLocation + "!scripts/");
        ArchiveScriptLocation archiveScriptLocation = createArchiveScriptLocationFromFile(zipFile);

        SortedSet<Script> scripts = archiveScriptLocation.getScripts();
        AssertUtils.assertPropertyLenientEquals("fileName", asList("01_script.sql", "02_script.sql"), scripts);
    }

    @Test
    public void rootPathWithoutEndingSlash() {
        File zipFile = new File(zipFileLocation + "!scripts");
        ArchiveScriptLocation archiveScriptLocation = createArchiveScriptLocationFromFile(zipFile);

        SortedSet<Script> scripts = archiveScriptLocation.getScripts();
        AssertUtils.assertPropertyLenientEquals("fileName", asList("01_script.sql", "02_script.sql"), scripts);
    }

    @Test
    public void rootPathDoesNotExist() {
        File zipFile = new File(zipFileLocation + "!xxxx");
        ArchiveScriptLocation archiveScriptLocation = createArchiveScriptLocationFromFile(zipFile);

        SortedSet<Script> scripts = archiveScriptLocation.getScripts();
        assertTrue(scripts.isEmpty());
    }

    @Test(expected = MigrateBirdException.class)
    public void zipFileDoesNotExist() {
        File zipFile = new File("xxxx");
        createArchiveScriptLocationFromFile(zipFile);
    }


    private ArchiveScriptLocation createArchiveScriptLocationFromFile(File file) {
        return new ArchiveScriptLocation(file, "ISO-8859-1", "postprocessing",
                asSet(new Qualifier("qualifier1"), new Qualifier("qualifier2")), singleton(new Qualifier("patch")), "^([0-9]+)_",
                "(?:\\\\G|_)@([a-zA-Z0-9]+)_", "(?:\\\\G|_)#([a-zA-Z0-9]+)_", asSet("sql", "ddl"), null, false);
    }
}
