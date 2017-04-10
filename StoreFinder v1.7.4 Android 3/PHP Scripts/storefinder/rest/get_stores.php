<?php

    require '../header_rest.php';
    $controllerRest = new ControllerRest();

    $api_key = "";
    if(!empty($_GET['api_key']))
        $api_key = $_GET['api_key'];

    $lat = 0;
    if(!empty($_GET['lat']))
        $lat = str_replace(",", ".", $_GET['lat']);

    $lon = 0;
    if(!empty($_GET['lon']))
        $lon = str_replace(",", ".", $_GET['lon']);

    $radius = 0;
    if(!empty($_GET['radius']))
        $radius = $_GET['radius'];

    $category_id = 0;
    if(!empty($_GET['category_id']))
        $category_id = $_GET['category_id'];

    $featured = 0;
    if(!empty($_GET['featured']))
        $featured = $_GET['featured'];

    $default_store_count_to_find_distance = 10;
    if(!empty($_GET['default_store_count_to_find_distance']))
        $default_store_count_to_find_distance = $_GET['default_store_count_to_find_distance'];

    if( empty($api_key) ) {
        $arr = array();
        $arr['status'] = formatStatus('3', 'Invalid Access.');
        echo json_encode($arr);
        return;
    }
    
    if($lat == 0 || $lon == 0 || $radius == 0) {
        $arr = array();
        $arr['status'] = formatStatus('3', 'Invalid Access.');
        echo json_encode($arr);
        return;
    }

    if($category_id > 0) {
        $results = $controllerRest->getResultStoresNearbyByCategory($lat, $lon, $radius, $category_id);
    }
    else if($featured == 1) {
        $results = $controllerRest->getResultStoresNearbyFeatured($lat, $lon, $radius);
    }
    else {
        $results = $controllerRest->getResultStoresNearby($lat, $lon, $radius);
    }

    $ind = 0;
    $arrayObjs = array();
    foreach ($results as $row) {
        $arrayObj = array();
        foreach ($row as $columnName => $field) {
            if(!is_numeric($columnName)) {
                $val = preg_replace('~[\r\n]+~', '', $field);
                $val = htmlspecialchars(trim(strip_tags($val)));
                $arrayObj[$columnName] = $val;
            }
        }

        $arrayObj['photos'] = array();
        $store_id = $arrayObj['store_id'];
        if( !empty($store_id) ) {
            $resultPhotos = $controllerRest->getResultPhotosByStoreId($store_id);
            $arrayObj['photos'] = getArrayObjs($resultPhotos);
        }

        $arrayObjs[$ind] = $arrayObj;
        $ind += 1;
    }

    $max_distance = $controllerRest->getMaxDistanceFound($lat, $lon);
    $default_distance = $controllerRest->getMaxDistanceFoundDefaultStore($lat, $lon, $default_store_count_to_find_distance);

    $arrayJSON = array();
    $arrayJSON['stores'] = $arrayObjs;
    $arrayJSON['max_distance'] = $max_distance;
    $arrayJSON['default_distance'] = $default_distance;
    echo json_encode($arrayJSON);

    function formatStatus($status_code, $status_text) {
        $arr = array( 'status_code' => ''.$status_code.'', 'status_text' => ''.$status_text.'' );
        return $arr;
    }

    function getObj($results) {
        $arrayObj = array();
        foreach ($results as $row) {
            foreach ($row as $columnName => $field) {
                if(!is_numeric($columnName)) {
                    $arrayObj[$columnName] = $field;
                }
            }
        }
        return $arrayObj;
    }

    function getArrayObjs($results) {
        $ind = 0;
        $arrayObjs = array();
        foreach ($results as $row) {
            $arrayObj = array();
            foreach ($row as $columnName => $field) {
                if(!is_numeric($columnName)) {
                    $val = preg_replace('~[\r\n]+~', '', $field);
                    $val = htmlspecialchars(trim(strip_tags($val)));
                    $arrayObj[$columnName] = $val;
                }
            }
            $arrayObjs[$ind] = $arrayObj;
            $ind += 1;
        }
        return $arrayObjs;
    }

?>