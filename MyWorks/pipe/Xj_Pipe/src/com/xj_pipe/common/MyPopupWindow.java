package com.xj_pipe.common;

import com.xj_pipe.view.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;

public class MyPopupWindow extends PopupWindow
{
	private static final int TAKE_PICTURE = 0;
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int CUT_PHOTO_REQUEST_CODE = 2;
	private static final int SELECTIMG_SEARCH = 3;
	
	private Button btn_camera, btn_photo, btn_cancel;
	
	@SuppressWarnings("deprecation")
	public MyPopupWindow(Context mContext, View parent) 
	{
		View view = View.inflate(mContext, R.layout.item_popupwindow, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.popupwindow_fade_ins));

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		btn_camera = (Button) view.findViewById(R.id.btn_camera);
		btn_photo = (Button) view.findViewById(R.id.btn_photo);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		
//		
//		// 拍照
//		bt1.setOnClickListener(new OnClickListener() 
//		{
//			public void onClick(View v) 
//			{
////				photo();
//				dismiss();
//			}
//		});
//		
//		// 从相册选择
//		bt2.setOnClickListener(new OnClickListener()
//		{
//			public void onClick(View v) 
//			{
//				// 相册
////				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////				startActivityForResult(intent, RESULT_LOAD_IMAGE);
//				dismiss();
//			}
//		});
//		
//		// 取消
//		bt3.setOnClickListener(new OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				dismiss();
//			}
//		});
//
//	}
//	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
//	{
//		switch (requestCode) 
//		{
//			case TAKE_PICTURE:
//				if (drr.size() < 6 && resultCode == -1)
//				{// 拍照
//					startPhotoZoom(photoUri);
//				}
//				break;
//			case RESULT_LOAD_IMAGE:
//				if (drr.size() < 6 && resultCode == RESULT_OK && null != data) 
//				{// 相册返回
//					Uri uri = data.getData();
//					if (uri != null) {
//						startPhotoZoom(uri);
//					}
//				}
//				break;
//			case CUT_PHOTO_REQUEST_CODE:
//				if (resultCode == RESULT_OK && null != data) 
//				{// 裁剪返回
//					Bitmap bitmap = Bimp.getLoacalBitmap(drr.get(drr.size() - 1));
//					PhotoActivity.bitmap.add(bitmap);
//					bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
//							(int) (dp * 1.6f));
//					bmp.add(bitmap);
//					gridviewInit();
//				}
//				break;
//			case SELECTIMG_SEARCH:
//				if (resultCode == RESULT_OK && null != data) 
//				{
//					String text = "#" + data.getStringExtra("topic") + "#";
//					text = comment_content.getText().toString() + text;
//					comment_content.setText(text);
//	
//					comment_content.getViewTreeObserver().addOnPreDrawListener(
//							new OnPreDrawListener() {
//								public boolean onPreDraw() {
//									comment_content.setEnabled(true);
//									// 设置光标为末尾
//									CharSequence cs = comment_content.getText();
//									if (cs instanceof Spannable) {
//										Spannable spanText = (Spannable) cs;
//										Selection.setSelection(spanText,
//												cs.length());
//									}
//									comment_content.getViewTreeObserver()
//											.removeOnPreDrawListener(this);
//									return false;
//								}
//							});
//	
//				}
//	
//				break;
    }

	public Button btnCamera()
	{
		return btn_camera;
	}
	
	public Button btnPhoto()
	{
		return btn_photo;
	}
	
	public Button btnCancel()
	{
		return btn_cancel;
	}
	
}
