@echo on

title 工作站对接服务器

set MY_CONFIG_FILE=file:../conf/abutmentServer-application.properties
set LOG4j_CONFIG_FILE=file:../conf/abutmentServer-log4j.properties

set VM_FLAG=-Xms256M -Xmx512M

set MAIN_CLASS=by.ywzn.abutmentServer.main.Main

set CLASSPATH=../lib/*

set JAVA_HOME=%JAVA_HOME%

set path=%JAVA_HOME%\bin


java -cp %CLASSPATH%; %VM_FLAG% -DMY_CONFIG_FILE=%MY_CONFIG_FILE% -Dlog4j.configuration=%LOG4j_CONFIG_FILE% -Dlogging.config=%LOG4j_CONFIG_FILE% %MAIN_CLASS%

pause