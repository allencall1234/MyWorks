package com.xj_pipe.view;

import org.json.JSONObject;

import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.bean.UserBean;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.JsonUtils;
import com.xj_pipe.utils.ToastUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * 登录界面
 * @author Administrator
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener
{
	private EditText et_userName;
	private EditText et_userPass;
	
	private CheckBox checkBox;// 是否记住密码
	
	private Button btn_login;
	
	private ImageButton ib_show_hidden_pwd;// 显示隐藏密码
	
	private String username;
	private String password;
	private boolean isRememberPwd = false;// 设置记住密码状态标识
	public static SharedPreferences preferences = null;
	
	private ProgressDialog progressDialog = null;
	
	private boolean mHidden = false;// 设置是否隐藏密码标识
	
	private Handler handler = new Handler(new Handler.Callback() 
	{
		@Override
		public boolean handleMessage(Message msg) 
		{
			switch (msg.what)
			{
				case 0:
					progressDialog = ProgressDialog.show(LoginActivity.this,"", "正在登录中,请稍后……");
					progressDialog.setCancelable(true);
					progressDialog.setCanceledOnTouchOutside(false);
					UserHttp.Login(username, password, new HttpDataCallBack()
					{
						@Override
						public void HttpSuccess(String _result) 
						{
							progressDialog.dismiss();
							try 
							{
								JSONObject object = new JSONObject(_result);
								if (object.getBoolean("status") == true) 
								{
									JSONObject object2 = new JSONObject(object.getString("staff"));
									UserBean bean = (UserBean) JsonUtils.putJsonToObject(object2, "com.xj_pipe.bean.UserBean");
									MyApp.userInfo = bean;
									
									if (isRememberPwd)
									{
										preferences.edit().putString("username", et_userName.getText().toString().trim()).commit();
										preferences.edit().putString("password", et_userPass.getText().toString().trim()).commit();
									}
									else
									{
										preferences.edit().putString("username", "").commit();
										preferences.edit().putString("password", "").commit();
									}
									
									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									startActivity(intent);
									finish();
								}
								else
								{
									if (object.getString("message").equals("1"))
									{
										ToastUtils.ShowMessage(LoginActivity.this, "用户名不存在!");
									}
									else if (object.getString("message").equals("2")) 
									{
										ToastUtils.ShowMessage(LoginActivity.this, "密码错误!");
									}
									else if (object.getString("message").equals("3")) 
									{
										ToastUtils.ShowMessage(LoginActivity.this, "非巡检人员!");
									}
									else
									{
										ToastUtils.ShowMessage(LoginActivity.this, "登录异常!");
									}
								}
							} 
							catch (Exception e) 
							{
								e.printStackTrace();
							}
						}
						
						@Override
						public void HttpFail(int ErrCode) 
						{
							ToastUtils.ShowMessage(LoginActivity.this, "网络请求失败，请检查您的网络!");
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
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentLayout(R.layout.activity_login);
    	
    	hideTitleBanner();// 隐藏标题栏
    	
    	initView();
    }

	private void initView()
	{
		et_userName = (EditText) findViewById(R.id.et_userName);
		et_userPass = (EditText) findViewById(R.id.et_userPass);
		
		checkBox = (CheckBox) findViewById(R.id.checkbox);
		
		checkBox.setOnCheckedChangeListener(this);
		
		ib_show_hidden_pwd = (ImageButton) findViewById(R.id.ib_show_hidden_pwd);
		
		ib_show_hidden_pwd.setOnClickListener(this);
		
		preferences = getSharedPreferences("remember_pwd", MODE_PRIVATE);
		username = preferences.getString("username", "");
		password = preferences.getString("password", "");
		isRememberPwd = preferences.getBoolean("isRememberPwd", false);
		
		et_userName.setText(username);
		et_userPass.setText(password);
		
		if (isRememberPwd) 
		{
			checkBox.setChecked(true);
		}
		else
		{
			checkBox.setChecked(false);
		}
		
		btn_login = (Button) findViewById(R.id.btn_login);
		
		btn_login.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
			case R.id.btn_login:
				login();
				break;
	
			case R.id.ib_show_hidden_pwd:
				if (!mHidden) 
				{
					// 显示密码
					et_userPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					ib_show_hidden_pwd.setBackgroundResource(R.drawable.show_pass);
				} 
				else 
				{
					// 隐藏密码
					et_userPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
					ib_show_hidden_pwd.setBackgroundResource(R.drawable.hidden_pass);
				}
				mHidden = !mHidden;
				et_userPass.postInvalidate();
				break;
				
			default:
				break;
		}
	}

	private void login() 
	{
		username = et_userName.getText().toString().trim();
		password = et_userPass.getText().toString().trim();
		
		if (TextUtils.isEmpty(username))
		{
			ToastUtils.ShowMessage(this, "用户名不能为空!");
			return;
		}
		
		if (TextUtils.isEmpty(password))
		{
			ToastUtils.ShowMessage(this, "密码不能为空!");
			return;
		}
		
//		if (isRememberPwd)
//		{
//			preferences.edit().putString("username", et_userName.getText().toString().trim()).commit();
//			preferences.edit().putString("password", et_userPass.getText().toString().trim()).commit();
//		}
//		else
//		{
//			preferences.edit().putString("username", "").commit();
//			preferences.edit().putString("password", "").commit();
//		}
		
		handler.sendEmptyMessage(0);
		
//		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//		startActivity(intent);
//		finish();
		
	}

	@Override
	public void onCheckedChanged(CompoundButton btn, boolean isCheck) 
	{
		isRememberPwd = isCheck;
		preferences.edit().putBoolean("isRememberPwd", isCheck).commit();
	}
	
	@Override
	public void onMoreLisenter() {
		// 重写该方法并置空,防止在登录界面按菜单键会弹出主菜单
	}
}

