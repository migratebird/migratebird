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
package com.migratebird.script.executedscriptinfo.impl;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.time.DateUtils.parseDate;
import static com.migratebird.util.TestUtils.getDefaultExecutedScriptInfoSource;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

import javax.sql.DataSource;

import com.migratebird.database.Database;
import com.migratebird.script.ExecutedScript;
import com.migratebird.script.executedscriptinfo.ScriptIndexes;
import com.migratebird.util.SQLTestUtils;
import com.migratebird.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefaultExecutedScriptInfoSourceBaselineRevisionTest {

	/* The tested instance */
	private DefaultExecutedScriptInfoSource executedScriptInfoSource;

	private DataSource dataSource;
	private Database defaultDatabase;

	@Before
	public void initialize() throws Exception {
		defaultDatabase = TestUtils.getDatabases().getDefaultDatabase();
		dataSource = defaultDatabase.getDataSource();

		executedScriptInfoSource = getDefaultExecutedScriptInfoSource(
				defaultDatabase, true);
		executedScriptInfoSource
				.registerExecutedScript(createScript("1_folder/1_script.sql"));
		executedScriptInfoSource
				.registerExecutedScript(createScript("1_folder/2_script.sql"));
		executedScriptInfoSource
				.registerExecutedScript(createScript("2_folder/1_script.sql"));
		executedScriptInfoSource
				.registerExecutedScript(createScript("repeatable/script.sql"));
		executedScriptInfoSource
				.registerExecutedScript(createScript("postprocessing/script.sql"));
	}

	@After
	public void cleanUp() {
		dropExecutedScriptsTable();
	}

	@Test
	public void someScriptsFiltered() {
		executedScriptInfoSource = getDefaultExecutedScriptInfoSource(
				defaultDatabase, false, new ScriptIndexes("1.2"));

		SortedSet<ExecutedScript> result = executedScriptInfoSource
				.getExecutedScripts();
		assertPropertyLenientEquals(
				"script.fileName",
				asList("1_folder/2_script.sql", "2_folder/1_script.sql",
						"repeatable/script.sql", "postprocessing/script.sql"),
				result);
	}

	@Test
	public void allScriptsFiltered() {
		executedScriptInfoSource = getDefaultExecutedScriptInfoSource(
				defaultDatabase, false, new ScriptIndexes("999"));

		SortedSet<ExecutedScript> result = executedScriptInfoSource
				.getExecutedScripts();
		assertPropertyLenientEquals("script.fileName",
				asList("repeatable/script.sql", "postprocessing/script.sql"),
				result);
	}

	@Test
	public void noScriptsFiltered() {
		executedScriptInfoSource = getDefaultExecutedScriptInfoSource(
				defaultDatabase, false, new ScriptIndexes("1.0"));

		SortedSet<ExecutedScript> result = executedScriptInfoSource
				.getExecutedScripts();
		assertPropertyLenientEquals(
				"script.fileName",
				asList("1_folder/1_script.sql", "1_folder/2_script.sql",
						"2_folder/1_script.sql", "repeatable/script.sql",
						"postprocessing/script.sql"), result);
	}

	private void assertPropertyLenientEquals(String msg, List<String> expected,
			SortedSet<ExecutedScript> actual) {
		List<String> actualNames = new ArrayList<String>();
		for (ExecutedScript script : actual) {
			actualNames.add(script.getScript().getFileName());
		}
		assertEquals(msg, new HashSet<String>(expected), new HashSet<String>(
				actualNames));

	}

	private ExecutedScript createScript(String scriptName)
			throws ParseException {
		return new ExecutedScript(TestUtils.createScript(scriptName),
				parseDate("20/05/2008 10:20:00",
						new String[] { "dd/MM/yyyy hh:mm:ss" }), false);
	}

	private void dropExecutedScriptsTable() {
		SQLTestUtils.executeUpdateQuietly("drop table migratebird_scripts",
				dataSource);
	}

}