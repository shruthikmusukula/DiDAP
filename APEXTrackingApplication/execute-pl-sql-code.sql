select rtrim(xmlagg(xmlelement(E, ICAO, ',').extract('//text()')).GetClobVal(), ',') as list,
       rtrim(xmlagg(xmlelement(E, LATITUDE, ',').extract('//text()')).GetClobVal(), ',') as list,
       rtrim(xmlagg(xmlelement(E, LONGITUDE, ',').extract('//text()')).GetClobVal(), ',') as list
into :P1_ID, :P1_LAT, :P1_LONG
from whole_data;
