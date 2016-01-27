package com.xj.dms.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xj.dms.common.BaseActivity;
import com.xj.dms.common.MyApp;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.UserHttp;
/**
 * 修改用户资料界面
 * @author qinfan
 *
 * 2015-11-6
 */
public class AlterUserInfoActivity extends BaseActivity implements OnClickListener{
	private TextView tv_back;//底部箭头返回
	private EditText et_info_username,//用户名
					 et_info_pwd,//旧密码
					 et_info_new_pwd;//新密码
	private Button btn_alter;//确认修改按钮
	private String pwd,new_pwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_alter_user_info);
		hidebtn_more();//隐藏更多按钮
		setCenterTitle("用户资料修改");
		init();
	}
	
	private void init(){
		/**初始化控件*/
		tv_back = (TextView) findViewById(R.id.tv_back);
		et_info_username = (EditText) findViewById(R.id.et_info_username);
		et_info_pwd = (EditText) findViewById(R.id.et_info_pwd);
		et_info_new_pwd = (EditText) findViewById(R.id.et_info_new_pwd);
		btn_alter = (Button) findViewById(R.id.btn_alter);
		
		et_info_username.setEnabled(false);
		et_info_username.setText(MyApp.userBean.getUsername());
		
		getbtn_back().setOnClickListener(this);
		tv_back.setOnClickListener(this);
		btn_alter.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.tv_back:
			finish();
			break;
			
		case R.id.btn_alter:
			pwd = et_info_pwd.getText().toString();
			new_pwd = et_info_new_pwd.getText().toString();
			if(pwd == null || pwd.equals("")){
				showToast("原密码不能为空");
				return;
			}
			if(new_pwd == null || new_pwd.equals("")){
				showToast("新密码不能为空");
				return;
			}
			UserHttp.alertUserPwd(
					pwd,
					new_pwd, 
					MyApp.userBean.getUserId(), 
					new HttpDataCallBack() {
						@Override
						public void HttpSuccess(String _result) {
							try {
								JSONObject jsonObject = new JSONObject(_result);
								String status = jsonObject.getString("status");
								showToast(jsonObject.getString("message"));
								//如果密码修改成功则更改文件中的密码，用户下次登录可以直接用新密码
								if(status.equals("true")){
									finish();
									SharedPreferences preferences = 
											getSharedPreferences("rember_pwd", MODE_PRIVATE);
									preferences.edit().putString("pwd", new_pwd).commit();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						
						@Override
						public void HttpFail(int ErrCode) {
							
						}
					});
			break;
		}
	}
}
