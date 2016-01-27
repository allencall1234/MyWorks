package com.xj_pipe.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj_pipe.bean.MainNumBean;
import com.xj_pipe.view.R;
/**
 * 主菜单界面菜单适配器
 * @author qinfan
 *
 * 2015-12-14
 */
public class MenuGridViewAdapter extends ArrayAdapter<String> {
	//菜单图标
	int iconId[] = { R.drawable.lixingxunjian_selector,
			R.drawable.yingjixunjian_selector,
			R.drawable.shigubaoxiu_selector,
			R.drawable.tufashijian_selector,
			R.drawable.shigongshangbao_selector,
			R.drawable.xiaoxi_selector };

	String[] itemData;
	MainNumBean mainNumBean;

	public MenuGridViewAdapter(Context context, int resource, String[] objects,MainNumBean mainNumBean) {
		super(context, resource, objects);
		itemData = objects;
		this.mainNumBean = mainNumBean;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(getContext(),
					R.layout.gridview_item_detail, null);

			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_menu_title);
			holder.tvCount = (TextView) convertView
					.findViewById(R.id.tv_menu_count);
			holder.ivImg = (ImageView) convertView
					.findViewById(R.id.iv_menu_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitle.setText(itemData[position]);
		holder.ivImg.setBackgroundResource(iconId[position]);

		if (!TextUtils.isEmpty(mainNumBean.getLxCount())) {
			int lxCount = Integer.parseInt(mainNumBean.getLxCount());
			if (lxCount > 0) {
				if (position == 0) {
					holder.tvCount.setVisibility(View.VISIBLE);
					holder.tvCount.setText("" + lxCount);
				}
			}
		}

		if (!TextUtils.isEmpty(mainNumBean.getYjCount())) {
			int yjCount = Integer.parseInt(mainNumBean.getYjCount());
			if (yjCount > 0) {
				if (position == 1) {
					holder.tvCount.setVisibility(View.VISIBLE);
					holder.tvCount.setText("" + yjCount);
				}
			}
		}

		if (!TextUtils.isEmpty(mainNumBean.getMsgCount())) {
			int msgCount = Integer.parseInt(mainNumBean.getMsgCount());
			if (msgCount > 0) {
				if (position == 5) {
					holder.tvCount.setVisibility(View.VISIBLE);
					holder.tvCount.setText("" + msgCount);
				}
			}
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvTitle;//菜单项标题
		TextView tvCount;//提醒数目
		ImageView ivImg;//菜单项图标
	}
}