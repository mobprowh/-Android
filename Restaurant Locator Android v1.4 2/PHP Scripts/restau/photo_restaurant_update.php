<?php 

  require_once 'header.php';
  $controller = new ControllerPhoto();

  $extras = new Extras();
  
  
  $photo_id = $extras->decryptQuery1(KEY_SALT, $_SERVER['QUERY_STRING']);

  if($photo_id != null) {
        $photo = $controller->getPhotoByPhotoId($photo_id);

        if( isset($_POST['url_upload']) ) {
    
          $itm = new Photo();
          $itm->photo_url = trim($_POST['photo_url']);
          $itm->thumb_url = trim($_POST['thumb_url']);
          $itm->restaurant_id = $photo->restaurant_id;
          $itm->photo_id = $photo->photo_id;

          $controller->updatePhoto($itm);

          $viewUrl = $extras->encryptQuery1(KEY_SALT, 'restaurant_id', $photo->restaurant_id, 'photo_restaurant_view.php');
          echo "<script type='text/javascript'>location.href='$viewUrl';</script>";
        }

        if( isset($_POST['file_upload']) ) {

            $count = count($_FILES["file"]["name"]);
            if( !empty($_FILES["file"]["name"][0]) && !empty($_FILES["file"]["name"][1]) ) {
                uploadFile($controller, $photo);
            }
            else {
                echo "<script>alert('You must provide both Photo and Thumbnail file.');</script>";
            }
        }
  }
  else {
        echo "<script type='text/javascript'>location.href='403.php';</script>";
  }

  

?>

<?php 

  
  function uploadFile($controller, $photo) {

      $itm = new Photo();
      $itm->restaurant_id = $photo->restaurant_id;
      $itm->photo_id = $photo->photo_id;

      $desired_dir = Constants::IMAGE_UPLOAD_DIR;
      $errors= array();
      $count=count($_FILES["file"]["name"]);

      $pf_name = explode("/", $photo->photo_url);
      $pf_full_name = end($pf_name);
      $path_file = $desired_dir."/".$pf_full_name;

      if( file_exists($path_file) ) { 
          unlink($path_file);
      }

      $pf_name = explode("/", $photo->thumb_url);
      $pf_full_name = end($pf_name);
      $path_file = $desired_dir."/".$pf_full_name;

      if( file_exists($path_file) ) { 
          unlink($path_file);
      }

      for($key = 0; $key < $count; $key++){

          $file_name = $_FILES['file']['name'][$key];
          $file_size = $_FILES['file']['size'][$key];
          $file_tmp = $_FILES['file']['tmp_name'][$key];
          $file_type= $_FILES['file']['type'][$key];

          if($file_size > 2097152){
              $errors[]='File size must be less than 2 MB';
          }    

          $date = date_create();
          $timestamp =  time();
          $temp = explode(".", $_FILES["file"]["name"][0]);
          $extension = end($temp);


          $new_file_name = $desired_dir."/"."large_".$timestamp.".".$extension;

          if($key == 1)
            $new_file_name = $desired_dir."/"."thumb_".$timestamp.".".$extension;

          
          if(empty($errors)==true){
            if(is_dir($desired_dir)==false){
                // Create directory if it does not exist
                mkdir("$desired_dir", 0700);        
            }
            if(is_dir($file_name)==false){
                // rename the file if another one exist
                move_uploaded_file($file_tmp, $new_file_name);
            }else{                                  
                $new_dir = $new_file_name.time();
                rename($file_tmp, $new_dir) ;               
            }

            if($key == 0) {
                $itm->photo_url = Constants::ROOT_URL.$new_file_name;
            }

            if($key == 1) {
                $itm->thumb_url = Constants::ROOT_URL.$new_file_name;
            }

          }else{
              print_r($errors);
          }
      }

      
      $controller->updatePhoto($itm);

      $extras = new Extras();
      $viewUrl = $extras->encryptQuery1(KEY_SALT, 'restaurant_id', $photo->restaurant_id, 'photo_restaurant_view.php');
      echo "<script type='text/javascript'>location.href='$viewUrl';</script>";
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
          <h3 class="panel-title">Update Restaurant Photo</h3>
        </div>

        <div class="panel-body">
              <div class="row">
                <div class="col-md-6">

                  <form action="" method="POST">
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Photo URL" name="photo_url" required value="<?php echo $photo->photo_url;?>" >
                      </div>


                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Thumbnail URL" name="thumb_url" required  value="<?php echo $photo->thumb_url;?>" >
                      </div>


                      <br />
                     


                      <br /> 
                      <p>
                          <button type="submit" name="url_upload" class="btn btn-info"  role="button">Save</button> 

                          <?php 
                              $extras = new Extras();
                              $viewUrl = $extras->encryptQuery1(KEY_SALT, 'restaurant_id', $photo->restaurant_id, 'photo_restaurant_view.php');
                              echo "<a class='btn btn-info' href='$viewUrl' role='button'>Cancel</a>";
                          ?>
                          
                      </p>
                  </form><!--/.form --> 
                
                </div><!--/.col-md-6 -->


                
                <div class="col-md-6">

                  <form action="" method="POST" enctype="multipart/form-data">

                    <div class="input-group">
                      <p>Photo File</p>
                      <input type="file" name="file[]" />
                    </div>

                    <br /> 
                    <div class="input-group">
                      <p>Thumbnail File</p>
                      <input type="file" name="file[]" />
                    </div>

                    <br />
                   

                    <br /> 
                      <p>
                          <button type="submit" name="file_upload" class="btn btn-info"  role="button">Save</button> 
                          <?php 
                              $extras = new Extras();
                              $viewUrl = $extras->encryptQuery1(KEY_SALT, 'restaurant_id', $photo->restaurant_id, 'photo_restaurant_view.php');
                              echo "<a class='btn btn-info' href='$viewUrl' role='button'>Cancel</a>";
                          ?>
                      </p>

                  </form><!--/.form -->

                </div><!--/.col-md-6 -->

              </div><!--/.row -->
        </div><!--/.panel-body -->
      </div>


    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.js"></script>
    
  

</body></html>