<?php 

  require_once 'header.php';
  $controller = new ControllerStore();
  $controllerPhoto = new ControllerPhoto();
  $controllerRating = new ControllerRating();
  $stores = $controller->getStores();

  if(!empty($_SERVER['QUERY_STRING'])) {

      $extras = new Extras();
      $store_id = $extras->decryptQuery1(KEY_SALT, $_SERVER['QUERY_STRING']);
      $store_id_featured = $extras->decryptQuery2(KEY_SALT, $_SERVER['QUERY_STRING']);

      if( $store_id != null ) {
          $controller->deleteStore($store_id, 1);
          echo "<script type='text/javascript'>location.href='stores.php';</script>";
      }
      
      
      if($store_id_featured != null) {
          $itm = new Store();
          $itm->store_id = $store_id_featured[0];
          $itm->featured = $store_id_featured[1] == "yes" ? 0 : 1;
          
          $res = $controller->updateStoreFeatured($itm);

          
          echo "<script type='text/javascript'>location.href='stores.php';</script>";
      }

      // if($store_id_featured == null && $store_id == null) {
      //   echo "<script type='text/javascript'>location.href='403.php';</script>";
      // }
  }

  $begin = 0;
  $page = 1;
  $count = count($stores);
  $pages = intval($count/Constants::NO_OF_ITEMS_PER_PAGE);
  $search_criteria = "";
  if( isset($_POST['button_search']) ) {
      $search_criteria = trim(strip_tags($_POST['search']));
      $stores = $controller->getStoresBySearching($search_criteria);
  }

  else {
      

      if($count%Constants::NO_OF_ITEMS_PER_PAGE != 0)
        $pages += 1;

      if( !empty($_GET['page']) ) {

          $page = $_GET['page'];
          $begin = ($page -1) * Constants::NO_OF_ITEMS_PER_PAGE;
          $end = Constants::NO_OF_ITEMS_PER_PAGE;
          $stores = $controller->getStoresAtRange($begin, $end);
      }
      else {
          $begin = ($page -1) * Constants::NO_OF_ITEMS_PER_PAGE;
          $end = Constants::NO_OF_ITEMS_PER_PAGE;
          $stores = $controller->getStoresAtRange($begin, $end);

      }
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

      <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading clearfix">
          <h4 class="panel-title pull-left" style="padding-top: 7px;">Stores</h4>
          <div class="btn-group pull-right">
            <!-- <a href="car_insert.php" class="btn btn-default btn-sm">Add Car</a> -->
            <form method="POST" action="">
                  <input type="text" style="height:100%;color:#000000;padding-left:5px;" placeholder="Search" name="search" value="<?php echo $search_criteria; ?>">
                  <button type="submit" name="button_search" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-search"></span></button>
                  <button type="submit" class="btn btn-default btn-sm" name="reset"><span class="glyphicon glyphicon-refresh"></span></button>
                  <a href="store_insert.php" class="btn btn-default btn-sm"><span class='glyphicon glyphicon-plus'></span></a>
            </form>
          </div>
        </div>

        <!-- Table -->
        <table class="table">
          <thead>
              <tr>
                  <th>#</th>
                  <th>Name</th>
                  <th width="35%">Address</th>
                  <th>No of Photos</th>
                  <th>Rating</th>
                  <th>Featured</th>
                  <th>Action</th>
              </tr>

          </thead>
          <tbody>
              <?php 

                  if($stores != null) {

                    $ind = $begin + 1;
                    foreach ($stores as $store)  {
                      
                          $featured = "no";
                          if($store->featured == 1)
                              $featured = "yes";

                          $no_of_photos = $controllerPhoto->getNoOfPhotosByStoreId($store->store_id);
                          $rating = $controllerRating->getRatingByStoreId($store->store_id);

                          $extras = new Extras();
                          $updateUrl = $extras->encryptQuery1(KEY_SALT, 'store_id', $store->store_id, 'store_update.php');
                          $deleteUrl = $extras->encryptQuery1(KEY_SALT, 'store_id', $store->store_id, 'stores.php');
                          $featuredUrl = $extras->encryptQuery2(KEY_SALT, 'store_id', $store->store_id, 'featured', $featured, 'stores.php');
                          $viewUrl = $extras->encryptQuery1(KEY_SALT, 'store_id', $store->store_id, 'photo_store_view.php');
                          $photoUrl = $extras->encryptQuery1(KEY_SALT, 'store_id', $store->store_id, 'photo_store_insert.php');
                          $reviewUrl = $extras->encryptQuery1(KEY_SALT, 'store_id', $store->store_id, 'store_reviews_view.php');

                          echo "<tr>";
                          echo "<td>$ind</td>";
                          echo "<td>$store->store_name</td>";
                          echo "<td>$store->store_address</td>";
                          echo "<td>$no_of_photos Photo(s)</td>";
                          echo "<td>$rating</td>";
                          
                          if($store->featured == 1) {
                            echo "<td><a class='btn btn-primary btn-xs' href='$featuredUrl'>Featured</a> ";
                          }
                          else {
                            echo "<td><a class='btn btn-default btn-xs' href='$featuredUrl'>Featured</a> ";
                          }


                          echo "<td>
                                    <a class='btn btn-primary btn-xs' href='$updateUrl'><span class='glyphicon glyphicon-pencil'></span></a>
                                    <button  class='btn btn-primary btn-xs' data-toggle='modal' data-target='#modal_$store->store_id'><span class='glyphicon glyphicon-remove'></span></button>
                                    <a class='btn btn-primary btn-xs' href='$viewUrl'><span class='glyphicon glyphicon-th'></span></a>
                                    <a class='btn btn-primary btn-xs' href='$reviewUrl'><span>Reviews</span></a>
                                    
                                </td>";
                          echo "</tr>";


                          //<!-- Modal -->
                          echo "<div class='modal fade' id='modal_$store->store_id' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>

                                      <div class='modal-dialog'>
                                          <div class='modal-content'>
                                              <div class='modal-header'>
                                                    <button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>
                                                    <h4 class='modal-title' id='myModalLabel'>Deleting Store</h4>
                                              </div>
                                              <div class='modal-body'>
                                                    <p>Deleting this is not irreversible. Do you wish to continue?
                                              </div>
                                              <div class='modal-footer'>
                                                  <button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>
                                                  <a type='button' class='btn btn-primary' href='$deleteUrl'>Delete</a>
                                              </div>
                                          </div>
                                      </div>
                                </div>";

                          ++$ind;
                    }
                  }

              ?>

          </tbody>
          
        </table>
      </div>

      <div class="btn-group pull-right">
          <?php
              if(empty($search_criteria)) {
                    if($pages != 0) {
                        if($page == 1) {
                          echo "<a class='btn btn-primary btn-xs' href='stores.php?page=1'><span class='glyphicon glyphicon-chevron-left'></span></a>";
                        }
                        else {
                          $newPage = $page -1;
                          echo "<a class='btn btn-primary btn-xs' href='stores.php?page=$newPage'><span class='glyphicon glyphicon-chevron-left'></span></a>";
                        }

                        echo "<a class='btn btn-primary btn-xs' href='#'>$page/$pages</a>";

                        if($page == $pages) {

                          echo "<a class='btn btn-primary btn-xs' href='stores.php?page=$pages'><span class='glyphicon glyphicon-chevron-right'></span></a>";
                        }
                        else {
                          $newPage = $page + 1;
                          echo "<a class='btn btn-primary btn-xs' href='stores.php?page=$newPage'><span class='glyphicon glyphicon-chevron-right'></span></a>";
                        }
                    }
              }

              
              
          ?>
        </div>


    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.js"></script>
    
  

</body></html>