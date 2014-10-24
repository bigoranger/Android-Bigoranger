package com.bigoranger.bigoranger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.bigoranger.bigoranger.adapter.AddressAdapter;
import com.bigoranger.bigoranger.adapter.CategoryAdapter;
import com.bigoranger.bigoranger.adapter.ImageAdapter;
import com.bigoranger.bigoranger.domain.Address;
import com.bigoranger.bigoranger.domain.Category;
import com.bigoranger.bigoranger.domain.Service;
import com.bigoranger.bigoranger.domain.URLs;
import com.bigoranger.bigoranger.util.HttpUtil;
import com.bigoranger.bigoranger.util.JsonForResult;
import com.bigoranger.bigoranger.util.JsonUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("UseSparseArrays")
public class PublishGoodsActivity extends Activity implements OnClickListener {
	private static final int TAKE_PHOTO = 1;
	private static final int SELECT_IMAGE = 2;

	private static final String IMAGE = "BigOranger" + File.separator + "Image";
	private String picPath = ""; // 图片路径
	public static String name; // 文件名称
	public static String sdcardRoot = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator;; // sd卡根路径
	private String fileName = ""; // 文件绝对路径
	private File file; // 文件
	private Uri u; // 从相册获取图片的uri

	private int imageCount = 0; // 图片数量

	private static DisplayMetrics metrics; // 手机屏幕分辨率
	ContentResolver cr;

	public List<String> list = new ArrayList<String>();// 用于存放图片的集合
	private int index;
	HashMap<Integer, String> chkMap = new HashMap<Integer, String>();
	private com.bigoranger.bigoranger.MyGridView myGirdView; // 网格

