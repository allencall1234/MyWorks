package com.xj.dms.view.knowledgebase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.widget.BaseAdapter;

import com.xj.dms.adapter.CommonAdapter;
import com.xj.dms.adapter.ViewHolder;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.HttpJsonUtils;
import com.xj.dms.https.UserHttp;
import com.xj.dms.model.KnowledgeBaseBean;
import com.xj.dms.view.R;
import com.xj.dms.view.schedule.SubActivity;

/**
 * 知识库
 * 
 * @author zlt
 */
public class KnowledgeBaseActivity extends SubActivity {
	private int totalNum = -1;;// 从数据库中获取的数据总数
	private int num = 10;// 每页加载的数据数量

	private List<KnowledgeBaseBean> knowledgeBaseList;

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_LIBRARY_LIST;
	}

	@Override
	public BaseAdapter getAdapter() {
		CommonAdapter<KnowledgeBaseBean> adapter = new CommonAdapter<KnowledgeBaseBean>(
				this, knowledgeBaseList, R.layout.schedule_library_item) {
			@Override
			public void convert(ViewHolder viewHolder, KnowledgeBaseBean item) {
				viewHolder.setText(R.id.tv_knowledgeTitle, item.getTitle());
				viewHolder.setText(R.id.tv_content, item.getContext());
				viewHolder.setText(R.id.tv_type, "所属装置 : " + item.getType());
			}
		};
		return adapter;
	}

	@Override
	public int getTotalNumber() {
		return totalNum;
	}

	@Override
	public void initListData(final int page) {

		if (knowledgeBaseList == null) {
			knowledgeBaseList = new ArrayList<KnowledgeBaseBean>();
		}
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				UserHttp.knowledgeBaseQuery(page, num, "",
						new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								getKnowledgeBaseListData(_result);
								mHandler.obtainMessage(MESSAGE_SUCCESS)
										.sendToTarget();
							}

							@Override
							public void HttpFail(int ErrCode) {
								mHandler.obtainMessage(MESSAGE_FAILED)
										.sendToTarget();
							}
						});
			}
		});

	}

	// 获取数据并解析
	private List<KnowledgeBaseBean> getKnowledgeBaseListData(String result) {
		try {
			JSONObject object = new JSONObject(result);
			totalNum = object.getInt("total");
			JSONArray array = object.getJSONArray("knowledgeList");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				KnowledgeBaseBean bean = (KnowledgeBaseBean) HttpJsonUtils
						.putJsonToObject(object2,
								"com.xj.dms.model.KnowledgeBaseBean");

				knowledgeBaseList.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return knowledgeBaseList;

	}

	@Override
	public void onListViewItemClick(int position) {

		Intent intent = new Intent(KnowledgeBaseActivity.this,
				KnowledgeBaseDetailsActivity.class);
		KnowledgeBaseBean knowledgeBase_bean = knowledgeBaseList
				.get(position - 2);
		intent.putExtra("KnowledgeBaseBean", knowledgeBase_bean);

		startActivity(intent);
	}
}
