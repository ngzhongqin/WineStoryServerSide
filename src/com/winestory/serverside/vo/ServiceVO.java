package com.winestory.serverside.vo;

import java.io.Serializable;

public class ServiceVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serviceID;
	private String serviceClass;
	private String query;
	
	public String getServiceID() {
		return serviceID;
	}
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	public String getServiceClass() {
		return serviceClass;
	}
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	
}
