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
package com.migratebird.launch.task;

import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
*/
public class TaskConfigurationAddDatabaseConfigurationsTest {

    /* Tested object */
    private TaskConfiguration taskConfiguration;

    private Properties configuration;

    @Before
    public void initialize() {
        configuration = new Properties();
        taskConfiguration = new TaskConfiguration(configuration);
    }

    @Test
    public void addUnnamedDatabase() {
        DbMaintainDatabase unnamedDatabase = new DbMaintainDatabase(null, true, "dialect", "driver", "url", "user", "pass", "schemas", null);
        taskConfiguration.addDatabaseConfigurations(asList(unnamedDatabase));

        assertNamedDatabasePropertiesSet("<unnamed>");
        assertPropertySet("databases.names", "<unnamed>");
    }

    @Test
    public void addNamedDatabase() {
        DbMaintainDatabase namedDatabase = new DbMaintainDatabase("db1", true, "dialect", "driver", "url", "user", "pass", "schemas", null);
        taskConfiguration.addDatabaseConfigurations(asList(namedDatabase));

        assertNamedDatabasePropertiesSet("db1");
        assertPropertySet("databases.names", "db1");
    }

    @Test
    public void addNamedAndUnnamedDatabase() {
        DbMaintainDatabase unnamedDatabase = new DbMaintainDatabase(null, true, "dialect", "driver", "url", "user", "pass", "schemas", null);
        DbMaintainDatabase namedDatabase = new DbMaintainDatabase("db1", true, "dialect", "driver", "url", "user", "pass", "schemas", null);
        taskConfiguration.addDatabaseConfigurations(asList(unnamedDatabase, namedDatabase));

        assertNamedDatabasePropertiesSet("<unnamed>");
        assertNamedDatabasePropertiesSet("db1");
        assertPropertySet("databases.names", "<unnamed>,db1");
    }

    @Test
    public void unnamedDatabaseShouldBePutAsFirstDatabase() {
        DbMaintainDatabase namedDatabase = new DbMaintainDatabase("db1", true, "dialect", "driver", "url", "user", "pass", "schemas", null);
        DbMaintainDatabase unnamedDatabase = new DbMaintainDatabase(null, true, "dialect", "driver", "url", "user", "pass", "schemas", null);
        taskConfiguration.addDatabaseConfigurations(asList(namedDatabase, unnamedDatabase));

        assertNamedDatabasePropertiesSet("<unnamed>");
        assertNamedDatabasePropertiesSet("db1");
        assertPropertySet("databases.names", "<unnamed>,db1");
    }

    @Test
    public void addMultipleNamedDatabases() {
        DbMaintainDatabase namedDatabase1 = new DbMaintainDatabase("db1", true, "dialect", "driver", "url", "user", "pass", "schemas", null);
        DbMaintainDatabase namedDatabase2 = new DbMaintainDatabase("db2", true, "dialect", "driver", "url", "user", "pass", "schemas", null);
        taskConfiguration.addDatabaseConfigurations(asList(namedDatabase1, namedDatabase2));

        assertNamedDatabasePropertiesSet("db1");
        assertNamedDatabasePropertiesSet("db2");
        assertPropertySet("databases.names", "db1,db2");
    }

    private void assertNamedDatabasePropertiesSet(String name) {
        assertPropertySet("database." + name + ".dialect", "dialect");
        assertPropertySet("database." + name + ".driverClassName", "driver");
        assertPropertySet("database." + name + ".url", "url");
        assertPropertySet("database." + name + ".userName", "user");
        assertPropertySet("database." + name + ".password", "pass");
        assertPropertySet("database." + name + ".schemaNames", "schemas");
        assertPropertySet("database." + name + ".included", "true");
    }

    private void assertPropertySet(String property, String expected) {
        String actual = configuration.getProperty(property);
        assertEquals(expected, actual);
    }
}
