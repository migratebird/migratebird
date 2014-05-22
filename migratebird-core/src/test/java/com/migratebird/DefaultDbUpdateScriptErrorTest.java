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

import static org.mockito.Mockito.doThrow;

import com.migratebird.script.Script;
import com.migratebird.script.executedscriptinfo.ExecutedScriptInfoSource;
import com.migratebird.script.runner.ScriptRunner;
import com.migratebird.util.MigrateBirdException;
import com.migratebird.util.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDbUpdateScriptErrorTest {

	@Mock
	protected ExecutedScriptInfoSource executedScriptInfoSource;
	@Mock
	protected ScriptRunner scriptRunner;
	@Mock
	protected Script script;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void initialize() {
		script = TestUtils.createScriptWithContent("01_filename.sql",
				"content of script");
	}

	@Test
	public void errorMessageShouldContainFullScriptContents() {

		DefaultDbUpdate dbUpdate = createDefaultDbUpdate(10000);
		doThrow(new MigrateBirdException("error message")).when(scriptRunner)
				.execute(script);

		thrown.expect(MigrateBirdException.class);
		thrown.expectMessage("Full contents of failed script 01_filename.sql:\n"
				+ "----------------------------------------------------\n"
				+ "content of script\n"
				+ "----------------------------------------------------\n");
		

		dbUpdate.executeScript(script);
	}

	@Test
	public void loggingOfScriptContentsDisabledWhenMaxLengthIsSetTo0() {
		DefaultDbUpdate dbUpdate = createDefaultDbUpdate(0);
		doThrow(new MigrateBirdException("error message")).when(scriptRunner)
				.execute(script);

		thrown.expect(MigrateBirdException.class);
		thrown.expectMessage("Error while executing script 01_filename.sql: error message");
		
		dbUpdate.executeScript(script);
	}

	@Test
	public void largeScriptContentIsTruncated() {
		DefaultDbUpdate dbUpdate = createDefaultDbUpdate(5);
		doThrow(new MigrateBirdException("error message")).when(scriptRunner)
				.execute(script);


		thrown.expect(MigrateBirdException.class);
		thrown.expectMessage("Full contents of failed script 01_filename.sql:\n"
				+ "----------------------------------------------------\n"
				+ "conte... <remainder of script is omitted>\n"
				+ "----------------------------------------------------\n");
		
		dbUpdate.executeScript(script);

	}

	private DefaultDbUpdate createDefaultDbUpdate(
			long maxNrOfCharsWhenLoggingScriptContent) {
		return new DefaultDbUpdate(scriptRunner, null,
				executedScriptInfoSource, false, false, false, false, false,
				false, null, null, null, null, null, null,
				maxNrOfCharsWhenLoggingScriptContent, null);
	}

}
