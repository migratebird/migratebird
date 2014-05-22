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
package com.migratebird.database.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.migratebird.config.Config;
import com.migratebird.database.DatabaseConnection;
import com.migratebird.database.DatabaseConnectionManager;
import com.migratebird.database.DatabaseException;
import com.migratebird.database.DatabaseInfo;
import com.migratebird.database.DatabaseInfoFactory;
import com.migratebird.database.SQLHandler;
import com.migratebird.datasource.DataSourceFactory;
import com.migratebird.util.MigrateBirdException;

/**
*/
public class DefaultDatabaseConnectionManager implements DatabaseConnectionManager {

    protected List<DatabaseInfo> databaseInfos;
    protected SQLHandler sqlHandler;
    protected DatabaseInfoFactory databaseInfoFactory;
    protected DataSourceFactory dataSourceFactory;
    protected Map<String, DataSource> dataSourcesPerDatabaseName;

    protected Map<String, DatabaseConnection> databaseConnectionsPerDatabaseName = new HashMap<String, DatabaseConnection>();


    public DefaultDatabaseConnectionManager(Config config, SQLHandler sqlHandler, DataSourceFactory dataSourceFactory) {
        this(config, sqlHandler, dataSourceFactory, new HashMap<String, DataSource>());
    }

    public DefaultDatabaseConnectionManager(Config config, SQLHandler sqlHandler, DataSourceFactory dataSourceFactory, Map<String, DataSource> dataSourcesPerDatabaseName) {
        this.sqlHandler = sqlHandler;
        this.databaseInfoFactory = createDatabaseInfoFactory(config);
        this.dataSourceFactory = dataSourceFactory;
        this.dataSourcesPerDatabaseName = dataSourcesPerDatabaseName;
    }


    public SQLHandler getSqlHandler() {
        return sqlHandler;
    }

    /**
     * Gets the connection for the database with the given name.
     * The data source will be null if the database is disabled
     * The default database always has a data source even if disabled
     *
     * @param databaseName The name
     * @return The connection, not null
     */
    public DatabaseConnection getDatabaseConnection(String databaseName) {
        DatabaseConnection databaseConnection = databaseConnectionsPerDatabaseName.get(databaseName);
        if (databaseConnection == null) {
            databaseConnection = createDatabaseConnection(databaseName);
            databaseConnectionsPerDatabaseName.put(databaseName, databaseConnection);
        }
        return databaseConnection;
    }

    public List<DatabaseConnection> getDatabaseConnections() {
        List<DatabaseConnection> result = new ArrayList<DatabaseConnection>();
        for (DatabaseInfo databaseInfo : getDatabaseInfos()) {
            DatabaseConnection databaseConnection = getDatabaseConnection(databaseInfo.getName());
            result.add(databaseConnection);
        }
        return result;
    }


    protected DatabaseConnection createDatabaseConnection(String databaseName) {
        DatabaseInfo databaseInfo = getDatabaseInfo(databaseName);
        DataSource dataSource = null;
        if (!databaseInfo.isDisabled() || databaseInfo.isDefaultDatabase()) {
            dataSource = dataSourcesPerDatabaseName.get(databaseName);
            if (dataSource == null) {
                dataSource = dataSourceFactory.createDataSource(databaseInfo);
            }
        }
        return new DatabaseConnection(databaseInfo, sqlHandler, dataSource);
    }


    protected DatabaseInfo getDatabaseInfo(String databaseName) {
        for (DatabaseInfo databaseInfo : getDatabaseInfos()) {
            if (databaseInfo.hasName(databaseName)) {
                databaseInfo.validateMinimal();
                return databaseInfo;
            }
        }
        throw new DatabaseException("No database configuration found for " + (databaseName == null ? "default database" : "database with name " + databaseName) + ".");
    }

    protected List<DatabaseInfo> getDatabaseInfos() {
        if (databaseInfos == null) {
            databaseInfos = databaseInfoFactory.createDatabaseInfos();
            if (databaseInfos == null || databaseInfos.isEmpty()) {
                throw new MigrateBirdException("No database configuration found. At least one database should be defined in the properties or in the task configuration.");
            }
        }
        return databaseInfos;
    }


    protected DatabaseInfoFactory createDatabaseInfoFactory(Config configuration) {
        return new DatabaseInfoFactory(configuration);
    }
}
