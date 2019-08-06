# Setting Up a Database Schema in Oracle Database

The queries in this directory of the _NavalSonarDataAnalyticsPlatform_ repository, deal with loading collected aircraft data in CSV format into Oracle Database, as opposed to Hive. The SQL Queries created in this directory set up a two-table schema for handling ADSB Aircraft Data. All contents of this directory are written using SQL.
The files in this repository include: 
+ ADSB_FULL_SCHEMA_SETUP.sql
+ COPY_TABLE_FOR_USE.sql
+ CREATE_PLOTPOINTS_TABLE.sql
+ CREATE_PRIMARY_KEY_SEQUENCE.sql
+ SELECT_DISTINCT_ICAO.sql
+ SET_PRIMARY_KEYS.sql
+ UPDATE_FOREIGN_KEY_IN_NEW_TABLE.sql

### ```ADSB_FULL_SCHEMA_SETUP```

This file includes one large compiled SQL File to perform all the operations of the following files.

### ```COPY_TABLE_FOR_USE```

This file contains a copy of the organized table with set foreign and primary keys for production use. 

### ```CREATE_PLOTPOINTS_TABLE```

Uses sequence to create a table with unique and separated `ICAO`field values.

### ```CREATE_PRIMARY_KEY_SEQUENCE```

Creates a sequence in SQL for assigning primary keys to each `ICAO` field value.

### ```SELECT_DISTINCT_ICAO```

Creates a new table to host all distinct `ICAO` field values using the `DISTINCT` SQL operator.

### ```SET_PRIMARY_KEYS```

Uses the `UPDATE` SQL Command to assign a generated primary key from the sequence above to a unique `ICAO` field value.

### ```UPDATE_FOREIGN_KEY_IN_NEW_TABLE```

Adds the foreign key constraint to the `PLANETRACKS` table to create connection to `PLOTPOINTS` table.
