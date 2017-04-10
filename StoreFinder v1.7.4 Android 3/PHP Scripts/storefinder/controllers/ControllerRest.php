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
 
    public function getResultPhotos() 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_storefinder_photos WHERE is_deleted = 0');

        $stmt->execute();
        return $stmt;
    }

    public function getResultStores() 
    {
        $stmt = $this->pdo->prepare('SELECT tbl_storefinder_stores.store_id,
                                            tbl_storefinder_stores.store_name,
                                            tbl_storefinder_stores.store_address,
                                            tbl_storefinder_stores.store_desc,
                                            tbl_storefinder_stores.lat,
                                            tbl_storefinder_stores.lon,
                                            tbl_storefinder_stores.sms_no,
                                            tbl_storefinder_stores.phone_no,
                                            tbl_storefinder_stores.email,
                                            tbl_storefinder_stores.website,
                                            tbl_storefinder_stores.category_id,
                                            tbl_storefinder_stores.created_at,
                                            tbl_storefinder_stores.updated_at,
                                            tbl_storefinder_stores.featured,
                                            tbl_storefinder_stores.is_deleted,
                                            COALESCE(SUM(tbl_storefinder_ratings.rating), 0) as rating_total, 
                                            COALESCE(COUNT(tbl_storefinder_ratings.rating), 0) as rating_count
                                            
                                    FROM tbl_storefinder_stores 
                                    LEFT OUTER JOIN tbl_storefinder_ratings 
                                    ON tbl_storefinder_stores.store_id = tbl_storefinder_ratings.store_id 
                                    WHERE is_deleted = 0 GROUP BY tbl_storefinder_stores.store_id');
        $stmt->execute();
        return $stmt;
    }

    public function getResultCategories() 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_storefinder_categories WHERE is_deleted = 0 ORDER BY category ASC');
        $stmt->execute();
        return $stmt;
    }
    public function getResultNews() 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_storefinder_news WHERE is_deleted = 0 ORDER BY created_at DESC');
        $stmt->execute();
        return $stmt;
    }

    public function getResultReviews($tmpFrom, $tmpTo, $store_id) 
    {
        $stmt = $this->pdo->prepare('SELECT review_id, review, tbl_storefinder_reviews.created_at, thumb_url, photo_url, username, full_name 
                                        FROM tbl_storefinder_reviews 
                                        INNER JOIN tbl_storefinder_users ON 
                                                        tbl_storefinder_reviews.user_id = tbl_storefinder_users.user_id 

                                        WHERE is_deleted = 0 AND created_at >= :tmpFrom AND created_at <= :tmpTo AND store_id = :store_id');
        
        $stmt->execute(
                        array('tmpFrom' => $tmpFrom,
                                    'tmpTo' => $tmpTo,
                                    'store_id' => $store_id) );
        return $stmt;
    }

    public function getResultStoresRating($store_id) 
    {
        $stmt = $this->pdo->prepare('SELECT *, SUM(rating) as rating_total, COUNT(rating) as rating_count 
                                    FROM tbl_storefinder_stores 
                                    LEFT OUTER JOIN tbl_storefinder_ratings 
                                    ON tbl_storefinder_stores.store_id = tbl_storefinder_ratings.store_id 
                                    WHERE is_deleted = 0 AND tbl_storefinder_stores.store_id = :store_id GROUP BY tbl_storefinder_stores.store_id');
        $stmt->execute(
                        array('store_id' => $store_id) );

        return $stmt;
    }


    public function getResultReviewsCount($count, $store_id) 
    {
        $stmt = $this->pdo->prepare('(SELECT review_id, review, tbl_storefinder_reviews.created_at, thumb_url, photo_url, full_name 
                                        FROM tbl_storefinder_reviews 
                                        INNER JOIN tbl_storefinder_users ON 
                                                        tbl_storefinder_reviews.user_id = tbl_storefinder_users.user_id 

                                        WHERE is_deleted = 0 AND store_id = :store_id ORDER BY review_id DESC LIMIT :count) ORDER BY review_id ASC');
        
        $stmt->execute(
                        array( 'count' => $count,
                                    'store_id' => $store_id) );
        return $stmt;
    }

    public function getResultReviewsTotalCount($store_id) 
    {
        $stmt = $this->pdo->prepare('SELECT review_id, review, tbl_storefinder_reviews.created_at, thumb_url, photo_url, full_name 
                                        FROM tbl_storefinder_reviews 
                                        INNER JOIN tbl_storefinder_users ON 
                                                        tbl_storefinder_reviews.user_id = tbl_storefinder_users.user_id 

                                        WHERE is_deleted = 0 AND store_id = :store_id');
        
        $stmt->execute(
                        array( 'store_id' => $store_id) );

        $count = $stmt->rowCount();
        return $count;
    }


    public function getResultStoresNearbyByCategory($lat, $lon, $radius, $category_id) 
    {
        $stmt = $this->pdo->prepare('SELECT tbl_storefinder_stores.store_id,
                                            tbl_storefinder_stores.store_name,
                                            tbl_storefinder_stores.store_address,
                                            tbl_storefinder_stores.store_desc,
                                            tbl_storefinder_stores.lat,
                                            tbl_storefinder_stores.lon,
                                            tbl_storefinder_stores.sms_no,
                                            tbl_storefinder_stores.phone_no,
                                            tbl_storefinder_stores.email,
                                            tbl_storefinder_stores.website,
                                            tbl_storefinder_stores.category_id,
                                            tbl_storefinder_stores.created_at,
                                            tbl_storefinder_stores.updated_at,
                                            tbl_storefinder_stores.featured,
                                            tbl_storefinder_stores.is_deleted,
                                            COALESCE(SUM(tbl_storefinder_ratings.rating), 0) as rating_total, 
                                            COALESCE(COUNT(tbl_storefinder_ratings.rating), 0) as rating_count,
                                            COALESCE(( 6371 * acos( cos( radians(:lat_params) ) *  cos( radians( tbl_storefinder_stores.lat ) ) * 
                                            cos( radians( tbl_storefinder_stores.lon ) - radians(:lon_params) ) + sin( radians(:lat_params1) ) * 
                                            sin( radians( tbl_storefinder_stores.lat ) ) ) ), 0) AS distance 
                                            
                                    FROM tbl_storefinder_stores 
                                    LEFT OUTER JOIN tbl_storefinder_ratings 
                                    ON tbl_storefinder_stores.store_id = tbl_storefinder_ratings.store_id 
                                    WHERE is_deleted = 0 AND category_id = :category_id 
                                    GROUP BY tbl_storefinder_stores.store_id
                                    HAVING distance <= :radius
                                    ORDER BY distance ASC');

        $stmt->execute( array('lat_params' => $lat, 'lon_params' => $lon, 'lat_params1' => $lat, 'radius' => $radius, 'category_id' => $category_id ));
        return $stmt;
    }

    public function getResultStoresNearby($lat, $lon, $radius) 
    {
        $stmt = $this->pdo->prepare('SELECT tbl_storefinder_stores.store_id,
                                            tbl_storefinder_stores.store_name,
                                            tbl_storefinder_stores.store_address,
                                            tbl_storefinder_stores.store_desc,
                                            tbl_storefinder_stores.lat,
                                            tbl_storefinder_stores.lon,
                                            tbl_storefinder_stores.sms_no,
                                            tbl_storefinder_stores.phone_no,
                                            tbl_storefinder_stores.email,
                                            tbl_storefinder_stores.website,
                                            tbl_storefinder_stores.category_id,
                                            tbl_storefinder_stores.created_at,
                                            tbl_storefinder_stores.updated_at,
                                            tbl_storefinder_stores.featured,
                                            tbl_storefinder_stores.is_deleted,
                                            COALESCE(SUM(tbl_storefinder_ratings.rating), 0) as rating_total, 
                                            COALESCE(COUNT(tbl_storefinder_ratings.rating), 0) as rating_count,
                                            COALESCE(( 6371 * acos( cos( radians(:lat_params) ) *  cos( radians( tbl_storefinder_stores.lat ) ) * 
                                            cos( radians( tbl_storefinder_stores.lon ) - radians(:lon_params) ) + sin( radians(:lat_params1) ) * 
                                            sin( radians( tbl_storefinder_stores.lat ) ) ) ), 0) AS distance 
                                            
                                    FROM tbl_storefinder_stores 
                                    LEFT OUTER JOIN tbl_storefinder_ratings 
                                    ON tbl_storefinder_stores.store_id = tbl_storefinder_ratings.store_id 
                                    WHERE is_deleted = 0 
                                    GROUP BY tbl_storefinder_stores.store_id
                                    HAVING distance <= :radius
                                    ORDER BY distance ASC');

        $stmt->execute( array('lat_params' => $lat, 'lon_params' => $lon, 'lat_params1' => $lat, 'radius' => $radius ));
        return $stmt;
    }

    public function getResultStoresNearbyFeatured($lat, $lon, $radius) 
    {
        $stmt = $this->pdo->prepare('SELECT tbl_storefinder_stores.store_id,
                                            tbl_storefinder_stores.store_name,
                                            tbl_storefinder_stores.store_address,
                                            tbl_storefinder_stores.store_desc,
                                            tbl_storefinder_stores.lat,
                                            tbl_storefinder_stores.lon,
                                            tbl_storefinder_stores.sms_no,
                                            tbl_storefinder_stores.phone_no,
                                            tbl_storefinder_stores.email,
                                            tbl_storefinder_stores.website,
                                            tbl_storefinder_stores.category_id,
                                            tbl_storefinder_stores.created_at,
                                            tbl_storefinder_stores.updated_at,
                                            tbl_storefinder_stores.featured,
                                            tbl_storefinder_stores.is_deleted,
                                            COALESCE(SUM(tbl_storefinder_ratings.rating), 0) as rating_total, 
                                            COALESCE(COUNT(tbl_storefinder_ratings.rating), 0) as rating_count,
                                            COALESCE(( 6371 * acos( cos( radians(:lat_params) ) *  cos( radians( tbl_storefinder_stores.lat ) ) * 
                                            cos( radians( tbl_storefinder_stores.lon ) - radians(:lon_params) ) + sin( radians(:lat_params1) ) * 
                                            sin( radians( tbl_storefinder_stores.lat ) ) ) ), 0) AS distance 
                                            
                                    FROM tbl_storefinder_stores 
                                    LEFT OUTER JOIN tbl_storefinder_ratings 
                                    ON tbl_storefinder_stores.store_id = tbl_storefinder_ratings.store_id 
                                    WHERE is_deleted = 0 AND featured = 1 
                                    GROUP BY tbl_storefinder_stores.store_id
                                    HAVING distance <= :radius
                                    ORDER BY distance ASC');

        $stmt->execute( array('lat_params' => $lat, 'lon_params' => $lon, 'lat_params1' => $lat, 'radius' => $radius ));
        return $stmt;
    }

    public function getResultFeaturedStoresNearbyIgnoreRadiusAtMax($lat, $lon, $store_count) 
    {
        $stmt = $this->pdo->prepare('SELECT tbl_storefinder_stores.store_id,
                                            tbl_storefinder_stores.store_name,
                                            tbl_storefinder_stores.store_address,
                                            tbl_storefinder_stores.store_desc,
                                            tbl_storefinder_stores.lat,
                                            tbl_storefinder_stores.lon,
                                            tbl_storefinder_stores.sms_no,
                                            tbl_storefinder_stores.phone_no,
                                            tbl_storefinder_stores.email,
                                            tbl_storefinder_stores.website,
                                            tbl_storefinder_stores.category_id,
                                            tbl_storefinder_stores.created_at,
                                            tbl_storefinder_stores.updated_at,
                                            tbl_storefinder_stores.featured,
                                            tbl_storefinder_stores.is_deleted,
                                            COALESCE(SUM(tbl_storefinder_ratings.rating), 0) as rating_total, 
                                            COALESCE(COUNT(tbl_storefinder_ratings.rating), 0) as rating_count,
                                            COALESCE(( 6371 * acos( cos( radians(:lat_params) ) *  cos( radians( tbl_storefinder_stores.lat ) ) * 
                                            cos( radians( tbl_storefinder_stores.lon ) - radians(:lon_params) ) + sin( radians(:lat_params1) ) * 
                                            sin( radians( tbl_storefinder_stores.lat ) ) ) ), 0) AS distance 
                                            
                                    FROM tbl_storefinder_stores 
                                    LEFT OUTER JOIN tbl_storefinder_ratings 
                                    ON tbl_storefinder_stores.store_id = tbl_storefinder_ratings.store_id 
                                    WHERE is_deleted = 0 AND featured = 1 
                                    GROUP BY tbl_storefinder_stores.store_id
                                    ORDER BY distance ASC
                                    LIMIT 0, :store_count');

        $stmt->execute( array('lat_params' => $lat, 'lon_params' => $lon, 'lat_params1' => $lat, 'store_count' => $store_count ));
        return $stmt;
    }

    public function getResultFeaturedStoresNearbyRadiusAtMax($lat, $lon, $radius) 
    {
        $stmt = $this->pdo->prepare('SELECT tbl_storefinder_stores.store_id,
                                            tbl_storefinder_stores.store_name,
                                            tbl_storefinder_stores.store_address,
                                            tbl_storefinder_stores.store_desc,
                                            tbl_storefinder_stores.lat,
                                            tbl_storefinder_stores.lon,
                                            tbl_storefinder_stores.sms_no,
                                            tbl_storefinder_stores.phone_no,
                                            tbl_storefinder_stores.email,
                                            tbl_storefinder_stores.website,
                                            tbl_storefinder_stores.category_id,
                                            tbl_storefinder_stores.created_at,
                                            tbl_storefinder_stores.updated_at,
                                            tbl_storefinder_stores.featured,
                                            tbl_storefinder_stores.is_deleted,
                                            COALESCE(SUM(tbl_storefinder_ratings.rating), 0) as rating_total, 
                                            COALESCE(COUNT(tbl_storefinder_ratings.rating), 0) as rating_count,
                                            COALESCE(( 6371 * acos( cos( radians(:lat_params) ) *  cos( radians( tbl_storefinder_stores.lat ) ) * 
                                            cos( radians( tbl_storefinder_stores.lon ) - radians(:lon_params) ) + sin( radians(:lat_params1) ) * 
                                            sin( radians( tbl_storefinder_stores.lat ) ) ) ), 0) AS distance 
                                            
                                    FROM tbl_storefinder_stores 
                                    LEFT OUTER JOIN tbl_storefinder_ratings 
                                    ON tbl_storefinder_stores.store_id = tbl_storefinder_ratings.store_id 
                                    WHERE is_deleted = 0 AND featured = 1 
                                    GROUP BY tbl_storefinder_stores.store_id 
                                    HAVING distance <= :radius
                                    ORDER BY distance ASC');

        $stmt->execute( array('lat_params' => $lat, 'lon_params' => $lon, 'lat_params1' => $lat, 'radius' => $radius ));
        return $stmt;
    }

    public function getResultPhotosByStoreId($store_id) 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_storefinder_photos WHERE is_deleted = 0 AND store_id = :store_id ORDER BY created_at ASC');

        $stmt->execute( array('store_id' => $store_id) );
        return $stmt;
    }

    public function getResultNewsAtMax($max_count) 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_storefinder_news WHERE is_deleted = 0 ORDER BY created_at DESC LIMIT 0, :max_count');
        $stmt->execute( array('max_count' => $max_count) );
        return $stmt;
    }

    public function getMaxDistanceFound($lat, $lon) 
    {
        $stmt = $this->pdo->prepare('SELECT COALESCE(( 6371 * acos( cos( radians(:lat_params) ) *  cos( radians( tbl_storefinder_stores.lat ) ) * 
                                            cos( radians( tbl_storefinder_stores.lon ) - radians(:lon_params) ) + sin( radians(:lat_params1) ) * 
                                            sin( radians( tbl_storefinder_stores.lat ) ) ) ), 0) AS distance 
                                            
                                    FROM tbl_storefinder_stores 
                                    ORDER BY distance DESC
                                    LIMIT 0, 1');

        $stmt->execute( array('lat_params' => $lat, 'lon_params' => $lon, 'lat_params1' => $lat) );
        foreach ($stmt as $row) {
            return $row['distance'];
        }
        return 0;
    }

    public function getMaxDistanceFoundDefaultStore($lat, $lon, $default_store_count_to_find_distance) 
    {
        $stmt = $this->pdo->prepare('SELECT COALESCE(( 6371 * acos( cos( radians(:lat_params) ) *  cos( radians( tbl_storefinder_stores.lat ) ) * 
                                            cos( radians( tbl_storefinder_stores.lon ) - radians(:lon_params) ) + sin( radians(:lat_params1) ) * 
                                            sin( radians( tbl_storefinder_stores.lat ) ) ) ), 0) AS distance 
                                            
                                    FROM tbl_storefinder_stores 
                                    ORDER BY distance DESC
                                    LIMIT 0, :default_store_count_to_find_distance');

        $stmt->execute( array('lat_params' => $lat, 'lon_params' => $lon, 'lat_params1' => $lat, 'default_store_count_to_find_distance' => $default_store_count_to_find_distance) );
        foreach ($stmt as $row) {
            return $row['distance'];
        }
        return 0;
    }
}
 
?>