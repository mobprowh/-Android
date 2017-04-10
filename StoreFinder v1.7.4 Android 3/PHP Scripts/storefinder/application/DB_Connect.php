<?php
 
class DB_Connect {
 
    // constructor
    function __construct() 
    {
         
    }
 
    // destructor
    function __destruct() 
    {
        // $this->close();
    }
 
    // Connecting to database
    public function connect() 
    {
        $con = new PDO('mysql:host='.Constants::DB_HOST.';dbname='.Constants::DB_DATABASE.';charset=utf8', Constants::DB_USER, Constants::DB_PASSWORD);
        $con->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
        $con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
 
        // return database handler
        return $con;
    }
 
    // Closing database connection
    public function close() 
    {
        // mysql_close();
    }
 
}
 
?>