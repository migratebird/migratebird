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

import org.apache.maven.artifact.Artifact;
import com.migratebird.launch.task.MigrateBirdDatabase;
import com.migratebird.launch.task.MigrateBirdTask;
import com.migratebird.util.MigrateBirdException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
*/
public abstract class BaseDatabaseMojo extends BaseMojo {


    /**
     * Database instance configuration.
     *
     * @parameter
     */
    protected List<Database> databases;


    protected abstract MigrateBirdTask createMigrateBirdTask(List<MigrateBirdDatabase> migrateBirdDatabases);


    @Override
    protected MigrateBirdTask createMigrateBirdTask() {
        List<MigrateBirdDatabase> migrateBirdDatabases = getMigrateBirdDatabases();
        return createMigrateBirdTask(migrateBirdDatabases);
    }

    protected List<MigrateBirdDatabase> getMigrateBirdDatabases() {
        List<MigrateBirdDatabase> migrateBirdDatabases = new ArrayList<MigrateBirdDatabase>();
        if (databases == null) {
            return migrateBirdDatabases;
        }
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
            migrateBirdDatabases.add(migrateBirdDatabase);
        }
        return migrateBirdDatabases;
    }

    protected String getAllScriptLocations(String scriptLocations, List<ScriptArchiveDependency> scriptArchiveDependencies) {
        StringBuilder allScriptLocations = getScriptArchiveDependenciesAsString(scriptArchiveDependencies);
        if (!isBlank(scriptLocations)) {
            if (allScriptLocations.length() > 0) {
                allScriptLocations.append(", ");
            }
            allScriptLocations.append(scriptLocations);
        }
        return allScriptLocations.toString();
    }

    private StringBuilder getScriptArchiveDependenciesAsString(List<ScriptArchiveDependency> scriptArchiveDependencies) {
        StringBuilder result = new StringBuilder();
        if (scriptArchiveDependencies == null || scriptArchiveDependencies.isEmpty()) {
            return result;
        }

        for (ScriptArchiveDependency scriptArchiveDependency : scriptArchiveDependencies) {
            File artifactFile = resolveScriptArchieDependencyArtifact(scriptArchiveDependency);
            result.append(artifactFile.getPath());
            result.append(", ");
        }
        result.setLength(result.length() - 2);
        return result;
    }

    private File resolveScriptArchieDependencyArtifact(ScriptArchiveDependency scriptArchiveDependency) {
        String groupId = scriptArchiveDependency.getGroupId();
        String artifactId = scriptArchiveDependency.getArtifactId();
        String version = scriptArchiveDependency.getVersion();
        String type = scriptArchiveDependency.getType();
        String classifier = scriptArchiveDependency.getClassifier();

        Artifact artifact = artifactFactory.createArtifactWithClassifier(groupId, artifactId, version, type, classifier);
        try {
            resolver.resolve(artifact, remoteRepositories, localRepository);
        } catch (Exception e) {
            throw new MigrateBirdException("Unable to resolve script locations for group id: " + groupId + ", artifact id: " + artifactId + ", version: " + version + ", type: " + type + ", classifier: " + classifier, e);
        }
        return artifact.getFile();
    }
}
