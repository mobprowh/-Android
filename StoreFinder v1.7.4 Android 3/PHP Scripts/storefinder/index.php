<?php 
  require 'header_auth.php';
  $_SESSION['name'] = "";
  $controller = new ControllerAuthentication();
  if( isset($_POST['submit']) ) {
      $auth = $controller->login($_POST['username'], md5($_POST['password']) );
      if($auth != null) {
        $_SESSION['name'] = $auth->name;
        // header("Location:home.php");
        echo "<script type='text/javascript'>location.href='home.php';</script>";
      }
      else {
          echo "<script>alert('Invalid Username/Password.');</script>";
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

    <title>Store Finder Signin</title>

    <!-- Bootstrap core CSS -->
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="bootstrap/css/signin.css" rel="stylesheet">
    <link href="bootstrap/css/custom.css" rel="stylesheet">

  </head>

  <body>

    <div class="container">

      <form class="form-signin" role="form" method="POST">
        <img src="bootstrap/images/app_logo_large.png" class="center-block"/>
        <h3 class="form-signin-heading">Please sign in</h3>
        <input class="form-control" placeholder="Username" required="" autofocus="" type="text" name="username" required>
        <input class="form-control" placeholder="Password" required="" type="password" name="password" required>
        
        <button class="btn btn-lg btn-primary btn-block" type="submit" name="submit">Sign in</button>
      </form>

    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/bootstrap.js"></script>

  </body>
</html>