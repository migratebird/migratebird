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

import static com.migratebird.database.StoredIdentifierCase.LOWER_CASE;
import static com.migratebird.database.StoredIdentifierCase.MIXED_CASE;
import static com.migratebird.database.StoredIdentifierCase.UPPER_CASE;
import static com.migratebird.utils.DbUtils.closeQuietly;
import static org.apache.commons.lang.StringUtils.trimToNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.migratebird.config.Config;

/**
*/
public class IdentifierProcessorFactory {

    protected Config config;

    public IdentifierProcessorFactory(Config configuration) {
        this.config = configuration;
    }


    public IdentifierProcessor createIdentifierProcessor(String databaseDialect, String defaultSchemaName, DataSource dataSource) {
        String customIdentifierQuoteString = config.getCustomIdentifierQuoteString(databaseDialect);
        StoredIdentifierCase customStoredIdentifierCase = config.getCustomStoredIdentifierCase(databaseDialect);

        StoredIdentifierCase storedIdentifierCase = determineStoredIdentifierCase(customStoredIdentifierCase, dataSource);
        String identifierQuoteString = determineIdentifierQuoteString(customIdentifierQuoteString, dataSource);
        return new IdentifierProcessor(storedIdentifierCase, identifierQuoteString, defaultSchemaName);
    }


    /**
     * Determines the case the database uses to store non-quoted identifiers. This will use the connections
     * database metadata to determine the correct case.
     *
     * @param customStoredIdentifierCase The stored case: possible values 'lower_case', 'upper_case', 'mixed_case' and 'auto'
     * @param dataSource                 The datas ource, not null
     * @return The stored case, not null
     */
    protected StoredIdentifierCase determineStoredIdentifierCase(StoredIdentifierCase customStoredIdentifierCase, DataSource dataSource) {
        if (customStoredIdentifierCase != null) {
            return customStoredIdentifierCase;
        }

        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            DatabaseMetaData databaseMetaData = connection.getMetaData();
            if (databaseMetaData.storesUpperCaseIdentifiers()) {
                return UPPER_CASE;
            } else if (databaseMetaData.storesLowerCaseIdentifiers()) {
                return LOWER_CASE;
            } else {
                return MIXED_CASE;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Unable to determine stored identifier case.", e);
        } finally {
            closeQuietly(connection, null, null);
        }
    }

    /**
     * Determines the string used to quote identifiers to make them case-sensitive. This will use the connections
     * database metadata to determine the quote string.
     *
     * @param customIdentifierQuoteString If not null, it specifies a custom identifier quote string that replaces the one
     *                                    specified by the JDBC DatabaseMetaData object
     * @param dataSource                  The datas ource, not null
     * @return The quote string, null if quoting is not supported
     */
    protected String determineIdentifierQuoteString(String customIdentifierQuoteString, DataSource dataSource) {
        if (customIdentifierQuoteString != null) {
            return trimToNull(customIdentifierQuoteString);
        }

        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String quoteString = databaseMetaData.getIdentifierQuoteString();
            return trimToNull(quoteString);

        } catch (SQLException e) {
            throw new DatabaseException("Unable to determine identifier quote string.", e);
        } finally {
            closeQuietly(connection, null, null);
        }
    }

}
