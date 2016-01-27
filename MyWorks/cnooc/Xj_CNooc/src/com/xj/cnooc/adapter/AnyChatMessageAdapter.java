package com.xj.cnooc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xj.cnooc.common.CircleImageView;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.https.HttpURL;
import com.xj.cnooc.model.AnyChatMessage;
import com.xj.cnooc.utils.ImageLoadOptions;
import com.xj.cnooc.view.R;

/**
 * 会话适配器
 * 
 * @author zhulanting
 */
public class AnyChatMessageAdapter extends ArrayAdapter<AnyChatMessage> {

	private LayoutInflater inflater;
	private List<AnyChatMessage> mData;
	private Context mContext;

	public AnyChatMessageAdapter(Context context, int textViewResourceId,
			List<AnyChatMessage> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		this.mContext = context;
		mData = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AnyChatMessage item = mData.get(position);

		convertView = createViewByType(item.getType());
		if (item.getType() == 0) {
			TextView sMessage = ViewHolder
					.get(convertView, R.id.system_message);
			sMessage.setText(item.getMessage().toString());
			sMessage.setSelected(true);
		} else if (item.getType() == 1) {
			TextView mMessage = ViewHolder.get(convertView, R.id.tv_message);
			CircleImageView mLogo = ViewHolder.get(convertView, R.id.iv_avatar);
			mMessage.setText(item.getMessage().toString() + "");
			ImageLoader.getInstance().displayImage(
					HttpURL.SERVICE_URL + MyApp.globelUser.getPhoto(), mLogo,
					ImageLoadOptions.getOptions());
		} else {
			TextView mMessage = ViewHolder.get(convertView, R.id.tv_message);
			CircleImageView mLogo = ViewHolder.get(convertView, R.id.iv_avatar);
			TextView mUserName = ViewHolder.get(convertView, R.id.tv_name);
			mMessage.setText(item.getMessage().toString() + "");
			mLogo.setImageResource(R.drawable.head_img);
			mUserName.setText(item.getUserName() + "");
			mLogo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
		}
		// 填充数据
		// String avatar = item.getPhoto();
		// if(avatar!=null&& !avatar.equals("")){
		// ImageLoader.getInstance().displayImage(avatar, iv_recent_avatar,
		// ImageLoadOptions.getOptions());
		// }else{
		// iv_recent_avatar.setImageResource(R.drawable.logo6);
		// }

		return convertView;
	}

	private View createViewByType(int type) {
		// TODO Auto-generated method stub
		if (type == 0) {
			return inflater.inflate(R.layout.anychat_system_message, null);
		} else if (type == 1) {
			return inflater.inflate(R.layout.anychat_sent_message, null);
		} else {
			return inflater.inflate(R.layout.anychat_received_message, null);
		}
	}

}
