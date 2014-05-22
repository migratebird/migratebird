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
package com.migratebird.database.impl;

import static com.migratebird.utils.DbUtils.closeQuietly;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import com.migratebird.database.AbstractDatabase;
import com.migratebird.database.DatabaseConnection;
import com.migratebird.database.DatabaseException;
import com.migratebird.database.IdentifierProcessor;


/**
 * Implementation of {@link com.migratebird.database.AbstractDatabase} for a hsqldb database
 *
*/
public class InformixDatabase extends AbstractDatabase {

    public InformixDatabase(DatabaseConnection databaseConnection, IdentifierProcessor identifierProcessor) {
        super(databaseConnection, identifierProcessor);
    }

    @Override
    public String getSupportedDatabaseDialect() {
        return "informix";
    }

    @Override
    public Set<String> getTableNames(String schemaName) {
        return getSQLHandler().getItemsAsStringSet("select tabname from systables where owner = '" + schemaName + "' and tabid > 99 and tabtype = 'T'", getDataSource());
    }

    @Override
    public Set<String> getColumnNames(String schemaName, String tableName) {
        return getSQLHandler().getItemsAsStringSet("select sc.colname from syscolumns sc join systables st on sc.tabid = st.tabid and st.tabname = '" +
                tableName + "' and st.owner = '" + schemaName + "'", getDataSource());
    }

    @Override
    public Set<String> getViewNames(String schemaName) {
        return getSQLHandler().getItemsAsStringSet("select tabname from systables where owner = '" + schemaName + "' and tabid > 99 and tabtype = 'V'", getDataSource());
    }

    @Override
    public void disableReferentialConstraints(String schemaName) {
        disableConstraints(schemaName, "R");
    }

    @Override
    public void disableValueConstraints(String schemaName) {
        disableConstraints(schemaName, "N");
    }

    protected void disableConstraints(String schemaName, String constraintType) {
        Connection connection = null;
        Statement queryStatement = null;
        Statement alterStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getDataSource().getConnection();
            queryStatement = connection.createStatement();
            alterStatement = connection.createStatement();
            resultSet = queryStatement.executeQuery("SELECT SC.CONSTRNAME CONSTRAINTNAME FROM SYSCONSTRAINTS SC JOIN SYSTABLES ST " +
                    "ON SC.TABID = ST.TABID WHERE ST.OWNER='" + schemaName + "' AND SC.CONSTRTYPE='" + constraintType + "'");
            while (resultSet.next()) {
                String constraintName = resultSet.getString("CONSTRAINTNAME");
                alterStatement.executeUpdate("SET CONSTRAINTS " + quoted(constraintName) + " DISABLED");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Unable to disable value constraints for schema name: " + schemaName, e);
        } finally {
            closeQuietly(queryStatement);
            closeQuietly(connection, alterStatement, resultSet);
        }
    }
}
