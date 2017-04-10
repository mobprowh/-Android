<?php

  require '../header_rest.php';
  $controllerRest = new ControllerRest();
  $controllerUser = new ControllerUser();

  $password = "";
  if( !empty($_POST['password']) )
      $password = md5($_POST['password']);

  $full_name = "";
  if( !empty($_POST['full_name']) )
      $full_name = $_POST['full_name'];

  $email = "";
  if( !empty($_POST['email']) )
      $email = $_POST['email'];

    $user_id ="";
    if( !empty($_POST['user_id']) )
        $user_id = $_POST['user_id'];

    $login_hash ="";
    if( !empty($_POST['login_hash']) )
        $login_hash = $_POST['login_hash'];


    if(!$controllerUser->isUserIdExistAndHash($user_id, $login_hash)) {
        $json = "{ \"status\" : { \"status_code\" : \"3\", \"status_text\" : \"Invalid Access\" } }";

        echo $json;
    }
    else {

        $itm = $controllerUser->getUserByUserId($user_id);
        
        if($itm != null) {

            $itm->full_name = $full_name;
            $itm->password = $password;
            $controllerUser->updateUserNameAndPassword($itm);

            $itm = $controllerUser->getUserByUserId($user_id);
            $json = "{ \"user_info\" : { \"user_id\" : \"$itm->user_id\", \"username\" : \"$itm->username\", \"login_hash\" : \"$itm->login_hash\", \"facebook_id\" : \"$itm->facebook_id\", \"twitter_id\" : \"$itm->twitter_id\", \"full_name\" : \"$itm->full_name\", \"thumb_url\" : \"$itm->thumb_url\", \"photo_url\" : \"$itm->photo_url\", \"email\" : \"$itm->email\"}, \"status\" : { \"status_code\" : \"-1\", \"status_text\" : \"Success.\" } }";

            echo $json;
        }
    
    }


?>