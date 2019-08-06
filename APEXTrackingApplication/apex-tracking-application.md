# Visualization of Data through APEX

This code loads the extracted metadata into the Object Browser pane of APEX, and then plots markers 
according to the latitude and longtiude values using the Google Maps API and Marker Clusterer library.

The files in this directory include: 
+ execute-js-code.js
+ execute-pl-sql-code.sql
+ map-region.html

## execute-js-code.js
This file includes the JavaScript code to plot the markers.

## execute-pl-sql-code.sql
This file includes SQL scripts to load the data from Oracle XE into the APEX webpage.

## map-region.html
This file includes JavaScript code to be put into the region code editor. It makes the Google Map.


## Future Work
This code can use some improvements, namely:
+ Writing JavaScript code to plot markers without using an external library like Marker Clusterer.
