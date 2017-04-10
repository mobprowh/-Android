<?php

/**
 * Database config variables
 * Change this according to your server settings
 */
class Constants
{
	// Definitely your Database Host name
	const DB_HOST = "localhost";

	// change the user access, CPanel have user roles, when writing and reading files
	// set it to allow the certain User to read/write
	const DB_USER = "root";

	// change this according to your account credentials
	const DB_PASSWORD = "root";

	// if you wish you create your own name for 
	// Database then change the word "db_restaurants"
	const DB_DATABASE = "db_restaurants";

	// If deployed in a web server, change this according to your configuration
	// For Example. the domain name is www.someUrl.com, then if the php files are stored in
	// a folder named as "responsive" then the complete url would be
	// www.someUrl.com/responsive/
	const ROOT_URL = "http://localhost/personal/restau/";

	// FOLDER DIRECTORY FOR XML DATA PHP FILE
	// DONT CHANGE THIS
	const XML_FILE = "rest/data_xml.php";

	// FOLDER DIRECTORY FOR JSON DATA PHP FILE
	// DONT CHANGE THIS
	const JSON_FILE = "rest/data_json.php";

	// DON NOT CHANGE THIS
	// FOLDER DIRECTORY FOR IMAGES UPLOADED FROM
	// THE DESKTOP
	const IMAGE_UPLOAD_DIR = "upload_pic";

	// Default latitude for the map to be set when it is loaded in adding restaurants
	const MAP_DEFAULT_LATITUDE = 9.0831987;

	// Default  longitude for the map to be set when it is loaded in adding restaurants
	const MAP_DEFAULT_LONGITUDE = -79.5924069;

	// Adjust map zoom for Restaurant adding
	// lower value = zoom out
	// higher value = zoom in
	const MAP_DEFAULT_ZOOM_LEVEL_ADD = 8;

	// Adjust map zoom for Restaurant editing
	// lower value = zoom out
	// higher value = zoom in
	const MAP_DEFAULT_ZOOM_LEVEL_EDIT = 15;
}

?>