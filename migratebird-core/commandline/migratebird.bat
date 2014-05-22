@REM
@REM Copyright 2014 www.migratebird.com
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM         http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

@REM ----------------------------------------------------------------------------
@REM migratebird Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM MIGRATEBIRD_JDBC_DRIVER - JDBC driver library to be used by migratebird. May optionally be multiple jars separated by semicolons.
@REM Preferably, this variable is set in the script setJdbcDriver.bat.
@REM MIGRATEBIRD_HOME - location of migratebird's installed home dir
@REM MIGRATEBIRD_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MIGRATEBIRD_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending
@REM MIGRATEBIRD_OPTS - parameters passed to the Java VM when running migratebird
@REM     e.g. to debug migratebird itself, use
@REM set MIGRATEBIRD_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM ----------------------------------------------------------------------------

@REM This batch script is based on the batch script for starting maven2 (mvn.bat)

@REM Begin all REM lines with '@' in case MIGRATEBIRD_BATCH_ECHO is 'on'
@echo off
@REM enable echoing my setting MIGRATEBIRD_BATCH_ECHO to 'on'
@if "%MIGRATEBIRD_BATCH_ECHO%" == "on"  echo %MIGRATEBIRD_BATCH_ECHO%

@if (%MIGRATEBIRD_JDBC_DRIVER%)==() goto callSetJdbcDriver

@REM
set JDBC_DRIVER=%MIGRATEBIRD_JDBC_DRIVER%
goto afterCallSetJdbcDriver

:callSetJdbcDriver
call "setJdbcDriver.bat"

:afterCallSetJdbcDriver
@REM set %HOME% to equivalent of $HOME
if "%HOME%" == "" (set HOME=%HOMEDRIVE%%HOMEPATH%)

@REM Execute a user defined script before this one
if exist "%HOME%\migratebirdrc_pre.bat" call "%HOME%\migratebirdrc_pre.bat"

set ERROR_CODE=0

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal
if "%OS%"=="WINNT" @setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto chkMHome

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = %JAVA_HOME%
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:chkMHome
if not "%MIGRATEBIRD_HOME%"=="" goto valMHome

if "%OS%"=="Windows_NT" SET MIGRATEBIRD_HOME=%~dp0
if "%OS%"=="WINNT" SET MIGRATEBIRD_HOME=%~dp0
if not "%MIGRATEBIRD_HOME%"=="" goto valMHome

echo.
echo ERROR: MIGRATEBIRD_HOME not found in your environment.
echo Please set the MIGRATEBIRD_HOME variable in your environment to match the
echo location of the migratebird installation
echo.
goto error

:valMHome

:stripMHome
if not _%MIGRATEBIRD_HOME:~-1%==_\ goto checkMBat
set MIGRATEBIRD_HOME=%MIGRATEBIRD_HOME:~0,-1%
goto stripMHome

:checkMBat
if exist "%MIGRATEBIRD_HOME%\migratebird.bat" goto init

echo.
echo ERROR: MIGRATEBIRD_HOME is set to an invalid directory.
echo MIGRATEBIRD_HOME = %MIGRATEBIRD_HOME%
echo Please set the MIGRATEBIRD_HOME variable in your environment to match the
echo location of the migratebird installation
echo.
goto error
@REM ==== END VALIDATION ====

:init
@REM Decide how to startup depending on the version of windows

@REM -- Windows NT with Novell Login
if "%OS%"=="WINNT" goto WinNTNovell

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

:WinNTNovell

@REM -- 4NT shell
if "%@eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set MIGRATEBIRD_CMD_LINE_ARGS=%*
goto endInit

@REM The 4NT Shell from jp software
:4NTArgs
set MIGRATEBIRD_CMD_LINE_ARGS=%$
goto endInit

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
set MIGRATEBIRD_CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto endInit
set MIGRATEBIRD_CMD_LINE_ARGS=%MIGRATEBIRD_CMD_LINE_ARGS% %1
shift
goto Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit
SET MIGRATEBIRD_JAVA_EXE="%JAVA_HOME%\bin\java.exe"

SET MIGRATEBIRD_CLASSPATH="%MIGRATEBIRD_HOME%\migratebird-${project.version}.jar";%JDBC_DRIVER%

@REM Start migratebird
:runm2
%MIGRATEBIRD_JAVA_EXE% %MIGRATEBIRD_OPTS% -classpath %MIGRATEBIRD_CLASSPATH% com.migratebird.launch.commandline.CommandLine %MIGRATEBIRD_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT
if "%OS%"=="WINNT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set MIGRATEBIRD_JAVA_EXE=
set MIGRATEBIRD_OPTS=
set MIGRATEBIRD_CLASSPATH=
set JDBC_DRIVER=
set MIGRATEBIRD_CMD_LINE_ARGS=
goto finish

:endNT
@endlocal & set ERROR_CODE=%ERROR_CODE%

:postExec
if exist "%HOME%\migratebirdrc_post.bat" call "%HOME%\migratebirdrc_post.bat"
@REM pause the batch file if MIGRATEBIRD_BATCH_PAUSE is set to 'on'
if "%MIGRATEBIRD_BATCH_PAUSE%" == "on" pause

if "%MIGRATEBIRD_TERMINATE_CMD%" == "on" exit %ERROR_CODE%

exit /B %ERROR_CODE%
