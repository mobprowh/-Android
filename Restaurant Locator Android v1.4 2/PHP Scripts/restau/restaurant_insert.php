<?php 

  require_once 'header.php';
  $controller = new ControllerRestaurant();
  $controllerCategory = new ControllerCategory();
  $categories = $controllerCategory->getCategories();
  
  $extras = new Extras();
  if( isset($_POST['submit']) ) {
    
    $itm = new Restaurant();

    $itm->name = htmlspecialchars(trim(strip_tags($_POST['name'])), ENT_QUOTES);
    $itm->address = htmlspecialchars(trim(strip_tags($_POST['address'])), ENT_QUOTES);
    $itm->lat = trim(strip_tags($_POST['lat']));
    $itm->lon = trim(strip_tags($_POST['lon']));
    $itm->desc1 = $extras->removeHttp( htmlspecialchars(trim(strip_tags($_POST['desc1'])), ENT_QUOTES) );
    $itm->email = $extras->removeHttp( htmlspecialchars(trim(strip_tags($_POST['email'])), ENT_QUOTES) );
    $itm->website = $extras->removeHttp( htmlspecialchars(trim(strip_tags($_POST['website'])), ENT_QUOTES) );
    $itm->amenities = htmlspecialchars(trim(strip_tags($_POST['amenities'])), ENT_QUOTES);
    $itm->food_rating = trim(strip_tags($_POST['food_rating']));
    $itm->price_rating = $extras->removeHttp( trim(strip_tags($_POST['price_rating'])) );
    $itm->featured = trim(strip_tags($_POST['featured']));
    $itm->phone = htmlspecialchars(trim(strip_tags($_POST['phone'])), ENT_QUOTES);
    $itm->hours = htmlspecialchars(trim(strip_tags($_POST['hours'])), ENT_QUOTES);
    $itm->category_id = trim(strip_tags($_POST['category_id']));
    $itm->created_at = time();

    $controller->insertRestaurant($itm);
    echo "<script type='text/javascript'>location.href='restaurants.php';</script>";

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

    <title>Restau! Finder</title>

    <!-- Bootstrap core CSS -->
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="bootstrap/css/navbar-fixed-top.css" rel="stylesheet">
    <link href="bootstrap/css/custom.css" rel="stylesheet">

    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBB7Tce0Xd3GEb838FF5uRcIe8MQIRdQSo&sensor=false"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="maps.plugin.js"></script>
    <script type="text/javascript">
        $(function(){
            var mapDiv = document.getElementById('map');
                var map = new google.maps.Map(mapDiv, {
                  center: new google.maps.LatLng(<?php echo Constants::MAP_DEFAULT_LATITUDE . "," . Constants::MAP_DEFAULT_LONGITUDE; ?> ),
                  zoom: <?php echo Constants::MAP_DEFAULT_ZOOM_LEVEL_ADD; ?>,
                  mapTypeId: google.maps.MapTypeId.ROADMAP,

                });

            var marker;
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
          <a class="navbar-brand" href="#">Drinkea</a>
        </div>


        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li ><a href="home.php">Inicio</a></li>
            <li ><a href="categories.php">Categorias</a></li>
            <li class="active"><a href="restaurants.php">Bares</a></li>
            <li><a href="admin_access.php">Admin Access</a></li>
          </ul>
          
          <ul class="nav navbar-nav navbar-right">
            <li ><a href="index.php">Cerrar Sesión</a></li>
          </ul>
        </div><!--/.nav-collapse -->
        
      </div>
    </div>

    <div class="container">

      <!-- Example row of columns -->
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Add Restaurant</h3>
        </div>

        <div class="panel-body">
              <div class="row">
                <div class="col-md-7">

                  <form action="" method="POST">

                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Restaurant Name" name="name" required>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Address" name="address" required>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="email" class="form-control" placeholder="Email" name="email" required>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Website" required name="website" id="website">
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Amenities" required name="amenities">
                      </div>

                      <br />
                      <div class="input-group" style="width:100%;" >
                        <select class="form-control" style="width:100%;" name="food_rating">
                          <option value="0">Select Food Rating</option>
                          <option value="1.0">1.0</option>
                          <option value="1.5">1.5</option>
                          <option value="2.0">2.0</option>
                          <option value="2.5">2.5</option>
                          <option value="3.0">3.0</option>
                          <option value="3.5">3.5</option>
                          <option value="4.0">4.0</option>
                          <option value="4.5">4.5</option>
                          <option value="5.0">5.0</option>
                        </select>
                      </div>

                      <br />
                      <div class="input-group" style="width:100%;" >
                        <select class="form-control" style="width:100%;" name="price_rating">
                          <option value="0">Select Price Rating</option>
                          <option value="1.0">1.0</option>
                          <option value="1.5">1.5</option>
                          <option value="2.0">2.0</option>
                          <option value="2.5">2.5</option>
                          <option value="3.0">3.0</option>
                          <option value="3.5">3.5</option>
                          <option value="4.0">4.0</option>
                          <option value="4.5">4.5</option>
                          <option value="5.0">5.0</option>
                        </select>
                      </div>

                      <br />
                      <div class="input-group" style="width:100%;" >
                        <select class="form-control" style="width:100%;" name="featured">
                          <option value="-1">Select if Restaurant will be featured</option>
                          <option value="1">Restaurant Featured</option>
                          <option value="0">Restaurant Not Featured</option>
                        </select>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Phone Number" required name="phone" >
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Open/Closed Hours" required name="hours" >
                      </div>

                      <br />
                      <div class="input-group" style="width:100%;">
                        <select class="form-control" style="width:100%;" name="category_id">
                          <option value="None">Select Category</option>
                          <?php
                              if($categories != null) {
                                  foreach ($categories as $category)  {
                                        echo "<option value='$category->category_id'>$category->category</option>";
                                  }
                              }
                          ?>
                          
                        </select>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Click on the Map for Latitude" name="lat" onkeypress='validateLatLng(event)' id="latitude" required>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Click on the Map for Longitude" name="lon" onkeypress='validateLatLng(event)' id="longitude" required>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <textarea type="text" class="form-control" placeholder="Description" rows="10" name="desc1" id="details"></textarea>
                      </div>

                      <br /> 
                      <p>
                          <button type="submit" name="submit" class="btn btn-info" onclick="checkInput()" role="button">Save</button> 
                          <a class="btn btn-info" href="restaurants.php" role="button">Cancel</a>
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