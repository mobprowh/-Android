<?php

 
class ControllerRest
{
 
    private $db;
    private $pdo;
    function __construct() 
    {
        // connecting to database
        $this->db = new DB_Connect();
        $this->pdo = $this->db->connect();
    }
 
    function __destruct() { }
 
    public function getRestaurantsResult() 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_restaurants_restaurants WHERE is_deleted = 0');

        $stmt->execute();
        return $stmt;
    }

    public function getCategoriesResult() 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_restaurants_categories WHERE is_deleted = 0');
        $stmt->execute();
        return $stmt;
    }

    public function getPhotosResult() 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_restaurants_photos 
                                        INNER JOIN tbl_restaurants_restaurants 
                                        ON tbl_restaurants_photos.restaurant_id = tbl_restaurants_restaurants.restaurant_id 
                                        WHERE tbl_restaurants_restaurants.is_deleted = 0 AND tbl_restaurants_photos.is_deleted = 0');
        $stmt->execute();
        return $stmt;
    }


}
 
?>