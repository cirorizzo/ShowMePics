package com.android.showmepics;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Ciro Rizzo
 * 
 * Main ListActivity Class to manage the UI controls
 * For this example I used the SwipeRefreshLayout implementation
 * to have the Swipe down refresh feature
 *
 */

public class ShowMPMainListActivity extends ListActivity implements OnRefreshListener {
	private AsyncHandler mAsyncHandler;
	private ShowMPListItemObj mShowMPListItemObj;
	private SwipeRefreshLayout swipeLayout;
	
	private ShowMPAsyncConnection mShowMPAsyncConnection;
	
	private boolean isDownloadingPicturesData = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showmp_main_list);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		initObjs();
		
		
		startingDownloadingService();
	}

	/**
	 * Method to initialize objects used for
	 */
	private void initObjs() {
		mAsyncHandler = new AsyncHandler();
		
		mShowMPListItemObj = new ShowMPListItemObj();
		
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(R.color.blueback, R.color.green_on, R.color.gold, R.color.red);
		
		mShowMPAsyncConnection = 
				new ShowMPAsyncConnection(mAsyncHandler, mShowMPListItemObj, getApplicationContext());
	}
	
	/**
	 * Main Activity Events to detect the different stages of it
	 */
	@Override
    public void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * In case the activity come back, the startingService() is calling to download data
	 */
	public void onResume() {
		super.onResume();
		//startingService();
	}
	
	/**
	 * On Pause Activity ensuring to unregister the receiver
	 */
	public void onPause() {
		unregisterReceiver(responseReceiver);
		super.onPause();
	}
	
	
	/**
	 * Method to set the Custom Adapter used in the ListView
	 */
	private void setAdapterContent() {
		setListAdapter(new ShowMPItemObjAdapter(this, R.layout.item_of_list, mShowMPListItemObj.showMPList));
	}
	
	/**
	 * Intent Filters Setting, Receiver Registering and Starting the Image Download 
	 */
	private void startingDownloadingService() {
		setIntentFilterNReceiver();
		startPicsContentDownload();
	}

	
	// ------------------------------------------------------------------------
	// Intents Filter and Receiver Management
	// ------------------------------------------------------------------------
	private void setIntentFilterNReceiver() {
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(ShowMPConstants.KILLING_COMMAND);
		
		registerReceiver(responseReceiver, mIntentFilter);
	}
	// ------------------------------------------------------------------------

	/**
	 * Starting the Asynchronous Threads to Download Pictures from WebService
	 */
	private void startPicsContentDownload() {
		if (!isDownloadingPicturesData) {
			isDownloadingPicturesData = true;
		
			mShowMPAsyncConnection.startDownload();
		}
	}
	
	
	private void startWaitingProgress() {
		swipeLayout.setRefreshing(true);
	}

	private void stopWaitingProgress() {
		isDownloadingPicturesData = false;
		swipeLayout.setRefreshing(false);
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		startPicsContentDownload();
	}
	
	
	/**
	 * This is the Handler used to manage the events from the Asynchronous Threads
	 * This is only one of the method to communicate whit other application component 
	 * or services
	 * Another way is to use Intents, but for this case is more efficient using Handlers
	 */
	public class AsyncHandler extends Handler {
		public static final int PICS_CONTENT_STARTS_DOWNLOADING = 10;
		public static final int PICS_CONTENT_DOWNLOADED = 100;
		public static final int PICS_CONTENT_NO_CONNECTIVITY = -100;
		public static final int PICS_CONTENT_NO_DATA = -900;
		public static final int PICS_CONTENT_ERROR_CAUGHT = -999;
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PICS_CONTENT_STARTS_DOWNLOADING:
				startWaitingProgress();
				break;
			case PICS_CONTENT_DOWNLOADED:
				isDownloadingPicturesData = false;
				setAdapterContent();
				stopWaitingProgress();
				break;
			case PICS_CONTENT_NO_CONNECTIVITY:
				stopWaitingProgress();
				getOutMessage(ShowMPConstants.UI_DIALOG, 
						R.string.dialog_title_no_connectivity, 
						R.string.dialog_message_no_connectivity_killing, true);
				break;
			case PICS_CONTENT_NO_DATA:
				stopWaitingProgress();
				getOutMessage(ShowMPConstants.UI_DIALOG, 
						R.string.dialog_title_no_data, 
						R.string.dialog_message_no_data_killing, true);
				break;
			case PICS_CONTENT_ERROR_CAUGHT:
				stopWaitingProgress();
				getOutMessage(ShowMPConstants.UI_DIALOG, 
						R.string.dialog_title_error_caught, 
						R.string.dialog_message_error_caught_killing, true);
				break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView aListVw, View aView, int aPicturePosition, long id) {
		super.onListItemClick(aListVw, aView, aPicturePosition, id);

		if (!isDownloadingPicturesData) {
			if (mShowMPListItemObj != null) {
				Bitmap imgBMP = mShowMPListItemObj.getObj(aPicturePosition).photoThumbnail;

				Bundle mArgs = new Bundle();
				mArgs.putByteArray(ShowMPConstants.KEY_ARG_IMG, ShowMPUtilityLib.getBMPtoByteArray(imgBMP));
				ShowMPPictureFragment mShowMPPictureFragment = new ShowMPPictureFragment();
				mShowMPPictureFragment.setArguments(mArgs);

				try {
					getFragmentManager()
					.beginTransaction()
					.replace(R.id.container, mShowMPPictureFragment)
					.addToBackStack(null)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					ShowMPUtilityLib.logMe(e.getMessage());
				}
			}
		} else {
			// Use ShowMPConstants.TOAST_MSG to have a Toast Message
			// Use ShowMPConstants.UI_DIALOG to show a Dialog Message
			getOutMessage(ShowMPConstants.UI_DIALOG, 
					R.string.dialog_title_image_downloading, 
					R.string.dialog_message_image_downloading, false);
		}
	}

	
	/**
	 * 
	 * Use aMessageMode to have different UI Message
	 * 	aMessageMode = ShowMPConstants.TOAST_MSG to have a Toast Message
	 * 	aMessageMode =  ShowMPConstants.UI_DIALOG to show a Dialog Message
	 * 
	 * @param aMessageMode
	 * @param aTitleID
	 * @param aMessageID
	 * @param aKillIt
	 */
	private void getOutMessage(int aMessageMode, int aTitleID, int aMessageID, boolean aKillIt) {
		if (aMessageMode == ShowMPConstants.UI_DIALOG) {
			showDialog(aTitleID, aMessageID, aKillIt);
		} else if (aMessageMode == ShowMPConstants.TOAST_MSG) {
			showToast(aMessageID);
		}
	}
	
	
	/**
	 * Methods to manage the DialogFragment to interact with users
	 * @param aTitleID
	 * @param aMessageID
	 */
	private void showDialog(int aTitleID, int aMessageID, boolean aKillIt) {
		DialogFragment uiDialogMessage = UIDialogMessage.newInstance(aTitleID, aMessageID, aKillIt);
		uiDialogMessage.show(getFragmentManager(), "dialog");
	}
	
	
	/**
	 * Showing a Toast instead a Dialog
	 * @param aMessageID
	 */
	private void showToast(int aMessageID) {
		Toast.makeText(getApplicationContext(), getResources().getString(aMessageID), ShowMPConstants.AUTO_HIDE_DELAY_MILLIS).show();
	}
	
	/**
	 * Broadcast Receiver to Catch Intents from other Application Components
	 * This is only one of the available methods to communicate between two or 
	 * more Application Components each others
	 */
	private BroadcastReceiver responseReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intentReceived) {
			// TODO Auto-generated method stub
			// Broadcast Action by UIDialogMessage Class
			if (intentReceived.getAction().equalsIgnoreCase(ShowMPConstants.KILLING_COMMAND)) {
				ShowMPMainListActivity.this.finish();
			} 
		}
	};
}
