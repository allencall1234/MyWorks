package com.xj.cnooc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xj.cnooc.https.HttpURL;
import com.xj.cnooc.model.ReplyPostBean;
import com.xj.cnooc.utils.ImageLoadOptions;
import com.xj.cnooc.view.R;

public class CommentedPostListViewAdapter extends BaseAdapter {
	private List<ReplyPostBean> list;
	private Context context;

	public CommentedPostListViewAdapter(List<ReplyPostBean> list,
			Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	public List<ReplyPostBean> getReplyList() {
		return list;
	}

	public void setReplyList(List<ReplyPostBean> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ReplyPostBean bean = list.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.post_detailes_item_view, null);

			viewHolder = new ViewHolder();
			viewHolder.iv_head_img = (ImageView) convertView
					.findViewById(R.id.iv_head_img);
			viewHolder.tv_replyer = (TextView) convertView
					.findViewById(R.id.tv_replyer);
			viewHolder.tv_reply_content = (TextView) convertView
					.findViewById(R.id.tv_reply_content);
			viewHolder.tv_reply_time = (TextView) convertView
					.findViewById(R.id.tv_reply_time);
			// viewHolder.tv_reply_account = (TextView)
			// convertView.findViewById(R.id.tv_reply_account);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tv_replyer.setText(bean.getReplyName());
		viewHolder.tv_reply_content.setText(bean.getContent());
		viewHolder.tv_reply_time.setText("评论时间:" + bean.getCreateformat());
		// viewHolder.tv_reply_account.setText("回复(" + bean.getReply_account() +
		// ")");

		ImageLoader.getInstance().displayImage(
				HttpURL.SERVICE_URL + bean.getReply_post_img(),
				viewHolder.iv_head_img, ImageLoadOptions.getOptions());

		return convertView;
	}

	class ViewHolder {
		ImageView iv_head_img;// 帖子图片
		TextView tv_replyer;// 回帖人
		TextView tv_reply_content;// 回帖内容
		TextView tv_reply_time;// 回帖时间
		// TextView tv_reply_account;// 回帖数量
	}
}
