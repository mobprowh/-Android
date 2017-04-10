<?php
 
class News
{
    public $news_id;
    public $news_content;
    public $news_title;
    public $news_url;
    public $photo_url;
    public $created_at;
    public $updated_at;
    public $is_deleted;

    // constructor
    function __construct() 
    {

    }
 
    // destructor
    function __destruct() 
    {
         
    }
}
 
?>