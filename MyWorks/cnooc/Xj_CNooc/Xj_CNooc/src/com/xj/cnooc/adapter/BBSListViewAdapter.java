package com.xj.cnooc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xj.cnooc.model.BBSBean;
import com.xj.cnooc.view.R;

/**
 * 
 * @author Administrator
 * 各个技术讨论区的Adapter
 *
 */
public class BBSListViewAdapter extends BaseAdapter
{
	private Context context;
	private List<BBSBean> list;
	private LayoutInflater listContainer; 
	private int request_code;
	
	public BBSListViewAdapter(Context context, List<BBSBean> list, int request_code) 
	{
		super();
		this.context = context;
		listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文  
		this.list = list;
		this.request_code = request_code;
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
		BBSBean bean = list.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) 
		{
			//获取list_item布局文件的视图   
            convertView = listContainer.inflate(R.layout.post_item_view, null);
			
			viewHolder = new  ViewHolder();
			viewHolder.tv_discussion_name = (TextView) convertView.findViewById(R.id.tv_discussion_name);// 技术讨论区名称
			viewHolder.tv_moderator = (TextView) convertView.findViewById(R.id.tv_moderator);// 版主
			viewHolder.tv_discussion_introduce = (TextView) convertView.findViewById(R.id.tv_discussion_introduce);// 技术讨论区介绍
			viewHolder.tv_post_time = (TextView) convertView.findViewById(R.id.tv_post_time);// 发帖时间
			viewHolder.tv_post_account = (TextView) convertView.findViewById(R.id.tv_post_account);// 发帖数量
			viewHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			viewHolder.layout_count = (RelativeLayout) convertView.findViewById(R.id.layout_count);
			
			convertView.setTag(viewHolder);
		}
		
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tv_discussion_name.setText(bean.getName());// 设置版块名称
		viewHolder.tv_moderator.setText("版主：" + bean.getModeratorname());// 设置版主
		viewHolder.tv_discussion_introduce.setText(bean.getDescription());// 设置讨论区介绍
		viewHolder.tv_post_time.setText("发布时间：" + bean.getCtime());// 设置发帖时间
		viewHolder.tv_post_account.setText("贴数：(" + bean.getCount() + ")条");// 设置发帖的数量
		
//		if (bean.getOneModuleTotal() > 0) 
//		{
//			viewHolder.layout_count.setVisibility(View.VISIBLE);
//			viewHolder.tv_num.setText(String.valueOf(bean.getOneModuleTotal()));// 设置未读数量
//		}
		
		for (int i = 0; i < list.size(); i++) 
		{
			if (position == i && bean.getOneModuleTotal() > 0) 
			{
				if (request_code != 0x07) 
				{
					viewHolder.layout_count.setVisibility(View.VISIBLE);
					viewHolder.tv_num.setText(String.valueOf(bean.getOneModuleTotal()));// 设置未读数量
				}
				else if (request_code == 0x07) 
				{
					viewHolder.layout_count.setVisibility(View.GONE);
				}
			}
		}
		return convertView;
	}
	
	class ViewHolder
	{
		TextView tv_discussion_name;// 讨论区名称
		TextView tv_moderator;// 版主
		TextView tv_discussion_introduce;// 讨论区介绍
		TextView tv_post_time;// 发布时间
		TextView tv_post_account;// 发布数量
		TextView tv_num;// 未读数量
		RelativeLayout layout_count;
	}

}
