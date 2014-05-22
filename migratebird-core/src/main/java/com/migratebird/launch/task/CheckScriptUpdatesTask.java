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
import static com.migratebird.config.MigratebirdProperties.PROPERTY_FROM_SCRATCH_ENABLED;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_INCLUDED_QUALIFIERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_PATCH_ALLOWOUTOFSEQUENCEEXECUTION;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_POSTPROCESSINGSCRIPT_DIRNAME;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_QUALIFIERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_ENCODING;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_FILE_EXTENSIONS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_LOCATIONS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_SCRIPT_PATCH_QUALIFIERS;
import static com.migratebird.config.MigratebirdProperties.PROPERTY_USESCRIPTFILELASTMODIFICATIONDATES;

import java.util.List;

import com.migratebird.DbMaintainer;
import com.migratebird.MainFactory;

/**
 * Performs a dry run of the database update. May be used to verify if there are any updates or in a test that fails
 * if it appears that an irregular script update was performed.
 *
* @since 10-feb-2009
 */
public class CheckScriptUpdatesTask extends DbMaintainDatabaseTask {

    protected String scriptLocations;
    protected String scriptEncoding;
    protected String postProcessingScriptDirectoryName;
    protected Boolean fromScratchEnabled;
    protected Boolean autoCreateDbMaintainScriptsTable;
    protected Boolean allowOutOfSequenceExecutionOfPatches;
    protected String qualifiers;
    protected String patchQualifiers;
    protected String includedQualifiers;
    protected String excludedQualifiers;
    protected String scriptFileExtensions;
    protected Boolean useLastModificationDates;


    public CheckScriptUpdatesTask() {
    }

    public CheckScriptUpdatesTask(List<DbMaintainDatabase> taskDatabases, String scriptLocations, String scriptEncoding, String postProcessingScriptDirectoryName, Boolean fromScratchEnabled, Boolean autoCreateDbMaintainScriptsTable, Boolean allowOutOfSequenceExecutionOfPatches, String qualifiers, String patchQualifiers, String includedQualifiers, String excludedQualifiers, String scriptFileExtensions, Boolean useLastModificationDates) {
        super(taskDatabases);
        this.scriptLocations = scriptLocations;
        this.scriptEncoding = scriptEncoding;
        this.postProcessingScriptDirectoryName = postProcessingScriptDirectoryName;
        this.fromScratchEnabled = fromScratchEnabled;
        this.autoCreateDbMaintainScriptsTable = autoCreateDbMaintainScriptsTable;
        this.allowOutOfSequenceExecutionOfPatches = allowOutOfSequenceExecutionOfPatches;
        this.qualifiers = qualifiers;
        this.patchQualifiers = patchQualifiers;
        this.includedQualifiers = includedQualifiers;
        this.excludedQualifiers = excludedQualifiers;
        this.scriptFileExtensions = scriptFileExtensions;
        this.useLastModificationDates = useLastModificationDates;
    }


    @Override
    protected void addTaskConfiguration(TaskConfiguration taskConfiguration) {
        taskConfiguration.addDatabaseConfigurations(databases);
        taskConfiguration.addConfigurationIfSet(PROPERTY_SCRIPT_LOCATIONS, scriptLocations);
        taskConfiguration.addConfigurationIfSet(PROPERTY_SCRIPT_ENCODING, scriptEncoding);
        taskConfiguration.addConfigurationIfSet(PROPERTY_POSTPROCESSINGSCRIPT_DIRNAME, postProcessingScriptDirectoryName);
        taskConfiguration.addConfigurationIfSet(PROPERTY_FROM_SCRATCH_ENABLED, fromScratchEnabled);
        taskConfiguration.addConfigurationIfSet(PROPERTY_AUTO_CREATE_MIGRATEBIRD_SCRIPTS_TABLE, autoCreateDbMaintainScriptsTable);
        taskConfiguration.addConfigurationIfSet(PROPERTY_PATCH_ALLOWOUTOFSEQUENCEEXECUTION, allowOutOfSequenceExecutionOfPatches);
        taskConfiguration.addConfigurationIfSet(PROPERTY_QUALIFIERS, qualifiers);
        taskConfiguration.addConfigurationIfSet(PROPERTY_SCRIPT_PATCH_QUALIFIERS, patchQualifiers);
        taskConfiguration.addConfigurationIfSet(PROPERTY_INCLUDED_QUALIFIERS, includedQualifiers);
        taskConfiguration.addConfigurationIfSet(PROPERTY_EXCLUDED_QUALIFIERS, excludedQualifiers);
        taskConfiguration.addConfigurationIfSet(PROPERTY_SCRIPT_FILE_EXTENSIONS, scriptFileExtensions);
        taskConfiguration.addConfigurationIfSet(PROPERTY_USESCRIPTFILELASTMODIFICATIONDATES, useLastModificationDates);
    }

    @Override
    protected boolean doExecute(MainFactory mainFactory) {
        DbMaintainer migratebird = mainFactory.createDbMaintainer();
        migratebird.updateDatabase(true);
        return true;
    }


    public void setScriptLocations(String scriptLocations) {
        this.scriptLocations = scriptLocations;
    }

    public void setScriptEncoding(String scriptEncoding) {
        this.scriptEncoding = scriptEncoding;
    }

    public void setPostProcessingScriptDirectoryName(String postProcessingScriptDirectoryName) {
        this.postProcessingScriptDirectoryName = postProcessingScriptDirectoryName;
    }

    public void setFromScratchEnabled(Boolean fromScratchEnabled) {
        this.fromScratchEnabled = fromScratchEnabled;
    }

    public void setAutoCreateDbMaintainScriptsTable(Boolean autoCreateDbMaintainScriptsTable) {
        this.autoCreateDbMaintainScriptsTable = autoCreateDbMaintainScriptsTable;
    }

    public void setAllowOutOfSequenceExecutionOfPatches(Boolean allowOutOfSequenceExecutionOfPatches) {
        this.allowOutOfSequenceExecutionOfPatches = allowOutOfSequenceExecutionOfPatches;
    }

    public void setQualifiers(String qualifiers) {
        this.qualifiers = qualifiers;
    }

    public void setPatchQualifiers(String patchQualifiers) {
        this.patchQualifiers = patchQualifiers;
    }

    public void setIncludedQualifiers(String includedQualifiers) {
        this.includedQualifiers = includedQualifiers;
    }

    public void setExcludedQualifiers(String excludedQualifiers) {
        this.excludedQualifiers = excludedQualifiers;
    }

    public void setUseLastModificationDates(Boolean useLastModificationDates) {
        this.useLastModificationDates = useLastModificationDates;
    }

    public void setScriptFileExtensions(String scriptFileExtensions) {
        this.scriptFileExtensions = scriptFileExtensions;
    }
}
