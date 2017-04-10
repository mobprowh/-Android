<?php

class ControllerNews
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
 
    public function updateNews($itm) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_storefinder_news

                                        SET news_content = :news_content, 
                                            news_title = :news_title, 
                                            news_url = :news_url, 
                                            photo_url = :photo_url, 
                                            created_at = :created_at, 
                                            updated_at = :updated_at, 
                                            is_deleted = :is_deleted 

                                        WHERE news_id = :news_id');

        $result = $stmt->execute(
                            array('news_content' => $itm->news_content,
                                    'news_title' => $itm->news_title,  
                                    'news_url' => $itm->news_url,
                                    'photo_url' => $itm->photo_url,
                                    'created_at' => $itm->created_at,
                                    'updated_at' => $itm->updated_at,
                                    'is_deleted' => $itm->is_deleted,
                                    'news_id' => $itm->news_id ) );
        
        return $result ? true : false;

    }


    public function deleteNews($news_id, $is_deleted) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_storefinder_news 
                                        SET is_deleted = :is_deleted 
                                        WHERE news_id = :news_id ');
        
        $result = $stmt->execute(
                            array('news_id' => $news_id, 
                                    'is_deleted' => $is_deleted) );

        return $result ? true : false;
    }

    public function insertNews($itm) 
    {

        $stmt = $this->pdo->prepare('INSERT INTO tbl_storefinder_news( 
                                        news_id, 
                                        news_content, 
                                        news_title, 
                                        news_url, 
                                        photo_url, 
                                        created_at, 
                                        updated_at  ) 

                                    VALUES(
                                        :news_id, 
                                        :news_content, 
                                        :news_title, 
                                        :news_url, 
                                        :photo_url, 
                                        :created_at, 
                                        :updated_at )');
        
        $result = $stmt->execute(
                            array('news_id' => $itm->news_id,
                                    'news_content' => $itm->news_content,  
                                    'news_title' => $itm->news_title,
                                    'news_url' => $itm->news_url,
                                    'photo_url' => $itm->photo_url,
                                    'created_at' => $itm->created_at,
                                    'updated_at' => $itm->updated_at ) );
        
        return $result ? true : false;
    }
 

    public function getNews() 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_storefinder_news 
                                        WHERE is_deleted = 0 ORDER BY news_id DESC');
        
        $stmt->execute();

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new News();
            $itm->news_id = $row['news_id'];
            $itm->news_content = $row['news_content'];
            $itm->news_title = $row['news_title'];
            $itm->news_url = $row['news_url'];
            $itm->photo_url = $row['photo_url'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }

    public function getNewsBySearching($search) 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                FROM tbl_storefinder_news 
                                WHERE is_deleted = 0 AND news_title LIKE :search ORDER BY news_id DESC');
        
        $stmt->execute( array('search' => '%'.$search.'%'));

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new News();
            $itm->news_id = $row['news_id'];
            $itm->news_content = $row['news_content'];
            $itm->news_title = $row['news_title'];
            $itm->news_url = $row['news_url'];
            $itm->photo_url = $row['photo_url'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->is_deleted = $row['is_deleted'];

            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }


    public function getNewsByNewsId($news_id) 
    {
        
        $stmt = $this->pdo->prepare('SELECT * 
                                FROM tbl_storefinder_news
                                WHERE news_id = :news_id');
        
        $stmt->execute( array('news_id' => $news_id));

        foreach ($stmt as $row) 
        {
            $itm = new News();
            $itm->news_id = $row['news_id'];
            $itm->news_content = $row['news_content'];
            $itm->news_title = $row['news_title'];
            $itm->news_url = $row['news_url'];
            $itm->photo_url = $row['photo_url'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->is_deleted = $row['is_deleted'];

            return $itm;
        } 
        
        return null;
    }


    public function getLastInsertedId() {

        return $this->pdo->lastInsertId(); 
    }

    public function getNewsAtRange($begin, $end) 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_storefinder_news 
                                        WHERE is_deleted = 0 ORDER BY news_id ASC LIMIT :beg, :end');
        
        $stmt->execute( array('beg' => $begin, 'end' => $end) );

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new News();
            $itm->news_id = $row['news_id'];
            $itm->news_content = $row['news_content'];
            $itm->news_title = $row['news_title'];
            $itm->news_url = $row['news_url'];
            $itm->photo_url = $row['photo_url'];
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