package com.android.showmepics;

import android.graphics.Bitmap;

public class ShowMPConstants {
	// UI Messaging Modes
	public static final int UI_DIALOG = 700;
	public static final int TOAST_MSG = 300;
	
	// Dialog Consts
	public static final String DIALOG_KEY_TITLEID = "DIALOG_KEY_TITLEID";
	public static final String DIALOG_KEY_MESSAGEID = "DIALOG_KEY_MESSAGEID";
	public static final String DIALOG_KEY_KILLIT = "DIALOG_KEY_KILLIT";
	
	// Logging Contants
	public static final String TAG_LOG = "ShowMePics";
	public static final boolean DEBUG_MODE = true;
	
	// WebService API Constants
	public static final String FLICKR_API_BASE_URI = "https://api.flickr.com/services/rest/";
	public static final String FLICKR_API_KEY = "&api_key=91adbc4b8a91c5a9cf7c73a0521011c4";
	public static final String FLICKR_API_FORMAT = "&format=json";
	public static final String FLICKR_API_PAGE = "&page=";
	public static final String FLICKR_API_PER_PAGE = "&per_page=";
	public static final String FLICKR_API_MEDIA = "&media=photos";
	public static final String FLICKR_API_TAGS = "&tags=";
	public static final String FLICKR_API_NOJSONCALLBACK = "&nojsoncallback=1";
	
	public static final String FLICKR_API_METHOD_SEARCH = "?method=flickr.photos.search";
	
	public static final String FLICKR_API_GET_THUMBIMAGE_FMT = "https://farm%1s.staticflickr.com/%1s/%1s_%1s_%1s.jpg";
			
	// Data Management Constants
    public static final String MIN_SHOWING_PICS = "15";
    public static final String FLICKR_API_PAGE_NUMB = "1";
    public static final String SEARCH_TAG = "cat";
    
	// Timing Constants
	public static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	
	// Default Initializing Constants
	public static final String DEF_STR = "";
	public static final int DEF_INT = -1;
	public static final double DEF_DOUBLE = 0.0d;
	public static final Bitmap DEF_BMP = null;
	
	// Argument Key for Bundle Obj used on Fragment calling
	public static final String KEY_ARG_IMG = "KEY_ARG_IMG";
	
	
	// Command Constant
	public static final String KILLING_COMMAND = "KILLING_COMMAND";

}
