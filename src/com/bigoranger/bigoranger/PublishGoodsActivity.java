package com.bigoranger.bigoranger;

import android.app.Activity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class PublishGoodsActivity extends Activity implements OnClickListener {
	private static final String TAG = "uploadImage";
	private static final int TAKE_PHOTO = 1;
	private static final int SELECT_IMAGE = 2;
	private String json = null;

	private String picPath = ""; // 图片路径
	public static String name; // 文件名称
	public static String sdcardRoot; // sd卡根路径
	private static String fileName = ""; // 文件绝对路径
	private static final String IMAGE = "BigOranger" + File.separator + "Image";
	private File file; // 文件
	private Uri u; // 从相册获取图片的uri
	private static DisplayMetrics metrics; // 手机屏幕分辨率
	ContentResolver cr;

	public List<String> list = new ArrayList<String>();// 用于存放图片的集合
	private int index;
	private GridView myGirdView; // 网格
	private Button takePhoto, selectImage, saveBtn, backBtn;
	private EditText title, price, costPrice, presentation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.uploadpic);
		// 实例化sdcardRoot
		sdcardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;

		// 在指定位置创建用于存放图片文件的文件夹
		File f = new File(sdcardRoot + IMAGE);
		if (!f.exists()) {
			f.mkdirs();
			// Log.v("test", f.getPath());
		}

		metrics = new DisplayMetrics(); // 获取手机分辨率
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		init();
		selectImage.setOnClickListener(this);
		takePhoto.setOnClickListener(this);
		selectImage.setOnClickListener(this);
		takePhoto.setOnClickListener(this);
		this.myGirdView.setOnItemClickListener(new OnItemClickListenerImpl());
		title.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
		myGirdView.setOnItemClickListener(new OnItemClickListenerImpl());
		sdcardRoot = Environment.getExternalStorageDirectory() // 实例化sdcardRoot
				.getAbsolutePath() + File.separator;
		Log.v("ddd", "dfsesef");

	}

	private void init() {
		setContentView(R.layout.uploadpic);
		selectImage = (Button) findViewById(R.id.selectImage);
		takePhoto = (Button) findViewById(R.id.takePhoto);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		backBtn = (Button) findViewById(R.id.backBtn);

		title = (EditText) findViewById(R.id.title);
		price = (EditText) findViewById(R.id.price);
		costPrice = (EditText) findViewById(R.id.costPrice);
		presentation = (EditText) findViewById(R.id.presentation);

		myGirdView = (GridView) findViewById(R.id.myGridView);
	}

	@Override
	public void onClick(View v) {
		Log.e(TAG, v.getId() + "");
		switch (v.getId()) {
		case R.id.selectImage:
			selectImage();
			break;
		case R.id.takePhoto:
			takePhoto();
			picUpload();
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * Function：图片和普通上传 author：lionel
	 */
	private void picUpload() {
		if ((picPath != null && picPath.length() > 0)) {
			UploadFileTask uploadFileTask = new UploadFileTask(this);
			uploadFileTask.execute(picPath);
		}
	}

	/**
	 * 拍照
	 * 
	 * @param btn
	 */
	private void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		name = DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		// 给intent加上输出参数，为返回原始图片------
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) { // 判断是否存在SD卡

			// 根据日期生成文件名
			file = new File(sdcardRoot + IMAGE + File.separator, name);

			Uri uri = Uri.fromFile(file);// 根据文件得到Uri对象

			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			fileName = file.getPath();
			Log.v("filename001", fileName);
			startActivityForResult(intent, TAKE_PHOTO);

		} else {
			new AlertDialog.Builder(PublishGoodsActivity.this)
					.setMessage("检测到手机没有存储卡！请插入手机存储卡再开启本应用。")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).show();
		}
	}

	/**
	 * 
	 * Function：调用系统Intent获取系统图库中的图片 author：lionel
	 */
	private void selectImage() {
		// 调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_IMAGE);
	}

	/**
	 * 回调执行的方法，拍照或是选择照片后的自定义操作...
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {

				Log.v("拍照", "拍照");
				String str = fileName.substring(fileName.lastIndexOf(File.separator) + 1,
						fileName.lastIndexOf("."));
				String newName = str + "_mini";
				try {
					// 以字节流的形式压缩，以便上传到服务器！！
					saveCompressPic(fileName, newName);

					initImageAdapter(newName);// 初始化图片适配器

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (requestCode == SELECT_IMAGE) { // 选择照片
			if (resultCode == Activity.RESULT_OK) {
				Log.v("选择图片", "选择图片");
				// 当选择的图片不为空的话，在获取到图片的途径
				u = data.getData();
				Log.e(TAG, "uri = " + u);
				try {
					String[] pojo = { MediaStore.Images.Media.DATA };

					Cursor cursor = managedQuery(u, pojo, null, null, null);
					if (cursor != null) {
						cr = this.getContentResolver();
						int colunm_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String path = cursor.getString(colunm_index);
						/***
						 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，
						 * 你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
						 */
						if (path.endsWith("jpg") || path.endsWith("png")
								|| path.endsWith("jpeg")) {
							picPath = path;
							String str = DateFormat.format("yyyyMMdd_hhmmss",
									Calendar.getInstance(Locale.CHINA))
									.toString();
							String newName = str + "_mini";
							// 以字节流的形式压缩，以便上传到服务器！！
							saveCompressPic("", newName);
							initImageAdapter(newName);// 初始化图片适配器

							String toPath = sdcardRoot + IMAGE + File.separator;
							copyFile(picPath, toPath, str + ".jpg");// 将选择的图片复制到指定路径下

						} else {
							alert();
						}
					} else {
						alert();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		picUpload();
	}

	/**
	 * 初始化图片适配器
	 * 
	 * @param fname
	 */
	private void initImageAdapter(String fname) {

		list.add(sdcardRoot + IMAGE + File.separator + fname + ".jpg");
		if (list.size() > 0 && list.size() < 9) {
			myGirdView.setAdapter(new ImageAdapter(PublishGoodsActivity.this,
					list, metrics.widthPixels));
			// 显示上传进度
			showProgressDialog();
		} else {
			Toast.makeText(getApplicationContext(), "最多只能上传8张图片",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 显示图片上传进度
	 */
	@SuppressWarnings("unused")
	private void showProgressDialog() {
		final ProgressDialog proDia = ProgressDialog.show(this, "",
				"图片上传中,请稍候...");
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
				} finally {
					proDia.dismiss();
				}

			}
		}.start();
		proDia.show();
	}

	/**
	 * 复制文件
	 * 
	 * @param fromPath
	 * @param toPath
	 * @param newName
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void copyFile(String fromPath, String toPath, String newName)
			throws Exception {
		// 源文件
		FileInputStream in = new FileInputStream(new File(fromPath));
		// 目标文件
		FileOutputStream out = new FileOutputStream(new File(toPath, newName));
		byte[] buf = new byte[1024];
		int length = 0;
		while ((length = in.read(buf)) != -1) {
			out.write(buf, 0, length);
		}
		in.close();
		out.close();
	}

	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
	}

	/**
	 * 保存压缩图片(拍照和相册选择)到指定路径
	 * 
	 * @param path
	 * @param newName
	 * @throws Exception
	 */
	private void saveCompressPic(String path, String newName) throws Exception {

		Log.v("newName", newName);
		File file_02 = new File(sdcardRoot + IMAGE + File.separator, newName + ".jpg");
		FileOutputStream out = new FileOutputStream(file_02);

		Bitmap oldBitmap;
		// 调用方法，获取压缩图片
		if (!"".equals(path)) {
			oldBitmap = BitmapFactory.decodeFile(path);// 调用拍照得到的图片
			// 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
			int degree = Camera.readPictureDegree(path);
			oldBitmap = Camera.rotaingImageView(degree, oldBitmap); // 将图片旋转为正方向
		} else {
			// 调用相册选择得到的图片
			oldBitmap = BitmapFactory.decodeStream(cr.openInputStream(u));
		}
		// 按照指定尺寸大小压缩
		Bitmap mbitmap = Camera.scaleImg(oldBitmap, 100, 100);
		// 按指定质量大小压缩
		Bitmap sbitmap = Camera.compressImage(mbitmap);

		sbitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		out.flush();
		out.close();
	}

	/**
	 * 单击gridview单元格事件监听器
	 */
	private class OnItemClickListenerImpl implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ImageView showImg = new ImageView(PublishGoodsActivity.this);
			showImg.setScaleType(ImageView.ScaleType.CENTER); // 图片居中显示
			// 为imageView设置布局参数
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			showImg.setLayoutParams(layoutParams);

			index = position;
			String filename = list.get(position).replace("_mini", "");

			Log.v("widthpiels*heightPixels", metrics.widthPixels + "*"
					+ metrics.heightPixels);

			// 通过不同手机的分辨率，比例缩放图片
			Bitmap bitmap = Camera.getimage(filename, metrics.widthPixels,
					metrics.heightPixels);
			// Bitmap
			// thbm=ThumbnailUtils.extractThumbnail(bitmap,500,600);//生成指定大小缩略图

			// 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
			int degree = Camera.readPictureDegree(filename);

			Bitmap rotaBitmap = Camera.rotaingImageView(degree, bitmap); // 将图片旋转为正方向

			showImg.setImageBitmap(rotaBitmap); // 设置显示图片

			showBigImg(showImg); // 查看大图
		}
	}

	private class OnFocusChangeListenerImpl implements OnFocusChangeListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.view.View.OnFocusChangeListener#onFocusChange(android.view
		 * .View, boolean)
		 */
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (v.getId() == title.getId()) {
				String text = title.getText().toString();
				if (null != text && "".equals(text)) {
					if (hasFocus) {

					} else {
						JSONObject jsonObject = new JSONObject();
						try {
							jsonObject.put("title", text);
							json = String.valueOf(jsonObject);
						} catch (JSONException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}

	}

	/**
	 * 复制图片文件
	 * 
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void copyPic(String filePath, String fileName) throws Exception {
		// 源文件
		FileInputStream in = new FileInputStream(new File(filePath));
		// 目标文件
		FileOutputStream out = new FileOutputStream(new File(sdcardRoot + IMAGE
				+ File.separator, fileName + ".jpg"));
		byte[] buf = new byte[1024];
		int length = 0;
		while ((length = in.read(buf)) != -1) {
			out.write(buf, 0, length);
		}
		in.close();
		out.close();
	}

	/**
	 * 查看大图
	 * 
	 * @param iv
	 *            显示大图的ImageView组件
	 * 
	 */
	public void showBigImg(ImageView iv) {
		Dialog dialog = new AlertDialog.Builder(PublishGoodsActivity.this)
				.setView(iv)
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

						// 1,从sd卡删除
						File f1 = new File(list.get(index));// 缩略图
						File f2 = new File(list.get(index).replace("_mini", ""));// 原图
						if (f1.exists()) {
							f1.delete();
						}
						if (f2.exists()) {
							f2.delete();
						}
						// 2,向服务器发送请求...

						// 3,从显示视图删除，并更新到gridview
						list.remove(list.get(index));
						myGirdView.setAdapter(new ImageAdapter(
								PublishGoodsActivity.this, list,
								metrics.widthPixels));

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				}).create();
		dialog.show();
	}
}
