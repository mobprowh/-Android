<?php
 
class Restaurant
{
	public $restaurant_id;
    public $name;
    public $address;
    public $lat;
    public $lon;
    public $desc1;
    public $email;
    public $website;
    public $amenities;
    public $food_rating;
    public $price_rating;
    public $featured;
    public $phone;
    public $hours;
    public $created_at;
    public $category_id;
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