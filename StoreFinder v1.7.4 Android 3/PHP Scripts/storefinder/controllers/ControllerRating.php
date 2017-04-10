<?php
 
class ControllerRating
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
 
    public function insertRating($itm) 
    {
        $stmt = $this->pdo->prepare('INSERT INTO tbl_storefinder_ratings( 
                                        rating, 
                                        store_id, 
                                        user_id, 
                                        created_at, 
                                        updated_at ) 

                                    VALUES(
                                        :rating, 
                                        :store_id, 
                                        :user_id, 
                                        :created_at, 
                                        :updated_at)');

        $result = $stmt->execute(
                            array('rating' => $itm->rating,
                                    'store_id' => $itm->store_id,
                                    'user_id' => $itm->user_id,
                                    'created_at' => $itm->created_at,
                                    'updated_at' => $itm->updated_at) );
        
        return $result ? true : false;

    }

    public function checkUserCanRate($store_id, $user_id) 
    {
        $stmt = $this->pdo->prepare('SELECT *  
                                    FROM tbl_storefinder_ratings 
                                    
                                    WHERE store_id = :store_id AND user_id = :user_id');
        $stmt->execute(
                        array('store_id' => $store_id,
                            'user_id' => $user_id) );

        foreach ($stmt as $row) 
        {
            $itm = new Rating();
            $itm->rating_id = $row['rating_id'];
            $itm->rating = $row['rating'];
            $itm->store_id = $row['store_id'];
            $itm->user_id = $row['user_id'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];

            return $itm;
        }
        return null;
    }

    public function getRatingByStoreId($store_id) 
    {
        $stmt = $this->pdo->prepare('SELECT *  
                                    FROM tbl_storefinder_ratings 
                                    
                                    WHERE store_id = :store_id');
        $stmt->execute(
                        array('store_id' => $store_id) );

        $count = 0;
        $total = 0;
        foreach ($stmt as $row) 
        {
            $total += $row['rating'];
            ++$count;
        }

        if(intval($count) == 0 || intval($total) == 0)
            return 0;

        $rating = floatval($total) / floatval($count);
        $rating = number_format($rating, 1, '.', '');
        
        return $rating;
    }
 
}
 
?>