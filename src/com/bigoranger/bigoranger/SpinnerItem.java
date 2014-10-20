/**
 * 
 */
package com.bigoranger.bigoranger;

/**
 * Function：
 * @author lionel
 *
 */
public class SpinnerItem {
	private String name;
	private String value;
	
	public SpinnerItem() {}
	
	public SpinnerItem(String name, String value){
		this.name = name;
		this.value = value;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
