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
package com.migratebird;

import static com.migratebird.util.ReflectionUtils.createInstanceOfType;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.migratebird.config.Config;
import com.migratebird.config.Factory;
import com.migratebird.config.FactoryContext;
import com.migratebird.config.FactoryWithDatabase;
import com.migratebird.config.FactoryWithDatabaseContext;
import com.migratebird.config.FactoryWithoutDatabase;
import com.migratebird.database.DatabaseConnectionManager;
import com.migratebird.database.Databases;
import com.migratebird.database.DatabasesFactory;
import com.migratebird.database.SQLHandler;
import com.migratebird.database.impl.DefaultDatabaseConnectionManager;
import com.migratebird.database.impl.DefaultSQLHandler;
import com.migratebird.datasource.DataSourceFactory;
import com.migratebird.datasource.impl.SimpleDataSourceFactory;
import com.migratebird.script.archive.ScriptArchiveCreator;
import com.migratebird.script.executedscriptinfo.ExecutedScriptInfoSource;
import com.migratebird.script.runner.ScriptRunner;
import com.migratebird.structure.clean.DBCleaner;
import com.migratebird.structure.clear.DBClearer;
import com.migratebird.structure.constraint.ConstraintsDisabler;
import com.migratebird.structure.generate.DefaultUpdateSqlGenerator;
import com.migratebird.structure.generate.UpdateSqlGenerator;
import com.migratebird.structure.sequence.SequenceUpdater;

/**
*/
public class MainFactory {

    protected Config config;
    protected SQLHandler sqlHandler;
    protected DatabaseConnectionManager databaseConnectionManager;
    protected Map<String, DataSource> dataSourcesPerDatabaseName;
    protected Databases databases;

    protected FactoryContext factoryContext;
    protected FactoryWithDatabaseContext factoryWithDatabaseContext;

    public MainFactory(Config config) {
        this(config, new HashMap<String, DataSource>());
    }

    public MainFactory(Config config, DatabaseConnectionManager databaseConnectionManager) {
        this.config = config;
        this.sqlHandler = databaseConnectionManager.getSqlHandler();
        this.databaseConnectionManager = databaseConnectionManager;
        this.dataSourcesPerDatabaseName = new HashMap<String, DataSource>();
    }

    public MainFactory(Config config, Map<String, DataSource> dataSourcesPerDatabaseName) {
        this.config = config;
        this.sqlHandler = createSqlHandler();
        this.dataSourcesPerDatabaseName = dataSourcesPerDatabaseName;
    }

    public DbUpdate createDbUpdate() {
        return createInstance(DbUpdate.class);
    }

    public DBCleaner createDBCleaner() {
        return createInstance(DBCleaner.class);
    }

    public DBClearer createDBClearer() {
        return createInstance(DBClearer.class);
    }

    public ConstraintsDisabler createConstraintsDisabler() {
        return createInstance(ConstraintsDisabler.class);
    }

    public SequenceUpdater createSequenceUpdater() {
        return createInstance(SequenceUpdater.class);
    }

    public ScriptRunner createScriptRunner() {
        return createInstance(ScriptRunner.class);
    }

    public ExecutedScriptInfoSource createExecutedScriptInfoSource() {
        return createInstance(ExecutedScriptInfoSource.class);
    }

    public ScriptArchiveCreator createScriptArchiveCreator() {
        return createInstance(ScriptArchiveCreator.class);
    }

    @SuppressWarnings({ "unchecked" })
    protected <S> S createInstance(Class<S> type) {
        Factory factory = createFactoryForType(type);
        if (factory instanceof FactoryWithoutDatabase) {
            FactoryContext factoryContext = getFactoryContext();
            ((FactoryWithoutDatabase<?>) factory).init(factoryContext);

        } else if (factory instanceof FactoryWithDatabase) {
            ((FactoryWithDatabase<?>) factory).init(createFactoryWithDatabaseContext());
        }
        return (S) factory.createInstance();
    }

    protected synchronized FactoryContext getFactoryContext() {
        if (factoryContext == null) {
            factoryContext = new FactoryContext(config, this);
        }
        return factoryContext;
    }

    protected synchronized FactoryWithDatabaseContext createFactoryWithDatabaseContext() {
        if (factoryWithDatabaseContext == null) {
            Databases databases = getDatabases();
            factoryWithDatabaseContext = new FactoryWithDatabaseContext(config, this, databases, sqlHandler);
        }
        return factoryWithDatabaseContext;
    }

    @SuppressWarnings({ "unchecked" })
    protected <T extends Factory> T createFactoryForType(Class<?> type) {
        Class<T> clazz = (Class<T>) config.getFactoryClass(type);
        return createInstanceOfType(clazz, false, new Class<?>[0], new Object[0]);
    }

    public Databases getDatabases() {
        if (databases == null) {
            DatabaseConnectionManager databaseConnectionManager = getDatabaseConnectionManager();
            DatabasesFactory databasesFactory = new DatabasesFactory(config, databaseConnectionManager);
            databases = databasesFactory.createDatabases();
        }
        return databases;
    }

    protected DatabaseConnectionManager getDatabaseConnectionManager() {
        if (databaseConnectionManager == null) {
            DataSourceFactory dataSourceFactory = new SimpleDataSourceFactory();
            databaseConnectionManager = new DefaultDatabaseConnectionManager(config, sqlHandler, dataSourceFactory, dataSourcesPerDatabaseName);
        }
        return databaseConnectionManager;
    }

    protected SQLHandler createSqlHandler() {
        return new DefaultSQLHandler();
    }

    public UpdateSqlGenerator createUpdateSqlGenerator() {
        return new DefaultUpdateSqlGenerator();
    }
}
