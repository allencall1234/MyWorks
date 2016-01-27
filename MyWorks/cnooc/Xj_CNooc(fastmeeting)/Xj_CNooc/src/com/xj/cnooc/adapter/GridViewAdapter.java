package com.xj.cnooc.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xj.cnooc.model.ColumnImageBean;
import com.xj.cnooc.view.R;

/**
 * 
 * @author Administrator 主界面的功能菜单Adapter
 *
 */
public class GridViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<ColumnImageBean> list;

	public GridViewAdapter(Context mContext, List<ColumnImageBean> list) {
		super();
		this.mContext = mContext;
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

	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item_view, null);

			viewHolder = new ViewHolder();
			viewHolder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			viewHolder.tv_num = (TextView) convertView
					.findViewById(R.id.tv_num);
			viewHolder.iv_main_view = (ImageView) convertView
					.findViewById(R.id.iv_main_view);
			viewHolder.layout_count = (RelativeLayout) convertView
					.findViewById(R.id.layout_count);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ColumnImageBean bean = list.get(position);

		viewHolder.tv_title.setText(bean.getTextStr());
		viewHolder.iv_main_view.setImageResource(bean.getImageUrl());

		Log.i("msg", "bean.getindex = " + bean.getIndex() + ",position = "
				+ position);
		// 设置提醒消息数量
		if (bean.getIndex() > 0) {
			viewHolder.layout_count.setVisibility(View.VISIBLE);
			viewHolder.tv_num.setText(String.valueOf(bean.getIndex()));
		} else {
			viewHolder.layout_count.setVisibility(View.GONE);
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_title;// 菜单标题
		TextView tv_num; // 消息数目
		ImageView iv_main_view;// 图片
		RelativeLayout layout_count;
	}

}
