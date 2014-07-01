package com.android.showmepics;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


/**
 * @author Ciro Rizzo
 * 
 * DialogFragment Class used to manage the Dialogs used in the UI
 * using AlertDialog.Builder with a single "PositiveButton" where 
 * I managed a special behavior on clicking it, but only in some cases:
 * have a look killApp() method
 * 
 */

public class UIDialogMessage extends DialogFragment {

	public static UIDialogMessage newInstance(int aTitleID, int aMessageID) {
		return newInstance(aTitleID, aMessageID, true);
	}
	
	public static UIDialogMessage newInstance(int aTitleID, int aMessageID, boolean aKillIt) {
		UIDialogMessage frag = new UIDialogMessage();
		Bundle args = new Bundle();
		args.putInt(ShowMPConstants.DIALOG_KEY_TITLEID, aTitleID);
		args.putInt(ShowMPConstants.DIALOG_KEY_MESSAGEID, aMessageID);
		args.putBoolean(ShowMPConstants.DIALOG_KEY_KILLIT, aKillIt);
		frag.setArguments(args);
		return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int mTitleID = getArguments().getInt(ShowMPConstants.DIALOG_KEY_TITLEID);
		int mMessageID = getArguments().getInt(ShowMPConstants.DIALOG_KEY_MESSAGEID);
		final boolean mKillIt = getArguments().getBoolean(ShowMPConstants.DIALOG_KEY_KILLIT, true);
		 
		return new AlertDialog.Builder(getActivity())
		.setTitle(mTitleID)
		.setMessage(mMessageID)
		.setPositiveButton(getResources().getString(R.string.dialog_button_gotcha),
				new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				if (mKillIt)
					killApp();
			}
		})
		.create();
	}
	
	private void killApp() {
		Intent broadcastCallBack = new Intent();	
		broadcastCallBack.setAction(ShowMPConstants.KILLING_COMMAND);
		getActivity().sendBroadcast(broadcastCallBack);
	}
}
