package com.config;

public class Config {

	// Change this to YES or No to change format data using JSON or XML
	// false means XML data format
	// true means JSON data format
	public static boolean WILL_USE_JSON_FORMAT = false;

	// URL of the json file
	public static String DATA_JSON_URL = "http://www.pridashost.com/drinkea/rest/data_json.php";

	// URL of the xml file
	public static String DATA_XML_URL = "http://www.pridashost.com/drinkea/rest/data_xml.php";
	
	// Map zoom level
	public static int MAP_ZOOM_LEVEL = 14;

	// Search Category for all selection
	public final static String CATEGORY_ALL = "All";
	
	// Debug state, set this always to true to get always an update of data.
	public final static boolean WILL_DOWNLOAD_DATA = true;

	// Set to true if you want to display test ads in emulator
	public static final boolean TEST_ADS_USING_EMULATOR = true;

	// Set to true if you want to display test ads on your testing device
	public static final boolean TEST_ADS_USING_TESTING_DEVICE = false;

	// Add testing device hash
	// It is displated upon running the app, please check logcat.
	public static final String TESTING_DEVICE_HASH = "3BE2FA86964E0348BBE40ECFE3FAD546";

	// Set to true if you want to display ads in all views.
	public static final boolean WILL_SHOW_ADS = false;

	// You AdMob Banner Unit ID
	public static final String BANNER_UNIT_ID = "ca-app-pub-2513284293470814/4631467285";

	// Delay animation to show each view content
	// DO NOT EDIT THIS
	public final static int DELAY_SHOW_ANIMATION = 200;
}
