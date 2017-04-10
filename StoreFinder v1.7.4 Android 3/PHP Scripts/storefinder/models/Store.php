<?php
 
class Store
{
    public $store_id;
    public $store_name;
    public $store_address;
    public $store_desc;
    public $lat;
    public $lon;
    public $sms_no;
    public $phone_no;
    public $email;
    public $website;
    public $category_id;
    public $icon_id;
    public $created_at;
    public $updated_at;
    public $featured;
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