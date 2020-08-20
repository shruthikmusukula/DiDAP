#!/usr/bin/env bash

# run 'ipconfig' on local windows Host Machine to verify and update IPv4 address before this job runs or it will fail

#/usr/bin/sqoop export --connect jdbc:oracle:thin:system/system@192.168.86.36:1521:xe --username SHRUTHIK --password bin4 --direct --export-dir '/user/hive/warehouse/metadata.db/florida' --table SHRUTHIK.METADATA --input-lines-terminated-by '\n' --input-fields-terminated-by ','

export SQOOP_HOME=/home/cloudera/sqoop-1.4.7.bin__hadoop-2.6.0
export PATH=$PATH:$SQOOP_HOME/bin

export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:/home/cloudera/sqoop-1.4.7.bin__hadoop-2.6.0/lib/ojdbc6.jar

PARAMS=(
    export 
    --connect jdbc:oracle:thin:system/system@192.168.86.36:1521:xe 
    --username SHRUTHIK
    --password bin4
    --direct
    --export-dir '/user/hive/warehouse/metadata.db/florida'
    --table SHRUTHIK.METADATA
    --input-lines-terminated-by '\n'
    --input-fields-terminated-by ','
)
sqoop ${PARAMS[@]}

#sqoop help
echo $?
