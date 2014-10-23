/**
 * 
 */
package com.bigoranger.bigoranger.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bigoranger.bigoranger.domain.Address;
import com.bigoranger.bigoranger.domain.Category;
import com.bigoranger.bigoranger.domain.Service;

/**
 * Funtion：解析服务器端返回的json字符串
 * @author lionel
 * 
 */
public class JsonUtil {
	public static String userid = null;
	public static String imgid = null;
	public static int goodsid = 0;
	
	public static String parseJsonAddGoods(String jsonString){
		String msg = null;
		if(jsonString!=null&&jsonString.length()>0){
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonString);
				int status = jsonObject.optInt("status");
				msg = jsonObject.getString("msg");
				return msg;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return "未接收服务器数据！";
		
	}
	
	/**
	 * 
	 * Function：图片上传返回json解析
	 * author：lionel
	 * @param jsonString
	 * @return 上传成功  失败 具体内容由服务器端提供
	 */
	public static String parseJsonImageUpload(String jsonString){
		if(jsonString!=null&&jsonString.length()>0){
			try{
				JSONObject jsonObject = new JSONObject(jsonString);
				int status = jsonObject.optInt("status");
				String msg = jsonObject.getString("msg");
				if(status==1){
					goodsid = Integer.parseInt(jsonObject.getString("goodsid"));
					imgid = jsonObject.getString("imgid");
				}
				return msg;
			}catch(JSONException e){
				throw new RuntimeException("图片上传JSON转换异常"+e);
			}
		}
		return "服务器忙！图片上传参数异常";
	}
	
	/**
	 * 
	 * Function：获取分类数据 author：lionel
	 * 
	 * @param jsonString 
	 * @return 获取分类列表
	 */
 	public static List<Category> parseJsonGetCategory(String jsonString) {
		List<Category> list = new ArrayList<Category>();
		if (jsonString != null && jsonString.length() > 0) {
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				int status = jsonObject.optInt("status");
				String msg = jsonObject.getString("msg");

				/***************************************************************/
				if (status == 1) {
					JSONArray jsonArray = new JSONArray(msg);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						String id = jsonObject2.getString("Id");
						String title = jsonObject2.getString("Title");
						Category category = new Category(id, title);
						list.add(category);
					}
					/***************************************************************/
				}
			} catch (JSONException e) {
				throw new RuntimeException("服务器忙！"+e);
			}
		}
		return list;
	}

	/**
	 * 
	 * Function：初始化界面获取数据 author：lionel
	 */
	public static JsonForResult parseJsonFirst(String jsonString) {
		System.out.println(jsonString);
		List<Category> categories = new ArrayList<Category>();
		List<Address> addresses = new ArrayList<Address>();
		List<Service> services = new ArrayList<Service>();
		if (jsonString != null && jsonString.length() > 0) {
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				int status = Integer.parseInt(jsonObject.getString("status"));
				if (status == 1) {
					JSONArray cateArray = jsonObject.getJSONArray("category");
					JSONArray addrArray = jsonObject.getJSONArray("address");
					JSONArray servArray = jsonObject.getJSONArray("service");

					for (int i = 0; i < cateArray.length(); i++) {
						JSONObject categoryJson = cateArray.getJSONObject(i);
						String id = categoryJson.getString("Id");
						String title = categoryJson.getString("Title");
						Category category = new Category(id, title);
						categories.add(category);
					}
					for (int i = 0; i < addrArray.length(); i++) {
						JSONObject addressJson = addrArray.getJSONObject(i);
						String id = addressJson.getString("Id");
						String userId = addressJson.getString("UserId");
						userid = userId;
						String contacts = addressJson.getString("Contacts");
						String tel = addressJson.getString("Tel");
						String qq = addressJson.getString("QQ");
						String address = addressJson.getString("Address");
						String isDefault = addressJson.getString("IsDefault");
						String statusa = addressJson.getString("Status");
						Address addr = new Address(id, userId, contacts, tel,
								qq, address, isDefault, statusa);
						addresses.add(addr);
					}
					for (int i = 0; i < servArray.length(); i++) {
						JSONObject serviceJson = servArray.getJSONObject(i);
						String id = serviceJson.getString("Id");
						String title = serviceJson.getString("Title");
						String presentation = serviceJson
								.getString("Presentation");
						String price = serviceJson.getString("Price");
						String statuss = serviceJson.getString("Status");
						Service serv = new Service(id, title, presentation,
								price, statuss);
						services.add(serv);
					}
					JsonForResult result = new JsonForResult(categories,
							addresses, services);
					return result;
				}
			} catch (JSONException e) {
				throw new RuntimeException("服务器忙！"+e);
			}
		}
		return null;
	}
}
