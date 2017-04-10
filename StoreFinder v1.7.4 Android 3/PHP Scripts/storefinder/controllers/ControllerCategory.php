<?php
 
class ControllerCategory
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
 
    public function updateCategory($itm) 
    {
        
        $stmt = $this->pdo->prepare('UPDATE tbl_storefinder_categories 
                                        SET category = :category,
                                            category_icon =  :category_icon,
                                            updated_at = :updated_at

                                        WHERE category_id = :category_id');

        $result = $stmt->execute(
                            array('category' => $itm->category, 
                                    'category_icon' => $itm->category_icon, 
                                    'updated_at' => $itm->updated_at, 
                                    'category_id' => $itm->category_id) );
        
        return $result ? true : false;
    }

    public function deleteCategory($category_id, $is_deleted) 
    {

        $stmt = $this->pdo->prepare('UPDATE tbl_storefinder_categories 
                                        SET is_deleted = :is_deleted
                                        WHERE category_id = :category_id');


        $result = $stmt->execute(
                            array('is_deleted' => $is_deleted, 
                                    'category_id' => $category_id) );
        
        return $result ? true : false;
    }

    public function insertCategory($itm) 
    {
        $stmt = $this->pdo->prepare('INSERT INTO tbl_storefinder_categories( 
                                            category,
                                            category_icon,
                                            created_at,
                                            updated_at ) 
                                        VALUES( 
                                            :category,
                                            :category_icon,
                                            :created_at,
                                            :updated_at )');
        
        $result = $stmt->execute(
                            array('category' => $itm->category,
                                    'category_icon' => $itm->category_icon,
                                    'created_at' => $itm->created_at,
                                    'updated_at' => $itm->updated_at ) );
        
        return $result ? true : false;
    }
 
    
    public function getCategories() 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                FROM tbl_storefinder_categories 
                                 WHERE is_deleted = 0 ORDER BY category ASC');

        $stmt->execute();

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            // do something with $row
            $itm = new Category();
            $itm->category_id = $row['category_id'];
            $itm->category = $row['category'];
            $itm->category_icon = $row['category_icon'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        }
        return $array;
    }

    public function getCategoriesBySearching($search) 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_storefinder_categories 
                                        WHERE is_deleted = 0 AND category LIKE :search ORDER BY category ASC');

        $stmt->execute( array('search' => '%'.$search.'%'));

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            // do something with $row
            $itm = new Category();
            $itm->category_id = $row['category_id'];
            $itm->category = $row['category'];
            $itm->category_icon = $row['category_icon'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        }
        return $array;
    }


    public function getCategoryByCategoryId($category_id) 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_storefinder_categories WHERE category_id = :category_id');

        $stmt->execute( array('category_id' => $category_id));

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            // do something with $row
            $itm = new Category();
            $itm->category_id = $row['category_id'];
            $itm->category = $row['category'];
            $itm->category_icon = $row['category_icon'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->is_deleted = $row['is_deleted'];

            return $itm;
        }
        return null;
    }


    public function getCategoriesByCategory($category) 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_storefinder_categories WHERE category = :category');

        $stmt->execute( array('category' => $category));

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            // do something with $row
            $itm = new Category();
            $itm->category_id = $row['category_id'];
            $itm->category = $row['category'];
            $itm->category_icon = $row['category_icon'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->is_deleted = $row['is_deleted'];

            return $itm;
        }
        return null;
    }

    public function getCategoriesAtRange($begin, $end) 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_storefinder_categories 
                                        WHERE is_deleted = 0 ORDER BY category_id ASC LIMIT :beg, :end');
        
        $stmt->execute( array('beg' => $begin, 'end' => $end) );

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new Category();
            $itm->category_id = $row['category_id'];
            $itm->category = $row['category'];
            $itm->category_icon = $row['category_icon'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }

}
 
?>