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
package com.migratebird.structure.clear;

import static com.migratebird.config.MigratebirdProperties.PROPERTY_PRESERVE_MATERIALIZED_VIEWS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PRESERVE_SCHEMAS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PRESERVE_SEQUENCES;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PRESERVE_SYNONYMS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PRESERVE_TABLES;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PRESERVE_TRIGGERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PRESERVE_TYPES;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PRESERVE_VIEWS;
import static com.migratebird.structure.model.DbItemType.MATERIALIZED_VIEW;
import static com.migratebird.structure.model.DbItemType.SEQUENCE;
import static com.migratebird.structure.model.DbItemType.SYNONYM;
import static com.migratebird.structure.model.DbItemType.TABLE;
import static com.migratebird.structure.model.DbItemType.TRIGGER;
import static com.migratebird.structure.model.DbItemType.TYPE;
import static com.migratebird.structure.model.DbItemType.VIEW;

import java.util.HashSet;
import java.util.Set;

import com.migratebird.MainFactory;
import com.migratebird.config.FactoryWithDatabase;
import com.migratebird.script.executedscriptinfo.ExecutedScriptInfoSource;
import com.migratebird.structure.clear.impl.DefaultDBClearer;
import com.migratebird.structure.constraint.ConstraintsDisabler;
import com.migratebird.structure.model.DbItemIdentifier;

/**
*/
public class DBClearerFactory extends FactoryWithDatabase<DBClearer> {


    public DBClearer createInstance() {
        Set<DbItemIdentifier> itemsToPreserve = getItemsToPreserve();

        MainFactory mainFactory = factoryWithDatabaseContext.getMainFactory();
        ConstraintsDisabler constraintsDisabler = mainFactory.createConstraintsDisabler();
        ExecutedScriptInfoSource executedScriptInfoSource = mainFactory.createExecutedScriptInfoSource();

        return new DefaultDBClearer(getDatabases(), itemsToPreserve, constraintsDisabler, executedScriptInfoSource);
    }


    protected Set<DbItemIdentifier> getItemsToPreserve() {
        DbItemIdentifier executedScriptsTable = factoryWithDatabaseContext.getExecutedScriptsTable();
        Set<DbItemIdentifier> schemasToPreserve = factoryWithDatabaseContext.getSchemasToPreserve(PROPERTY_PRESERVE_SCHEMAS);

        Set<DbItemIdentifier> itemsToPreserve = new HashSet<DbItemIdentifier>();
        itemsToPreserve.add(executedScriptsTable);
        itemsToPreserve.addAll(schemasToPreserve);
        factoryWithDatabaseContext.addItemsToPreserve(TABLE, PROPERTY_PRESERVE_TABLES, itemsToPreserve);
        factoryWithDatabaseContext.addItemsToPreserve(VIEW, PROPERTY_PRESERVE_VIEWS, itemsToPreserve);
        factoryWithDatabaseContext.addItemsToPreserve(MATERIALIZED_VIEW, PROPERTY_PRESERVE_MATERIALIZED_VIEWS, itemsToPreserve);
        factoryWithDatabaseContext.addItemsToPreserve(SYNONYM, PROPERTY_PRESERVE_SYNONYMS, itemsToPreserve);
        factoryWithDatabaseContext.addItemsToPreserve(SEQUENCE, PROPERTY_PRESERVE_SEQUENCES, itemsToPreserve);
        factoryWithDatabaseContext.addItemsToPreserve(TRIGGER, PROPERTY_PRESERVE_TRIGGERS, itemsToPreserve);
        factoryWithDatabaseContext.addItemsToPreserve(TYPE, PROPERTY_PRESERVE_TYPES, itemsToPreserve);
        return itemsToPreserve;
    }
}
