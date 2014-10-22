/**
 * 
 */
package com.bigoranger.bigoranger.adapter;

import java.util.List;

import com.bigoranger.bigoranger.domain.Service;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

/**
 * @author lionel
 *
 */
public class ServiceAdapter extends BaseAdapter {
	private List<Service> services;
	private Context context;
	
	/**
	 * @param services
	 * @param context
	 */
	public ServiceAdapter(List<Service> services, Context context) {
		super();
		this.services = services;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return services.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CheckBox chk = null;
		if(convertView!=null){
			chk = (CheckBox) convertView;
		}else{
			chk = new CheckBox(context);
			Service service = services.get(position);
			chk.setText(service.getTitle());
		}
		return chk;
	}
}
