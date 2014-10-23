package com.bigoranger.bigoranger.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;


import android.os.StrictMode;
import android.util.Log;

/**
 * 
 * 实现文件上传的工具类
 * 
 * @Title:
 * @Description: 实现TODO
 * @Copyright:Copyright (c) 2011
 * @Company:易程科技股份有限公司
 * @Date:2012-7-2
 * @author longgangbai
 * @version 1.0
 */
public class UploadUtils {
	private static final String TAG = "uploadImg";
	private static final int TIME_OUT = 10 * 10000000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码
	public static final String SUCCESS = "1";
	public static final String FAILURE = "0";

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param RequestURL
	 *            请求的rul
	 * @return 返回响应的内容
	 */
	public static String uploadFile(File file, String RequestURL) {
		Log.v("filepaht", file.getPath());
		String BOUNDARY = "*****"; // 边界标识
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		String result = null;

		try {
			Log.e("URL",RequestURL);
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			
			/**
			 * 设置请求属性
			 */
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {
				 /*设置StrictMode 否则HTTPURLConnection连接失败，因为这是在主进程中进行网络连接*/
				 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
				
				 OutputStream outputSteam = conn.getOutputStream();

				DataOutputStream dos = new DataOutputStream(outputSteam);
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				
				/**
				 * 设置图片请求参数信息
				 */
				sb.append("Content-Disposition: form-data; name=\"Filedata\"; fileName=\"" + file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
								
				FileInputStream is = new FileInputStream(file);
				/**
				 * 采用缓冲区读取文件数据，一次读取8k
				 */
				byte[] bytes = new byte[8192];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					/* 数据写入DataOutputStream中 */
					dos.write(bytes, 0, len);
				}					
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				/* 关闭流，写入的东西自动生成Http正文*/
				is.close();
				dos.flush();
				dos.close();
			}
						
			int res = conn.getResponseCode();
			Log.e(TAG, "response code:" + res);
			String jsonString = HttpUtil.recJsonfromServer(conn.getInputStream());
            Log.v("jsonString", jsonString);
			result = JsonUtil.parseJsonImageUpload(jsonString);	
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
//			if (res == 200) {
//				return SUCCESS;
//			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		return FAILURE;
		return result;
	}
}