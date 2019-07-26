CREATE TABLE planeTracks (
ind STRING,
altitude BIGINT,
mounteng INT, 
engtype INT,
engines INT,
galt BIGINT,
gnd STRING,
lcao STRING,
id BIGINT,
lnhg FLOAT,
latitude FLOAT,
longitude FLOAT,
mil BOOLEAN,
mlat BOOLEAN,
postime DOUBLE,
spd FLOAT,
spdtyp INT,
species INT,
tt STRING,
trak FLOAT,
trkh BOOLEAN,
vsi BIGINT,
vsit INT,
wtc INT)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ‘,’
LOCATION ‘/user/hive/warehouse/data’;
