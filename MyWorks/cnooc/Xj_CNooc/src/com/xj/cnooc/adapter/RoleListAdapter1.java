package com.xj.cnooc.adapter;

import java.util.List;

import com.xj.cnooc.model.RoleInfo;
import com.xj.cnooc.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoleListAdapter1 extends ArrayAdapter<RoleInfo> {

	private int textViewResourceId;
	private Context mContext = null;

	public RoleListAdapter1(Context context, int textViewResourceId,
			List<RoleInfo> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.textViewResourceId = textViewResourceId;
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RoleInfo info = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater.inflate(textViewResourceId, null);
		TextView tv = (TextView) convertView.findViewById(R.id.text1);
		tv.setSelected(true);
		tv.setText(info.getName());
		ImageView imageView = (ImageView) convertView.findViewById(R.id.image1);
		if (info.isCheck()) {
			imageView.setVisibility(View.VISIBLE);
		}else {
			imageView.setVisibility(View.GONE);
		}
		return convertView;
	}

}
