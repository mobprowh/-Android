<?php
 
class ControllerRestaurant
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
 
    public function updateRestaurant($itm) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_restaurants_restaurants 

                                        SET name = :name, 
                                            address = :address, 
                                            lat = :lat, 
                                            lon = :lon, 
                                            desc1 = :desc1, 
                                            email = :email, 
                                            website = :website, 
                                            amenities = :amenities, 
                                            food_rating = :food_rating, 
                                            price_rating = :price_rating, 
                                            featured = :featured, 
                                            phone = :phone, 
                                            hours = :hours, 
                                            created_at = :created_at, 
                                            category_id = :category_id 
                                        WHERE restaurant_id = :restaurant_id');

        $result = $stmt->execute(
                            array('name' => $itm->name,
                                    'address' => $itm->address,  
                                    'lat' => $itm->lat,
                                    'lon' => $itm->lon,
                                    'desc1' => $itm->desc1,
                                    'email' => $itm->email,
                                    'website' => $itm->website,
                                    'amenities' => $itm->amenities,
                                    'food_rating' => $itm->food_rating,
                                    'price_rating' => $itm->price_rating,
                                    'featured' => $itm->featured,
                                    'phone' => $itm->phone,
                                    'hours' => $itm->hours,
                                    'created_at' => $itm->created_at,
                                    'category_id' => $itm->category_id,
                                    'restaurant_id' => $itm->restaurant_id ) );
        
        return $result ? true : false;

    }


    public function deleteRestaurant($restaurant_id, $is_deleted) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_restaurants_restaurants 
                                        SET is_deleted = :is_deleted 
                                        WHERE restaurant_id = :restaurant_id ');
        
        $result = $stmt->execute(
                            array('restaurant_id' => $restaurant_id, 
                                    'is_deleted' => $is_deleted) );

        return $result ? true : false;
    }

    public function updateRestaurantFeatured($itm) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_restaurants_restaurants 
                                        SET featured = :featured 
                                        WHERE restaurant_id = :restaurant_id ');
        
        $result = $stmt->execute(
                            array('restaurant_id' => $itm->restaurant_id, 
                                    'featured' => $itm->featured) );
        
        return $result ? true : false;
    }


    public function insertRestaurant($itm) 
    {
        $stmt = $this->pdo->prepare('INSERT INTO tbl_restaurants_restaurants( 
                                    name, 
                                    address, 
                                    lat, 
                                    lon, 
                                    desc1, 
                                    email, 
                                    website, 
                                    amenities, 
                                    food_rating, 
                                    price_rating, 
                                    featured, 
                                    phone, 
                                    hours, 
                                    created_at, 
                                    category_id ) 

                                VALUES(
                                    :name, 
                                    :address, 
                                    :lat, 
                                    :lon, 
                                    :desc1, 
                                    :email, 
                                    :website, 
                                    :amenities, 
                                    :food_rating, 
                                    :price_rating, 
                                    :featured, 
                                    :phone, 
                                    :hours, 
                                    :created_at, 
                                    :category_id )');
        
        $result = $stmt->execute(
                            array('name' => $itm->name,
                                    'address' => $itm->address,  
                                    'lat' => $itm->lat,
                                    'lon' => $itm->lon,
                                    'desc1' => $itm->desc1,
                                    'email' => $itm->email,
                                    'website' => $itm->website,
                                    'amenities' => $itm->amenities,
                                    'food_rating' => $itm->food_rating,
                                    'price_rating' => $itm->price_rating,
                                    'featured' => $itm->featured,
                                    'phone' => $itm->phone,
                                    'hours' => $itm->hours,
                                    'created_at' => $itm->created_at,
                                    'category_id' => $itm->category_id ) );
        
        return $result ? true : false;
    }
 

    public function getRestaurants() 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_restaurants_restaurants 
                                        WHERE is_deleted = 0 ORDER BY name ASC');
        
        $stmt->execute();

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new Restaurant();
            $itm->restaurant_id = $row['restaurant_id'];
            $itm->name = $row['name'];
            $itm->address = $row['address'];
            $itm->lat = $row['lat'];
            $itm->lon = $row['lon'];
            $itm->desc1 = $row['desc1'];
            $itm->email = $row['email'];
            $itm->website = $row['website'];
            $itm->amenities = $row['amenities'];
            $itm->food_rating = $row['food_rating'];
            $itm->price_rating = $row['price_rating'];
            $itm->featured = $row['featured'];
            $itm->phone = $row['phone'];
            $itm->hours = $row['hours'];
            $itm->created_at = $row['created_at'];
            $itm->category_id = $row['category_id'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }

    public function getRestaurantsBySearching($search) 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                FROM tbl_restaurants_restaurants 
                                WHERE is_deleted = 0 AND name LIKE :search ORDER BY name ASC');
        
        $stmt->execute( array('search' => '%'.$search.'%'));

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new Restaurant();
            $itm->restaurant_id = $row['restaurant_id'];
            $itm->name = $row['name'];
            $itm->address = $row['address'];
            $itm->lat = $row['lat'];
            $itm->lon = $row['lon'];
            $itm->desc1 = $row['desc1'];
            $itm->email = $row['email'];
            $itm->website = $row['website'];
            $itm->amenities = $row['amenities'];
            $itm->food_rating = $row['food_rating'];
            $itm->price_rating = $row['price_rating'];
            $itm->featured = $row['featured'];
            $itm->phone = $row['phone'];
            $itm->hours = $row['hours'];
            $itm->created_at = $row['created_at'];
            $itm->category_id = $row['category_id'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }


    public function getRestaurantByRestaurantId($restaurant_id) 
    {
        
        $stmt = $this->pdo->prepare('SELECT * 
                                FROM tbl_restaurants_restaurants 
                                WHERE restaurant_id = :restaurant_id');
        
        $stmt->execute( array('restaurant_id' => $restaurant_id));

        foreach ($stmt as $row) 
        {
            $itm = new Restaurant();
            $itm->restaurant_id = $row['restaurant_id'];
            $itm->name = $row['name'];
            $itm->address = $row['address'];
            $itm->lat = $row['lat'];
            $itm->lon = $row['lon'];
            $itm->desc1 = $row['desc1'];
            $itm->email = $row['email'];
            $itm->website = $row['website'];
            $itm->amenities = $row['amenities'];
            $itm->food_rating = $row['food_rating'];
            $itm->price_rating = $row['price_rating'];
            $itm->featured = $row['featured'];
            $itm->phone = $row['phone'];
            $itm->hours = $row['hours'];
            $itm->created_at = $row['created_at'];
            $itm->category_id = $row['category_id'];
            $itm->is_deleted = $row['is_deleted'];

            return $itm;
        } 
        
        return null;
    }


    public function getRestaurantsFeatured() 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                FROM tbl_restaurants_restaurants 
                                WHERE featured = 1 AND is_deleted = 0');
        
        $stmt->execute();

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new Restaurant();
            $itm->restaurant_id = $row['restaurant_id'];
            $itm->name = $row['name'];
            $itm->address = $row['address'];
            $itm->lat = $row['lat'];
            $itm->lon = $row['lon'];
            $itm->desc1 = $row['desc1'];
            $itm->email = $row['email'];
            $itm->website = $row['website'];
            $itm->amenities = $row['amenities'];
            $itm->food_rating = $row['food_rating'];
            $itm->price_rating = $row['price_rating'];
            $itm->featured = $row['featured'];
            $itm->phone = $row['phone'];
            $itm->hours = $row['hours'];
            $itm->created_at = $row['created_at'];
            $itm->category_id = $row['category_id'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        } 
        return $array;
    }

}
 
?>