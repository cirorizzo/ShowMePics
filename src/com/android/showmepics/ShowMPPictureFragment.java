package com.android.showmepics;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ShowMPPictureFragment extends Fragment {
	private Bitmap mImgBMP = null;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getArguments().containsKey(ShowMPConstants.KEY_ARG_IMG)) {
			byte[] byteArray = getArguments().getByteArray(ShowMPConstants.KEY_ARG_IMG);
			
			mImgBMP = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		}
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_picture, container,
				false);
		
		ImageView imgVwEnlargedPic = (ImageView) rootView.findViewById(R.id.imgVwEnlargedPic);
		imgVwEnlargedPic.setImageBitmap(mImgBMP);
		return rootView;
	}

}
