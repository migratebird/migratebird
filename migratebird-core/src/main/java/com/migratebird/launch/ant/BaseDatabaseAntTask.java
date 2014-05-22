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
package com.migratebird.launch.ant;


import java.util.ArrayList;
import java.util.List;

import com.migratebird.launch.task.MigrateBirdDatabase;


/**
 * Base class for ant tasks that perform operations on a database.
 *
*/
public abstract class BaseDatabaseAntTask extends BaseAntTask {

    protected List<Database> databases = new ArrayList<Database>();

    /**
     * Registers a target database on which a task (e.g. update) can be executed.
     *
     * @param database The configuration of the database
     */
    public void addDatabase(Database database) {
        databases.add(database);
    }

    public List<MigrateBirdDatabase> getMigrateBirdDatabases() {
        List<MigrateBirdDatabase> result = new ArrayList<MigrateBirdDatabase>();
        for (Database database : databases) {
            MigrateBirdDatabase migrateBirdDatabase = new MigrateBirdDatabase();
            migrateBirdDatabase.setName(database.getName());
            migrateBirdDatabase.setIncluded(database.isIncluded());
            migrateBirdDatabase.setDialect(database.getDialect());
            migrateBirdDatabase.setDriverClassName(database.getDriverClassName());
            migrateBirdDatabase.setUrl(database.getUrl());
            migrateBirdDatabase.setUserName(database.getUserName());
            migrateBirdDatabase.setPassword(database.getPassword());
            migrateBirdDatabase.setSchemaNames(database.getSchemaNames());
            result.add(migrateBirdDatabase);
        }
        return result;
    }
}
