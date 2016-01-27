package com.xj.cnooc.view.bbs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.cnooc.adapter.BBSListViewAdapter;
import com.xj.cnooc.biz.UserHttpBiz;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.common.MyUtils;
import com.xj.cnooc.https.HttpDataCallBack;
import com.xj.cnooc.model.BBSBean;
import com.xj.cnooc.view.R;

public class BBSFragment extends Fragment {
	private ListView lv_post_list;
	private TextView tv_empty;
	private BBSListViewAdapter adapter;
	private View view;
	private int loginId;

	private List<BBSBean> bbs_list;

	private ProgressDialog dialog;

	private int request_code;
	private int itemId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.bbs_public_view, container, false);
		initView();

		setRetainInstance(true);
		return view;
	}

	private void initView() {
		bbs_list = new ArrayList<BBSBean>();
		lv_post_list = (ListView) view.findViewById(R.id.lv_post_list);
		tv_empty = (TextView) view.findViewById(R.id.tv_empty);

		loginId = MyApp.globelUser.getAccountid();
		lv_post_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						PostListViewActivity.class);
				itemId = position;
				BBSBean bbs_bean = bbs_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable("BBSBean", bbs_bean);
				intent.putExtras(bundle);
				startActivityForResult(intent, 0x08);
			}
		});
		initListViewData();
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (bbs_list.size() == 0) {
					dialog = ProgressDialog.show(getActivity(), "",
							"正在加载中......");
					dialog.setCancelable(true);
					// 从网络获取数据
					UserHttpBiz.queryBBS(loginId, new HttpDataCallBack() {
						@Override
						public void HttpSuccess(String _result) {
							dialog.dismiss();
							adapter = new BBSListViewAdapter(getActivity(),
									getBBSList(_result), request_code);
							if (adapter.getCount() <= 0) {
								lv_post_list.setVisibility(View.GONE);
								tv_empty.setVisibility(View.VISIBLE);
							}
							lv_post_list.setAdapter(adapter);
						}

						@Override
						public void HttpFail(int ErrCode) {
							dialog.dismiss();
							Toast.makeText(getActivity(), "网络请求失败,请检查您的网络!",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
				break;

			default:
				break;
			}
			return false;
		}
	});

	// 初始化数据
	private void initListViewData() {
		handler.sendEmptyMessage(0);
	}

	// 将解析的json数据加入到集合中
	private List<BBSBean> getBBSList(String _result) {
		try {
			JSONObject jsonObject = new JSONObject(_result);
			if (jsonObject.getBoolean("status") == true) {
				JSONArray jsonArray = jsonObject.getJSONArray("listBbsModule");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject bbs_obj = jsonArray.getJSONObject(i);
					BBSBean bean = (BBSBean) MyUtils.putJsonToObject(bbs_obj,
							"com.xj.cnooc.model.BBSBean");
					bbs_list.add(bean);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		MyApp.bbsTitle = bbs_list;
		return bbs_list;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0x08) {
			if (resultCode == 0x07) {
				request_code = 0x07;
				adapter = new BBSListViewAdapter(getActivity(), bbs_list,
						request_code);
				lv_post_list.setAdapter(adapter);
			}
		}
	}
}
