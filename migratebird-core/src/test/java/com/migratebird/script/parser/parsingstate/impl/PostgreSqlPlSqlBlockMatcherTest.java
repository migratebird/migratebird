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
package com.migratebird.script.parser.parsingstate.impl;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
*/
public class PostgreSqlPlSqlBlockMatcherTest {

    private PostgreSqlPlSqlBlockMatcher postgreSqlPlSqlBlockMatcher = new PostgreSqlPlSqlBlockMatcher();

    @Test
    public void startOfStoredProcedure() {
        assertIsStartOfStoredProcedure("CREATE FUNCTION");
        assertIsStartOfStoredProcedure("CREATE OR REPLACE FUNCTION");
        assertIsStartOfStoredProcedure("CREATE RULE");
        assertIsStartOfStoredProcedure("CREATE OR REPLACE RULE");
        assertIsStartOfStoredProcedure("BEGIN");
    }

    @Test
    public void noStartOfStoredProcedure() {
        assertIsNotStartOfStoredProcedure("DECLARE");
        assertIsNotStartOfStoredProcedure("CREATE PROCEDURE");
        assertIsNotStartOfStoredProcedure("CREATE TRIGGER");
    }

    @Test
    public void noStartOfStoredProcedure_spacing() {
        assertIsNotStartOfStoredProcedure(" CREATE FUNCTION");
        assertIsNotStartOfStoredProcedure("CREATE  FUNCTION");
        assertIsNotStartOfStoredProcedure("CREATE FUNCTION SOMETHING");
        assertIsNotStartOfStoredProcedure("CREATE\nFUNCTION");
    }

    private void assertIsStartOfStoredProcedure(String text) {
        assertTrue(text, postgreSqlPlSqlBlockMatcher.isStartOfPlSqlBlock(new StringBuilder(text)));
    }

    private void assertIsNotStartOfStoredProcedure(String text) {
        assertFalse(text, postgreSqlPlSqlBlockMatcher.isStartOfPlSqlBlock(new StringBuilder(text)));
    }
}
