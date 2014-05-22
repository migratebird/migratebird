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
package com.migratebird.database;

import static com.migratebird.config.MigratebirdProperties.PROPERTY_DATABASE_START;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_DIALECT_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_DRIVERCLASSNAME_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_INCLUDED_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PASSWORD_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCHEMANAMES_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_URL_END;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_USERNAME_END;

import java.util.ArrayList;
import java.util.List;

import com.migratebird.config.Config;

public class DatabaseInfoFactory {

    protected Config config;

    public DatabaseInfoFactory(Config config) {
        this.config = config;
    }


    public List<DatabaseInfo> createDatabaseInfos() {
        List<DatabaseInfo> databaseInfos = new ArrayList<DatabaseInfo>();

        List<String> databaseNames = config.getDatabaseNames();
        if (databaseNames.isEmpty()) {
            databaseInfos.add(getUnnamedDatabaseInfo());
            return databaseInfos;
        }

        // the first database is the default database
        boolean defaultDatabase = true;
        for (String databaseName : databaseNames) {
            boolean disabled = !isDatabaseIncluded(databaseName);
            DatabaseInfo databaseInfo = createDatabaseInfo(databaseName, disabled, defaultDatabase);
            databaseInfos.add(databaseInfo);
            defaultDatabase = false;
        }
        return databaseInfos;
    }


    protected DatabaseInfo getUnnamedDatabaseInfo() {
        String driverClassName = config.getProperty(null, PROPERTY_DRIVERCLASSNAME_END);
        String url = config.getProperty(null, PROPERTY_URL_END);
        String userName = config.getProperty(null, PROPERTY_USERNAME_END);
        String password = config.getProperty(null, PROPERTY_PASSWORD_END);
        String databaseDialect = config.getProperty(null, PROPERTY_DIALECT_END);
        List<String> schemaNames = config.getListDatabaseProperty(null, PROPERTY_SCHEMANAMES_END);
        return new DatabaseInfo(null, databaseDialect, driverClassName, url, userName, password, schemaNames, false, true);
    }


    /**
     * @param databaseName    The name that identifies the database, not null
     * @param disabled        True if this database is disabled
     * @param defaultDatabase True if this database is the default database, there should only be 1 default database
     * @return a DataSource that connects with the database as configured for the given database name
     */
    protected DatabaseInfo createDatabaseInfo(String databaseName, boolean disabled, boolean defaultDatabase) {
        String driverClassName = config.getProperty(databaseName, PROPERTY_DRIVERCLASSNAME_END);
        String url = config.getProperty(databaseName, PROPERTY_URL_END);
        String userName = config.getProperty(databaseName, PROPERTY_USERNAME_END);
        String password = config.getProperty(databaseName, PROPERTY_PASSWORD_END);
        String databaseDialect = config.getProperty(databaseName, PROPERTY_DIALECT_END);
        List<String> schemaNames = config.getListDatabaseProperty(databaseName, PROPERTY_SCHEMANAMES_END);
        return new DatabaseInfo(databaseName, databaseDialect, driverClassName, url, userName, password, schemaNames, disabled, defaultDatabase);
    }

    /**
     * @param databaseName the logical name that identifies the database
     * @return whether the database with the given name is included in the set of database to be updated by db maintain
     */
    protected boolean isDatabaseIncluded(String databaseName) {
        return config.getBoolean(PROPERTY_DATABASE_START + '.' + databaseName + '.' + PROPERTY_INCLUDED_END, true);
    }

}
