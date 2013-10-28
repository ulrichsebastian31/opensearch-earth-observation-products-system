package com.astrium.hmas.bean.FeasibilityBean;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Parameter.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Parameter bean : this class 
 *   																	describes the object returned to the 
 *   																	client after the OpenSearch description
 *   																	file parsing. The parameter object 
 *   																	contains one the available parameters
 *   																	to use and its different options.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Parameter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 196504742762325217L;
	public String name;
	public String key;
	public List<String> options;
	public String min;
	public String max;
	
	public Parameter() {
		super();
		this.options = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min2) {
		this.min = min2;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}
	
	
	
	

}
