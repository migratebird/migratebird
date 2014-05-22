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
package com.migratebird.maven.plugin;

import com.migratebird.launch.task.ClearDatabaseTask;
import com.migratebird.launch.task.DbMaintainDatabase;
import com.migratebird.launch.task.DbMaintainTask;

import java.util.List;

/**
 * Task that removes all database items like tables, views etc from the database and empties the MIGRATEBIRD_SCRIPTS table.
 *
* @goal clearDatabase
 */
public class ClearDatabaseMojo extends BaseDatabaseMojo {

    @Override
    protected DbMaintainTask createDbMaintainTask(List<DbMaintainDatabase> dbMaintainDatabases) {
        return new ClearDatabaseTask(dbMaintainDatabases);
    }
}
