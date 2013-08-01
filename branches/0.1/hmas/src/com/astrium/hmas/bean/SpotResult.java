package com.astrium.hmas.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



public class SpotResult implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152527853451710081L;
	public String code;
	public String message;
	public Map<String, SpotScene> spotScenes = new HashMap<String, SpotScene>();
	
	public SpotResult() {
	}

	public SpotResult(String code, String message, HashMap<String, SpotScene> spotScenes) {
		this.code = code;
		this.message = message;
		this.spotScenes = spotScenes;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, SpotScene> getScenes() {
		return spotScenes;
	}

	public void setScenes(Map<String, SpotScene> spotScenes) {
		this.spotScenes = spotScenes;
	}
	
	
	
	

}
