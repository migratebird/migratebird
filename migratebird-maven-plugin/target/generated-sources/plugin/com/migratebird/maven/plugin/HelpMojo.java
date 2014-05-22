package com.migratebird.maven.plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Display help information on migratebird-maven-plugin.<br/> Call <pre>  mvn migratebird:help -Ddetail=true -Dgoal=&lt;goal-name&gt;</pre> to display parameter details.
 *
 * @version generated on Fri May 23 00:34:38 EEST 2014
 * @author org.apache.maven.tools.plugin.generator.PluginHelpGenerator (version 2.5)
 * @goal help
 * @requiresProject false
 */
public class HelpMojo
    extends AbstractMojo
{
    /**
     * If <code>true</code>, display all settable properties for each goal.
     * 
     * @parameter expression="${detail}" default-value="false"
     */
    private boolean detail;

    /**
     * The name of the goal for which to show help. If unspecified, all goals will be displayed.
     * 
     * @parameter expression="${goal}"
     */
    private java.lang.String goal;

    /**
     * The maximum length of a display line, should be positive.
     * 
     * @parameter expression="${lineLength}" default-value="80"
     */
    private int lineLength;

    /**
     * The number of spaces per indentation level, should be positive.
     * 
     * @parameter expression="${indentSize}" default-value="2"
     */
    private int indentSize;


    /** {@inheritDoc} */
    public void execute()
        throws MojoExecutionException
    {
        if ( lineLength <= 0 )
        {
            getLog().warn( "The parameter 'lineLength' should be positive, using '80' as default." );
            lineLength = 80;
        }
        if ( indentSize <= 0 )
        {
            getLog().warn( "The parameter 'indentSize' should be positive, using '2' as default." );
            indentSize = 2;
        }

        StringBuffer sb = new StringBuffer();

        append( sb, "com.migratebird:migratebird-maven-plugin:1.0-SNAPSHOT", 0 );
        append( sb, "", 0 );

        append( sb, "migratebird-maven-plugin", 0 );
        append( sb, "Migratebird: The database migration framework for Java.", 1 );
        append( sb, "", 0 );

        if ( goal == null || goal.length() <= 0 )
        {
            append( sb, "This plugin has 11 goals:", 0 );
            append( sb, "", 0 );
        }

        if ( goal == null || goal.length() <= 0 || "checkScriptUpdates".equals( goal ) )
        {
            append( sb, "migratebird:checkScriptUpdates", 0 );
            append( sb, "Performs a dry run of the database update. May be used to verify if there are any updates or in a test that fails if it appears that an irregular script update was performed.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "allowOutOfSequenceExecutionOfPatches", 2 );
                append( sb, "If this property is set to true, a patch script is allowed to be executed even if another script with a higher index was already executed.", 3 );
                append( sb, "", 0 );

                append( sb, "autoCreateDbMaintainScriptsTable", 2 );
                append( sb, "Sets the autoCreateDbMaintainScriptsTable property. If set to true, the table MIGRATEBIRD_SCRIPTS will be created automatically if it does not exist yet. If false, an exception is thrown, indicating how to create the table manually. False by default.", 3 );
                append( sb, "", 0 );

                append( sb, "configFile", 2 );
                append( sb, "The DbMaintain configuration file (common for native dbMaintain, through ant or this maven-plugin).", 3 );
                append( sb, "", 0 );

                append( sb, "databases", 2 );
                append( sb, "Database instance configuration.", 3 );
                append( sb, "", 0 );

                append( sb, "excludedQualifiers", 2 );
                append( sb, "Optional comma-separated list of script qualifiers. All excluded qualifiers must be registered using the qualifiers property. Scripts qualified with one of the excluded qualifiers will not be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "fromScratchEnabled", 2 );
                append( sb, "Sets the fromScratchEnabled property, that indicates the database can be recreated from scratch if needed. From-scratch recreation is needed in following cases:\n-\tA script that was already executed has been modified\n-\tA new script has been added with an index number lower than the one of an already executed script\n-\tAn script that was already executed has been removed or renamed\nIf set to false, DbMaintain will give an error if one of these situations occurs. The default is false.", 3 );
                append( sb, "", 0 );

                append( sb, "includedQualifiers", 2 );
                append( sb, "Optional comma-separated list of script qualifiers. All included qualifiers must be registered using the qualifiers property. Only scripts which are qualified with one of the included qualifiers will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "patchQualifiers", 2 );
                append( sb, "The qualifier to use to determine whether a script is a patch script. Defaults to patch. E.g. 01_#patch_myscript.sql", 3 );
                append( sb, "", 0 );

                append( sb, "postProcessingScriptDirectoryName", 2 );
                append( sb, "Comma separated list of directories and files in which the post processing database scripts are located. Directories in this list are recursively search for files. Defaults to postprocessing", 3 );
                append( sb, "", 0 );

                append( sb, "qualifiers", 2 );
                append( sb, "Optional comma-separated list of script qualifiers. All custom qualifiers that are used in script file names must be declared.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptArchiveDependencies", 2 );
                append( sb, "Defines where the scripts can be found that must be executed on the database. Multiple dependencies may be configured. At least one scriptArchiveDependency or scriptLocation (can be both) must be defined.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptEncoding", 2 );
                append( sb, "Encoding to use when reading the script files. Defaults to ISO-8859-1", 3 );
                append( sb, "", 0 );

                append( sb, "scriptFileExtensions", 2 );
                append( sb, "Sets the scriptFileExtensions property, that defines the extensions of the files that are regarded to be database scripts. The extensions should not start with a dot. The default is \'sql,ddl\'.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptLocations", 2 );
                append( sb, "Defines where the scripts can be found that must be executed on the database. Multiple locations may be configured, separated by comma\'s. A script location can be a folder or a jar file. This property is required. At least one scriptArchiveDependency or scriptLocation (can be both) must be defined.", 3 );
                append( sb, "", 0 );

                append( sb, "useLastModificationDates", 2 );
                append( sb, "Defines whether the last modification dates of the scripts files can be used to determine whether the contents of a script has changed. If set to true, DbMaintain will not look at the contents of scripts that were already executed on the database, if the last modification date is still the same. If it did change, it will first calculate the checksum of the file to verify that the content really changed. Setting this property to true improves performance: if set to false the checksum of every script must be calculated for each run. True by default.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "cleanDatabase".equals( goal ) )
        {
            append( sb, "migratebird:cleanDatabase", 0 );
            append( sb, "Task that removes the data of all database tables. The MIGRATEBIRD_SCRIPTS table will not be cleaned.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "configFile", 2 );
                append( sb, "The DbMaintain configuration file (common for native dbMaintain, through ant or this maven-plugin).", 3 );
                append( sb, "", 0 );

                append( sb, "databases", 2 );
                append( sb, "Database instance configuration.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "clearDatabase".equals( goal ) )
        {
            append( sb, "migratebird:clearDatabase", 0 );
            append( sb, "Task that removes all database items like tables, views etc from the database and empties the MIGRATEBIRD_SCRIPTS table.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "configFile", 2 );
                append( sb, "The DbMaintain configuration file (common for native dbMaintain, through ant or this maven-plugin).", 3 );
                append( sb, "", 0 );

                append( sb, "databases", 2 );
                append( sb, "Database instance configuration.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "createScriptArchive".equals( goal ) )
        {
            append( sb, "migratebird:createScriptArchive", 0 );
            append( sb, "Task that enables creating a jar file that packages all database update scripts. This jar can then be used as input for the updateDatabase task to apply changes on a target database. This way, database updates can be distributed as a deliverable, just like a war or ear file.\nThe created jar file will contain all configuration concerning the scripts in the META-INF folder.\n\nThis operation will hook into the package stage and will attach the generated archive to the build (so that it is installed in the local repository). The archive will have the same artifact id and group id as configured in the pom. An optional classifier can be configured also. A typical usage would be to create a pom of packaging type pom and then add this pom to your scripts folder.\n<project>\n<groupId>mygroup</groupId>\n<artifactId>myScripts</artifactId>\n<version>version</version>\n<packaging>pom</packaging>\n\n<build>\n<plugins>\n<plugin>\n<groupId>com.migratebird</groupId>\n<artifactId>migratebird-maven-plugin</artifactId>\n<version>-current\u00a0migratebird\u00a0version-</version>\n<configuration>\n<databases>\n<database>\n<dialect>oracle</dialect>\n<driverClassName>oracle.jdbc.driver.OracleDriver</driverClassName>\n<userName>user</userName>\n<password>pass</password>\n<url>jdbc:oracle:thin:@//localhost:1521/XE</url>\n<schemaNames>TEST</schemaNames>\n</database>\n</databases>\n</configuration>\n<executions>\n<execution>\n<goals>\n<goal>createScriptArchive</goal>\n</goals>\n</execution>\n</executions>\n</plugins>\n</plugin>\n</build>\n</project>\n\nThe installed artifact can then later be used as a scriptArchiveDependency in for example the updateDatabase task.\n\nYou can also specify an explicit archive name. In that case, the archive will just be generated and not attached to the build.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "archiveFileName", 2 );
                append( sb, "Explicitly defines the target name for the generated script archive. By default, the current artifact id will be taken (optionally appended with a classifier). If you explicitly set an archive file name, the artifact will no longer be attached to the build (so not installed in the local repository).", 3 );
                append( sb, "", 0 );

                append( sb, "configFile", 2 );
                append( sb, "The DbMaintain configuration file (common for native dbMaintain, through ant or this maven-plugin).", 3 );
                append( sb, "", 0 );

                append( sb, "patchQualifiers", 2 );
                append( sb, "The qualifier to use to determine whether a script is a patch script. Defaults to patch. E.g. 01_#patch_myscript.sql", 3 );
                append( sb, "", 0 );

                append( sb, "postProcessingScriptDirectoryName", 2 );
                append( sb, "Comma separated list of directories and files in which the post processing database scripts are located. Directories in this list are recursively search for files. Defaults to postprocessing", 3 );
                append( sb, "", 0 );

                append( sb, "qualifier", 2 );
                append( sb, "An optional qualifier for the artifact. This can be used if the archive is not the main artifact of the pom.", 3 );
                append( sb, "", 0 );

                append( sb, "qualifiers", 2 );
                append( sb, "Optional comma-separated list of script qualifiers. All custom qualifiers that are used in script file names must be declared.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptEncoding", 2 );
                append( sb, "Encoding to use when reading the script files. Defaults to ISO-8859-1", 3 );
                append( sb, "", 0 );

                append( sb, "scriptFileExtensions", 2 );
                append( sb, "Sets the scriptFileExtensions property, that defines the extensions of the files that are regarded to be database scripts. The extensions should not start with a dot. The default is \'sql,ddl\'.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptLocations (Default: ${basedir})", 2 );
                append( sb, "Defines where the scripts can be found that must be added to the jar file. Multiple locations may be configured, separated by comma\'s. Only folder names can be provided. Defaults to the current folder.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "disableConstraints".equals( goal ) )
        {
            append( sb, "migratebird:disableConstraints", 0 );
            append( sb, "Task that disables or drops all foreign key and not null constraints.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "configFile", 2 );
                append( sb, "The DbMaintain configuration file (common for native dbMaintain, through ant or this maven-plugin).", 3 );
                append( sb, "", 0 );

                append( sb, "databases", 2 );
                append( sb, "Database instance configuration.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "help".equals( goal ) )
        {
            append( sb, "migratebird:help", 0 );
            append( sb, "Display help information on migratebird-maven-plugin.\nCall\n\u00a0\u00a0mvn\u00a0migratebird:help\u00a0-Ddetail=true\u00a0-Dgoal=<goal-name>\nto display parameter details.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "detail (Default: false)", 2 );
                append( sb, "If true, display all settable properties for each goal.", 3 );
                append( sb, "", 0 );

                append( sb, "goal", 2 );
                append( sb, "The name of the goal for which to show help. If unspecified, all goals will be displayed.", 3 );
                append( sb, "", 0 );

                append( sb, "indentSize (Default: 2)", 2 );
                append( sb, "The number of spaces per indentation level, should be positive.", 3 );
                append( sb, "", 0 );

                append( sb, "lineLength (Default: 80)", 2 );
                append( sb, "The maximum length of a display line, should be positive.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "markDatabaseAsUpToDate".equals( goal ) )
        {
            append( sb, "migratebird:markDatabaseAsUpToDate", 0 );
            append( sb, "This operation updates the state of the database to indicate that all scripts have been executed, without actually executing them. This can be useful when you want to start using DbMaintain on an existing database, or after having fixed a problem directly on the database.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "autoCreateDbMaintainScriptsTable", 2 );
                append( sb, "Sets the autoCreateDbMaintainScriptsTable property. If set to true, the table MIGRATEBIRD_SCRIPTS will be created automatically if it does not exist yet. If false, an exception is thrown, indicating how to create the table manually. False by default.", 3 );
                append( sb, "", 0 );

                append( sb, "configFile", 2 );
                append( sb, "The DbMaintain configuration file (common for native dbMaintain, through ant or this maven-plugin).", 3 );
                append( sb, "", 0 );

                append( sb, "databases", 2 );
                append( sb, "Database instance configuration.", 3 );
                append( sb, "", 0 );

                append( sb, "excludedQualifiers", 2 );
                append( sb, "Optional comma-separated list of script qualifiers. All excluded qualifiers must be registered using the qualifiers property. Scripts qualified with one of the excluded qualifiers will not be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "includedQualifiers", 2 );
                append( sb, "Optional comma-separated list of script qualifiers. All included qualifiers must be registered using the qualifiers property. Only scripts which are qualified with one of the included qualifiers will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "qualifiers", 2 );
                append( sb, "Optional comma-separated list of script qualifiers. All custom qualifiers that are used in script file names must be declared.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptArchiveDependencies", 2 );
                append( sb, "Defines where the scripts can be found that must be registered in the database. Multiple dependencies may be configured. At least one scriptArchiveDependency or scriptLocation (can be both) must be defined.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptFileExtensions", 2 );
                append( sb, "Sets the scriptFileExtensions property, that defines the extensions of the files that are regarded to be database scripts. The extensions should not start with a dot. The default is \'sql,ddl\'.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptLocations", 2 );
                append( sb, "Defines where the scripts can be found that must be registered in the database. Multiple locations may be configured, separated by comma\'s. A script location can be a folder or a jar file. At least one scriptArchiveDependency or scriptLocation (can be both) must be defined.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "markErrorScriptPerformed".equals( goal ) )
        {
            append( sb, "migratebird:markErrorScriptPerformed", 0 );
            append( sb, "Task that indicates that the failed script was manually performed. The script will NOT be run again in the next update. No scripts will be executed by this task.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "configFile", 2 );
                append( sb, "The DbMaintain configuration file (common for native dbMaintain, through ant or this maven-plugin).", 3 );
                append( sb, "", 0 );

                append( sb, "databases", 2 );
                append( sb, "Database instance configuration.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "markErrorScriptReverted".equals( goal ) )
        {
            append( sb, "migratebird:markErrorScriptReverted", 0 );
            append( sb, "Task that indicates that the failed script was manually reverted. The script will be run again in the next update. No scripts will be executed by this task.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "configFile", 2 );
                append( sb, "The DbMaintain configuration file (common for native dbMaintain, through ant or this maven-plugin).", 3 );
                append( sb, "", 0 );

                append( sb, "databases", 2 );
                append( sb, "Database instance configuration.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "updateDatabase".equals( goal ) )
        {
            append( sb, "migratebird:updateDatabase", 0 );
            append( sb, "Task that updates the database to the latest version.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "allowOutOfSequenceExecutionOfPatches", 2 );
                append( sb, "If this property is set to true, a patch script is allowed to be executed even if another script with a higher index was already executed.", 3 );
                append( sb, "", 0 );

                append( sb, "autoCreateDbMaintainScriptsTable", 2 );
                append( sb, "Sets the autoCreateDbMaintainScriptsTable property. If set to true, the table MIGRATEBIRD_SCRIPTS will be created automatically if it does not exist yet. If false, an exception is thrown, indicating how to create the table manually. False by default.", 3 );
                append( sb, "", 0 );

                append( sb, "cleanDb", 2 );
                append( sb, "Indicates whether the database should be \'cleaned\' before scripts are executed. If true, the records of all database tables, except for the ones listed in \'migratebird.preserve.*\' or \'dbMaintain.preserveDataOnly.*\' are deleted before and after executing the scripts. False by default.", 3 );
                append( sb, "", 0 );

                append( sb, "configFile", 2 );
                append( sb, "The DbMaintain configuration file (common for native dbMaintain, through ant or this maven-plugin).", 3 );
                append( sb, "", 0 );

                append( sb, "databases", 2 );
                append( sb, "Database instance configuration.", 3 );
                append( sb, "", 0 );

                append( sb, "disableConstraints", 2 );
                append( sb, "If set to true, all foreign key and not null constraints of the database are automatically disabled before and after the execution of the scripts. False by default.", 3 );
                append( sb, "", 0 );

                append( sb, "excludedQualifiers", 2 );
                append( sb, "Optional comma-separated list of script qualifiers. All excluded qualifiers must be registered using the qualifiers property. Scripts qualified with one of the excluded qualifiers will not be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "fromScratchEnabled", 2 );
                append( sb, "Sets the fromScratchEnabled property, that indicates the database can be recreated from scratch if needed. From-scratch recreation is needed in following cases:\n-\tA script that was already executed has been modified\n-\tA new script has been added with an index number lower than the one of an already executed script\n-\tAn script that was already executed has been removed or renamed\nIf set to false, DbMaintain will give an error if one of these situations occurs. The default is false.", 3 );
                append( sb, "", 0 );

                append( sb, "includedQualifiers", 2 );
                append( sb, "Optional comma-separated list of script qualifiers. All included qualifiers must be registered using the qualifiers property. Only scripts which are qualified with one of the included qualifiers will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "patchQualifiers", 2 );
                append( sb, "The qualifier to use to determine whether a script is a patch script. Defaults to patch. E.g. 01_#patch_myscript.sql", 3 );
                append( sb, "", 0 );

                append( sb, "postProcessingScriptDirectoryName", 2 );
                append( sb, "Comma separated list of directories and files in which the post processing database scripts are located. Directories in this list are recursively search for files. Defaults to postprocessing", 3 );
                append( sb, "", 0 );

                append( sb, "qualifiers", 2 );
                append( sb, "Optional comma-separated list of script qualifiers. All custom qualifiers that are used in script file names must be declared.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptArchiveDependencies", 2 );
                append( sb, "Defines where the scripts can be found that must be executed on the database. Multiple dependencies may be configured. At least one scriptArchiveDependency or scriptLocation (can be both) must be defined.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptEncoding", 2 );
                append( sb, "Encoding to use when reading the script files. Defaults to ISO-8859-1", 3 );
                append( sb, "", 0 );

                append( sb, "scriptFileExtensions", 2 );
                append( sb, "Sets the scriptFileExtensions property, that defines the extensions of the files that are regarded to be database scripts. The extensions should not start with a dot. The default is \'sql,ddl\'.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptLocations", 2 );
                append( sb, "Defines where the scripts can be found that must be executed on the database. Multiple locations may be configured, separated by comma\'s. A script location can be a folder or a jar file. This property is required. At least one scriptArchiveDependency or scriptLocation (can be both) must be defined.", 3 );
                append( sb, "", 0 );

                append( sb, "scriptParameterFile", 2 );
                append( sb, "Sets the scriptParameterFile property. If set, the corresponding properties file will be loaded and all occurrences of parameters in the script that match a property will be replaced by the corresponding property value. Script parameters are formatted as in ${param}.", 3 );
                append( sb, "", 0 );

                append( sb, "updateSequences", 2 );
                append( sb, "If set to true, all sequences and identity columns are set to a sufficiently high value, so that test data can be inserted without having manually chosen test record IDs clashing with automatically generated keys.", 3 );
                append( sb, "", 0 );

                append( sb, "useLastModificationDates", 2 );
                append( sb, "Defines whether the last modification dates of the scripts files can be used to determine whether the contents of a script has changed. If set to true, DbMaintain will not look at the contents of scripts that were already executed on the database, if the last modification date is still the same. If it did change, it will first calculate the checksum of the file to verify that the content really changed. Setting this property to true improves performance: if set to false the checksum of every script must be calculated for each run. True by default.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "updateSequences".equals( goal ) )
        {
            append( sb, "migratebird:updateSequences", 0 );
            append( sb, "This operation is also mainly useful for automated testing purposes. This operation sets all sequences and identity columns to a minimum value. By default this value is 1000, but is can be configured with the lowestAcceptableSequenceValue option. The updateDatabase operation offers an option to automatically update the sequences after the scripts were executed.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "configFile", 2 );
                append( sb, "The DbMaintain configuration file (common for native dbMaintain, through ant or this maven-plugin).", 3 );
                append( sb, "", 0 );

                append( sb, "databases", 2 );
                append( sb, "Database instance configuration.", 3 );
                append( sb, "", 0 );

                append( sb, "lowestAcceptableSequenceValue", 2 );
                append( sb, "Threshold indicating the minimum value of sequences. If sequences are updated, all sequences having a lower value than this one are set to this value. Defaults to 1000.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( getLog().isInfoEnabled() )
        {
            getLog().info( sb.toString() );
        }
    }

    /**
     * <p>Repeat a String <code>n</code> times to form a new string.</p>
     *
     * @param str String to repeat
     * @param repeat number of times to repeat str
     * @return String with repeated String
     * @throws NegativeArraySizeException if <code>repeat < 0</code>
     * @throws NullPointerException if str is <code>null</code>
     */
    private static String repeat( String str, int repeat )
    {
        StringBuffer buffer = new StringBuffer( repeat * str.length() );

        for ( int i = 0; i < repeat; i++ )
        {
            buffer.append( str );
        }

        return buffer.toString();
    }

    /** 
     * Append a description to the buffer by respecting the indentSize and lineLength parameters.
     * <b>Note</b>: The last character is always a new line.
     * 
     * @param sb The buffer to append the description, not <code>null</code>.
     * @param description The description, not <code>null</code>.
     * @param indent The base indentation level of each line, must not be negative.
     */
    private void append( StringBuffer sb, String description, int indent )
    {
        for ( Iterator it = toLines( description, indent, indentSize, lineLength ).iterator(); it.hasNext(); )
        {
            sb.append( it.next().toString() ).append( '\n' );
        }
    }

    /** 
     * Splits the specified text into lines of convenient display length.
     * 
     * @param text The text to split into lines, must not be <code>null</code>.
     * @param indent The base indentation level of each line, must not be negative.
     * @param indentSize The size of each indentation, must not be negative.
     * @param lineLength The length of the line, must not be negative.
     * @return The sequence of display lines, never <code>null</code>.
     * @throws NegativeArraySizeException if <code>indent < 0</code>
     */
    private static List toLines( String text, int indent, int indentSize, int lineLength )
    {
        List lines = new ArrayList();

        String ind = repeat( "\t", indent );
        String[] plainLines = text.split( "(\r\n)|(\r)|(\n)" );
        for ( int i = 0; i < plainLines.length; i++ )
        {
            toLines( lines, ind + plainLines[i], indentSize, lineLength );
        }

        return lines;
    }

    /** 
     * Adds the specified line to the output sequence, performing line wrapping if necessary.
     * 
     * @param lines The sequence of display lines, must not be <code>null</code>.
     * @param line The line to add, must not be <code>null</code>.
     * @param indentSize The size of each indentation, must not be negative.
     * @param lineLength The length of the line, must not be negative.
     */
    private static void toLines( List lines, String line, int indentSize, int lineLength )
    {
        int lineIndent = getIndentLevel( line );
        StringBuffer buf = new StringBuffer( 256 );
        String[] tokens = line.split( " +" );
        for ( int i = 0; i < tokens.length; i++ )
        {
            String token = tokens[i];
            if ( i > 0 )
            {
                if ( buf.length() + token.length() >= lineLength )
                {
                    lines.add( buf.toString() );
                    buf.setLength( 0 );
                    buf.append( repeat( " ", lineIndent * indentSize ) );
                }
                else
                {
                    buf.append( ' ' );
                }
            }
            for ( int j = 0; j < token.length(); j++ )
            {
                char c = token.charAt( j );
                if ( c == '\t' )
                {
                    buf.append( repeat( " ", indentSize - buf.length() % indentSize ) );
                }
                else if ( c == '\u00A0' )
                {
                    buf.append( ' ' );
                }
                else
                {
                    buf.append( c );
                }
            }
        }
        lines.add( buf.toString() );
    }

    /** 
     * Gets the indentation level of the specified line.
     * 
     * @param line The line whose indentation level should be retrieved, must not be <code>null</code>.
     * @return The indentation level of the line.
     */
    private static int getIndentLevel( String line )
    {
        int level = 0;
        for ( int i = 0; i < line.length() && line.charAt( i ) == '\t'; i++ )
        {
            level++;
        }
        for ( int i = level + 1; i <= level + 4 && i < line.length(); i++ )
        {
            if ( line.charAt( i ) == '\t' )
            {
                level++;
                break;
            }
        }
        return level;
    }
}
