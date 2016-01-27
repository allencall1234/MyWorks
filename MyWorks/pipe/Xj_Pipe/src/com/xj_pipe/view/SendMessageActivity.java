package com.xj_pipe.view;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;

/**
 * 发送消息界面
 * @author Administrator
 *
 */
public class SendMessageActivity extends BaseActivity {
	private TextView send_message_person, alert_linkman,send_message;
	private EditText send_msg_content;
	String receiver = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_send_message);
		setCenterTitle("发消息");
		hideLeftLogo();

		init();
	}

	private void init(){
		send_message_person = (TextView) findViewById(R.id.send_message_person);
		Intent intent = getIntent();
		String str_send_message_person = intent.getStringExtra("person");
		send_message_person.setText(str_send_message_person.substring(0, str_send_message_person.length()-1));

		receiver = intent.getStringExtra("receiver");

		alert_linkman = (TextView) findViewById(R.id.alert_linkman);
		alert_linkman.setOnClickListener(this);

		send_message = (TextView) findViewById(R.id.send_message);
		send_msg_content = (EditText) findViewById(R.id.send_msg_content);
		send_message.setOnClickListener(this);
	}

	@Override
	public void onBackLisenter() {
		super.onBackLisenter();
		finish();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.alert_linkman:
			finish();
			break;

		case R.id.send_message:
			String msg_content = send_msg_content.getText().toString();
			if(TextUtils.isEmpty(send_msg_content.getText().toString())){
				showToast("消息内容不能为空！");
			}else{
				SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());//获取当前时间
				String createtime = formatter.format(curDate);
				UserHttp.sendMessage(
						MyApp.userInfo.getStaffId(), 
						createtime, 
						receiver.substring(0, receiver.length()-1), 
						msg_content, 
						0, 
						new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								// TODO Auto-generated method stub
								JSONObject jsonObject;
								try {
									jsonObject = new JSONObject(_result);
									boolean flag = jsonObject.getBoolean("status");
									if(flag == true){
										showToast("发送成功！");
										finish();
									}else{
										showToast("发送失败！");
									}
								} catch (JSONException e) {
									 e.printStackTrace();
								}
							}

							@Override
							public void HttpFail(int ErrCode) {
								showToast("网络连接失败，请检查您的网络！");
							}
						});
			}

			break;
		default:
			break;
		}
	}
}
