<?php 

  require_once 'header.php';
  $controller = new ControllerStore();
  $controllerCategory = new ControllerCategory();

  $categories = $controllerCategory->getCategories();
  

  $extras = new Extras();
  $store_id = $extras->decryptQuery1(KEY_SALT, $_SERVER['QUERY_STRING']);

  if($store_id != null) {

        $store = $controller->getStoreByStoreId($store_id);

        if( isset($_POST['submit']) ) {
    
            $itm = $store;
            $itm->store_name = htmlspecialchars(trim(strip_tags($_POST['store_name'])));
            $itm->store_address = htmlspecialchars(trim(strip_tags($_POST['store_address'])));

            $store_desc = preg_replace('~[\r\n]+~', '', $_POST['store_desc']);
            $itm->store_desc = htmlspecialchars(trim(strip_tags($store_desc)));
            
            $itm->lat = trim(strip_tags($_POST['lat']));
            $itm->lon = htmlspecialchars(trim(strip_tags($_POST['lon'])), ENT_QUOTES);
            $itm->website = $extras->removeHttp( htmlspecialchars(trim(strip_tags($_POST['website'])), ENT_QUOTES) );
            $itm->phone_no = htmlspecialchars(trim(strip_tags($_POST['phone_no'])), ENT_QUOTES);
            $itm->email = trim(strip_tags($_POST['email']));
            $itm->sms_no = trim(strip_tags($_POST['sms_no']));
            $itm->category_id = trim(strip_tags($_POST['category_id']));
            $itm->updated_at = time();
            $itm->featured = trim(strip_tags($_POST['featured']));

            $controller->updateStore($itm);
            echo "<script type='text/javascript'>location.href='stores.php';</script>";
      }
  }
  else {
      echo "<script type='text/javascript'>location.href='403.php';</script>";
  }

?>


