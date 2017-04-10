<?php

  require '../header_rest.php';
  $controllerRest = new ControllerRest();
  $controllerUser = new ControllerUser();

  if(empty($_GET['store_id']) )
  {
        $json = "{ \"status\" : { \"status_code\" : \"3\", \"status_text\" : \"Invalid Access.\" } }";

        echo $json;
  }

  else {

    $tmp_from = empty($_GET['tmp_from']) ? 0 : $_GET['tmp_from'];
    $tmp_to = time();
    $store_id = $_GET['store_id'];

    $resultReviews = $controllerRest->getResultReviews($tmp_from, $tmp_to, $store_id);

      // header ("content-type: text/json");
      // header("Content-Type: application/text; charset=ISO-8859-1");
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
                echo "]";

      echo "}";

    
  }
?>