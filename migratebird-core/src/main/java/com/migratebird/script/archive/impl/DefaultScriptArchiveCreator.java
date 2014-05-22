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
package com.migratebird.script.archive.impl;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.File;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.migratebird.script.Script;
import com.migratebird.script.archive.ScriptArchiveCreator;
import com.migratebird.script.executedscriptinfo.ScriptIndexes;
import com.migratebird.script.qualifier.Qualifier;
import com.migratebird.script.repository.ScriptRepository;
import com.migratebird.script.repository.impl.ArchiveScriptLocation;
import com.migratebird.util.MigrateBirdException;


/**
 * Creates a archive file containing all scripts in all configured script locations
 *
*/
public class DefaultScriptArchiveCreator implements ScriptArchiveCreator {

    /* The logger instance for this class */
    private static Log logger = LogFactory.getLog(DefaultScriptArchiveCreator.class);

    protected ScriptRepository scriptRepository;
    protected String scriptEncoding;
    protected String postProcessingScriptDirName;
    protected Set<Qualifier> registeredQualifiers;
    protected Set<Qualifier> patchQualifiers;
    protected String scriptIndexRegexp;
    protected String qualifierRegexp;
    protected String targetDatabaseRegexp;
    protected Set<String> scriptFileExtensions;
    protected ScriptIndexes baseLineRevision;
    protected boolean ignoreCarriageReturnsWhenCalculatingCheckSum;


    public DefaultScriptArchiveCreator(ScriptRepository scriptRepository, String scriptEncoding, String postProcessingScriptDirName, Set<Qualifier> registeredQualifiers, Set<Qualifier> patchQualifiers, String scriptIndexRegexp, String qualifierRegexp, String targetDatabaseRegexp, Set<String> scriptFileExtensions, ScriptIndexes baseLineRevision, boolean ignoreCarriageReturnsWhenCalculatingCheckSum) {
        this.scriptRepository = scriptRepository;
        this.scriptEncoding = scriptEncoding;
        this.postProcessingScriptDirName = postProcessingScriptDirName;
        this.registeredQualifiers = registeredQualifiers;
        this.patchQualifiers = patchQualifiers;
        this.scriptIndexRegexp = scriptIndexRegexp;
        this.qualifierRegexp = qualifierRegexp;
        this.targetDatabaseRegexp = targetDatabaseRegexp;
        this.scriptFileExtensions = scriptFileExtensions;
        this.baseLineRevision = baseLineRevision;
        this.ignoreCarriageReturnsWhenCalculatingCheckSum = ignoreCarriageReturnsWhenCalculatingCheckSum;
    }

    /**
     * Creates a archive file containing all scripts in all configured script locations
     *
     * @param archiveFileName The name of the archivie file to create
     */
    @Override
    public void createScriptArchive(String archiveFileName) {
        if (isBlank(archiveFileName)) {
            throw new MigrateBirdException("Unable to create script archive. No archive file name was specified.");
        }
        try {
            logger.info("Creating script archive: " + archiveFileName);
            SortedSet<Script> allScripts = scriptRepository.getAllScripts();
            ArchiveScriptLocation archiveScriptLocation = new ArchiveScriptLocation(allScripts, scriptEncoding, postProcessingScriptDirName, registeredQualifiers, patchQualifiers, scriptIndexRegexp, qualifierRegexp, targetDatabaseRegexp, scriptFileExtensions, baseLineRevision, ignoreCarriageReturnsWhenCalculatingCheckSum);
            archiveScriptLocation.writeToJarFile(new File(archiveFileName));

        } catch (Exception e) {
            throw new MigrateBirdException("Error creating script archive " + archiveFileName, e);
        }
    }

}
