# Scaling Up with Hive

The hive queries in this directory of the _NavalSonarDataAnalyticsPlatform_ repository, deal with loading collected aircraft data in CSV format. The usage of Hive and the Hadoop ecosystem allows us to scale up and handle the expected data overflow. Over time, this data analytics platform is expected to handle terabytes of data. All contents of this directory are written using HiveQL.
The files in this repository include: 
+ CreatePlaneTracksTable.q
+ HiveTableJoins.q
+ LoadOverwrittenCSVData.q
+ MoreThanOneFieldGroupBy.q
+ OneFieldGroupBy.q
+ OneFieldGroupByAggreggate.q
+ OrderBy.q
+ SelectDistinct.q

### ```CreatePlaneTracksTable```

This file includes the basic schema used for organizing ADSB Exchange Aircraft Data. A MapReduce Job is run as a result of this query to create a table with the specified fields, such as Latitude and Longitude.

### HiveTableJoins

This file includes guidelines on the 4 types of table join that can be made using the planeTracks table with unique `ICAO` values.

### LoadOverwrittenCSVData
Loading data from a CSV file in the Hadoop Distributed File System (HDFS) into the created `planetracks` Hive table.

### MoreThanOneFieldGroupBy
Using the `GROUP BY` clause in HiveQL with more than one field and without the use of aggreggate functions.

### OneFieldGroupBy

Using the `GROUP BY` clause in HiveQL with only one data field and without the use of aggreggate functions.

### OneFieldGroupByAggreggate

Using the `GROUP BY` clause in HiveQL with only one data field and the use of the `count(*)` aggreggate function.

## OrderBy
Using the `ORDER BY` clause in HiveQL to organize the presented aircraft data from a query.

## SelectDistinct
Demonstration of the common usage of the `DISTINCT` operator in HiveQL query unique `ICAO` values from the loaded CSV Aircraft Data.

