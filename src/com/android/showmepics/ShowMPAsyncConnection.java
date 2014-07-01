package com.android.showmepics;

import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Message;

import com.android.showmepics.ShowMPMainListActivity.AsyncHandler;


/**
 * @author Ciro Rizzo
 *
 * This is the most important class of the project 'cause managing different Threads
 * caring of Synchronizing executions, especially for the Threads for Downloading 
 * Pictures Content and Images
 * 
 * All Threads using Handler and Messages to communicate with other Application Components
 * An other way to communicate is to use Intents, this is not the case
 */

public class ShowMPAsyncConnection {
	private AsyncHandler mAsyncConnectionHandler;
	private ShowMPListItemObj mShowMPListItemObj;
	private Context mCtx;
	private Semaphore firstSemaphore;

	public ShowMPAsyncConnection() {
		// TODO Auto-generated constructor stub
	}
	
	public ShowMPAsyncConnection(AsyncHandler aAsyncHandler) {
		// TODO Auto-generated constructor stub

		mAsyncConnectionHandler = aAsyncHandler;
	}
	
	public ShowMPAsyncConnection(AsyncHandler aAsyncHandler, ShowMPListItemObj aShowMPListItemObj) {
		// TODO Auto-generated constructor stub

		mAsyncConnectionHandler = aAsyncHandler;
		mShowMPListItemObj = aShowMPListItemObj;
	}

	public ShowMPAsyncConnection(AsyncHandler aAsyncHandler, ShowMPListItemObj aShowMPListItemObj, Context context) {
		// TODO Auto-generated constructor stub

		mAsyncConnectionHandler = aAsyncHandler;
		mShowMPListItemObj = aShowMPListItemObj;
		mCtx = context;
	}
	
	public void startDownload() {
		if (ShowMPUtilityLib.isConnectivityOn(mCtx)) {
			sendAsyncHandlerMessage(AsyncHandler.PICS_CONTENT_STARTS_DOWNLOADING);
			
			// Semaphore to Synchronize the following Threads
			firstSemaphore = new Semaphore(1, true);
			
			// Starting Thread to download the Pictures Content from the WebService
			DownloadPicsContent mDownloadPicsContent = new DownloadPicsContent();
			mDownloadPicsContent.execute();
			
			// Starting Thread to download Images from the WebService
			DownloadPicsImage mDownloadPicsImage = new DownloadPicsImage();
			mDownloadPicsImage.execute(mShowMPListItemObj);			
		} else {
			sendAsyncHandlerMessage(AsyncHandler.PICS_CONTENT_NO_CONNECTIVITY);
		}
	}

	private void sendAsyncHandlerMessage(int aMessageID) {
		Message msg = Message.obtain(mAsyncConnectionHandler, aMessageID);
		mAsyncConnectionHandler.sendMessage(msg);
	}
	
	
	/**
	 * Main Thread to Download Pictures Contents
	 */
	public class DownloadPicsContent extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String mURL = ShowMPUtilityLib.getPicsContentURL();
			String mResult = null;
			firstSemaphore.acquireUninterruptibly();
			
			try {
				mResult = ShowMPUtilityLib.getStrHTTPResponse(mURL);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				ShowMPUtilityLib.logMe(e.getMessage());
			}
			return mResult;
		}	
		
		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject jObj = new JSONObject(result).getJSONObject("photos");
				
				if (jObj != null) {
					fillingListPhotoItem(jObj);					
				} else {
					// TODO Send Handler Msg Error
					sendAsyncHandlerMessage(AsyncHandler.PICS_CONTENT_ERROR_CAUGHT);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ShowMPUtilityLib.logMe(e.getMessage());
				sendAsyncHandlerMessage(AsyncHandler.PICS_CONTENT_ERROR_CAUGHT);
			} finally {
				firstSemaphore.release();
			}
		}
	}

	/**
	 * Thread to Download Thumbnail/Enlarged Pictures 
	 */
	public class DownloadPicsImage extends AsyncTask<ShowMPListItemObj, Void, Void> {

		@Override
		protected Void doInBackground(ShowMPListItemObj... params) {
			// TODO Auto-generated method stub
			
			ShowMPListItemObj tmpShowMPListItemObj = params[0];
			String mURL = "";
			
			firstSemaphore.acquireUninterruptibly();
			
			try {
				
				for (int i = 0; i < tmpShowMPListItemObj.getCount(); i++) {
					mURL = ShowMPUtilityLib.getPicsThumbnailURL(tmpShowMPListItemObj.getObj(i).photoFarm, 
							tmpShowMPListItemObj.getObj(i).photoServer, 
							tmpShowMPListItemObj.getObj(i).photoId, 
							tmpShowMPListItemObj.getObj(i).photoSecret);
					
					tmpShowMPListItemObj.getObj(i).photoThumbnail = ShowMPUtilityLib.getBmpHTTPResponse(mURL);
				}
				
				if (tmpShowMPListItemObj.getCount() == 0)
					sendAsyncHandlerMessage(AsyncHandler.PICS_CONTENT_NO_DATA);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				ShowMPUtilityLib.logMe(e.getMessage());
				sendAsyncHandlerMessage(AsyncHandler.PICS_CONTENT_ERROR_CAUGHT);
			}
			return null;
		}	

		@Override
		protected void onPostExecute(Void dummy) {
			try {
				sendAsyncHandlerMessage(AsyncHandler.PICS_CONTENT_DOWNLOADED);
			} finally {
				firstSemaphore.release();
			}
		}
	}

	/**
	 * Method to fill ShowMPListItemObj with Pics Content grabbed from WebService
	 * 
	 * @param aJObj
	 * @throws JSONException
	 */
	private void fillingListPhotoItem(JSONObject aJObj) throws JSONException {
		JSONArray jArray = new JSONArray();

		try {

			jArray = aJObj.getJSONArray("photo");

			for (int i = 0; i < jArray.length(); i++) {
				String photoId = jArray.getJSONObject(i).getString("id");
				String photoOwner = jArray.getJSONObject(i).getString("owner");
				String photoSecret = jArray.getJSONObject(i).getString("secret");
				String photoServer = jArray.getJSONObject(i).getString("server");
				String photoFarm = jArray.getJSONObject(i).getString("farm");
				String photoTitle = jArray.getJSONObject(i).getString("title");
				String photolLink = jArray.getJSONObject(i).getString("id");

				mShowMPListItemObj.addItem(new ShowMPItemObj(String.valueOf(i), 
						photoId, 
						photoOwner, 
						photoSecret,
						photoServer,
						photoFarm,
						photoTitle, 
						photolLink,
						null));
			}
			
			if (jArray.length() == 0)
				sendAsyncHandlerMessage(AsyncHandler.PICS_CONTENT_NO_DATA);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ShowMPUtilityLib.logMe(e.getMessage());
			sendAsyncHandlerMessage(AsyncHandler.PICS_CONTENT_ERROR_CAUGHT);
		}
	}

}
