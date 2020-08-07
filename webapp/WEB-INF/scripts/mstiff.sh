#!/usr/bin/env bash

javac -cp /usr/lib/hadoop/*:/usr/lib/hadoop/client-0.20/* -d /home/cloudera/MSTIFF_Code /home/cloudera/MSTIFF_Code/GET_MST_mod.java & jar -cvf GET_MST_mod.jar -C /home/cloudera/MSTIFF_Code/ .

hadoop jar /home/cloudera/GET_MST_mod.jar GET_MST_mod /user/cloudera/MSTIFF_Code /user/cloudera/successfulOutput_fullScript 
echo $?

