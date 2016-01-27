package com.xj.cnooc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xj.cnooc.view.R;

/**
 * 电网故障与评估已回复界面专家评议ListView适配器
 * 
 * @author qinfan 2015-9-18
 */
public class FinishPingYiAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> listItems;
	private LayoutInflater listContainer;

	public FinishPingYiAdapter(Context context,
			List<Map<String, Object>> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		if (listItems == null) {
			return -1;
		}
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.fault_detail_pingyi,
					null);
			// 获取控件对象
			viewHolder.pingyi_name = (TextView) convertView
					.findViewById(R.id.tv_pingyi_name);
			viewHolder.pingyi_content = (TextView) convertView
					.findViewById(R.id.tv_pingyi_content);
			viewHolder.pingyi_date = (TextView) convertView
					.findViewById(R.id.tv_pingyi_date);

			// 设置控件集到convertView
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 设置文字和图片
		viewHolder.pingyi_name.setText((String) listItems.get(position).get(
				"pingyi_name"));
		viewHolder.pingyi_content.setText((String) listItems.get(position).get(
				"pingyi_content"));
		viewHolder.pingyi_date.setText((String) listItems.get(position).get(
				"pingyi_date"));
		return convertView;
	}

	// 视图容器
	public final class ViewHolder {
		// 自定义控件集合
		public TextView pingyi_name;
		public TextView pingyi_content;
		public TextView pingyi_date;
	}

}
