<?php

class ControllerStore
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
 
    public function updateStore($itm) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_storefinder_stores

                                        SET store_name = :store_name, 
                                            store_address = :store_address, 
                                            store_desc = :store_desc, 
                                            lat = :lat, 
                                            lon = :lon, 
                                            sms_no = :sms_no, 
                                            phone_no = :phone_no, 
                                            email = :email, 
                                            website = :website, 
                                            category_id = :category_id, 
                                            created_at = :created_at, 
                                            updated_at = :updated_at, 
                                            featured = :featured, 
                                            is_deleted = :is_deleted 

                                        WHERE store_id = :store_id');

        $result = $stmt->execute(
                            array('store_name' => $itm->store_name,
                                    'store_address' => $itm->store_address,  
                                    'store_desc' => $itm->store_desc,
                                    'lat' => $itm->lat,
                                    'lon' => $itm->lon,
                                    'sms_no' => $itm->sms_no,
                                    'phone_no' => $itm->phone_no,
                                    'email' => $itm->email,
                                    'website' => $itm->website,
                                    'category_id' => $itm->category_id,
                                    'created_at' => $itm->created_at,
                                    'updated_at' => $itm->updated_at,
                                    'featured' => $itm->featured,
                                    'is_deleted' => $itm->is_deleted,
                                    'store_id' => $itm->store_id ) );
        
        return $result ? true : false;

    }


    public function deleteStore($store_id, $is_deleted) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_storefinder_stores 
                                        SET is_deleted = :is_deleted 
                                        WHERE store_id = :store_id ');
        
        $result = $stmt->execute(
                            array('store_id' => $store_id, 
                                    'is_deleted' => $is_deleted) );

        return $result ? true : false;
    }

    public function updateStoreFeatured($itm) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_storefinder_stores 
                                        SET featured = :featured 
                                        WHERE store_id = :store_id ');
        
        $result = $stmt->execute(
                            array('store_id' => $itm->store_id, 
                                    'featured' => $itm->featured) );
        
        return $result ? true : false;
    }


    public function insertStore($itm) 
    {

        $stmt = $this->pdo->prepare('INSERT INTO tbl_storefinder_stores( 
                                        store_name, 
                                        store_address, 
                                        store_desc, 
                                        lat, 
                                        lon, 
                                        sms_no, 
                                        phone_no, 
                                        email, 
                                        website, 
                                        category_id, 
                                        created_at, 
                                        updated_at, 
                                        featured ) 

                                    VALUES(
                                        :store_name, 
                                        :store_address, 
                                        :store_desc, 
                                        :lat, 
                                        :lon, 
                                        :sms_no, 
                                        :phone_no, 
                                        :email, 
                                        :website, 
                                        :category_id, 
                                        :created_at, 
                                        :updated_at, 
                                        :featured )');
        
        $result = $stmt->execute(
                            array('store_name' => $itm->store_name,
                                    'store_address' => $itm->store_address,  
                                    'store_desc' => $itm->store_desc,
                                    'lat' => $itm->lat,
                                    'lon' => $itm->lon,
                                    'sms_no' => $itm->sms_no,
                                    'phone_no' => $itm->phone_no,
                                    'email' => $itm->email,
                                    'website' => $itm->website,
                                    'category_id' => $itm->category_id,
                                    'created_at' => $itm->created_at,
                                    'updated_at' => $itm->updated_at,
                                    'featured' => $itm->featured ) );
        
        return $result ? true : false;
    }
 

    public function getStores() 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_storefinder_stores 
                                        WHERE is_deleted = 0 ORDER BY store_name ASC');
        
        $stmt->execute();

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new Store();
            $itm->store_id = $row['store_id'];
            $itm->store_name = $row['store_name'];
            $itm->store_address = $row['store_address'];
            $itm->store_desc = $row['store_desc'];
            $itm->lat = $row['lat'];
            $itm->lon = $row['lon'];
            $itm->sms_no = $row['sms_no'];
            $itm->phone_no = $row['phone_no'];
            $itm->email = $row['email'];
            $itm->website = $row['website'];
            $itm->category_id = $row['category_id'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->featured = $row['featured'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }

    public function getStoresBySearching($search) 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                FROM tbl_storefinder_stores 
                                WHERE is_deleted = 0 AND store_name LIKE :search ORDER BY store_name ASC');
        
        $stmt->execute( array('search' => '%'.$search.'%'));

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new Store();
            $itm->store_id = $row['store_id'];
            $itm->store_name = $row['store_name'];
            $itm->store_address = $row['store_address'];
            $itm->store_desc = $row['store_desc'];
            $itm->lat = $row['lat'];
            $itm->lon = $row['lon'];
            $itm->sms_no = $row['sms_no'];
            $itm->phone_no = $row['phone_no'];
            $itm->email = $row['email'];
            $itm->website = $row['website'];
            $itm->category_id = $row['category_id'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->featured = $row['featured'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }


    public function getStoreByStoreId($store_id) 
    {
        
        $stmt = $this->pdo->prepare('SELECT * 
                                FROM tbl_storefinder_stores 
                                WHERE store_id = :store_id');
        
        $stmt->execute( array('store_id' => $store_id));

        foreach ($stmt as $row) 
        {
            $itm = new Store();
            $itm->store_id = $row['store_id'];
            $itm->store_name = $row['store_name'];
            $itm->store_address = $row['store_address'];
            $itm->store_desc = $row['store_desc'];
            $itm->lat = $row['lat'];
            $itm->lon = $row['lon'];
            $itm->sms_no = $row['sms_no'];
            $itm->phone_no = $row['phone_no'];
            $itm->email = $row['email'];
            $itm->website = $row['website'];
            $itm->category_id = $row['category_id'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->featured = $row['featured'];
            $itm->is_deleted = $row['is_deleted'];

            return $itm;
        } 
        
        return null;
    }


    public function getStoreFeatured() 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                FROM tbl_storefinder_stores 
                                WHERE featured = 1 AND is_deleted = 0 ORDER BY store_name ASC');
        
        $stmt->execute();

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new Store();
            $itm->store_id = $row['store_id'];
            $itm->store_name = $row['store_name'];
            $itm->store_address = $row['store_address'];
            $itm->store_desc = $row['store_desc'];
            $itm->lat = $row['lat'];
            $itm->lon = $row['lon'];
            $itm->sms_no = $row['sms_no'];
            $itm->phone_no = $row['phone_no'];
            $itm->email = $row['email'];
            $itm->website = $row['website'];
            $itm->category_id = $row['category_id'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->featured = $row['featured'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        } 
        return $array;
    }

    public function getLastInsertedId() {

        return $this->pdo->lastInsertId(); 
    }

    public function getStoresAtRange($begin, $end) 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_storefinder_stores 
                                        WHERE is_deleted = 0 ORDER BY store_id ASC LIMIT :beg, :end');
        
        $stmt->execute( array('beg' => $begin, 'end' => $end) );

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new Store();
            $itm->store_id = $row['store_id'];
            $itm->store_name = $row['store_name'];
            $itm->store_address = $row['store_address'];
            $itm->store_desc = $row['store_desc'];
            $itm->lat = $row['lat'];
            $itm->lon = $row['lon'];
            $itm->sms_no = $row['sms_no'];
            $itm->phone_no = $row['phone_no'];
            $itm->email = $row['email'];
            $itm->website = $row['website'];
            $itm->category_id = $row['category_id'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->featured = $row['featured'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }

}
 
?>