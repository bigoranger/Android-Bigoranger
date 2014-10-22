/**
 * 
 */
package com.bigoranger.bigoranger.domain;

/**
 * @author 服务
 *
 */
public class Service {
	private String id;
	private String title;
	private String presentation;
	private String price;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the presentation
	 */
	public String getPresentation() {
		return presentation;
	}
	/**
	 * @param presentation the presentation to set
	 */
	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
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
	 * @param title
	 * @param presentation
	 * @param price
	 * @param status
	 */
	public Service(String id, String title, String presentation, String price,
			String status) {
		super();
		this.id = id;
		this.title = title;
		this.presentation = presentation;
		this.price = price;
		this.status = status;
	}
	/**
	 * 
	 */
	public Service() {
		// TODO Auto-generated constructor stub
	}
	
}
