/**
 * 
 */
package com.bigoranger.bigoranger.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bigoranger.bigoranger.domain.Address;

/**
 * @author Administrator
 * 
 */
public class AddressAdapter extends BaseAdapter {
	private List<Address> addresss;
	private Context context;

	/**
	 * @param addresss
	 * @param context
	 */
	public AddressAdapter(List<Address> addresss, Context context) {
		super();
		this.addresss = addresss;
		this.context = context;
	}

	@Override
	public int getCount() {
		return addresss.size();
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
		TextView tv = null;
		if (convertView != null) {
			tv = (TextView) convertView;
		} else {
			tv = new TextView(context);
		}
		Address address = addresss.get(position);
		tv.setText(address.getContacts() + "   " + address.getTel());
		return tv;
	}

}
