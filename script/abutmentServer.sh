#!/bin/bash

MY_CONFIG_FILE=file:../conf/abutmentServer-application.properties
LOG4j_CONFIG_FILE=file:../conf/abutmentServer-log4j.properties

#-----------------------------------------------------------------------
# Process Entrance class
MAIN_CLASS=by.ywzn.abutmentServer.main.Main
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# Process Name to display
PROC_NAME=$(basename $0)_ADAPTER
#-----------------------------------------------------------------------

#=======================================================================
# Define LANG
#=======================================================================
#-----------------------------------------------------------------------
osName=`uname`
if [ "$osName" = "SunOS" ]; then
        LANG="zh_CN.UTF-8"
elif [ "$osName" = "Linux" ]; then
        LANG="zh_CN.utf8"
fi
export LANG
#-----------------------------------------------------------------------

#=======================================================================
# Define arguments for process
#=======================================================================



#-----------------------------------------------------------------------
# Flags for java Virtal Machine
VM_FLAG="-Xrs -Xms256M -Xmx512M"
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# List of blank-separated paths defining the contents of the classes
# and jars
# Examples:
#       LOADER_PATH="foo foo/*.jar lib/nms-util.jar"
#     "foo": Add this folder as a class repository
#     "foo/*.jar": Add all the JARs of the specified folder as class
#                  repositories
#     "lib/nms-util.jar": Add lib/nms-util.jar as a class repository
LOADER_PATH="../lib/*.jar ../conf/*.properties"
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# Specify the java_home
# if it has JAVA_HOME environment argument,specify to it,
# else change it to correct java path
#-----------------------------------------------------------------------
if [ -n $JAVA_HOME ]; then
    JAVA_CMD_EXEC=$JAVA_HOME/bin/java
else
    JAVA_CMD_EXEC=`which java`
fi

#-----------------------------------------------------------------------
# Process Arguments
PROC_ARGS=
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# Process log file
LOG_FILE=./output
#-----------------------------------------------------------------------

#=======================================================================
# Define information tag
#=======================================================================
RUNNING_TAG="[Y]"
NOT_RUNNING_TAG="[N]"

#=======================================================================
# Define information JAVA_OPTS。 It's not necessary.
#=======================================================================
JAVA_OPTS=""

#-----------------------------------------------------------------------
# Process command array
ARRAY=
#-----------------------------------------------------------------------
SCRIPT_SERVER_IDS=`ps -ef | grep "Diname=$PROC_NAME" | grep -v grep | awk '{print $2}'`
set_classpath(){
        set ${LOADER_PATH}
        while [ $# -gt 0 ]; do
                classpath=${classpath}:$1
                shift
        done
        CLASSPATH=${classpath}:${CLASSPATH}
}

is_proc_run(){
        ps -ef | grep -w "${PROC_NAME}" | grep -v grep &>/dev/null
        return $?
}

param_add_quotes(){
    i=0
    for var in "$@";do
            if [ "$i" -eq 0 ]; then
                    ARRAY[$i]=$var
            else
                    ARRAY[$i]=\"$var\"
            fi
            i=$[$i+1]
    done
}

status_proc(){
        is_proc_run
        if [ $? -eq 0 ]; then
                PROC_PID=`ps -ef | grep -w "${PROC_NAME}" | grep -v grep |awk '{print $2}'`
                echo "${RUNNING_TAG} ${PROC_NAME} is running($PROC_PID) !"
        else
                echo "${NOT_RUNNING_TAG} ${PROC_NAME} is not running !"
        fi
}

exec_proc(){
        is_proc_run
        if [ $? -eq 0 ]; then
                echo "${RUNNING_TAG} ${PROC_NAME} is running !"
                set_classpath
                param_add_quotes "$@"
                echo ${ARRAY[*]}
                ${JAVA_CMD_EXEC} -Diname="${PROC_NAME}" -DMY_CONFIG_FILE=${MY_CONFIG_FILE} -Dlog4j.configuration=${LOG4j_CONFIG_FILE} -Dlogging.config=${LOG4j_CONFIG_FILE} ${VM_FLAG} -cp ${CLASSPATH} ${MAIN_CLASS} ${ARRAY[*]}
        else
                echo "${NOT_RUNNING_TAG} ${PROC_NAME} is not running !"
        fi
}

start_proc(){
        is_proc_run
        if [ $? -eq 0 ]; then
                echo "${INFO_TAG} ${PROC_NAME} is already running !"
        else
                echo "${INFO_TAG} Starting ${PROC_NAME} ..."
                set_classpath
                nohup ${JAVA_CMD_EXEC} -Diname="${PROC_NAME}" -DMY_CONFIG_FILE=${MY_CONFIG_FILE} -Dlog4j.configuration=${LOG4j_CONFIG_FILE} -Dlogging.config=${LOG4j_CONFIG_FILE} ${VM_FLAG} -cp ${CLASSPATH} ${MAIN_CLASS} $1 > ${LOG_FILE} 2>&1 
                sleep 1
                is_proc_run
                if [ $? -eq 0 ]; then
                        echo "${INFO_TAG} ${PROC_NAME} started !"
                else
                        echo "${ERROR_TAG} ${PROC_NAME} starts failed !"
                fi
        fi
}

stop_proc(){
        set_classpath
        ${JAVA_CMD_EXEC} -Diname="${PROC_NAME}" -DMY_CONFIG_FILE=${MY_CONFIG_FILE} -Dlog4j.configuration=${LOG4j_CONFIG_FILE} -Dlogging.config=${LOG4j_CONFIG_FILE} ${VM_FLAG} -cp ${CLASSPATH} ${MAIN_CLASS} stop
}

kill_server(){
		is_proc_run
		if [ ! $? -eq 0 ]; then
	                echo "${INFO_TAG} ${PROC_NAME} is stopped!"
	                return 0
	    else
	            tmp=$SCRIPT_SERVER_IDS
	            kill_run $tmp
	            if [ ! $? -eq 0 ]; then
	                    echo "Can't stop ${INFO_TAG} ${PROC_NAME}!"
	                    return 1
	            else
	                    echo "${INFO_TAG} ${PROC_NAME} is stopped!"
	                    return 0
	            fi
	    fi
}

kill_run(){
        kill -9 $1
        return $?
}

usage(){
        echo ${PROC_NAME} usage:
        echo -e "`basename $0` <start|stop|exec|restart|status>"
        echo -e "\t start       ${PROC_NAME} start adapter"
        echo -e "\t stop        ${PROC_NAME} stop running"
        echo -e "\t exec        ${PROC_NAME} execute command"
        echo -e "\t restart     ${PROC_NAME} restart adapter"
        echo -e "\t status      ${PROC_NAME} show adapter status"
}


#=======================================================================
# Main Program begin
#=======================================================================
case $1 in
        start)
                start_proc $1
                ;;
        exec)
                exec_proc "$@"
                ;;
        stop)
                stop_proc
                sleep 2
                kill_server
                ;;
        restart)
                sleep 1
                stop_proc
                sleep 3
                start_proc start
                ;;
        status)
                status_proc $1
                ;;
        *)
                usage
esac