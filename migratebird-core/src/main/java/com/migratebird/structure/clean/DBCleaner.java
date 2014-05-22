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
package com.migratebird.structure.clean;


/**
 * Defines the contract for implementations that delete data from the database, that could cause problems when performing
 * updates to the database, such as adding not null columns or foreign key constraints.
 *
*/
public interface DBCleaner {


    /**
     * Delete all data from all database tables.
     */
    void cleanDatabase();

}