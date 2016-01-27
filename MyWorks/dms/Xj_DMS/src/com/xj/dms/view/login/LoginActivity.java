package com.xj.dms.view.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.dms.common.CircleImageView;
import com.xj.dms.common.CustomSwitch;
import com.xj.dms.common.CustomSwitch.OnChangeListener;
import com.xj.dms.common.MyApp;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.UserHttp;
import com.xj.dms.model.UserBean;
import com.xj.dms.view.MainActivity;
import com.xj.dms.view.MainMenuActivity;
import com.xj.dms.view.R;

public class LoginActivity extends Activity implements OnClickListener,OnChangeListener{
	private EditText et_user_password,et_user_name;
	private LinearLayout ll_rember_pwd;
	private ImageButton ib_hidden_pwd;
	private int flag=0;
	private TextView tv_login,tv_back;
	private CustomSwitch switch_rember_pwd;
	private SharedPreferences preferences=null;
	private boolean isRemberPwd;
	private String username,pwd;
	private CircleImageView circleImageView;
	private ProgressDialog progress;

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				progress=ProgressDialog.show(LoginActivity.this,"", "正在登录中……");
				UserHttp.Login(et_user_name.getText().toString(), et_user_password.getText().toString(), new HttpDataCallBack() {

					@Override
					public void HttpSuccess(String _result) {
						try {
							progress.dismiss();
							JSONObject jsonObject = new JSONObject(_result);
							String status = jsonObject.getString("status");
							Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
							MyApp.userBean = new UserBean();
							MyApp.userBean.setName(jsonObject.getString("name"));
							MyApp.userBean.setPlatform("所属装置："+jsonObject.getString("platform"));
							MyApp.userBean.setRole(jsonObject.getString("role"));
							MyApp.userBean.setLoginDate(jsonObject.getString("loginDate"));
							MyApp.userBean.setUserId(jsonObject.getInt("userid"));
							if(status.equals("true")){
								if(isRemberPwd){
									preferences.edit().putString("username", et_user_name.getText().toString()).commit();
									preferences.edit().putString("pwd", et_user_password.getText().toString()).commit();
								} else {
									preferences.edit().putString("username", "").commit();
									preferences.edit().putString("pwd", "").commit();
								}
								MyApp.isLogin = true;
								MyApp.userBean.setUsername(et_user_name.getText().toString());
								Intent intent=new Intent();
								intent.setClass(LoginActivity.this, MainMenuActivity.class);
								LoginActivity.this.startActivity(intent);
								LoginActivity.this.finish();
//								MainActivity.instance.finish();
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

			default:
				break;
			}
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init(){
		circleImageView = (CircleImageView) findViewById(R.id.circleImageView);

		preferences=getSharedPreferences("rember_pwd", MODE_PRIVATE);
		username = preferences.getString("username", "");
		pwd = preferences.getString("pwd", "");
		isRemberPwd = preferences.getBoolean("isrember", true);

		if(username == null || username.equals("")){
			circleImageView.setImageResource(R.drawable.def_icon);
		}else{
			circleImageView.setImageResource(R.drawable.login_head_img);
		}

		et_user_password = (EditText) findViewById(R.id.et_user_password);
		et_user_password.setText(pwd);
		et_user_name = (EditText) findViewById(R.id.et_user_name);
		et_user_name.setText(username);

		ll_rember_pwd = (LinearLayout) findViewById(R.id.ll_rember_pwd);
		et_user_password.setOnFocusChangeListener(new OnFocusChangeListener() {  
			@Override  
			public void onFocusChange(View v, boolean hasFocus) {  
				if(hasFocus) {
					// 此处为得到焦点时的处理内容
					ll_rember_pwd.setVisibility(View.VISIBLE);
				} else {
					// 此处为失去焦点时的处理内容
				}
			}
		});
		et_user_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


		ib_hidden_pwd = (ImageButton) findViewById(R.id.ib_hidden_pwd);
		ib_hidden_pwd.setOnClickListener(this);

		tv_login = (TextView) findViewById(R.id.tv_login);
		tv_login.setOnClickListener(this);

		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(this);

		switch_rember_pwd = (CustomSwitch) findViewById(R.id.swicth_rember_pwd);
		switch_rember_pwd.setChecked(isRemberPwd);
		switch_rember_pwd.setOnChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_hidden_pwd:
			//显示密码
			if(flag == 0){
				ib_hidden_pwd.setBackgroundResource(R.drawable.show_pass);
				et_user_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);  
				flag = 1;
			}
			else if(flag == 1){
				ib_hidden_pwd.setBackgroundResource(R.drawable.hidden_pass);
				et_user_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);  
				flag = 0;
			}
			break;

		case R.id.tv_login:
			if(et_user_name.getText().toString()==null || et_user_name.getText().toString().equals("")){
				Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			if(et_user_password.getText().toString()==null || et_user_password.getText().toString().equals("")){
				Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			handler.sendEmptyMessage(0);

			break;

		case R.id.tv_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onChanged(CustomSwitch mSwitch, boolean checkState) {
		switch (mSwitch.getId()) {
		case R.id.swicth_rember_pwd:
			setRemberPwd(checkState);
			break;

		default:
			break;
		}
	}

	//设置记住密码
	public void setRemberPwd(boolean enable){
		isRemberPwd = enable;
		preferences.edit().putBoolean("isrember", enable).commit();
	}

	//是否记住密码
	public boolean isRemberPwd(){
		return isRemberPwd;
	}

}
