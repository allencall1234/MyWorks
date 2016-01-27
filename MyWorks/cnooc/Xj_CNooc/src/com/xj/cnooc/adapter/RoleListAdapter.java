package com.xj.cnooc.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.https.HttpURL;
import com.xj.cnooc.model.RoleInfo;
import com.xj.cnooc.utils.ImageLoadOptions;
import com.xj.cnooc.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoleListAdapter extends BaseAdapter {
	private Context mContext;
	private List<RoleInfo> mRoleList;

	public RoleListAdapter(Context context, List<RoleInfo> roleInfos) {
		super();
		mContext = context;
		mRoleList = roleInfos;
	}

	@Override
	public int getCount() {
		return mRoleList.size();
	}

	@Override
	public Object getItem(int position) {
		return mRoleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.anychat_role_list_cell, null);
			holder.mname = (TextView) convertView.findViewById(R.id.mname);
			holder.mRoleID = (TextView) convertView.findViewById(R.id.mRoleID);
			holder.mRoleIcon = (ImageView) convertView
					.findViewById(R.id.roleHeaderImg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		RoleInfo info = mRoleList.get(position);
		holder.mname.setText("" + info.getName());
		holder.mname.setSelected(true);
		holder.mRoleID.setText("" + info.getUserID());
		if (position == 0) {	//默认排序时，自己在第一位，所以凭此加载头像
			ImageLoader.getInstance().displayImage(
					HttpURL.SERVICE_URL
							+ MyApp.globelUser.getPhoto(),
					holder.mRoleIcon, ImageLoadOptions.getOptions());
		} else {
			holder.mRoleIcon.setImageResource(info.getRoleIconID());
		}
		return convertView;
	}

	public class ViewHolder {
		TextView mname;
		TextView mRoleID;
		ImageView mRoleIcon;
	}
}
