<?php 

  require_once 'header.php';
  $controller = new ControllerStore();
  $controllerReview = new ControllerReview();
  $controllerUser = new ControllerUser();

  

  if(!empty($_SERVER['QUERY_STRING'])) {

      $extras = new Extras();
      $store_id = $extras->decryptQuery1(KEY_SALT, $_SERVER['QUERY_STRING']);
      $review_delete = $extras->decryptQuery2(KEY_SALT, $_SERVER['QUERY_STRING']);

      $reviews = $controllerReview->getReviewsByStoreId($store_id);
      $store = $controller->getStoreByStoreId($store_id);

      if($review_delete != null) {
          $store_id = $review_delete[0];
          $review_id = $review_delete[1];
          $controllerReview->deleteReview($review_id, 1);

          $viewUrl = $extras->encryptQuery1(KEY_SALT, 'store_id', $store_id, 'store_reviews_view.php');
          echo "<script type='text/javascript'>location.href='$viewUrl';</script>";
      }

      if($store_id == null) {
        echo "<script type='text/javascript'>location.href='403.php';</script>";
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
          <h4 class="panel-title pull-left" style="padding-top: 7px;"><?php echo $store->store_name."'s"; ?> Review</h4>
          <div class="btn-group pull-right">
            <!-- <a href="car_insert.php" class="btn btn-default btn-sm">Add Car</a> -->
            <form method="POST" action="">
                  <a href="stores.php" class="btn btn-default btn-sm"><span class='glyphicon glyphicon-arrow-left'></span></a>
            </form>
          </div>
        </div>

        <!-- Table -->
        <table class="table">
          <thead>
              <tr>
                  <th>#</th>
                  <th >Name</th>
                  <th width="50%">Review</th>
                  <th>Action</th>
              </tr>

          </thead>
          <tbody>
              <?php 

                  if($reviews != null) {

                    $ind = 1;
                    foreach ($reviews as $review)  {
                      
                          $extras = new Extras();
                          $deleteUrl = $extras->encryptQuery2(KEY_SALT, 'review_id', $review->store_id, 'photo_id', $review->review_id, 'store_reviews_view.php');

                          $user = $controllerUser->getUserByUserId($review->user_id);

                          echo "<tr>";
                          echo "<td>$ind</td>";
                          echo "<td>$user->full_name</td>";
                          echo "<td>$review->review</td>";
                          
                
                          echo "<td>
                                    <button  class='btn btn-primary btn-xs' data-toggle='modal' data-target='#modal_$review->review_id'><span class='glyphicon glyphicon-remove'></span></button>
                                    
                                </td>";
                          echo "</tr>";


                          //<!-- Modal -->
                          echo "<div class='modal fade' id='modal_$review->review_id' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>

                                      <div class='modal-dialog'>
                                          <div class='modal-content'>
                                              <div class='modal-header'>
                                                    <button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>
                                                    <h4 class='modal-title' id='myModalLabel'>Deleting Review</h4>
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


    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.js"></script>
    
  

</body></html>