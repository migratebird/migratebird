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
package com.migratebird.maven.plugin;

import com.migratebird.launch.task.CleanDatabaseTask;
import com.migratebird.launch.task.MigrateBirdDatabase;
import com.migratebird.launch.task.MigrateBirdTask;

import java.util.List;

/**
 * Task that removes the data of all database tables.
 * The MIGRATEBIRD_SCRIPTS table will not be cleaned.
 *
* @goal cleanDatabase
 */
public class CleanDatabaseMojo extends BaseDatabaseMojo {

    @Override
    protected MigrateBirdTask createMigrateBirdTask(List<MigrateBirdDatabase> migrateBirdDatabases) {
        return new CleanDatabaseTask(migrateBirdDatabases);
    }
}
