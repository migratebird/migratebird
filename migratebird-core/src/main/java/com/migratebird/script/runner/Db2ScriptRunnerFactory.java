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
package com.migratebird.script.runner;

import com.migratebird.config.FactoryWithDatabase;
import com.migratebird.script.runner.impl.db2.Db2ScriptRunner;

/**
*/
public class Db2ScriptRunnerFactory extends FactoryWithDatabase<ScriptRunner> {


    @Override
    public ScriptRunner createInstance() {
        String db2Command = getConfiguration().getDb2Command();
        return new Db2ScriptRunner(getDatabases(), db2Command);
    }
}
