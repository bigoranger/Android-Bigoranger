/**
 * 
 */
package com.bigoranger.bigoranger.domain;

/**
 * @author 地址
 *
 */
public class Address {
	private String id;
	private String userId;
	private String contacts;
	private String tel;
	private String qq;
	private String address;
	private String isDefault;
	private String status;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the contacts
	 */
	public String getContacts() {
		return contacts;
	}
	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the qq
	 */
	public String getQQ() {
		return qq;
	}
	/**
	 * @param qq the qq to set
	 */
	public void setQQ(String qq) {
		this.qq = qq;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the isDefault
	 */
	public String getIsDefault() {
		return isDefault;
	}
	/**
	 * @param isDefault the isDefault to set
	 */
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @param id
	 * @param userId
	 * @param contacts
	 * @param tel
	 * @param qq
	 * @param address
	 * @param isDefault
	 * @param status
	 */
	public Address(String id, String userId, String contacts, String tel,
			String qq, String address, String isDefault, String status) {
		super();
		this.id = id;
		this.userId = userId;
		this.contacts = contacts;
		this.tel = tel;
		this.qq = qq;
		this.address = address;
		this.isDefault = isDefault;
		this.status = status;
	}
	
	
}
