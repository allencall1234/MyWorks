package com.xj.cnooc.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xj.cnooc.biz.UserHttpBiz;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.common.MyUtils;
import com.xj.cnooc.https.HttpDataCallBack;
import com.xj.cnooc.model.UserBean;
import com.xj.cnooc.utils.UserHelper;

public class LoginActivity extends Activity implements OnClickListener
{
	private EditText login_et_username;
	private EditText login_et_password;
	private Button btn_login;
	private ImageView iv_show_hidden_pass;
	
	public static int hitchtotal;// 故障支持条数
	public static int asstotal;// 评估支持条数
	public static int recordtotal;// 知识库新添条数
	public static int bbsTotal;// 论坛中未看帖子条数
	public static int chatTotal;
	
	private boolean mHidden = false;// 设置是否隐藏密码标识
	
	//在本地存储密码和邮箱（用户名）
	private static final String INTENT_USERNAME = "intent_username";//用户名
	private static final String INTENT_PASSWORD = "intent_password";//密码

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		initView();
		
		if(UserHelper.getInstance() != null)
		{
			String useranme = UserHelper.getInstance().getSharedPreferences().getString(INTENT_USERNAME,"");
			String password = UserHelper.getInstance().getSharedPreferences().getString(INTENT_PASSWORD, "");
			login_et_username.setText(useranme);
			login_et_password.setText(password);
		}
	}
	
	private void initView() 
	{
	    setContentView(R.layout.login_view);
	    
		login_et_username = (EditText) findViewById(R.id.et_username);
		login_et_password = (EditText) findViewById(R.id.et_password);
		
		btn_login = (Button) findViewById(R.id.btn_login);
		
		iv_show_hidden_pass = (ImageView) findViewById(R.id.iv_show_hidden_pass);
		
		iv_show_hidden_pass.setOnClickListener(this);
		
		btn_login.setOnClickListener(this);// 登录监听
		
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.btn_login:
				
				String userName = login_et_username.getText().toString().trim();
				String password = login_et_password.getText().toString().trim();
				
				// 如果用户名为空
				if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) 
				{
					Toast.makeText(LoginActivity.this, R.string.login_error_isNull, Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					UserHttpBiz.Login(userName, password, new HttpDataCallBack() 
					{
						final Message message = new Message();
						@Override
						public void HttpSuccess(String _result) 
						{
							try 
							{
								JSONObject object = new JSONObject(_result);
								message.obj = object;
								handler.sendMessage(message);
							} 
							catch (Exception e) 
							{
								e.printStackTrace();
							}
						}
						
						@Override
						public void HttpFail(int ErrCode)
						{
							Toast.makeText(LoginActivity.this, "网络请求失败,请检查您的网络!", Toast.LENGTH_SHORT).show();
						}
					});
				}
				break;
			case R.id.iv_show_hidden_pass:
				if (!mHidden) 
				{  
					// 显示密码
					login_et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					iv_show_hidden_pass.setImageResource(R.drawable.show_pass);
	            } 
				else 
				{  
					// 隐藏密码
					login_et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());  
					iv_show_hidden_pass.setImageResource(R.drawable.hidden_pass);
	            }  
				mHidden = !mHidden;  
				login_et_password.postInvalidate();  
				break;
		}
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			JSONObject obj = (JSONObject) msg.obj;
			try
			{
				if (obj.getString("message").equals("0")) 
				{
				    JSONObject object = new JSONObject(obj.getString("userInfo"));
				    MyApp.hitchtotal = obj.getInt("hitchtotal");
					MyApp.asstotal = obj.getInt("asstotal");
					MyApp.bbsTotal = obj.getInt("bbsTotal");
					Log.i("msg", "loginActivity : bbsTotal = " + MyApp.bbsTotal);
					MyApp.recordtotal = obj.getInt("recordtotal");
					UserBean bean = (UserBean) MyUtils.putJsonToObject(object, "com.xj.cnooc.model.UserBean");
					MyApp.globelUser = bean;
					
					if(UserHelper.getInstance() != null)
					{
						System.out.println(login_et_username.getText().toString() + login_et_password.getText().toString());
						
						Editor editor = UserHelper.getInstance().getSharedPreferences().edit();
						editor.putString(INTENT_USERNAME, login_et_username.getText().toString());
						editor.putString(INTENT_PASSWORD, login_et_password.getText().toString());
						editor.commit();
					}
					
					Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this, MainGridViewActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
				}
				else if (obj.getString("message").equals("4"))
				{
					Toast.makeText(LoginActivity.this, "用户名不存在或无角色,请联系管理员!", Toast.LENGTH_SHORT).show();
				}
				else if (obj.getString("message").equals("5")) 
				{
					Toast.makeText(LoginActivity.this, "密码错误!", Toast.LENGTH_SHORT).show();
				}
				else if (obj.getString("message").equals("6")) 
				{
					Toast.makeText(LoginActivity.this, "当前账户不可用，请授权!", Toast.LENGTH_SHORT).show();
				}
				else if (obj.getString("message").equals("7")) 
				{
					Toast.makeText(LoginActivity.this, "查询数据失败，系统异常!", Toast.LENGTH_SHORT).show();
				}
				else if (obj.getString("message").equals("8"))
				{
					Toast.makeText(LoginActivity.this, "非厂家支持人员或者系统专家，禁止登录!", Toast.LENGTH_SHORT).show();
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		}
	};

}
