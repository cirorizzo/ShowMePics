package com.android.showmepics;

import android.graphics.Bitmap;

public class ShowMPItemObj {
	public String id;
	public String photoId;
	public String photoOwner;
	public String photoUserName;
	public String photoSecret;
	public String photoServer;
	public String photoFarm;
	public String photoTitle;
	public String photoLink;
	public Bitmap photoThumbnail;
	
	public ShowMPItemObj() {
		initMembers(ShowMPConstants.DEF_STR,
				ShowMPConstants.DEF_STR,
				ShowMPConstants.DEF_STR,
				ShowMPConstants.DEF_STR,
				ShowMPConstants.DEF_STR,
				ShowMPConstants.DEF_STR,
				ShowMPConstants.DEF_STR,
				ShowMPConstants.DEF_STR,
				ShowMPConstants.DEF_BMP);
	}
			
	public ShowMPItemObj(String aId, String aPhotoId, String aPhotoOwner, 
			String aPhotoSecret, String aPhotoServer, String aPhotoFarm, 
			String aPhotoTitle, String aPhotolLink, Bitmap aPhotoThumbnail) {
		
		initMembers(aId, 
				aPhotoId, 
				aPhotoOwner, 
				aPhotoSecret, 
				aPhotoServer, 
				aPhotoFarm, 
				aPhotoTitle, 
				aPhotolLink, 
				aPhotoThumbnail);
	}
	
	private void initMembers(String aId, String aPhotoId, String aPhotoOwner, 
			String aPhotoSecret, String aPhotoServer, String aPhotoFarm, 
			String aPhotoTitle, String aPhotolLink, Bitmap aPhotoThumbnail) {
		
		this.id = aId;
		this.photoId = aPhotoId;
		this.photoOwner = aPhotoOwner;
		this.photoUserName = null;
		this.photoSecret = aPhotoSecret;
		this.photoServer = aPhotoServer;
		this.photoFarm = aPhotoFarm;
		this.photoTitle = aPhotoTitle;
		this.photoLink = aPhotolLink;
		this.photoThumbnail = aPhotoThumbnail;
	}
}
