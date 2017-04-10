<?php

  require '../header_rest.php';
  $controllerRest = new ControllerRest();
  $controllerUser = new ControllerUser();
  $controllerReview = new ControllerReview();

  $user_id = 0;
  if(!empty($_POST['user_id']) )
    $user_id = $_POST['user_id'];

  $store_id = 0;
  if(!empty($_POST['store_id']) )
    $store_id = $_POST['store_id'];

  $login_hash = 0;
  if(!empty($_POST['login_hash']) )
    $login_hash = $_POST['login_hash'];

  $review = "";
  if(!empty($_POST['review']) )
    $review = $_POST['review'];


  if( !empty($user_id) && !empty($store_id) && !empty($login_hash) && !empty($review) ) {

    if(!$controllerUser->isUserIdExistAndHash($user_id, $login_hash)) {
        $json = "{ \"status\" : { \"status_code\" : \"3\", \"status_text\" : \"Invalid Access\" } }";

        echo $json;
    }
    else {

      $itm = new Review();
      $itm->review = $review;
      $itm->store_id = $store_id; 
      $itm->user_id = $user_id;
      $itm->created_at = time();
      $itm->updated_at = time();
      $controllerReview->insertReview($itm);

      $tmp_from = 0;
      $tmp_to = time();

      $resultReviews = $controllerRest->getResultReviews($tmp_from, $tmp_to, $store_id);

      // header ("content-type: text/json");
      header("Content-Type: application/text; charset=ISO-8859-1");
      echo "{";

                // REVIEWS
                echo "\"reviews\" : [";
                $no_of_rows = $resultReviews->rowCount();
                $ind = 0;
                $count = $resultReviews->columnCount();
                foreach ($resultReviews as $row) 
                {
                    echo "{";
                    $inner_ind = 0;
                    foreach ($row as $columnName => $field) 
                    {

                        $val = trim(strip_tags($field));

                        if($columnName == "review") {
                            $val1 = preg_replace('~[\r\n]+~', '', $val);
                            $val = htmlspecialchars(trim(strip_tags($val1)));
                        }
                        
                        if(!is_numeric($columnName)) {
                            echo "\"$columnName\" : \"$val\"";

                            if($inner_ind < $count - 1)
                              echo ",";

                            ++$inner_ind;
                        }
                    }

                    if($count > $inner_ind) {
                        echo "\"slug\" : \"slug\"";
                    }

                    echo "}";

                    if($ind < $no_of_rows - 1)
                      echo ",";

                    ++$ind;
                }
                echo "], \"status\" : { \"status_code\" : \"-1\", \"status_text\" : \"Success.\" }";

      echo "}";
    }
   

      
  }
  else {

      $json = "{ \"status\" : { \"status_code\" : \"3\", \"status_text\" : \"Invalid Access\" } }";

      echo $json;    
  }
?>