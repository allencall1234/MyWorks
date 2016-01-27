package com.xj_pipe.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.bean.MessageBean;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;

/**
 * 消息详情界面
 * @author Administrator
 *
 */
public class MessageDetailActivity extends BaseActivity {
	private TextView send_name,time,msg_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_message_detail);
		setCenterTitle("消息详情");
		hideLeftLogo();
		initViews();
	}


	private void initViews(){
		Intent intent = getIntent();
		MessageBean messageBean = intent.getParcelableExtra("messageBeans");

		send_name = (TextView) findViewById(R.id.send_name);
		time = (TextView) findViewById(R.id.time);
		msg_content = (TextView) findViewById(R.id.msg_content);

		send_name.setText(messageBean.getCreaterName());
		time.setText(messageBean.getCreatetime());
		msg_content.setText(messageBean.getContent());

		UserHttp.updateMessageState(messageBean.getId(), 1, new HttpDataCallBack() {
			
			@Override
			public void HttpSuccess(String _result) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(_result);
					jsonObject.getBoolean("result");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void HttpFail(int ErrCode) {
				showToast("网络连接失败，请检查您的网络！");
			}
		});

	}

	@Override
	public void onBackLisenter() {
		super.onBackLisenter();
		finish();
	}

}
