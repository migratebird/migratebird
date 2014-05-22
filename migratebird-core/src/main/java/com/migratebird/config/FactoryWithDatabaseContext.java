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

import static com.migratebird.structure.model.DbItemIdentifier.getItemIdentifier;
import static com.migratebird.structure.model.DbItemIdentifier.parseItemIdentifier;
import static com.migratebird.structure.model.DbItemIdentifier.parseSchemaIdentifier;
import static com.migratebird.structure.model.DbItemType.TABLE;
import static com.migratebird.util.ReflectionUtils.createInstanceOfType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.migratebird.MainFactory;
import com.migratebird.database.Database;
import com.migratebird.database.Databases;
import com.migratebird.database.SQLHandler;
import com.migratebird.script.parser.ScriptParserFactory;
import com.migratebird.structure.model.DbItemIdentifier;
import com.migratebird.structure.model.DbItemType;

/**
*/
public class FactoryWithDatabaseContext extends FactoryContext {

    private Databases databases;
    private SQLHandler sqlHandler;

    public FactoryWithDatabaseContext(Config config, MainFactory mainFactory, Databases databases, SQLHandler sqlHandler) {
        super(config, mainFactory);
        this.databases = databases;
        this.sqlHandler = sqlHandler;
    }

    public DbItemIdentifier getExecutedScriptsTable() {
        String executedScriptsTableName = getConfiguration().getExecutedScriptsTableName();
        Database defaultDatabase = databases.getDefaultDatabase();
        return getItemIdentifier(TABLE, defaultDatabase.getDefaultSchemaName(), executedScriptsTableName, defaultDatabase, true);
    }

    /**
     * @param propertyPreserveSchemas The preserve property name, not null
     * @return The configured set of schemas to preserve, not null
     */
    public Set<DbItemIdentifier> getSchemasToPreserve(String propertyPreserveSchemas) {
        Set<DbItemIdentifier> result = new HashSet<DbItemIdentifier>();
        List<String> schemasToPreserve = getConfiguration().getStringList(propertyPreserveSchemas);
        for (String schemaToPreserve : schemasToPreserve) {
            DbItemIdentifier itemIdentifier = parseSchemaIdentifier(schemaToPreserve, databases);
            if (itemIdentifier == null) {
                // the database is disabled, ignore item identifier
                continue;
            }
            result.add(itemIdentifier);
        }
        return result;
    }

    /**
     * Adds the items to preserve configured by the given property to the given list.
     * 
     * @param dbItemType The type of item, not null
     * @param itemsToPreserveProperty The property to get the preserved items, not null
     * @param itemsToPreserve The set to add the items to, not null
     */
    public void addItemsToPreserve(DbItemType dbItemType, String itemsToPreserveProperty, Set<DbItemIdentifier> itemsToPreserve) {
        List<String> items = getConfiguration().getStringList(itemsToPreserveProperty);
        for (String itemToPreserve : items) {
            DbItemIdentifier itemIdentifier = parseItemIdentifier(dbItemType, itemToPreserve, databases);
            if (itemIdentifier == null) {
                // the database is disabled, ignore item identifier
                continue;
            }
            itemsToPreserve.add(itemIdentifier);
        }
    }

    public Map<String, ScriptParserFactory> getDatabaseDialectScriptParserFactoryMap() {
        Map<String, ScriptParserFactory> databaseDialectScriptParserClassMap = new HashMap<String, ScriptParserFactory>();
        boolean backSlashEscapingEnabled = getConfiguration().getBackSlashEscapingEnabled();
        Properties scriptParameters = getScriptParameters();
        for (String databaseDialect : getDatabaseDialectsInUse()) {
            Class<? extends ScriptParserFactory> scriptParserFactoryClass = getConfiguration().getConfiguredClass(ScriptParserFactory.class,
                    databaseDialect);
            ScriptParserFactory factory = createInstanceOfType(scriptParserFactoryClass, false, new Class<?>[] { boolean.class, Properties.class },
                    new Object[] { backSlashEscapingEnabled, scriptParameters });
            databaseDialectScriptParserClassMap.put(databaseDialect, factory);
        }
        return databaseDialectScriptParserClassMap;
    }

    protected Properties getScriptParameters() {
        String scriptParameterFile = getConfiguration().getScriptParameterFile();
        try {
            Properties scriptParameters = null;
            if (scriptParameterFile != null) {
                scriptParameters = new Properties();
                String scriptEncoding = getConfiguration().getScriptEncoding();
                scriptParameters.load(new InputStreamReader(new FileInputStream(scriptParameterFile), scriptEncoding));
            }
            return scriptParameters;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load script parameter file " + scriptParameterFile, e);
        }
    }

    public Set<String> getDatabaseDialectsInUse() {
        Set<String> dialects = new HashSet<String>();
        for (Database database : databases.getDatabases()) {
            if (database != null) {
                dialects.add(database.getSupportedDatabaseDialect());
            }
        }
        return dialects;
    }

    public Databases getDatabases() {
        return databases;
    }

    public SQLHandler getSqlHandler() {
        return sqlHandler;
    }
}
