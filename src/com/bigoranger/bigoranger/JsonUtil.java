/**
 * 
 */
package com.bigoranger.bigoranger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Spinner;

/**
 * @author lionel
 * 
 */
public class JsonUtil {
	private static final int TIME_OUT = 10 * 10000000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码
	private static List<SpinnerItem> list_itemItems = null;

	public static List<String> parseJsonfromServer(String jsonString) {
		List<String> list = new ArrayList<String>();
		SpinnerItem spinner_Item = null;
		if(jsonString!=null && jsonString.length()>0){
			try {
				JSONObject jsonMap = new JSONObject(jsonString);
				Iterator<String> iterator = jsonMap.keys();
				while (iterator.hasNext()) {
					spinner_Item = new SpinnerItem();
					String name = iterator.next();
					String value = (String) jsonMap.get(name);

					spinner_Item.setName(name);
					spinner_Item.setValue(value);
					list.add(value);
					
					list_itemItems.add(spinner_Item);
				}
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}
		return list;
	}
	
	public String findNameByValue(String value){
		return "";
	}

	/**
	 * 
	 * Function：发送json数据到指定的URL author：lionel
	 * 
	 * @param json
	 * @param RequestURL
	 */
	public static void sendJson2Server(JSONObject json, String RequestURL) {
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
			conn.setRequestProperty("Content-Type", "application/json");

			if (json != null) {
				System.out.println(json);
				OutputStream os = conn.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				// 把JSON数据转换成String类型使用输出流向服务器写
				String content = String.valueOf(json);
				dos.write(content.getBytes());
				dos.flush();
				dos.close();

				String result = JsonUtil.recJsonfromServer(conn
						.getInputStream());
				System.out.println(result);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
