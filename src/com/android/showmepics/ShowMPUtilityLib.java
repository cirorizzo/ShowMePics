package com.android.showmepics;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * @author Ciro Rizzo
 * 
 * This Class is used to collect the main utility functions used in the project
 * 
 */

public class ShowMPUtilityLib {
	/** 
	 * Checking the available Data Connectivity
	 * @param Context
	 * @return boolean value (true = Connectivity is On)
	 */
	public static boolean isConnectivityOn(Context ctx) {
    	boolean resCode = false;

    	try {
    		ConnectivityManager cm =
    				(ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

    		resCode = cm.getActiveNetworkInfo().isConnectedOrConnecting();

    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    		ShowMPUtilityLib.logMe(e.getMessage());
    	}

    	return resCode;
    }
	
	/**
	 * Conversion method to manage Bitmap Object to Byte Array
	 * @param aBMP
	 * @return byte[]
	 */
	public static byte[] getBMPtoByteArray(Bitmap aBMP) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		aBMP.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

	/**
	 * Method to manage in easy way Logs
	 * @param aStr
	 */
	public static void logMe(String aStr) {
		if (ShowMPConstants.DEBUG_MODE)
			Log.d(ShowMPConstants.TAG_LOG, aStr);
	}
	
	
	/**
	 * Get String Response from HTTP Request on WebService
	 * 
	 * @param aURL
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getStrHTTPResponse(String aURL) throws ClientProtocolException, IOException {
		String mResult = "";

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(aURL);

		HttpResponse hResponse = httpClient.execute(httpGet);

		if (hResponse.getStatusLine().getStatusCode() == 200) {

			HttpEntity hEntity = hResponse.getEntity();
			if (hEntity != null) 
				mResult = EntityUtils.toString(hEntity);
		}

		return mResult;
	}
	
	/**
	 * Get Bitmap Response from HTTP Request on WebService
	 * 
	 * @param aURL
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static Bitmap getBmpHTTPResponse(String aURL) throws ClientProtocolException, IOException {
		Bitmap mResult = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(aURL);

		HttpResponse hResponse = httpClient.execute(httpGet);

		if (hResponse.getStatusLine().getStatusCode() == 200) {

			HttpEntity hEntity = hResponse.getEntity();
			if (hEntity != null) {
				byte[] bytes = EntityUtils.toByteArray(hEntity);

				mResult = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			}
		}

		return mResult;
	}
	
	public static String getPicsContentURL() {
		return ShowMPConstants.FLICKR_API_BASE_URI +
				ShowMPConstants.FLICKR_API_METHOD_SEARCH +
				ShowMPConstants.FLICKR_API_KEY +
				ShowMPConstants.FLICKR_API_TAGS + ShowMPConstants.SEARCH_TAG +
				ShowMPConstants.FLICKR_API_FORMAT +
				ShowMPConstants.FLICKR_API_PAGE + ShowMPConstants.FLICKR_API_PAGE_NUMB +
				ShowMPConstants.FLICKR_API_PER_PAGE + ShowMPConstants.MIN_SHOWING_PICS +
				ShowMPConstants.FLICKR_API_MEDIA +
				ShowMPConstants.FLICKR_API_NOJSONCALLBACK;
	}
	
	public static String getPicsThumbnailURL(String aPhotoFarm, String aPhotoServer,
			String aPhotoId, String photoSecret) {
		
		return String.format(ShowMPConstants.FLICKR_API_GET_THUMBIMAGE_FMT,
				aPhotoFarm,
				aPhotoServer,
				aPhotoId, 
				photoSecret,
				"m");
	}
}
