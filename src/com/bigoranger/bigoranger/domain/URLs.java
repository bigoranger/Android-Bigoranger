/**
 * 
 */
package com.bigoranger.bigoranger.domain;

import java.io.Serializable;

/**
 * @author lionel
 * 
 */
public class URLs implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String HOST = "http://121.40.187.66";				//主机
	public final static String ADD_GOODS_URL = HOST + "/Api/Goods/add";				//添加商品
	public final static String GET_CATEGORY_URL = HOST	+ "/Api/Goods/getcategory"; //获取分类
	public final static String IMAGE_UPLOAD_URL = HOST + "/Api/Goods/upload";		//上传图片
	public final static String DELIMG_URL = HOST + "/Api/Goods/delimg";				//删除图片
	public final static String SAVE_GOODS_INFO_URL = HOST + "/Api/Goods/save";		//保存卖家商品信息
	
}
