<?php

  require '../header_rest.php';
  $controllerRest = new ControllerRest();
  $controllerUser = new ControllerUser();
  $controllerRating = new ControllerRating();

  $user_id = 0;
  if(!empty($_POST['user_id']) )
    $user_id = $_POST['user_id'];

  $store_id = 0;
  if(!empty($_POST['store_id']) )
    $store_id = $_POST['store_id'];

  $login_hash = 0;
  if(!empty($_POST['login_hash']) )
    $login_hash = $_POST['login_hash'];

  if( !empty($user_id) && !empty($store_id) && !empty($login_hash)) {

    if(!$controllerUser->isUserIdExistAndHash($user_id, $login_hash)) {
        $json = "{ \"status\" : { \"status_code\" : \"3\", \"status_text\" : \"Invalid Access\" } }";

        echo $json;
    }
    else {

      $itm = $controllerRating->checkUserCanRate($store_id, $user_id);
      $canRate = $itm != null ? -1 : 1;

      // header ("content-type: text/json");
      header("Content-Type: application/text; charset=ISO-8859-1");
      echo "{"; echo "\"store_rating\" : { \"store_id\" : \"$store_id\", \"can_rate\" : \"$canRate\" }, \"status\" : { \"status_code\" : \"-1\", \"status_text\" : \"Success.\" }"; echo "}";
    }
         
  }
  else {

      $json = "{ \"status\" : { \"status_code\" : \"3\", \"status_text\" : \"Invalid Access\" } }";

      echo $json;    
  }
?>