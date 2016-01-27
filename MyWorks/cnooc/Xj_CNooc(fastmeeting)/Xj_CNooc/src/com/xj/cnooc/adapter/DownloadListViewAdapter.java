package com.xj.cnooc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xj.cnooc.model.AttachmentBean;
import com.xj.cnooc.utils.FileUtils;
import com.xj.cnooc.view.R;

public class DownloadListViewAdapter extends BaseAdapter {
	private List<AttachmentBean> att_list;
	private Context context;

	public DownloadListViewAdapter(List<AttachmentBean> att_list,
			Context context) {
		this.att_list = att_list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return att_list.size();
	}

	@Override
	public Object getItem(int position) {
		return att_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final AttachmentBean bean = att_list.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.download_listitem_view, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_fileTitle = (TextView) convertView
					.findViewById(R.id.tv_fileTitle);
			viewHolder.ib_download = (ImageButton) convertView
					.findViewById(R.id.ib_download);
			viewHolder.downloadProgress = (ProgressBar) convertView
					.findViewById(R.id.downloadProgress);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final FileUtils fileUtils = new FileUtils(viewHolder.downloadProgress,
				context, viewHolder.ib_download);
		viewHolder.tv_fileTitle.setText(bean.getName());

		viewHolder.ib_download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 下载文件
				fileUtils.asyncDownloadFile(bean.getPath(), bean.getName(),
						bean.getType());
			}
		});

		return convertView;
	}

	class ViewHolder {
		private TextView tv_fileTitle;
		private ImageButton ib_download;
		private ProgressBar downloadProgress;
	}

}
