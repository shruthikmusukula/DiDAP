var a = document.getElementById('P1_LAT').value;
    var LAT = a.split(",");
    var b = document.getElementById('P1_LONG').value; 
    var LONG = b.split(",");
    var j = document.getElementById('P1_ID').value;
    var IDs = j.split(",");

    var map = new google.maps.Map(document.getElementById('map'), {
      zoom: 3,
      center: new google.maps.LatLng(54.5260, 15.2551),
      mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    var infowindow = new google.maps.InfoWindow();

    var i;
    var markerClusterer = new MarkerClusterer(map, [], {imagePath: 'https://raw.githubusercontent.com/googlemaps/js-marker-clusterer/gh-pages/images/m'});

    for (i = 0; i < IDs.length; i++) {  
      marker = new google.maps.Marker({
        position: new google.maps.LatLng(LAT[i], LONG[i]),
        map: map
      });
        markerClusterer.addMarker(marker);
    }
