package com.android.showmepics;

import java.util.ArrayList;

import android.graphics.Bitmap;

/**
 * @author Ciro Rizzo
 * This Class is used to manage the list of items of Pictures Data caught from the WebService
 * and shown on UI by ShowMPItemObjAdapter (the Custom Adapter)
 */

public class ShowMPListItemObj {
	public ArrayList<ShowMPItemObj> showMPList;
	
	public ShowMPListItemObj() {
		// TODO Auto-generated constructor stub
		showMPList = new ArrayList<ShowMPItemObj>();
	}

	public void addItem(ShowMPItemObj aShowMPItemObj) {
		showMPList.add(aShowMPItemObj);
	}
	
	public int getCount() {
		return showMPList.size();
	}
	
	public ShowMPItemObj getObj(int aIdx) {
		return showMPList.get(aIdx);
	}
	
	/**
	 * This method is used to set in the ArrayList the icon of Weather in a second stage
	 * @param aIdx
	 * @param aWeather_icon_bmp
	 */
	public void setShowMPImage(int aIdx, Bitmap aShowMPImage) {
		showMPList.get(aIdx).photoThumbnail = aShowMPImage;
	}
}
