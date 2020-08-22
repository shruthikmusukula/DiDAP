CREATE TABLE IF NOT EXISTS Metadata.florida
(filePath STRING, dateStamp INT,navTime STRING,FathTime STRING,latitude DOUBLE,longitude DOUBLE,waterDepth FLOAT,towfishDepth FLOAT)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/user/hive/warehouse/metadata.db';
