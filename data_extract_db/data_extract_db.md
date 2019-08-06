# Extracting Data from MSTIFF Files

This code reads in _Marine Sonar Technology Image File Format_ files, which contain sonar data and other navigational information from unmanned underwater vehicles.
Furthermore, it uploads the data into a database with a multi-table organization schema.
The files in this directory include: 
+ Constants.java
+ Util.java
+ BLOBInsert.java
+ DBMain.java
+ ExtractDB.java
+ Runner.java

## Constants

This file includes basic constants used to run the program, namely input and output directories. 

## Util

This file includes various utility functions to process and save data. Some of the methods are listed below.

### Conversion Methods
Common conversions used in the code, including hex to byte, byte to hex, Julian time to standard time, and float to degrees.

### Array Manipulation Methods
Array manipulations used throughout the code, including horizonatally joining two arrays, vertically joining two arrays, and removing excess empty lines.

### File Manipulation Methods
Common functions involving input and output to files, including writing data to a CSV, writing data as an image, and creating a list of all files of a specified extension within a directory.

### String Manipulation Methods
Methods to manipulate strings, including removing the path prefix to a file name and surrounding a string with single quotes.

## BLOBInsert
This file includes methods to write binary large objects to a table in a database.

### ```tableSetup(String tableName, boolean first) ```
Creates a new table in the database with the name specified, and drops the existing table if ```first``` is false.

### ```loadFile(String tableName, byte[][] file, String im1, String im2)```
Loads the given byte array into the table specified, as well as with the two image names given.

### ```loadFile(String tableName, String file, String im1, String im2)```
Loads the given file into the table specified, as well as with the two image names given.

## DBMain
This file includes some common functions used in conjunction with the database. Detailed descriptions of all methods are included below.

### ```DBinit()```
Establishes connection and initializes query statement.

### ```getConn()```
Returns connection for general external use.

### ```createTable(String tableName, String statement, boolean first, boolean select)```
Creates a new table in the database with the name specified and specified statement. If ```first``` is false, the existing table of the same name is dropped. If select is true, the given statement is executed as a select query.

### ```executeQuery(String query)```
Executes the given query.

### ```updateFileNameDB(String table, String colName)```
Removes directory path from file name in the given column of the table specified.

### ```linkDistinctVals(String mainTable, String mainValue, String newValue, String linkedTable, String linkedValue)```
Fills out ```newValue``` column based on connection between ```mainValue``` in ```mainTable``` to ```linkedValue``` in ```linkedTable```.

### ```DBfinish()```
Closes connection and other variables as necessary.

## ExtractDB
This file contains the main methods for extracting data from the MSTIFF file. The methods are further explained below.

### ```init()```
Initializes variables required for program

### ```fileInit(String LoadFile)```
Reads in specified file, checks whether it is of the MST file format, and extracts image file directory (which contains the addresses for every other data field) by converting the byte values to hex.

### ```imageInit()```
Extracts fields relevant to image, as well as right and left channel sonar iamge data.

### ```metaInit()```
Extracts fields relevant to metadata as well as specified data fields, including navigation information and fathometer data.

### ```fileProcess(String x)```
Given a file input path, extract all data, combine metadata for the same mission, and combine left and right channel image as well as subsequent images within the same mission.

### ```saveIm()```
Writes temporary image data to publicly available variable.

### ```getImage()```
Returns image data.

### ```saveMeta()```
Writes temporary metadata to publicly available variable.

### ```getheader()```
Returns header with SQL variable types.

### ```getMetaAvailable()```, ```getMeta()```
Timing and returning metadata.

### ```fileInit(String LoadFile)```
Reads in specified file, checks whether it is of the MST file format, and extracts image file directory (which contains the addresses for every other data field) by converting the byte values to hex.

### ```imageInit()```
Extracts fields relevant to image, as well as right and left channel sonar iamge data.

### ```metaInit()```
Extracts fields relevant to metadata as well as specified data fields, including navigation information and fathometer data.

### ```fileProcess(String x)```
Given a file input path, extract all data, combine metadata for the same mission, and combine left and right channel image as well as subsequent images within the same mission.

## Runner
This file is the main runner for the program. The main method executes the database sequence, while the other methods are outlined below.

### ```runDBSequence(String prefix)```
With a prefix used to name all tables with the same convention, this method will process all files and save extracted data to tables. 

### ```saveMetaTable()```
When all metadata for a particular mission has been extracted, this method will loop through all lines and insert into a new table in the database.

### ```saveImage()```
Saves the current image, along with the files it has been extracted from. 

### ```createTableUniqueFilenames()```
Creates a table of unique filenames only.

## Future Work
This code can use some improvements, namely:
+ Automatic recognition of whether a table already exists, rather than manually toggling whether or not to drop the table
+ Further incorporation of the linked structure to use filename id numbers to link metadata, images, and mine tags
