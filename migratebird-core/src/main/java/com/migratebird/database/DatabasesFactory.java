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
package com.migratebird.database;

import static org.apache.commons.lang.StringUtils.isBlank;
import static com.migratebird.util.ReflectionUtils.createInstanceOfType;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.migratebird.config.Config;
import com.migratebird.util.MigrateBirdException;

/**
*/
public class DatabasesFactory {

    protected Config config;
    protected DatabaseConnectionManager databaseConnectionManager;
    protected IdentifierProcessorFactory identifierProcessorFactory;


    public DatabasesFactory(Config config, DatabaseConnectionManager databaseConnectionManager) {
        this.config = config;
        this.databaseConnectionManager = databaseConnectionManager;
        this.identifierProcessorFactory = new IdentifierProcessorFactory(config);
    }


    public Databases createDatabases() {
        List<Database> databases = new ArrayList<Database>();
        List<String> disabledDatabaseNames = new ArrayList<String>();

        Database defaultDatabase = null;
        for (DatabaseConnection databaseConnection : databaseConnectionManager.getDatabaseConnections()) {
            DatabaseInfo databaseInfo = databaseConnection.getDatabaseInfo();

            boolean disabled = databaseInfo.isDisabled();
            if (disabled) {
                disabledDatabaseNames.add(databaseInfo.getName());
            }

            if (databaseInfo.isDefaultDatabase()) {
                defaultDatabase = createDatabase(databaseConnection);
                if (!disabled) {
                    databases.add(defaultDatabase);
                }

            } else if (!disabled) {
                databases.add(createDatabase(databaseConnection));
            }
        }
        return new Databases(defaultDatabase, databases, disabledDatabaseNames);
    }


    protected Database createDatabase(DatabaseConnection databaseConnection) {
        String databaseDialect = getDatabaseDialect(databaseConnection);
        DataSource dataSource = databaseConnection.getDataSource();
        String defaultSchemaName = databaseConnection.getDatabaseInfo().getDefaultSchemaName();
        IdentifierProcessor identifierProcessor = identifierProcessorFactory.createIdentifierProcessor(databaseDialect, defaultSchemaName, dataSource);

        Class<Database> clazz = config.getConfiguredClass(Database.class, databaseDialect);
        return createInstanceOfType(clazz, false,
                new Class<?>[]{DatabaseConnection.class, IdentifierProcessor.class},
                new Object[]{databaseConnection, identifierProcessor}
        );
    }


    protected String getDatabaseDialect(DatabaseConnection databaseConnection) {
        DatabaseInfo databaseInfo = databaseConnection.getDatabaseInfo();
        String dialect = databaseInfo.getDialect();
        if (isBlank(dialect)) {
            dialect = DatabaseDialectDetector.autoDetectDatabaseDialect(databaseInfo.getUrl());
            if (dialect == null) {
                throw new MigrateBirdException("Unable to determine dialect from jdbc url. Please specify the dialect explicitly. E.g oracle, hsqldb, mysql, db2, postgresql, derby or mssql.");
            }
        }
        return dialect;
    }
}
