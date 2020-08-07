#!/usr/bin/env bash

# run 'ipconfig' on local windows Host Machine to verify and update IPv4 address before this job runs or it will fail

sqoop export --connect jdbc:oracle:thin:system/system@192.168.86.36:1521:xe --username SHRUTHIK --password bin4 --direct --export-dir '/user/hive/warehouse/metadata.db/florida' --table SHRUTHIK.METADATA --input-lines-terminated-by '\n' --input-fields-terminated-by ',' 2&>1 output.txt
