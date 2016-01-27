package com.xj_pipe.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xj_pipe.view.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoGridViewAdapter extends BaseAdapter
{
	private List<String> imgUrlList;
	private Context context;

	public PhotoGridViewAdapter(Context context, List<String> imgUrlList)
	{
		this.context = context;
		this.imgUrlList = imgUrlList;
	}
	
	@Override
	public int getCount() 
	{
		return imgUrlList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return imgUrlList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		String imgUrl = imgUrlList.get(position);
		
		ViewHolder holder;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_zhenggai_check_details, null);
			
			holder = new ViewHolder();
			holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
//			holder.tv_imgName = (TextView) convertView.findViewById(R.id.tv_imgName);
			
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		

		
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
		
		ImageLoader.getInstance().displayImage(imgUrl, holder.iv_img);
		
//		for (int i = 1; i < photoList.size() + 1; i++) 
//		{
//			holder.tv_imgName.setText(String.valueOf(i));
//		}
		
//		Bitmap map = getImageThumbnail(bean.getImgUrl(), 80, 100);
//		holder.iv_img.setImageBitmap(map);
//		holder.tv_imgName.setText(bean.getImgName());
		
		return convertView;
	} 
	
	class ViewHolder
	{
		private ImageView iv_img;
		private TextView tv_imgName;
	}
	
	private Bitmap getImageThumbnail(String imagePath, int width, int height) {  
        Bitmap bitmap = null;  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        // 获取这个图片的宽和高，注意此处的bitmap为null  
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        options.inJustDecodeBounds = false; // 设为 false  
        // 计算缩放比  
        int h = options.outHeight;  
        int w = options.outWidth;  
        int beWidth = w / width;  
        int beHeight = h / height;  
        int be = 1;  
        if (beWidth < beHeight) {  
            be = beWidth;  
        } else {  
            be = beHeight;  
        }  
        if (be <= 0) {  
            be = 1;  
        }  
        options.inSampleSize = be;  
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false  
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象  
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        return bitmap;  
    }  

}

