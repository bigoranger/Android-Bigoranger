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
 * @author lionel
 * 
 */
public class JsonUtil {
	/**
	 * 
	 * Function：获取分类数据 author：lionel
	 * 
	 * @param jsonString
	 * @return
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
