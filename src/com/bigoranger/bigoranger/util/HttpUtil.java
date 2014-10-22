/**
 * 
 */
package com.bigoranger.bigoranger.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 * @author lionel
 * 
 */
public class HttpUtil {
	private static final int TIME_OUT = 10 * 10000000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码

	/**
	 * 
	 * Function：发送json数据到指定的URL author：lionel
	 * @param json
	 * @param RequestURL
	 */
	public static String sendJson2Server(JSONObject json, String RequestURL) {
		String result = "";
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type",
					"text/xml");

			if (json != null) {
				System.out.println(json);
				OutputStream os = conn.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				// 把JSON数据转换成String类型使用输出流向服务器写
				String content = String.valueOf(json);

				dos.write(content.getBytes());
				dos.flush();
				dos.close();

				result = HttpUtil.recJsonfromServer(conn
						.getInputStream());
				System.out.println(result);
			}else{
				conn.connect();
				result = HttpUtil.recJsonfromServer(conn
						.getInputStream());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 
	 * Function：处理服务器响应结果 author：lionel
	 * 
	 * @param in
	 * @return
	 */
	public static String recJsonfromServer(InputStream in) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, CHARSET));
			StringBuilder sb = new StringBuilder();
			String msg = null;
			while ((msg = reader.readLine()) != null) {
				sb.append(msg);
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
