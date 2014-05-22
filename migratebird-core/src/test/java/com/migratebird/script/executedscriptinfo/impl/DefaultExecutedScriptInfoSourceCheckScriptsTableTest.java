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
package com.migratebird.script.executedscriptinfo.impl;

import com.migratebird.database.Database;
import com.migratebird.script.ExecutedScript;
import com.migratebird.script.Script;
import com.migratebird.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

import static org.apache.commons.lang.time.DateUtils.parseDate;
import static com.migratebird.util.SQLTestUtils.assertTableExists;
import static com.migratebird.util.SQLTestUtils.executeUpdateQuietly;
import static com.migratebird.util.TestUtils.createScript;

public class DefaultExecutedScriptInfoSourceCheckScriptsTableTest {

    /* The tested instance with auto-create configured */
    private DefaultExecutedScriptInfoSource executedScriptInfoSourceAutoCreate;

    private DataSource dataSource;
    private Database defaultDatabase;

    private ExecutedScript executedScript;
    private Script script;


    @Before
    public void initialize() throws Exception {
        defaultDatabase = TestUtils.getDatabases().getDefaultDatabase();
        dataSource = defaultDatabase.getDataSource();

        executedScriptInfoSourceAutoCreate = TestUtils.getDefaultExecutedScriptInfoSource(defaultDatabase, true);

        executedScript = new ExecutedScript(createScript("1_script1.sql"), parseDate("20/05/2008 10:20:00", new String[]{"dd/MM/yyyy hh:mm:ss"}), false);
        script = createScript("1_script1_renamed.sql");

        dropExecutedScriptsTable();
    }

    @After
    public void cleanUp() {
        dropExecutedScriptsTable();
    }


    @Test
    public void registerExecutedScript() {
        executedScriptInfoSourceAutoCreate.registerExecutedScript(executedScript);
        assertExecutedScriptsTableWasCreated();
    }

    @Test
    public void updateExecutedScript() {
        executedScriptInfoSourceAutoCreate.updateExecutedScript(executedScript);
        assertExecutedScriptsTableWasCreated();
    }

    @Test
    public void clearAllExecutedScripts() {
        executedScriptInfoSourceAutoCreate.clearAllExecutedScripts();
        assertExecutedScriptsTableWasCreated();
    }

    @Test
    public void getExecutedScripts() {
        executedScriptInfoSourceAutoCreate.getExecutedScripts();
        assertExecutedScriptsTableWasCreated();
    }

    @Test
    public void deleteExecutedScript() {
        executedScriptInfoSourceAutoCreate.deleteExecutedScript(executedScript);
        assertExecutedScriptsTableWasCreated();
    }

    @Test
    public void renameExecutedScript() {
        executedScriptInfoSourceAutoCreate.renameExecutedScript(executedScript, script);
        assertExecutedScriptsTableWasCreated();
    }

    @Test
    public void deleteAllExecutedPostprocessingScripts() {
        executedScriptInfoSourceAutoCreate.deleteAllExecutedPostprocessingScripts();
        assertExecutedScriptsTableWasCreated();
    }

    @Test
    public void markErrorScriptsAsSuccessful() {
        executedScriptInfoSourceAutoCreate.markErrorScriptsAsSuccessful();
        assertExecutedScriptsTableWasCreated();
    }

    @Test
    public void removeErrorScripts() {
        executedScriptInfoSourceAutoCreate.removeErrorScripts();
        assertExecutedScriptsTableWasCreated();
    }


    private void assertExecutedScriptsTableWasCreated() {
        assertTableExists(executedScriptInfoSourceAutoCreate.getQualifiedExecutedScriptsTableName(), dataSource);
    }

    private void dropExecutedScriptsTable() {
        executeUpdateQuietly("drop table migratebird_scripts", dataSource);
    }

}
