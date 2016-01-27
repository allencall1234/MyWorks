package com.xj.cnooc.view.bbs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xj.cnooc.adapter.CommentedPostListViewAdapter;
import com.xj.cnooc.biz.UserHttpBiz;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.common.MyUtils;
import com.xj.cnooc.common.XListView;
import com.xj.cnooc.common.XListView.IXListViewListener;
import com.xj.cnooc.https.HttpDataCallBack;
import com.xj.cnooc.https.HttpURL;
import com.xj.cnooc.model.AttachmentBean;
import com.xj.cnooc.model.PostBean;
import com.xj.cnooc.model.ReplyPostBean;
import com.xj.cnooc.view.R;

/**
 * 
 * @author Administrator
 * 点击帖子列表进入到的帖子详情界面Activity
 *
 */
public class PostDetailsActivity extends Activity implements OnClickListener, IXListViewListener
{
//	private int post_position;//点击的帖子的下标
	private PostBean postBean;
	private Context context;
	private CommentedPostListViewAdapter adapter;
	private String bbs_title;// 技术讨论区名称
	private int position;
	private int postId;// 帖子id
	private int loginId;// 账号id
	
	private int totalNum = 0;//已回复帖子的总数量
	private int num = 10;//每页加载的帖子数量
	private int curragePage = 1;//当前页
	
	private int reply_total = 0;// 回帖数量
	
	private TextView tv_title;// 帖子标题
	private TextView tv_poster;// 发帖人
	private ImageButton ib_collection_img;// 收藏
	private TextView tv_post_content;// 帖子内容
	private TextView tv_fujianName;// 附件名称
	private ImageButton ib_download_file_img;// 下载附件
//	private ImageButton ib_download_file_img;// 下载附件
	private TextView tv_post_time;// 发帖时间
//	private TextView tv_post_reply_account;// 回帖数量
	private ImageButton ib_commented_img;// 评论
	private TextView tv_collect;
	
	private RelativeLayout rl_commenting;// 底部评论输入框
	
	private ImageView iv_head_img;// 帖子图片
	
	private XListView lv_reply_list;// 回复列表
	
//	private ListView lv_reply_list;// 回复列表
	
//	private Button btn_back;// 返回
//	private RelativeLayout rellayout_back;
//	private RelativeLayout rellayout_setting;
	private ImageView iv_back;
	private ImageView iv_setting;
	private TextView tv_center_title;// 标题
	private TextView tv_right_title;
//	private Button btn_setting;// 设置
	
	private Button btn_send;// 发送
	
	private EditText et_sendmessage;// 要发送的信息
	
	private ScrollView mScrollView;
	
	private ArrayList<ReplyPostBean> reply_list;// 回复信息
	
	private String send_message;// 回复的内容
	
	private ArrayList<AttachmentBean> attList;// 附件
	
