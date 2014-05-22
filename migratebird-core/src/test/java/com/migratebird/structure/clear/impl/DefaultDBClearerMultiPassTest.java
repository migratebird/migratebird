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
package com.migratebird.structure.clear.impl;

import static java.util.Arrays.asList;
import static com.migratebird.util.CollectionUtils.asSet;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.migratebird.database.Database;
import com.migratebird.database.Databases;
import com.migratebird.script.executedscriptinfo.ExecutedScriptInfoSource;
import com.migratebird.structure.constraint.ConstraintsDisabler;
import com.migratebird.structure.model.DbItemIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for the {@link DefaultDBClearer} to verify that we will keep
 * trying to drop database objects even if we get exceptions (until we make no
 * more progress).
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultDBClearerMultiPassTest {

	private DefaultDBClearer defaultDBClearer;

	@Mock
	protected Database database;
	@Mock
	protected ConstraintsDisabler constraintsDisabler;
	@Mock
	protected ExecutedScriptInfoSource executedScriptInfoSource;

	private static final String SCHEMA = "MYSCHEMA";
	private final Set<String> tableNames = asSet("TABLE1", "TABLE2", "TABLE3");

	/**
	 * Configures the tested object.
	 */
	@Before
	public void setUp() {
		Databases databases = new Databases(database, asList(database),
				new ArrayList<String>());

		defaultDBClearer = new DefaultDBClearer(databases,
				new HashSet<DbItemIdentifier>(), constraintsDisabler,
				executedScriptInfoSource);
		when(database.getTableNames(SCHEMA)).thenReturn(tableNames);
		when(database.getSchemaNames()).thenReturn(asSet(SCHEMA));
	}

	/**
	 * When we throw an exception on the first pass then it is ignored and we
	 * try another pass (which succeeds).
	 */
	@Test
	public void testClearDatabase_IgnoreFirstErrorOnDropTable() {
		doThrow(new RuntimeException("error message")).doNothing()
				.when(database).dropTable(SCHEMA, "TABLE2");

		defaultDBClearer.clearDatabase();
	}

	/**
	 * When exceptions do not decrease then we throw an exception.
	 */
	@Test(expected = IllegalStateException.class)
	public void testClearDatabase_ThrowExceptionWhenExceptionsDoNotDecrease() {
		doThrow(new IllegalStateException("error message")).when(database)
				.dropTable(SCHEMA, "TABLE2");

		defaultDBClearer.clearDatabase();
	}

}