<!DOCTYPE html>
<html lang="en"><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="http://getbootstrap.com/assets/ico/favicon.ico">

    <title>Store Finder</title>

    <!-- Bootstrap core CSS -->
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="bootstrap/css/navbar-fixed-top.css" rel="stylesheet">
    <link href="bootstrap/css/custom.css" rel="stylesheet">

    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBB7Tce0Xd3GEb838FF5uRcIe8MQIRdQSo&sensor=false"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript">
        $(function(){
            var mapDiv = document.getElementById('map');
                var map = new google.maps.Map(mapDiv, {
                  center: new google.maps.LatLng( <?php echo Constants::MAP_DEFAULT_LATITUDE . "," . Constants::MAP_DEFAULT_LONGITUDE; ?> ),
                  zoom: 10,
                  mapTypeId: google.maps.MapTypeId.ROADMAP,

                });

            var marker = new google.maps.Marker({
                    position: new google.maps.LatLng( <?php echo "$store->lat, $store->lon";?>, true ),
                    map: map,
                    title: 'Here!'
                });

            if(marker != null) {
                map.setCenter(marker.getPosition());
            }

            google.maps.event.addListener(map, 'click', function (mouseEvent) {

                if(marker != null)
                  marker.setMap(null);

                var lat = document.getElementById('latitude');
                var longi = document.getElementById('longitude');
                lat.value = mouseEvent.latLng.lat(); //alert(mouseEvent.latLng.toUrlValue());
                longi.value = mouseEvent.latLng.lng();

                marker = new google.maps.Marker({
                    position: mouseEvent.latLng,
                    map: map,
                    title: 'Here!'
                });

                geocodePosition(marker.getPosition());
            });

            var geocoder = new google.maps.Geocoder();
            function codeAddress(address) {
                //In this case it gets the address from an element on the page, 
                // but obviously you  could just pass it to the method instead
                geocoder.geocode( { 'address': address}, function(results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        //In this case it creates a marker, but you can get the lat and lng from the location.LatLng
                        
                        if(marker != null)
                            marker.setMap(null);
                        
                        map.setCenter(results[0].geometry.location);
                        marker = new google.maps.Marker({
                            map: map, 
                            position: results[0].geometry.location
                        });

                        var lat = document.getElementById('latitude');
                        var longi = document.getElementById('longitude');
                        lat.value = results[0].geometry.location.lat();
                        longi.value = results[0].geometry.location.lng();

                    } else {
                        alert("Geocode was not successful for the following reason: " + status);
                    }
                });
            }

            function geocodePosition(pos) {
                var geocoder = new google.maps.Geocoder();
                geocoder.geocode({
                    latLng: pos
                    }, function(responses) {
                        if (responses && responses.length > 0) {
                            var txtAddress = document.getElementById('txtAddress');
                            txtAddress.value = responses[0].formatted_address;
                        }
                    });
            }

            document.getElementById("btnGeocode").addEventListener("click", function(){
                var txtAddress = document.getElementById("txtAddress");
                codeAddress(txtAddress.value);
            });

        });

        function validateLatLng(evt) {
            var theEvent = evt || window.event;
            var key = theEvent.keyCode || theEvent.which;
            key = String.fromCharCode( key );

            if(theEvent.keyCode == 8 || theEvent.keyCode == 127) {
                
            }
            else {
                var regex = /[0-9.]|\./;
                if( !regex.test(key) ) {
                  theEvent.returnValue = false;
                  if(theEvent.preventDefault) theEvent.preventDefault();
                }  
            }
        }
    </script>


    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">


        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Store Finder</a>
        </div>


        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li ><a href="home.php">Home</a></li>
            <li ><a href="categories.php">Categories</a></li>
            <li class="active"><a href="stores.php">Stores</a></li>
            <li ><a href="news.php">News</a></li>
            <li ><a href="admin_access.php">Admin Access</a></li>
            <li ><a href="users.php">Users</a></li>
          </ul>
          
          <ul class="nav navbar-nav navbar-right">
            <li ><a href="index.php">Logout</a></li>
          </ul>
        </div><!--/.nav-collapse -->
        
      </div>
    </div>

    <div class="container">

      <!-- Example row of columns -->
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Update Store</h3>
        </div>

        <div class="panel-body">
              <div class="row">
                <div class="col-md-7">

                  <form action="" method="POST">

                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Store Name" name="store_name" required value="<?php echo $store->store_name; ?>">
                      </div>

                      <br />
                      <div class="input-group">
                          <span class="input-group-addon"></span>
                          <input type="text" class="form-control" placeholder="Address" name="store_address" required id="txtAddress" value="<?php echo $store->store_address; ?>">
                          <span class="input-group-btn">
                              <button class="btn btn-default" type="button" id="btnGeocode">Find</button>
                          </span>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Email" name="email" value="<?php echo $store->email; ?>">
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Phone No" name="phone_no" value="<?php echo $store->phone_no; ?>">
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="SMS No" name="sms_no" value="<?php echo $store->sms_no; ?>">
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Website" name="website" value="<?php echo $store->website; ?>">
                      </div>

                      <br />
                      <div class="input-group" style="width:100%;" >
                        <select class="form-control" style="width:100%;" name="featured">
                          <option value="-1"  >Select if Store will be featured</option>
                          <option value="1" <?php echo $store->featured == 1 ? "selected" : ""; ?>>Store Featured</option>
                          <option value="0" <?php echo $store->featured == 0 ? "selected" : ""; ?>>Store Not Featured</option>
                        </select>
                      </div>

                      
                      <br />
                      <div class="input-group" style="width:100%;">
                        <select class="form-control" style="width:100%;" name="category_id">
                          <option value="None">Select Category</option>
                          <?php
                              if($categories != null) {


                                  foreach ($categories as $category)  {

                                        $selected = "";

                                        if($store->category_id == $category->category_id)
                                          $selected = "selected";

                                        echo "<option value='$category->category_id' $selected>$category->category</option>";
                                  }
                              }
                          ?>
                          
                        </select>
                      </div>

                     
                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Click on the Map for Latitude" name="lat" onkeypress='validateLatLng(event)' id="latitude" required value="<?php echo $store->lat; ?>">
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Click on the Map for Longitude" name="lon" onkeypress='validateLatLng(event)' id="longitude" required value="<?php echo $store->lon; ?>">
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <textarea type="text" class="form-control" placeholder="Description" rows="10" name="store_desc" id="details"><?php echo $store->store_desc; ?></textarea>
                      </div>

                      <br /> 
                      <p>
                          <button type="submit" name="submit" class="btn btn-info" onclick="checkInput()" role="button">Save</button> 
                          <a class="btn btn-info" href="stores.php" role="button">Cancel</a>
                      </p>
                  </form> 
                  


                </div>
                <div class="col-md-5">
                  <h4>Click the Map to get latitude/longitude:</h4>
                  <div id="map" style="width:100%; height:400px"></div>
               </div>
        </div>
      </div>


    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.js"></script>
    <script>
        function checkInput() {
            var website = document.getElementById("website");
            var details = document.getElementById("details");


            var website = document.getElementById("website");
            var details = document.getElementById("details");

            var strWebsite = website.value.replace("http://", "");
            strFb = strWebsite.replace("https://", "");
            website.value = strWebsite;

            var strDetails = details.value.replace("http://", "");
            strFb = strDetails.replace("https://", "");
            details.value = strDetails;
        }
    </script>
    
  

</body></html>