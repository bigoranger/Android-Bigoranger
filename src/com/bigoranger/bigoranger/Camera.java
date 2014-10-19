package com.bigoranger.bigoranger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

public class Camera {

	/**
	 *  图片质量（占用内存大小）压缩
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 90;

		Log.v("finally_size", baos.toByteArray().length / 1024 + "");

		while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10

		}
		Log.v("finally_size", baos.toByteArray().length / 1024 + "");

		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中

		Log.v("isBm_size", isBm.available() / 1024 + ""); // 87

		// 把ByteArrayInputStream数据生成图片,
		// 注意：以bitmap形式存在时，图片又会变大！！！！ 所以上传采取用流的形式
		Bitmap newBitmap = BitmapFactory.decodeStream(isBm, null, null);

		// Log.v("bitmap_size",newBitmap.getByteCount()/1024+""); //1500

		return newBitmap;
	}

	/**
	 * 将图片等比例缩放
	 * @param srcPath
	 * @param widthPixels 手机原始分辨率的width
	 * @param heightPixels 手机原始分辨率的height
	 * @return
	 */
	public static Bitmap getimage(String srcPath,float widthPixels,float heightPixels) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();

		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);
		
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		    
		float hh = heightPixels;
		float ww = widthPixels;
		
		int be = 1;
		if (w > h && w > ww) {
			be = (int)Math.ceil(newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int)Math.ceil(newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		Log.v("width", bitmap.getWidth()+"");
		Log.v("height", bitmap.getHeight()+"");
		return bitmap;
	}
	
	/**
	 * 按照指定height和width大小压缩图片 -- 尺寸
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {

		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth1 = newWidth;
		int newHeight1 = newHeight;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;

	}

	/*** 旋转图片 
	* @param angle 
    * @param bitmap 
	* @return Bitmap 
	*/  
   public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
		 //旋转图片 动作  
		 Matrix matrix = new Matrix();;  
		 matrix.postRotate(angle);  
		 System.out.println("angle2=" + angle);  
		 Log.v("angle", angle+"");
		 // 创建新的图片  
		 Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
		 bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
		 return resizedBitmap;  
   }
   
   /**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
   public static int readPictureDegree(String path) {
       int degree  = 0;
       try {
               ExifInterface exifInterface = new ExifInterface(path);
               int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
               switch (orientation) {
               case ExifInterface.ORIENTATION_ROTATE_90:
                       degree = 90;
                       break;
               case ExifInterface.ORIENTATION_ROTATE_180:
                       degree = 180;
                       break;
               case ExifInterface.ORIENTATION_ROTATE_270:
                       degree = 270;
                       break;
               }
       } catch (IOException e) {
               throw new RuntimeException(e);
       }
       return degree;
   }

}
