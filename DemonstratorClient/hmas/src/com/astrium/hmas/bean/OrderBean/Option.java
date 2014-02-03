package com.astrium.hmas.bean.OrderBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Option.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Option bean : this class 
 *   																	describes the returned object after
 *   																	the user has choosed the options to
 *   																	order the desired product.
 *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (C) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 */
public class Option implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1972111374881752368L;
	
	public String name;
	public String identifier;
	public List<String> allowedTokens;
	public String xml;
	public String idGroup;
	
	public Option() {
		this.allowedTokens = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<String> getAllowedTokens() {
		return allowedTokens;
	}

	public void setAllowedTokens(List<String> allowedTokens) {
		this.allowedTokens = allowedTokens;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(String idGroup) {
		this.idGroup = idGroup;
	}
	
	
	
	
	

}
