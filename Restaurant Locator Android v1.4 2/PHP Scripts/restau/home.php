<?php 

  require_once 'header.php';
  $controller = new ControllerRestaurant();
  $restaurants = $controller->getRestaurantsFeatured();

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

      <div class="panel panel-default">
              <!-- Default panel contents -->
              <div class="panel-heading clearfix">
                <h4 class="panel-title pull-left" style="padding-top:7px;padding-bottom:6px;">Featured Restaurants</h4>
                <div class="btn-group pull-right">
                  
                </div>
              </div>

              <!-- Table -->
              <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Address</th>
                    </tr>

                </thead>
                <tbody>
                    <?php 

                        if($restaurants != null) {

                          $ind = 1;
                          foreach ($restaurants as $restaurant)  {
                                echo "<tr>";
                                echo "<td>$ind</td>";
                                echo "<td>$restaurant->name</td>";
                                echo "<td>$restaurant->address</td>";
                                echo "</tr>";

                                ++$ind;
                          }
                        }

                    ?>

                </tbody>
                
              </table>
            </div>

            <div class="panel panel-default">
              <!-- Default panel contents -->
              <div class="panel-heading clearfix">
                <h4 class="panel-title pull-left" style="padding-top:7px;padding-bottom:6px;">Rest XML/JSON</h4>
                <div class="btn-group pull-right">
                  
                </div>
              </div>
              <br />
              <p style="padding-left:10px;">Note: (Copy this URL to Config.h/Config.java in Restau! iOS/Android App)</p>
              <!-- Table -->
              <table class="table">
                <thead>
                    <tr>
                        <th>Type</th>
                        <th>URL</th>
                    </tr>

                </thead>
                <tbody>
                    <tr>
                        <th>JSON</th>
                        <th><?php 
                                $json_path = Constants::ROOT_URL.Constants::JSON_FILE;
                                echo "<a href='$json_path'>$json_path</a>"; 
                            ?>
                        </th>
                    </tr>

                    <tr>
                        <th>XML</th>
                        <th><?php 
                                $xml_path = Constants::ROOT_URL.Constants::XML_FILE;
                                echo "<a href='$xml_path'>$xml_path</a>"; 
                            ?>
                        </th>
                    </tr>

                </tbody>
                
              </table>
            </div>
      


    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.js"></script>
    
  

</body></html>