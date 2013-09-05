package com.astrium.hmas.bean;

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
