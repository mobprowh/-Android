package com.config;

public class Config {

	// Server API KEY
    public static final String API_KEY = "45090dcae2aYMK";

	// Change this on your own consumer key
	public static final String TWITTER_CONSUMER_KEY = "R0u1hsVNb1lOEMzbxW6Ju1jcm";
	
	// Change this on your own consumer secret
	public static final String TWITTER_CONSUMER_SECRET = "CGIZRrb7mabu16g89U1Uz1oDKor7qxWkqpQy4KLfmWzA9VsQBd";
	
	// Set to true if you want to display test ads in emulator
	public static final boolean TEST_ADS_USING_EMULATOR = true;
	
	// Set to true if you want to display test ads on your testing device
	public static final boolean TEST_ADS_USING_TESTING_DEVICE = true;
	
	// Add testing device hash
	// It is displayed upon running the app, please check logcat.
	public static final String TESTING_DEVICE_HASH = "3BE2FA86964E0348BBE40ECFE3FAD546";
	
	// Set to true if you want to display ads in all views.
	public static final boolean WILL_SHOW_ADS = true;
	
	// You AdMob Banner Unit ID
	public static final String BANNER_UNIT_ID = "ca-app-pub-1616470634359992/2091423063";
	
	// Change this url depending on the name of your web hosting.
	public static String BASE_URL = "http://mangasaurgames.com/apps/storefinder-v1.7.3/";

	// Your email that you wish that users on your app will contact you.
	public static String ABOUT_US_EMAIL = "mangasaurgames@gmail.com";


    // Default filter distance in kilometers
    public final static int DEFAULT_FILTER_DISTANCE_IN_KM = 1000;

    // Max filter distance in kilometers
    public final static int MAX_RADIUS_STORE_VALUE_IN_KM = 5000;

	// Max home store featured to be displayed
	// set to -1 if you want to display all
	public static int  HOME_STORE_FEATURED_COUNT = 5;

	// Max home news to be displayed
	// set to -1 if you want to display all
	public static int HOME_NEWS_COUNT = 5;

	// Adjust this if you want to display reviews at a
	// certain count and shows the View More Comments
	public static int MAX_REVIEW_COUNT_PER_LISTING = 15;
	
	// Edit this if you wish to increase 
	// character count when adding reviews.
	public static int MAX_CHARS_REVIEWS = 255;
	
	// Map zoom level
	public static int MAP_ZOOM_LEVEL = 14;
	
	// Edit this to increase radius in searching store
	public static int MAX_SEARCH_RADIUS = 200000;
	
	// Edit this to increase radius to show stores nearby
	public static int MAX_RADIUS_NEARBY_IN_METERS = 20000;
	
	// Debug state, set this always to true to get always an update of data.
	public final static boolean WILL_DOWNLOAD_DATA = true;
	
	// adjust this depending on the offset of you map info window.
	public final static float MAP_INFO_WINDOW_X_OFFSET = 0.25f;

	// Get number of featured stores in home to be fetched
	public final static int HOME_FEATURED_COUNT = 5;

	// This is used to find the maximum default distance found
	// in a given number of stores
	public final static int DEFAULT_STORE_COUNT_TO_FIND_DISTANCE = 20;

	// DO NOT EDIT THIS
	public static boolean RANK_STORES_ACCORDING_TO_NEARBY = true;

	// DO NOT EDIT THIS
	public static String GET_NEWS_JSON_URL = BASE_URL + "rest/get_news.php";

	// DO NOT EDIT THIS
	public static String GET_STORES_NEWS_JSON_URL = BASE_URL + "rest/get_stores_news.php";

	// DO NOT EDIT THIS
	public static String GET_STORES_JSON_URL = BASE_URL + "rest/get_stores.php";

	// DO NOT EDIT THIS
	public static String GET_CATEGORIES_JSON_URL = BASE_URL + "rest/get_categories.php";

	// DO NOT EDIT THIS
	public static String REGISTER_URL = BASE_URL + "rest/register.php";
	
	// DO NOT EDIT THIS
	public static String USER_PHOTO_UPLOAD_URL = BASE_URL + "rest/file_uploader_user_photo.php";
	
	// DO NOT EDIT THIS
	public static String REVIEWS_URL = BASE_URL + "rest/review_load_more.php";
	
	// DO NOT EDIT THIS
	public static String POST_REVIEW_URL = BASE_URL + "rest/post_review.php";
	
	// DO NOT EDIT THIS
	public static String POST_RATING_URL = BASE_URL + "rest/post_rating.php";
	
	// DO NOT EDIT THIS
	public static String GET_RATING_USER_URL = BASE_URL + "rest/get_rating_user.php";
	
	// DO NOT EDIT THIS
	public static String LOGIN_URL = BASE_URL + "rest/login.php";
	
	// DO NOT EDIT THIS
	public static String UPDATE_USER_PROFILE_URL = BASE_URL + "rest/update_user_profile.php";
	
	// DO NOT EDIT THIS
	public static String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?";

	// DO NOT EDIT THIS
	public static String GET_HOME_STORES_NEWS_JSON_URL = BASE_URL + "rest/get_home_stores_news.php";

	// WEATHER_APP_ID
	public static String  WEATHER_APP_ID = "bc697957c11ecf39a5c4ada6e7e340ac";
	
	// DO NOT EDIT THIS
	public final static int DELAY_SHOW_ANIMATION = 200;

    // DO NOT EDIT THIS
    public final static double DEBUG_LATITUDE = 37.332331;

    // DO NOT EDIT THIS
    public final static double DEBUG_LONGITUDE= -122.031219;

    // DO NOT EDIT THIS
    public final static boolean DEBUG_LOCATION = false;

	// DO NOT EDIT THIS
    public final static boolean AUTO_ADJUST_DISTANCE = true;
}
