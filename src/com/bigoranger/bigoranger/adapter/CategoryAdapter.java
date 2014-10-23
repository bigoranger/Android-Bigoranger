/**
 * 
 */
package com.bigoranger.bigoranger.adapter;

import java.util.List;

import com.bigoranger.bigoranger.domain.Category;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author lionel
 *
 */
public class CategoryAdapter extends BaseAdapter {
	private List<Category> categories;
	private Context context;
	
	/**
	 * @param categories
	 * @param context
	 */
	public CategoryAdapter(List<Category> categories, Context context) {
		super();
		this.categories = categories;
		this.context = context;
	}

	@Override
	public int getCount() {
		return categories.size();
	}

	@Override
	public Object getItem(int position) {
		return categories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = null;
		if(convertView!=null){
			tv = (TextView) convertView;
		}else{
			tv = new TextView(context);
		}
		Category category = categories.get(position);
		tv.setId(Integer.parseInt(category.getId()));
		tv.setText(category.getTitle());
		return tv;
	}

}
