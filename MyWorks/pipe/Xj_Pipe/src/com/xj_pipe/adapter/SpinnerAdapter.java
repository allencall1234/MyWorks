package com.xj_pipe.adapter;

import com.xj_pipe.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class SpinnerAdapter extends ArrayAdapter<String> {

	private Context mContext;

	public SpinnerAdapter(Context context, String[] arrays) {
		super(context, android.R.layout.simple_spinner_dropdown_item, arrays);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		// 修改Spinner展开后的字体颜色
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.custom_spinner_layout,
					parent, false);
		}

		return super.getDropDownView(position, convertView, parent);
	}

	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// // 修改Spinner选择后结果的字体颜色
	// if (convertView == null) {
	// LayoutInflater inflater = LayoutInflater.from(mContext);
	// convertView = inflater.inflate(
	// android.R.layout.simple_spinner_item, parent, false);
	// }
	//
	// // 此处text1是Spinner默认的用来显示文字的TextView
	// TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
	// tv.setText(mArrays[position]);
	// tv.setTextSize(18f);
	// tv.setTextColor(Color.BLUE);
	// return convertView;
	// }
}
