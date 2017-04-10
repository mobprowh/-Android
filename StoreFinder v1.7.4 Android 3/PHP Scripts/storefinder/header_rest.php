<?php 
	require_once 'application/Config.php';
	require_once 'application/Globals.php';
	require_once 'application/Extras.php';
	
	// Debugging status
	if (DEBUG) 
	{
		// Report all errors, warnings, interoperability and compatibility
		error_reporting(E_ALL|E_STRICT);
		// Show errors with output
		ini_set("display_errors", "on");
	}
	else 
	{
		error_reporting(0);
		ini_set("display_errors", "off");
	}

	require_once '../application/DB_Connect.php';
 
    require_once '../models/User.php';
    require_once '../controllers/ControllerUser.php';
    
    require_once '../models/Photo.php';
    require_once '../controllers/ControllerPhoto.php';
    require_once '../controllers/ControllerRest.php';
    
    require_once '../models/Review.php';
    require_once '../controllers/ControllerReview.php';

    require_once '../models/Rating.php';
    require_once '../controllers/ControllerRating.php';
?>