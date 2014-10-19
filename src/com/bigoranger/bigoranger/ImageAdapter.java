package com.bigoranger.bigoranger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.*;
public class ImageAdapter extends BaseAdapter {
	private Context ctx;
	
	private List<String> listPicPath;
	//获取sd卡跟路径
//	private String sdcardRoot= 
//			Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
	private int minWidth;
	public ImageAdapter(Context ctx,List<String> listPicPath,int minWidth){
		this.ctx=ctx;
		this.minWidth=(int) (minWidth-35)/4;;

		this.listPicPath=listPicPath;
		if(this.listPicPath==null){
			this.listPicPath=new ArrayList<String>();
		}
	}
	@Override
	public int getCount() {
		return listPicPath.size();
	}

	@Override
	public Object getItem(int position) {
		return listPicPath.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView=new ImageView(ctx);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		String fname=(String) listPicPath.get(position);//从文件路径集合中去指定位置上的缩略图的图片路径
		
		Bitmap bm=BitmapFactory.decodeFile(fname);//通过指定路径得到原图
		
		imageView.setImageBitmap(bm);//将指定文件下的略缩图设置到ImageView上

		Log.e("width", minWidth+"");
		imageView.setMinimumHeight(minWidth);
		return imageView;
	  }
}
