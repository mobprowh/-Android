<?php

  require_once '../header_rest.php';
  $controllerUser = new ControllerUser();



  if( !empty($_POST['username']) )
      $username = $_POST['username'];

  if( !empty($_POST['password']) )
      $password = md5($_POST['password']);

  if( !empty($_POST['facebook_id']) )
      $facebook_id = $_POST['facebook_id'];

  if( !empty($_POST['twitter_id']) )
      $twitter_id = $_POST['twitter_id'];

  if( !empty($username) && !empty($password) ) {
      
      $user = $controllerUser->loginUser($username, $password);
      if($user != null) {
          // update the hash
          $controllerUser->updateUserHash($user);
          $json = translateJSON($user);
      }
      else {

          $json = "{ \"status\" : { \"status_code\" : \"1\", \"status_text\" : \"Username/Password Invalid or you are being denied to access. Please try again.\" } }";

      }

      echo $json;
  }

  else if( !empty($facebook_id) ) {

      $user = $controllerUser->loginFacebook($facebook_id);
      if($user != null) {
          // update the hash
          $controllerUser->updateUserHash($user);
          $json = translateJSON($user);
      }
      else {

          $json = "{ \"status\" : { \"status_code\" : \"2\", \"status_text\" : \"Invalid Login.\" } }";

      }

      echo $json;
  }

  else if( !empty($twitter_id) ) {

      $user = $controllerUser->loginTwitter($twitter_id);
      if($user != null) {
          // update the hash
          $controllerUser->updateUserHash($user);
          $json = translateJSON($user);
      }
      else {

          $json = "{ \"status\" : { \"status_code\" : \"2\", \"status_text\" : \"Invalid Login.\" } }";
      }

      echo $json;
  }
  else {

      $json = "{ \"status\" : { \"status_code\" : \"3\", \"status_text\" : \"Invalid Access.\" } }";

      echo $json;
  }



  function translateJSON($itm) {

      
      $json = "{ \"user_info\" : { \"user_id\" : \"$itm->user_id\", \"username\" : \"$itm->username\", \"login_hash\" : \"$itm->login_hash\", \"facebook_id\" : \"$itm->facebook_id\", \"twitter_id\" : \"$itm->twitter_id\", \"full_name\" : \"$itm->full_name\", \"thumb_url\" : \"$itm->thumb_url\", \"photo_url\" : \"$itm->photo_url\", \"email\" : \"$itm->email\" }, \"status\" : { \"status_code\" : \"-1\", \"status_text\" : \"Success.\" } }";

      return $json;
  }

?>