	private Button takePhoto, selectImage, saveBtn, backBtn;
	private EditText titleEdit, priceEdit, costPriceEdit, presentationEdit;
	private Spinner category_Spinner, address_Spinner, tradeWay_Spinner;
	private LinearLayout service_Linearlayout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.uploadpic);
		init();

		selectImage.setOnClickListener(this);
		takePhoto.setOnClickListener(this);
		myGirdView.setOnItemClickListener(new OnItemClickListenerImpl());
		titleEdit.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
		saveBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);

		/********************************************************************************/
		// 在指定位置创建用于存放图片文件的文件夹
		File f = new File(sdcardRoot + IMAGE);
		if (!f.exists()) {
			f.mkdirs();
		}
		metrics = new DisplayMetrics(); // 获取手机分辨率
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		/*********************************************************************************/
	}

	/**
	 * 
	 * Function：用户点击添加商品页面初始化 author：lionel
	 */
	@SuppressLint("HandlerLeak")
	private void init() {
		setContentView(R.layout.uploadpic);
		selectImage = (Button) findViewById(R.id.selectImage);
		takePhoto = (Button) findViewById(R.id.takePhoto);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		backBtn = (Button) findViewById(R.id.backBtn);

		titleEdit = (EditText) findViewById(R.id.title);
		priceEdit = (EditText) findViewById(R.id.price);
		costPriceEdit = (EditText) findViewById(R.id.costPrice);
		presentationEdit = (EditText) findViewById(R.id.presentation);

		category_Spinner = (Spinner) findViewById(R.id.category);
		address_Spinner = (Spinner) findViewById(R.id.address);
		tradeWay_Spinner = (Spinner) findViewById(R.id.tradeWay);
		myGirdView = (MyGridView) findViewById(R.id.myGridView);
		service_Linearlayout = (LinearLayout) findViewById(R.id.service_Linerlayout);

		String[] tItems = getResources().getStringArray(R.array.TradeWay);
		ArrayAdapter<String> tradAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, tItems);
		tradeWay_Spinner.setAdapter(tradAdapter);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String jsonString = (String) msg.obj;
				JsonForResult result = JsonUtil.parseJsonFirst(jsonString);

				List<Category> categories = result.getCategories();
				List<Address> addresses = result.getAddresses();
				List<Service> services = result.getServices();

				CategoryAdapter cateAdapter = new CategoryAdapter(categories,
						PublishGoodsActivity.this);
				AddressAdapter addrAdapter = new AddressAdapter(addresses,
						PublishGoodsActivity.this);

				category_Spinner.setAdapter(cateAdapter);
				address_Spinner.setAdapter(addrAdapter);

				Iterator<Service> iterator = services.iterator();

				while (iterator.hasNext()) {
					Service service = iterator.next();
					CheckBox chk = new CheckBox(PublishGoodsActivity.this);
					chk.setId(Integer.parseInt(service.getId()));
					chk.setText(service.getTitle());
					service_Linearlayout.addView(chk);
					chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								chkMap.put(buttonView.getId(),
										(String) buttonView.getText());
							}
						}
					});
				}
			}
		};

		// Thread thread = new Thread(new Runnable() {
		// @Override
		// public void run() {
		// String jsonString = HttpUtil.sendJson2Server(null,
		// URLs.ADD_GOODS_URL);
		// Message msg = Message.obtain();
		// msg.obj = jsonString;
		// handler.sendMessage(msg);
		// }
		// });
		// thread.start();

	}

	/**
	 * Function：事件监听处理
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectImage:
			selectImage();
			break;
		case R.id.takePhoto:
			takePhoto();
			break;
		case R.id.saveBtn:
			saveGoods();
			break;
		case R.id.backBtn:
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * Function：添加商品保存信息 author：lionel
	 */
	private void saveGoods() {

		String title = titleEdit.getText().toString().trim();
		String price = priceEdit.getText().toString().trim();
		String costPrice = costPriceEdit.getText().toString().trim();
		String presentation = presentationEdit.getText().toString().trim();
		int categoryId = category_Spinner.getSelectedView().getId();
		int addressId = address_Spinner.getSelectedView().getId();
		int tradeWayId = (int) tradeWay_Spinner.getSelectedItemId() + 1;
		StringBuilder sb = new StringBuilder();
		Set<Integer> chkIds = chkMap.keySet();
		Iterator<Integer> iterator = chkIds.iterator();
		while (iterator.hasNext()) {
			sb.append(iterator.next());
			if (iterator.hasNext()) {
				sb.append("|");
			}
		}
		String serviceId = sb.toString();
		int goodsid = JsonUtil.goodsid;
		try {
			final JSONObject saveJson = new JSONObject();
			saveJson.put("imgcount", imageCount);
			saveJson.put("GoodsId", goodsid);
			saveJson.put("Title", title);
			saveJson.put("Price", price);
			saveJson.put("CostPrice", costPrice);
			saveJson.put("Presentation", presentation);
			saveJson.put("CategoryId", categoryId);
			saveJson.put("AddressId", addressId);
			saveJson.put("TradeWay", tradeWayId);
			saveJson.put("Server", serviceId);

			final Handler saveHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					String jsonString = (String) msg.obj;
					String result = JsonUtil.parseJsonAddGoods(jsonString);
					Toast.makeText(PublishGoodsActivity.this, result,
							Toast.LENGTH_SHORT).show();
				}
			};

			new Thread(new Runnable() {

				@Override
				public void run() {
					String jsonString = HttpUtil.sendJson2Server(saveJson,
							URLs.SAVE_GOODS_INFO_URL);
					Message msg = Message.obtain();
					msg.obj = jsonString;
					saveHandler.sendMessage(msg);
				}

			}).start();

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Function：图片和普通上传 author：lionel
	 */
	private void picUpload(String userId, int goodsId) {
		if ((picPath != null && picPath.length() > 0)) {
			String userid = userId;
			int goodsid = goodsId;
			String param = "?userid=" + userid + "&goodsid=" + goodsid;
			UploadFileTask uploadFileTask = new UploadFileTask(this,
					URLs.IMAGE_UPLOAD_URL + param);
			Log.e("http", URLs.IMAGE_UPLOAD_URL + param);
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
			picPath = file.getPath();
			fileName = picPath;
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
	@SuppressLint("ShowToast")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {
				String str = null;
				if (!"".equals(fileName)) {
					str = fileName.substring(fileName.lastIndexOf("/") + 1,
							fileName.lastIndexOf("."));
				}
				String newName = str + "mini";
				try {
					// 以字节流的形式压缩，以便上传到服务器！！
					saveCompressPic(fileName, newName);
					initImageAdapter(newName);// 初始化图片适配器
					picPath = fileName;
				} catch (Exception e) {
					throw new RuntimeException("图片压缩失败！" + e);
				}
			}
		}

		if (requestCode == SELECT_IMAGE) { // 选择照片
			if (resultCode == Activity.RESULT_OK) {
				// 当选择的图片不为空的话，在获取到图片的途径
				u = data.getData();
				try {
					String[] pojo = { MediaStore.Images.Media.DATA };

					Cursor cursor = managedQuery(u, pojo, null, null, null);
					if (cursor != null) {
						cr = this.getContentResolver();
						int colunm_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String path = cursor.getString(colunm_index);

						if (path.endsWith("jpg") || path.endsWith("png")
								|| path.endsWith("jpeg")) {
							picPath = path;
							String str = DateFormat.format("yyyyMMdd_hhmmss",
									Calendar.getInstance(Locale.CHINA))
									.toString();
							String newName = str + "_mini";
							// 以字节流的形式压缩，以便上传到服务器！！
							// saveCompressPic(str, newName);
							saveCompressPic(picPath, newName);
							Log.e("newName", newName);
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
					// throw new RuntimeException("相册选择图片失败！请重新尝试..."+e);
					Toast.makeText(PublishGoodsActivity.this,
							"你选择的图片不存在，请重新选择", Toast.LENGTH_SHORT);
					selectImage();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		imageCount++;
		// picUpload(JsonUtil.userid,JsonUtil.goodsid);
	}

	/**
	 * 初始化图片适配器
	 * 
	 * @param fname
	 */
	private void initImageAdapter(String fname) {
		// 传入文件名，添加文件路径到list
		list.add(sdcardRoot + IMAGE + File.separator + fname + ".jpg");
		if (list.size() > 0 && list.size() < 9) {
			myGirdView.setAdapter(new ImageAdapter(PublishGoodsActivity.this,
					list, metrics.widthPixels));
			// 2秒后显示上传进度
			showProgressDialog();
		} else {
			Toast.makeText(getApplicationContext(), "最多只能上传8张图片",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 显示图片上传进度
	 */
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

		File file_02 = new File(sdcardRoot + IMAGE + File.separator, newName
				+ ".jpg");
		FileOutputStream out = new FileOutputStream(file_02);

		Bitmap oldBitmap;
		// 调用方法，获取压缩图片
		if (path != null || !"".equals(path)) {
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

			// 判断图片是否存在，不存在则更新视图
			if (!new File(filename).exists()) {
				Toast.makeText(getApplicationContext(), "图片文件不存在",
						Toast.LENGTH_SHORT).show();
				list.remove(position);
				myGirdView.setAdapter(new ImageAdapter(
						PublishGoodsActivity.this, list, metrics.widthPixels));
				return;
			}
			// 通过不同手机的分辨率，比例缩放图片
			Bitmap bitmap = Camera.getimage(filename, metrics.widthPixels,
					metrics.heightPixels);

			// 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
			int degree = Camera.readPictureDegree(filename);

			Bitmap rotaBitmap = Camera.rotaingImageView(degree, bitmap); // 将图片旋转为正方向

			showImg.setImageBitmap(rotaBitmap); // 设置显示图片

			showBigImg(showImg); // 查看大图
		}
	}

	/**
	 * Function：监听title失去焦点事件，发送请求到服务器
	 * 
	 * @author lionel
	 * 
	 */
	private class OnFocusChangeListenerImpl implements OnFocusChangeListener {
		@SuppressLint("HandlerLeak")
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (v.getId() == titleEdit.getId()) {
				if (!hasFocus) {
					String text = titleEdit.getText().toString().trim();

					if (text != null && text.length() > 0) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("Title", text);
						final JSONObject jsonObject = new JSONObject(map);
						final Handler getCate_handler = new Handler() {
							@Override
							public void handleMessage(Message msg) {
								String jsonString = (String) msg.obj;
								List<Category> categories = JsonUtil
										.parseJsonGetCategory(jsonString);
								category_Spinner.setAdapter(null);
								CategoryAdapter new_CateAdapter = new CategoryAdapter(
										categories, PublishGoodsActivity.this);
								category_Spinner.setAdapter(new_CateAdapter);
							}
						};

						new Thread(new Runnable() {
							@Override
							public void run() {
								String json = HttpUtil.sendJson2Server(
										jsonObject, URLs.GET_CATEGORY_URL);
								Message msg = Message.obtain();
								msg.obj = json;
								getCate_handler.sendMessage(msg);
							}
						}).start();
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
					@SuppressLint("ShowToast")
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

						// 1,从sd卡删除
						File f1 = new File(list.get(index));// 缩略图
						File f2 = new File(list.get(index).replace("_mini", ""));// 原图
						// 2,向服务器发送请求...
						String param = "?imgid=" + JsonUtil.imgid;
						JSONObject json = new JSONObject();
						try {
							json.put("imgid", JsonUtil.imgid);
						} catch (JSONException e) {
							throw new RuntimeException(e);
						}
						String jsonString = HttpUtil.sendJson2Server(json, URLs.DELIMG_URL);
						JSONObject result = JsonUtil.parseJsonDelImg(jsonString);
						int status = result.optInt("status");
						String msg = "";
						try {
							msg = result.getString("msg");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if(status==1){
							if (f1.exists()) {
								f1.delete();
							}
							if (f2.exists()) {
								f2.delete();
							}
							// 3,从显示视图删除，并更新到gridview
							list.remove(list.get(index));
							myGirdView.setAdapter(new ImageAdapter(
									PublishGoodsActivity.this, list,
									metrics.widthPixels));
						}
						Toast.makeText(PublishGoodsActivity.this, msg, Toast.LENGTH_SHORT);
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
