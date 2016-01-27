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

public class SupportListAdapter extends BaseAdapter {
	private Context context;// 运行上下文
	private LayoutInflater listContainer;
	private List<Map<String, Object>> listItems;

	public SupportListAdapter(Context context,
			List<Map<String, Object>> listItems) {
		// TODO Auto-generated constructor stub
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (listItems == null) {
			return -1;
		}
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
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
			convertView = listContainer.inflate(R.layout.support_list_detail,
					null);
			// 获取控件对象
			viewHolder.support_title = (TextView) convertView
					.findViewById(R.id.fault_support_title);
			viewHolder.support_content = (TextView) convertView
					.findViewById(R.id.fault_support_content);
			viewHolder.support_date = (TextView) convertView
					.findViewById(R.id.fault_support_date);

			// 设置控件集到convertView
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 设置文字和图片
		viewHolder.support_title.setText((String) listItems.get(position).get(
				"support_title"));
		viewHolder.support_content.setText((String) listItems.get(position)
				.get("support_content"));
		viewHolder.support_date.setText((String) listItems.get(position).get(
				"support_date"));
		return convertView;
	}

	// 视图容器
	public final class ViewHolder {
		// 自定义控件集合
		public TextView support_title;
		public TextView support_content;
		public TextView support_date;
	}

}
