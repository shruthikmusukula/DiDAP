#!bin/bash

pathOne=$1
pathTwo=$2

hdfs dfs -copyFromLocal "$pathOne" "$pathTwo" /*
