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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.migratebird.database.Databases;
import com.migratebird.script.executedscriptinfo.ExecutedScriptInfoSource;
import com.migratebird.structure.constraint.ConstraintsDisabler;
import com.migratebird.structure.constraint.impl.DefaultConstraintsDisabler;
import com.migratebird.structure.model.DbItemIdentifier;
import com.migratebird.util.MigrateBirdException;
import com.migratebird.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static com.migratebird.structure.model.DbItemIdentifier.parseItemIdentifier;
import static com.migratebird.structure.model.DbItemIdentifier.parseSchemaIdentifier;
import static com.migratebird.structure.model.DbItemType.*;
import static com.migratebird.util.CollectionUtils.asSet;
import static com.migratebird.util.TestUtils.getDefaultExecutedScriptInfoSource;
import static org.junit.Assert.fail;

/**
 * Test class for the {@link com.migratebird.structure.clear.DBClearer} with preserve items configured, but some items do not exist.
 *
*/
public class DefaultDBClearerPreserveDoesNotExistTest {

    /* The logger instance for this class */
    private static Log logger = LogFactory.getLog(DefaultDBClearerPreserveDoesNotExistTest.class);

    private Databases databases;
    private ConstraintsDisabler constraintsDisabler;
    private ExecutedScriptInfoSource executedScriptInfoSource;


    /**
     * Configures the tested object.
     * <p/>
     * todo Test_trigger_Preserve Test_CASE_Trigger_Preserve
     */
    @Before
    public void initialize() {
        databases = TestUtils.getDatabases();
        constraintsDisabler = new DefaultConstraintsDisabler(databases);
        executedScriptInfoSource = getDefaultExecutedScriptInfoSource(databases.getDefaultDatabase(), true);
    }


    /**
     * Test for schemas to preserve that do not exist.
     */
    @Test(expected = MigrateBirdException.class)
    public void schemasToPreserveDoNotExist() {
        Set<DbItemIdentifier> itemsToPreserve = asSet(
                parseSchemaIdentifier("unexisting_schema1", databases),
                parseSchemaIdentifier("unexisting_schema2", databases));
        createDbClearer(itemsToPreserve).clearDatabase();
    }

    private DefaultDBClearer createDbClearer(Set<DbItemIdentifier> itemsToPreserve) {
        return new DefaultDBClearer(databases, itemsToPreserve, constraintsDisabler, executedScriptInfoSource);
    }

    /**
     * Test for tables to preserve that do not exist.
     */
    @Test(expected = MigrateBirdException.class)
    public void tablesToPreserveDoNotExist() {
        Set<DbItemIdentifier> itemsToPreserve = asSet(
                parseItemIdentifier(TABLE, "unexisting_table1", databases),
                parseItemIdentifier(TABLE, "unexisting_table2", databases));
        createDbClearer(itemsToPreserve).clearDatabase();
    }

    /**
     * Test for views to preserve that do not exist.
     */
    @Test(expected = MigrateBirdException.class)
    public void viewsToPreserveDoNotExist() {
        Set<DbItemIdentifier> itemsToPreserve = asSet(
                parseItemIdentifier(VIEW, "unexisting_view1", databases),
                parseItemIdentifier(VIEW, "unexisting_view2", databases));
        createDbClearer(itemsToPreserve).clearDatabase();
    }

    /**
     * Test for materialized views to preserve that do not exist.
     */
    @Test(expected = MigrateBirdException.class)
    public void materializedViewsToPreserveDoNotExist() {
        Set<DbItemIdentifier> itemsToPreserve = asSet(
                parseItemIdentifier(MATERIALIZED_VIEW, "unexisting_materializedView1", databases),
                parseItemIdentifier(MATERIALIZED_VIEW, "unexisting_materializedView1", databases));
        createDbClearer(itemsToPreserve).clearDatabase();
    }

    /**
     * Test for sequences to preserve that do not exist.
     */
    @Test
    public void sequencesToPreserveDoNotExist() {
        if (!databases.getDefaultDatabase().supportsSequences()) {
            logger.warn("Current dialect does not support sequences. Skipping test.");
            return;
        }
        try {
            Set<DbItemIdentifier> itemsToPreserve = asSet(
                    parseItemIdentifier(SEQUENCE, "unexisting_sequence1", databases),
                    parseItemIdentifier(SEQUENCE, "unexisting_sequence2", databases));

            createDbClearer(itemsToPreserve).clearDatabase();
            fail("DbMaintainException expected.");
        } catch (MigrateBirdException e) {
            // expected
        }
    }

    /**
     * Test for synonyms to preserve that do not exist.
     */
    @Test
    public void synonymsToPreserveDoNotExist() {
        if (!databases.getDefaultDatabase().supportsSynonyms()) {
            logger.warn("Current dialect does not support synonyms. Skipping test.");
            return;
        }
        try {
            Set<DbItemIdentifier> itemsToPreserve = asSet(
                    parseItemIdentifier(SYNONYM, "unexisting_synonym1", databases),
                    parseItemIdentifier(SYNONYM, "unexisting_synonym2", databases));

            createDbClearer(itemsToPreserve).clearDatabase();
            fail("DbMaintainException expected.");
        } catch (MigrateBirdException e) {
            // expected
        }
    }
}
