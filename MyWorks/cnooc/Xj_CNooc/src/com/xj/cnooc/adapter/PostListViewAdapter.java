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
import com.xj.cnooc.model.PostBean;
import com.xj.cnooc.utils.ImageLoadOptions;
import com.xj.cnooc.view.R;

/**
 * 
 * @author Administrator
 * 各个版块要展示的帖子列表Adapter
 * 
 *
 */
public class PostListViewAdapter extends BaseAdapter
{
	private List<PostBean> list;
	private Context context;
	
	public PostListViewAdapter(List<PostBean> list, Context context) 
	{
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() 
	{
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		PostBean bean = list.get(position);
		
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.discuss_item_view, null);
			
			viewHolder = new ViewHolder();
			viewHolder.tv_post_title = (TextView) convertView.findViewById(R.id.tv_post_title);
			viewHolder.iv_head_img = (ImageView) convertView.findViewById(R.id.iv_head_img);
			viewHolder.tv_post_content = (TextView) convertView.findViewById(R.id.tv_post_content);
			viewHolder.tv_poster = (TextView) convertView.findViewById(R.id.tv_poster);
			viewHolder.tv_post_time = (TextView) convertView.findViewById(R.id.tv_post_time);
			viewHolder.tv_pinglun_account = (TextView) convertView.findViewById(R.id.tv_pinglun_account);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tv_post_title.setText(bean.getTitle());
		viewHolder.tv_post_content.setText(bean.getContent());
		viewHolder.tv_poster.setText("发帖人：" + bean.getBbsPostName());
		viewHolder.tv_post_time.setText("发帖时间：" + bean.getCtime());
		viewHolder.tv_pinglun_account.setText("评论：(" + bean.getAnswerCount() +")条");
		
	    ImageLoader.getInstance().displayImage(HttpURL.SERVICE_URL + bean.getPhoto(),viewHolder.iv_head_img, ImageLoadOptions.getOptions());

		return convertView;
	}
	
	class ViewHolder
	{
		ImageView iv_head_img;// 帖子图片
		TextView tv_post_title;// 帖子标题
		TextView tv_post_content;// 帖子内容
		TextView tv_poster;// 发帖人
		TextView tv_post_time;// 发帖时间
		TextView tv_pinglun_account;// 评论数目
	}

}
