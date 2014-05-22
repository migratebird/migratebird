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

import static com.migratebird.config.MigratebirdProperties.PROPERTY_AUTO_CREATE_MIGRATEBIRD_SCRIPTS_TABLE;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_EXCLUDED_QUALIFIERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_INCLUDED_QUALIFIERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_QUALIFIERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_FILE_EXTENSIONS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_LOCATIONS;

import java.util.List;

import com.migratebird.DbMaintainer;
import com.migratebird.MainFactory;

/**
 * Task that marks the database as up-to-date, without executing any script. You can use this operation to prepare
 * an existing database to be managed by DbMaintain, or after having manually fixed a problem.
 *
*/
public class MarkDatabaseAsUpToDateTask extends DbMaintainDatabaseTask {

    protected String scriptLocations;
    protected Boolean autoCreateDbMaintainScriptsTable;
    protected String qualifiers;
    protected String includedQualifiers;
    protected String excludedQualifiers;
    protected String scriptFileExtensions;


    public MarkDatabaseAsUpToDateTask() {
    }

    public MarkDatabaseAsUpToDateTask(List<DbMaintainDatabase> taskDatabases, String scriptLocations, Boolean autoCreateDbMaintainScriptsTable, String qualifiers, String includedQualifiers, String excludedQualifiers, String scriptFileExtensions) {
        super(taskDatabases);
        this.scriptLocations = scriptLocations;
        this.autoCreateDbMaintainScriptsTable = autoCreateDbMaintainScriptsTable;
        this.qualifiers = qualifiers;
        this.includedQualifiers = includedQualifiers;
        this.excludedQualifiers = excludedQualifiers;
        this.scriptFileExtensions = scriptFileExtensions;
    }


    @Override
    protected void addTaskConfiguration(TaskConfiguration taskConfiguration) {
        taskConfiguration.addDatabaseConfigurations(databases);
        taskConfiguration.addConfigurationIfSet(PROPERTY_SCRIPT_LOCATIONS, scriptLocations);
        taskConfiguration.addConfigurationIfSet(PROPERTY_AUTO_CREATE_MIGRATEBIRD_SCRIPTS_TABLE, autoCreateDbMaintainScriptsTable);
        taskConfiguration.addConfigurationIfSet(PROPERTY_QUALIFIERS, qualifiers);
        taskConfiguration.addConfigurationIfSet(PROPERTY_INCLUDED_QUALIFIERS, includedQualifiers);
        taskConfiguration.addConfigurationIfSet(PROPERTY_EXCLUDED_QUALIFIERS, excludedQualifiers);
        taskConfiguration.addConfigurationIfSet(PROPERTY_SCRIPT_FILE_EXTENSIONS, scriptFileExtensions);
    }

    @Override
    protected boolean doExecute(MainFactory mainFactory) {
        DbMaintainer migratebird = mainFactory.createDbMaintainer();
        migratebird.markDatabaseAsUpToDate();
        return true;
    }


    public void setScriptLocations(String scriptLocations) {
        this.scriptLocations = scriptLocations;
    }

    public void setAutoCreateDbMaintainScriptsTable(Boolean autoCreateDbMaintainScriptsTable) {
        this.autoCreateDbMaintainScriptsTable = autoCreateDbMaintainScriptsTable;
    }

    public void setQualifiers(String qualifiers) {
        this.qualifiers = qualifiers;
    }

    public void setIncludedQualifiers(String includedQualifiers) {
        this.includedQualifiers = includedQualifiers;
    }

    public void setExcludedQualifiers(String excludedQualifiers) {
        this.excludedQualifiers = excludedQualifiers;
    }

    public void setScriptFileExtensions(String scriptFileExtensions) {
        this.scriptFileExtensions = scriptFileExtensions;
    }
}