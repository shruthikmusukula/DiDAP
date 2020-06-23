# Extracting Data from MSTIFF Files

This code reads in _Marine Sonar Technology Image File Format_ files, which contain sonar data and other navigational information from unmanned underwater vehicles.
The files in this directory include: 
+ Constants.java
+ Util.java
+ Extract.java
+ SonarProcessing.java

## Constants

This file includes basic constants used to run the program, namely input and output directories. 

## Util

This file includes various utility functions to process and save data. Some of the methods are listed below.

### Conversion Methods
Common conversions used in the code, including hex to byte, byte to hex, Julian time to standard time, and float to degrees.

### Array Manipulation Methods
Array manipulations used throughout the code, including horizonatally joining two arrays and vertically joining two arrays.

### File Manipulation Methods
Common functions involving input and output to files, including writing data to a CSV, writing data as an image, and creating a list of all files of a specified extension within a directory.

### String Manipulation Methods
Methods to manipulate strings, including removing the path prefix to a file name and surrounding a string with single quotes.

## Extract

This file includes the main methods for processing files and extracting data. Detailed descriptions of these methods follow.

### ```init()```
Initializes variables required for program

### ```fileInit(String LoadFile)```
Reads in specified file, checks whether it is of the MST file format, and extracts _image file directory_ (which contains the addresses for every other data field) by converting the byte values to hex.

### ```imageInit()```
Extracts fields relevant to image, as well as right and left channel sonar iamge data.

### ```metaInit()```
Extracts fields relevant to metadata as well as specified data fields, including navigation information and fathometer data.

### ```fileProcess(String x)```
Given a file input path, extract all data, combine metadata for the same mission, and combine left and right channel image as well as subsequent images within the same mission. 

### ```saveIm()```
Saves image to local disk

### ```saveMeta()```
Saves metadata as a CSV on the local disk

### ```getData(String dTag, int Num_Fields)``` 
Given the known tag of a data field, as well as the number of subfields, find the location of data in the file using the image file directory and convert from binary.

### ```getData(String dTag)``` 
Given the known tag of a data field that is less than 4 bytes, convert the value from binary.

### ```getAddress(String dTag)```
Uses the image file directory to find the location of a specific data value.

### ```getChannel2(String dTag)```
Given the known tag of channel data, read in image data.

### Meta Data Methods
The remaining methods in this file use a similar process to find the location of data within the image file and converting from binary. 

## SonarProcessing

This file includes the main runner for this program. After finding all of the MSTIFF files in the specified directory, the program will go through the sequence for processing the files individually.
