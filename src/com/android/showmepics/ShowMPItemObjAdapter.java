package com.android.showmepics;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Ciro Rizzo
 *
 * This is the class to achieve from the ArrayList of Pictures Data collected in the ShowMPListItemObj
 * The Custom Adapter manage the right formatting of the single converted View in the parent group 
 * inflating the data in every single component View 
 */

public class ShowMPItemObjAdapter extends ArrayAdapter<ShowMPItemObj> {

	public ShowMPItemObjAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	public ShowMPItemObjAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	public ShowMPItemObjAdapter(Context context, int resource,
			ShowMPItemObj[] objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public ShowMPItemObjAdapter(Context context, int resource,
			List<ShowMPItemObj> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public ShowMPItemObjAdapter(Context context, int resource,
			int textViewResourceId, ShowMPItemObj[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public ShowMPItemObjAdapter(Context context, int resource,
			int textViewResourceId, List<ShowMPItemObj> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		convertView = inflater.inflate(R.layout.item_of_list, null);
		
		ImageView imgVwThumbnail = (ImageView) convertView.findViewById(R.id.imgVwThumbnail);
		TextView txtVwTitleItem = (TextView) convertView.findViewById(R.id.txtVwTitleItem);
		TextView txtVwSubTitle_01 = (TextView) convertView.findViewById(R.id.txtVwSubTitle_01);
		
		ShowMPItemObj mShowMPItemObj = getItem(position);
		
		imgVwThumbnail.setImageBitmap(mShowMPItemObj.photoThumbnail);
		txtVwTitleItem.setText(mShowMPItemObj.photoTitle);
		txtVwSubTitle_01.setText(mShowMPItemObj.photoLink);
		
		return convertView;
	}
}
