<?php 

  require_once 'header.php';
  $controller = new ControllerNews();
  

  $extras = new Extras();
  $news_id = $extras->decryptQuery1(KEY_SALT, $_SERVER['QUERY_STRING']);

  if($news_id != null) {

        $news = $controller->getNewsByNewsId($news_id);

        if( isset($_POST['submit']) ) {
    
            $itm = $news;
            $itm->news_url = htmlspecialchars(trim(strip_tags($_POST['news_url'])));
            $itm->news_title = htmlspecialchars(trim(strip_tags($_POST['news_title'])));
            
            $news_content = preg_replace('~[\r\n]+~', '', $_POST['news_content']);
            $itm->news_content = htmlspecialchars(trim(strip_tags($news_content)));
            
            $itm->updated_at = time();
            $itm->photo_url = trim(strip_tags($_POST['photo_url']));
            
            $count = count($_FILES["file"]["name"]);

            if( !empty($_FILES["file"]["name"][0])) {
                uploadFile($controller, $itm);
            }
            else {

                $controller->updateNews($itm);
                echo "<script type='text/javascript'>location.href='news.php';</script>";
            }
      }
  }
  else {
      echo "<script type='text/javascript'>location.href='403.php';</script>";
  }

  function uploadFile($controller, $itm) {

      $extras = new Extras();
      
      $desired_dir = Constants::IMAGE_UPLOAD_DIR;
      $errors= array();
      $count=count($_FILES["file"]["name"]);

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


          $new_file_name = $desired_dir."/"."news_".$timestamp.".".$extension;

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

          }else{
              print_r($errors);
          }
      }

      $controller->updateNews($itm);
      echo "<script type='text/javascript'>location.href='news.php';</script>";
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
            <li ><a href="stores.php">Stores</a></li>
            <li class="active"><a href="news.php">News</a></li>
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
          <h3 class="panel-title">Update News</h3>
        </div>

        <div class="panel-body">
              <div class="row">
                <form action="" method="POST" enctype="multipart/form-data">
                <div class="col-md-7">

                  

                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="News Title" name="news_title" required value="<?php echo $news->news_title; ?>">
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="News URL" name="news_url" required value="<?php echo $news->news_url; ?>">
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Photo Url" name="photo_url" value="<?php echo $news->photo_url; ?>">
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <textarea type="text" class="form-control" placeholder="News Content" rows="10" name="news_content" id="details"><?php echo $news->news_content; ?></textarea>
                      </div>

                   
                  


                </div>
                <div class="col-md-5">

                  <h4>Instead of URL, upload via File</h4>
                  

                    <div class="input-group">
                      <p>Photo File</p>
                      <input type="file" name="file[]" />
                    </div>

                    <br /> 
                      <p>
                          <button type="submit" name="submit" class="btn btn-info" onclick="checkInput()" role="button">Save</button> 
                          <a class="btn btn-info" href="news.php" role="button">Cancel</a>
                      </p>

                  

               </div>

               </form>
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