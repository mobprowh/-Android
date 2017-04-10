<?php

  require_once '../application/DB_Connect.php';
  require_once '../controllers/ControllerRest.php';
  $controllerRest = new ControllerRest();

  $resultRestaurants = $controllerRest->getRestaurantsResult();
  $resultCategories = $controllerRest->getCategoriesResult();
  $resultPhotos = $controllerRest->getPhotosResult();

  

  header ("content-type: text/json");
  // header("Content-Type: application/text; charset=ISO-8859-1");
  echo "{";

            // RESTAURANTS
            echo "\"restaurants\" : [";
            $no_of_rows = $resultRestaurants->rowCount();
            $ind = 0;
            $count = $resultRestaurants->columnCount();
            foreach ($resultRestaurants as $row) 
            {
                echo "{";
                $inner_ind = 0;
                foreach ($row as $columnName => $field) 
                {
                    if(!is_numeric($columnName)) {
                        $val = trim(strip_tags($field));
                        echo "\"$columnName\" : \"$val\"";

                        if($inner_ind < $count - 1)
                          echo ",";

                        ++$inner_ind;
                    }
                }
                echo "}";

                if($ind < $no_of_rows - 1)
                  echo ",";

                ++$ind;
            }
            echo "], ";

            // CATEGORIES
            echo "\"categories\" : [";
            $no_of_rows = $resultCategories->rowCount();
            $ind = 0;
            $count = $resultCategories->columnCount();
            foreach ($resultCategories as $row) 
            {
                echo "{";
                $inner_ind = 0;
                foreach ($row as $columnName => $field) 
                {
                    if(!is_numeric($columnName)) {
                        $val = trim(strip_tags($field));
                        echo "\"$columnName\" : \"$val\"";

                        if($inner_ind < $count - 1)
                          echo ",";

                        ++$inner_ind;
                    }
                }
                echo "}";

                if($ind < $no_of_rows - 1)
                  echo ",";

                ++$ind;
            }
            echo "], ";


            // PHOTOS
            echo "\"photos\" : [";
            $no_of_rows = $resultPhotos->rowCount();
            $ind = 0;
            $count = $resultPhotos->columnCount();
            foreach ($resultPhotos as $row) 
            {
                echo "{";
                $inner_ind = 0;
                foreach ($row as $columnName => $field) 
                {
                    if(!is_numeric($columnName)) {
                        $val = trim(strip_tags($field));
                        echo "\"$columnName\" : \"$val\"";

                        if($inner_ind < $count - 1)
                          echo ",";

                        ++$inner_ind;
                    }
                }
                echo "}";

                if($ind < $no_of_rows - 1)
                  echo ",";

                ++$ind;
            }
            echo "] ";


            
       
  echo "}";
?>