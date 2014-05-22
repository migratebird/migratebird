#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# DbMaintain Start Up Shell script
#
# Required ENV vars:
# ------------------
#   JAVA_HOME - location of a JDK home dir
#
# Optional ENV vars
# -----------------
#   MIGRATEBIRD_JDBC_DRIVER - JDBC driver library to be used by DbMaintain. May optionally be multiple jars separated by semicolons.
#        Preferably, this variable is set in the script setJdbcDriver.sh.
#   MIGRATEBIRD_HOME - location of migratebird's installed home dir
#   MIGRATEBIRD_OPTS - parameters passed to the Java VM when running DbMaintain
#     e.g. to debug DbMaintain itself, use
#       set MIGRATEBIRD_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
# ----------------------------------------------------------------------------

# This shell script is based on the shell script for starting maven2 (mvn)

QUOTED_ARGS=""
while [ "$1" != "" ] ; do

  QUOTED_ARGS="$QUOTED_ARGS $1"
  shift

done

if [ -f /etc/migratebirdrc ] ; then
  . /etc/migratebirdrc
fi

if [ -f "$HOME/.migratebirdrc" ] ; then
  . "$HOME/.migratebirdrc"
fi

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
mingw=false
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  MINGW*) mingw=true;;
  Darwin*) darwin=true 
           if [ -z "$JAVA_VERSION" ] ; then
             JAVA_VERSION="CurrentJDK"
           else
             echo "Using Java version: $JAVA_VERSION"
           fi
           if [ -z "$JAVA_HOME" ] ; then
             JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
           fi
           ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=`java-config --jre-home`
  fi
fi

if [ -z "$MIGRATEBIRD_HOME" ] ; then
  ## resolve links - $0 may be a link to migratebird's home
  PRG="$0"

  # need this for relative symlinks
  while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
      PRG="$link"
    else
      PRG="`dirname "$PRG"`/$link"
    fi
  done

  saveddir=`pwd`

  MIGRATEBIRD_HOME=`dirname "$PRG"`

  # make it fully qualified
  MIGRATEBIRD_HOME=`cd "$MIGRATEBIRD_HOME" && pwd`

  cd "$saveddir"
  # echo Using migratebird at $MIGRATEBIRD_HOME
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$MIGRATEBIRD_HOME" ] &&
    MIGRATEBIRD_HOME=`cygpath --unix "$MIGRATEBIRD_HOME"`
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
fi

# For Migwn, ensure paths are in UNIX format before anything is touched
if $mingw ; then
  [ -n "$MIGRATEBIRD_HOME" ] &&
    MIGRATEBIRD_HOME="`(cd "$MIGRATEBIRD_HOME"; pwd)`"
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME="`(cd "$JAVA_HOME"; pwd)`"
  # TODO classpath?
fi

if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD="`which java`"
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi

if [ -z "$JAVA_HOME" ] ; then
  echo "Warning: JAVA_HOME environment variable is not set."
fi

MIGRATEBIRD_LAUNCHER="com.migratebird.launch.commandline.CommandLine"
MIGRATEBIRD_JAR="${MIGRATEBIRD_HOME}/migratebird-${project.version}.jar"

# Check if $MIGRATEBIRD_JDBC_DRIVER is set. If not, call setJdbcDriver.sh.
if [ -z "$MIGRATEBIRD_JDBC_DRIVER" ] ; then
  if [ -f "$MIGRATEBIRD_HOME/setJdbcDriver.sh" ] ; then
    . "$MIGRATEBIRD_HOME/setJdbcDriver.sh"
  else
    . setJdbcDriver.sh
  fi
else
  JDBC_DRIVER="$MIGRATEBIRD_JDBC_DRIVER"
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$MIGRATEBIRD_HOME" ] &&
    MIGRATEBIRD_HOME=`cygpath --path --windows "$MIGRATEBIRD_HOME"`
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$HOME" ] &&
    HOME=`cygpath --path --windows "$HOME"`
  [ -n "$MIGRATEBIRD_JAR" ] &&
    MIGRATEBIRD_JAR=`cygpath --path --windows "$MIGRATEBIRD_JAR"`
  [ -n "$JDBC_DRIVER" ] &&
    JDBC_DRIVER=`cygpath --path --windows "$JDBC_DRIVER"`
fi

if $cygwin; then
  CLASSPATH_SEPARATOR=";"
else
  CLASSPATH_SEPARATOR=":"
fi

MIGRATEBIRD_CLASSPATH="${MIGRATEBIRD_JAR}${CLASSPATH_SEPARATOR}${CLASSPATH_SEPARATOR}${JDBC_DRIVER}"

exec "$JAVACMD" \
  $MIGRATEBIRD_OPTS \
  -classpath "${MIGRATEBIRD_CLASSPATH}" ${MIGRATEBIRD_LAUNCHER} $QUOTED_ARGS