	private String attName = "";// 附件名称
	
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initData();
		
	}
	
	protected void initData()
	{
//		post_position = getIntent().getIntExtra("post_position", post_position);// 获取点击的帖子下标
		
		Bundle bundle = getIntent().getExtras();
		bbs_title = bundle.getString("bbs_title");
		position = bundle.getInt("position");
		postBean = (PostBean) getIntent().getSerializableExtra("PostBean");
		
		postId = postBean.getId();
		loginId = MyApp.globelUser.getAccountid();
		
		totalNum = postBean.getAnswerCount();// 获取总贴数
//		bean = MyApp.post_list.get(post_position);
		
		reply_total = postBean.getAnswerCount();
		
		setContentView(R.layout.post_detailes_view);
		
		context = PostDetailsActivity.this;
		
		initView();
		
		initListViewData();// 初始化界面数据
		
		/*
		 * scrollview里面嵌套了一个listview，通过设置一个方法设置了listview的高度现在的情况就是进到
		 * 这个界面的时候看到的不是最上面而是中间的，该问题的解决方法mScrollView.smoothScrollTo(0, 20);
		 * 如此以上代码还是无效，在代码里去掉listview的焦点lv.setFocuable(false),就可以了
		 */
		mScrollView.smoothScrollTo(0, 20);
		lv_reply_list.setFocusable(false);
		
	}

	private void initView() 
	{
		reply_list = new ArrayList<ReplyPostBean>();
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_poster = (TextView) findViewById(R.id.tv_poster);
		tv_post_content = (TextView) findViewById(R.id.tv_post_content);
		
		ib_collection_img = (ImageButton) findViewById(R.id.ib_collection_img);
		tv_collect = (TextView) findViewById(R.id.tv_collect);
		tv_fujianName = (TextView) findViewById(R.id.tv_fujianName);
		ib_download_file_img = (ImageButton) findViewById(R.id.ib_download_file_img);
		ib_commented_img = (ImageButton) findViewById(R.id.ib_commented_img);
		
		tv_post_time = (TextView) findViewById(R.id.tv_post_time);
//		tv_post_reply_account = (TextView) findViewById(R.id.tv_post_time);
		
		iv_head_img = (ImageView) findViewById(R.id.iv_head_img);
		
//		downloadProgress = (ProgressBar)findViewById(R.id.downloadProgress);
		
		lv_reply_list = (XListView) findViewById(R.id.lv_reply_list);
//		lv_reply_list = (ListView) findViewById(R.id.lv_reply_list);
		
//		btn_back = (Button) findViewById(R.id.btn_back);
//		rellayout_back = (RelativeLayout) findViewById(R.id.rellayout_back);
//		rellayout_setting = (RelativeLayout) findViewById(R.id.rellayout_setting);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);
		tv_center_title = (TextView) findViewById(R.id.tv_center_title);
		tv_right_title = (TextView) findViewById(R.id.tv_right_title);
//		btn_setting = (Button) findViewById(R.id.btn_setting);
		
		rl_commenting = (RelativeLayout) findViewById(R.id.rl_commenting);
		
		tv_center_title.setText("帖子详情");
		
//		btn_setting.setVisibility(View.GONE);
		iv_setting.setVisibility(View.GONE);
		tv_right_title.setVisibility(View.VISIBLE);
		tv_right_title.setText(String.valueOf(reply_total + "条回帖"));
		
		btn_send = (Button) findViewById(R.id.btn_send);
		
		et_sendmessage = (EditText) findViewById(R.id.et_sendmessage);
		
		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		
		tv_title.setText(postBean.getTitle());
		tv_poster.setText("发帖人:" + postBean.getBbsPostName());
		tv_post_content.setText(postBean.getContent());
		tv_post_time.setText("发帖时间：" + postBean.getCtime());
		
		// 加载发帖人头像
		ImageLoader.getInstance().loadImage(HttpURL.SERVICE_URL + postBean.getPhoto(), new ImageLoadingListener() 
		{
			@Override
			public void onLoadingStarted(String imageUri, View view)
			{
				iv_head_img.setImageResource(R.drawable.login_head_img);
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) 
			{
				iv_head_img.setImageResource(R.drawable.login_head_img);
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) 
			{
				iv_head_img.setImageBitmap(loadedImage);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) 
			{
				
			}
		});
//		tv_post_reply_account.setText("回帖(" + postBean.getAnswerCount() +")");
		
		// 设置上拉加载
		lv_reply_list.setPullLoadEnable(true);
		
		// 设置上拉加载事件监听
		lv_reply_list.setXListViewListener(this);
		
//		btn_back.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		if (bbs_title.equals("我的收藏")) 
		{
			tv_collect.setText("已收藏");
		}
		else
		{
			ib_collection_img.setOnClickListener(this);
		}
		ib_commented_img.setOnClickListener(this);
		ib_download_file_img.setOnClickListener(this);
		
	}
	
	private void initListViewData()
	{
		new Thread()
		{
			@Override
			public void run() 
			{
				super.run();
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.iv_back:// 返回
				finish();
				break;
			case R.id.ib_commented_img:// 底部评论编辑框
				rl_commenting.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_send:// 发送
				send_message = et_sendmessage.getText().toString().trim();
				if (TextUtils.isEmpty(send_message)) 
				{
					Toast.makeText(context, "发送内容不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				new Thread()
				{
					@Override
					public void run()
					{
						handler.sendEmptyMessage(3);
					};
				}.start();
				break;
			case R.id.ib_collection_img:// 收藏
				new Thread()
				{
					@Override
					public void run() 
					{
						handler.sendEmptyMessage(2);
					};
				}.start();
				break;
			case R.id.ib_download_file_img:// 下载附件
//				new Thread()
//				{
//					public void run() 
//					{
//						handler.sendEmptyMessage(4);
//					};
//				}.start();
				Intent intent = new Intent(context, DownloadDetailsActivity.class);
				intent.putExtra("att_list", attList);
				startActivity(intent);
				break;
		}
	}
	
	private Handler handler = new Handler(new Handler.Callback() 
	{
		@Override
		public boolean handleMessage(Message msg)
		{
			switch (msg.what) 
			{
			    case 0:
					dialog = ProgressDialog.show(context, "", "数据正在加载中，请稍后...");
					dialog.setCancelable(true);
					UserHttpBiz.queryReplyPostList(curragePage, num, postId, loginId, new HttpDataCallBack() 
					{
						@Override
						public void HttpSuccess(String _result) 
						{
							dialog.dismiss();
							if (totalNum < 10) 
							{
								lv_reply_list.setPullLoadEnable(false);
							}
							adapter = new CommentedPostListViewAdapter(getReplyPostData(_result), context);
							lv_reply_list.setAdapter(adapter);
							setListViewHeightBasedOnChildren(lv_reply_list);
						}
						
						@Override
						public void HttpFail(int ErrCode)
						{
							dialog.dismiss();
							Toast.makeText(context, "网络请求失败,请检查您的网络!", Toast.LENGTH_SHORT).show();
						}
					});
				    break;
				case 1:
					UserHttpBiz.queryReplyPostList(curragePage, num, postId, loginId, new HttpDataCallBack() 
					{
						@Override
						public void HttpSuccess(String _result) 
						{
							getReplyPostData(_result);
							adapter.notifyDataSetChanged();
							setListViewHeightBasedOnChildren(lv_reply_list);
							int pages = totalNum / num == 0 ? totalNum / num : totalNum / num + 1;
							if (curragePage >= pages)
							{
								Toast.makeText(context, "数据已全部加载完", Toast.LENGTH_SHORT).show();
								onStopLoad();// 停止刷新
							}
						}
						
						@Override
						public void HttpFail(int ErrCode)
						{
							Toast.makeText(context, "网络请求失败,请检查您的网络!", Toast.LENGTH_SHORT).show();
						}
					});
					break;
				case 2:
					UserHttpBiz.collectionPost(postId, loginId, new HttpDataCallBack()
					{
						@Override
						public void HttpSuccess(String _result)
						{
							try
							{
								JSONObject object = new JSONObject(_result);
								if (object.getBoolean("status") == true) 
								{
									if (object.getInt("message") == 0) 
									{
										Toast.makeText(context, "此帖子收藏成功", Toast.LENGTH_SHORT).show();
										MyCollectionFragment.collection_list.add(postBean);
									}
									else if (object.getInt("message") == 2) 
									{
										Toast.makeText(context, "服务器异常", Toast.LENGTH_SHORT).show();
									}
								}
								 else
								{
									if (object.getInt("message") == 1) 
									{
										Toast.makeText(context, "此帖子已收藏", Toast.LENGTH_SHORT).show();
									}
									else if (object.getInt("message") == 2) 
									{
										Toast.makeText(context, "服务器异常", Toast.LENGTH_SHORT).show();
									}
								}
							
							} 
							catch (JSONException e)
							{
								e.printStackTrace();
							}
							
						}
						
						@Override
						public void HttpFail(int ErrCode) 
						{
							Toast.makeText(context, "网络请求失败,请检查您的网络!", Toast.LENGTH_SHORT).show();
						}
					});
					break;
				case 3:
					UserHttpBiz.savePostReplyContent(postId, send_message, loginId, new HttpDataCallBack()
					{
						@Override
						public void HttpSuccess(String _result)
						{
							System.out.println(_result);
							try 
							{
								JSONObject object = new JSONObject(_result);
								if (object.getBoolean("status") == true) 
								{
									reply_list.add(new ReplyPostBean(MyApp.globelUser.getName(), send_message, MyUtils.getTime()));
									reply_total = reply_total + 1;
									tv_right_title.setText(String.valueOf(reply_total + "条回帖"));
									adapter.notifyDataSetChanged();
									rl_commenting.setVisibility(View.GONE);
									et_sendmessage.setText("");
//									Toast.makeText(context, "保存回复内容成功", Toast.LENGTH_SHORT).show();
									if (bbs_title.equals("已评论贴")) 
									{
										setResult(0x03);
									}
									else if (bbs_title.equals("我的收藏")) 
									{
										setResult(0x02);
									}
									else if (bbs_title.equals(PostListViewActivity.bbs_title)) 
									{
										setResult(0x01);
									}
								}
								finish();
							}
							catch (JSONException e) 
							{
								e.printStackTrace();
							}
						}
						
						@Override
						public void HttpFail(int ErrCode) 
						{
							Toast.makeText(context, "网络请求失败,请检查您的网络!", Toast.LENGTH_SHORT).show();
						}
					});
					break;
				case 4:
//					if (fileCount <= attList.size() - 1) 
//					{
//						AttachmentBean bean = attList.get(fileCount);
//						String filePath = bean.getPath();
//						downloadProgress = new ProgressBar(context);
//						fileCount ++ ;
//						//第一个参数指的是文件下载的URL，第二个参数指的是文件存放的名称
//						if(bean.getName().endsWith(".jpeg") ||bean.getName().endsWith(".jpg") || bean.getName().endsWith(".png"))
//						{
//							
////							bitmapUtil = new BitmapUtils(downloadProgress);
////						    bitmapUtil.downloadFile(filePath, bean.getName());
//							bitmapUtil.asyncDownloadFile(filePath, bean.getName());
//						}
//						else
//						{
//							fileUtil = new FileUtils(downloadProgress, context);
////							fileUtil.downloadFile(filePath, bean.getName(), context);
//							fileUtil.asyncDownloadFile(filePath, bean.getName());
//						}
//					}
					break;
			}
			return false;
		}
	});
	
	// 获取回复信息
	private List<ReplyPostBean> getReplyPostData(String _result)
	{
		attList = new ArrayList<AttachmentBean>();
//		if (attList.size() > 0) 
//		{
//			attList.removeAll(attList);
//		}
		try 
		{
			JSONObject jsonObject = new JSONObject(_result);
			if (jsonObject.getBoolean("status") == true) 
			{
				JSONArray jsonArray = jsonObject.getJSONArray("replyList");
				
				for (int i = 0; i < jsonArray.length(); i++) 
				{
					JSONObject jsonObject2 = jsonArray.getJSONObject(i).getJSONObject("bbsPostReply");
					
					ReplyPostBean bean = (ReplyPostBean) MyUtils.putJsonToObject(jsonObject2, "com.xj.cnooc.model.ReplyPostBean");
					
				    reply_list.add(bean);
				}
				
				JSONArray jsonArray2 = jsonObject.getJSONArray("attList");
				if (jsonArray2.length() == 1)
				{
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(0);
					AttachmentBean bean = (AttachmentBean) MyUtils.putJsonToObject(jsonObject2, "com.xj.cnooc.model.AttachmentBean");
					tv_fujianName.setText(bean.getName());
					bean.setType("1");
					attList.add(bean);
					ib_download_file_img.setVisibility(View.VISIBLE);
				}
				else if(jsonArray2.length() > 1)
				{
					for (int i = 0; i < jsonArray2.length(); i++)
					{
						JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
						AttachmentBean bean = (AttachmentBean) MyUtils.putJsonToObject(jsonObject2, "com.xj.cnooc.model.AttachmentBean");
						
						if (attName.equals("")) 
						{
							attName = attName + bean.getName();
						}
						else
						{
							attName = attName + "，" + bean.getName();
						}
						bean.setType("1");
					    attList.add(bean);
					}
				  ib_download_file_img.setVisibility(View.VISIBLE);
				  tv_fujianName.setText(attName);
				}
				else if (jsonArray2.length() < 1) 
				{
					tv_fujianName.setText("无附件");
					ib_download_file_img.setVisibility(View.GONE);
				}
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return reply_list;
	}
	
	@Override
	public void onRefresh() 
	{
		onStopLoad();
	}

	@Override
	public void onLoadMore()
	{
		if (totalNum < num) 
		{
			Toast.makeText(context, "数据已全部加载完", Toast.LENGTH_SHORT).show();
			onStopLoad();// 停止刷新
		}
		else
		{
			int pages = totalNum / num == 0 ? totalNum / num : totalNum / num + 1;
			if(pages >= curragePage)
			{
				curragePage++;
			}
			new Thread()
			{
				@Override
				public void run() 
				{
					super.run();
					handler.sendEmptyMessage(1);
				}
			}.start();
		}
		onStopLoad();
		
		
	}
	
	/**
	 * 停止刷新
	 */
	private void onStopLoad()
	{
		lv_reply_list.stopRefresh();
		lv_reply_list.stopLoadMore();
		lv_reply_list.setRefreshTime(MyUtils.getTime());
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {  
        // 获取ListView对应的Adapter  
        ListAdapter listAdapter = listView.getAdapter();  
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) 
        { // listAdapter.getCount()返回数据项的数目  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0); // 计算子项View 的宽高  
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight  
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        // listView.getDividerHeight()获取子项间分隔符占用的高度  
        // params.height最后得到整个ListView完整显示需要的高度  
        listView.setLayoutParams(params);  
    }  
	
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//		if (keyCode == event.KEYCODE_BACK) 
//		{
//			if (activity_flag != null) 
//			{
//				if (activity_flag.equals("PostListViewActivity"))
//				{
//					startActivity(new Intent(PostDetailsActivity.this,
//							PostListViewActivity.class));
//					this.finish();
//				}
//				else if (activity_flag.equals("CommentedPostFragment"))
//				{
//
//					startActivity(new Intent(PostDetailsActivity.this,
//							PostListViewActivity.class));
//					this.finish();
//				}
//			}
//			startActivity(new Intent(PostDetailsActivity.this,
//					PostListViewActivity.class));
////			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//			this.finish();
//		}
//		
//		return true;
//	}
	

}
