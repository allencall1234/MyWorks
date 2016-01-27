package com.xj_pipe.common;

import java.util.ArrayList;

import com.animate.explosion.ExplosionField;
import com.xj_pipe.adapter.CommonAdapter;
import com.xj_pipe.adapter.ViewHolder;
import com.xj_pipe.view.PhotoActivity;
import com.xj_pipe.view.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PhotoModule extends LinearLayout {

	public ArrayList<String> gList = null;
	
	private Gallery mGallery = null;
	private ImageView mImageView = null;

	private CommonAdapter<String> mAdapter = null;

	private Context mContext;

	private ExplosionField mExplosionField = null;
	public PhotoModule(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context)
				.inflate(R.layout.photo_module_layout, this);
		mContext = context;
		
		mExplosionField = ExplosionField.attach2Window((Activity) mContext);
		
		initViews();
	}

	private void initViews() {
		
		gList = new ArrayList<String>();
		mGallery = (Gallery) findViewById(R.id.photo_module_gallery);
		mImageView = (ImageView) findViewById(R.id.photo_module_image);

		mGallery.setAdapter(mAdapter = new CommonAdapter<String>(mContext,
				gList, R.layout.gallery_item) {

			@Override
			public void convert(final ViewHolder viewHolder, String item) {
				// TODO Auto-generated method stub
				viewHolder.setImageByUrl(R.id.photo_view, item);
				viewHolder.setListener(R.id.photo_del, new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 移除时添加爆炸效果
						mExplosionField.explode(viewHolder.getConvertView());
						
						int position = viewHolder.getPosition();
						gList.remove(position);
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		});

		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, PhotoActivity.class);
				intent.putExtra("position", position);
				intent.putStringArrayListExtra("bitmaps", gList);
				mContext.startActivity(intent);

			}
		});
		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhotoPickDialog.createDialog((Activity) mContext).show();
			}
		});
	}

	public void addBitmap(String bitmap) {
		// TODO Auto-generated method stub
		if (bitmap != null) {
			gList.add(bitmap);
			mAdapter.notifyDataSetChanged();
			mGallery.setSelection(mAdapter.getCount()-1, true);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
//		recyleBitmaps();
		super.onDetachedFromWindow();
	}

//	// 回收列表中的bitmap
//	private void recyleBitmaps() {
//		int total = gList.size();
//		for (int i = 0; i < total; i++) {
//			Bitmap bitmap = gList.get(i);
//			if (bitmap != null && !bitmap.isRecycled()) {
//				bitmap.recycle();
//			}
//		}
//		gList = null;
//	}
}
