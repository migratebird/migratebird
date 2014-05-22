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
package com.migratebird.script.runner.impl;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.Reader;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.migratebird.database.Database;
import com.migratebird.database.Databases;
import com.migratebird.database.SQLHandler;
import com.migratebird.script.Script;
import com.migratebird.script.parser.ScriptParser;
import com.migratebird.script.parser.ScriptParserFactory;
import com.migratebird.script.runner.ScriptRunner;
import com.migratebird.util.MigrateBirdException;

/**
 * Default implementation of a script runner that uses JDBC to execute the script.
 *
*/
public class JdbcScriptRunner implements ScriptRunner {

    /* The logger instance for this class */
    private static Log logger = LogFactory.getLog(JdbcScriptRunner.class);

    protected Databases databases;
    protected SQLHandler sqlHandler;
    protected Map<String, ScriptParserFactory> databaseDialectScriptParserFactoryMap;


    public JdbcScriptRunner(Map<String, ScriptParserFactory> databaseDialectScriptParserFactoryMap, Databases databases, SQLHandler sqlHandler) {
        this.databaseDialectScriptParserFactoryMap = databaseDialectScriptParserFactoryMap;
        this.databases = databases;
        this.sqlHandler = sqlHandler;
    }


    /**
     * Executes the given script.
     * <p/>
     * All statements should be separated with a semicolon (;). The last statement will be
     * added even if it does not end with a semicolon.
     *
     * @param script The script, not null
     */
    public void execute(Script script) {
        Reader scriptContentReader = null;
        try {
            // Define the target database on which to execute the script
            Database targetDatabase = getTargetDatabaseDatabase(script);
            if (targetDatabase == null) {
                logger.info("Script " + script.getFileName() + " has target database " + script.getTargetDatabaseName() + ". This database is disabled, so the script is not executed.");
                return;
            }

            // get content stream
            scriptContentReader = script.getScriptContentHandle().openScriptContentReader();
            // create a script parser for the target database in question 
            ScriptParser scriptParser = databaseDialectScriptParserFactoryMap.get(targetDatabase.getSupportedDatabaseDialect()).createScriptParser(scriptContentReader);
            // parse and execute the statements
            parseAndExecuteScript(targetDatabase, scriptParser);

        } finally {
            closeQuietly(scriptContentReader);
        }
    }

    private void parseAndExecuteScript(Database targetDatabase, ScriptParser scriptParser) {
        DataSource dataSource = targetDatabase.getDataSource();
        try {
            sqlHandler.startTransaction(dataSource);

            String statement;
            while ((statement = scriptParser.getNextStatement()) != null) {
                //trgy
                // if (statement.startsWith("@")){
                //     String include
                // }
                sqlHandler.execute(statement, dataSource);
            }
            sqlHandler.endTransactionAndCommit(dataSource);

        } catch (MigrateBirdException e) {
            sqlHandler.endTransactionAndRollback(dataSource);
            throw e;
        }
    }

    @Override
	public void initialize() {
        // nothing to initialize
    }

    @Override
	public void close() {
        // nothing to close
    }

    /**
     * @param script The script, not null
     * @return The db support to use for the script, not null
     */
    protected Database getTargetDatabaseDatabase(Script script) {
        String databaseName = script.getTargetDatabaseName();
        if (databaseName == null) {
            Database database = databases.getDefaultDatabase();
            if (database.getDatabaseInfo().isDisabled()) {
                return null;
            }
            return database;
        }
        if (!databases.isConfiguredDatabase(databaseName)) {
            throw new MigrateBirdException("Error executing script " + script.getFileName() + ". No database initialized with the name " + script.getTargetDatabaseName());
        }
        return databases.getDatabase(databaseName);
    }

}
