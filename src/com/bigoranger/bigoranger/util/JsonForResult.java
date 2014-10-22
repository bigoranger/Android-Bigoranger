/**
 * 
 */
package com.bigoranger.bigoranger.util;

import java.util.List;

import com.bigoranger.bigoranger.domain.Address;
import com.bigoranger.bigoranger.domain.Category;
import com.bigoranger.bigoranger.domain.Service;

/**
 * @author lionel
 *
 */
public class JsonForResult {
	private List<Category> categories;
	private List<Address> addresses;
	private List<Service> services;
	/**
	 * @param categories
	 * @param addresses
	 * @param services
	 */
	public JsonForResult(List<Category> categories, List<Address> addresses,
			List<Service> services) {
		super();
		this.categories = categories;
		this.addresses = addresses;
		this.services = services;
	}
	/**
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	/**
	 * @return the addresses
	 */
	public List<Address> getAddresses() {
		return addresses;
	}
	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	/**
	 * @return the services
	 */
	public List<Service> getServices() {
		return services;
	}
	/**
	 * @param services the services to set
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}
}
