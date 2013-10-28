package com.astrium.hmas.bean.FeasibilityBean;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FeasibilitySearch.java
 *   File Type                                          :               Source Code
 *   Description                                        :               FeasibilitySearch bean : this class 
 *   																	describes the object sent to the server
 *   																	representing a feasibility analysis 
 *   																	request.
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
import java.util.HashMap;
import java.util.Map;

public class FeasibilitySearch implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4884352374010670431L;
	public Map<String, String> parameters;
	public String platform;
	public String sensorType;
	public String instrument;
	public double nwlon;
	public double nwlat;
	public double selon;
	public double selat;
	public String start;
	public String end;
	public String azimuth;
	public String elevation;
	public String trackAlong;
	public String trackAcross;
	public String minLuminosity;
	public String polMode;
	public String cloudCover;
	public String snowCover;
	public String maxSunGlint;
	public String haze;
	public String sandWind;
	public String maxNoiseLevel;
	public String maxAmbiguityLevel;
	public String resolution;
	public String sensorMode;
	public String compositeType;
	public String coverageType;
	
	
	public FeasibilitySearch() {
		parameters = new HashMap<String, String>();
	}


	public Map<String, String> getParameters() {
		return parameters;
	}


	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}


	public String getPlatform() {
		return platform;
	}


	public void setPlatform(String platform) {
		this.platform = platform;
	}


	public String getSensorType() {
		return sensorType;
	}


	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}


	public String getInstrument() {
		return instrument;
	}


	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}


	public double getNwlon() {
		return nwlon;
	}


	public void setNwlon(double nwlon) {
		this.nwlon = nwlon;
	}


	public double getNwlat() {
		return nwlat;
	}


	public void setNwlat(double nwlat) {
		this.nwlat = nwlat;
	}


	public double getSelon() {
		return selon;
	}


	public void setSelon(double selon) {
		this.selon = selon;
	}


	public double getSelat() {
		return selat;
	}


	public void setSelat(double selat) {
		this.selat = selat;
	}


	public String getStart() {
		return start;
	}


	public void setStart(String start) {
		this.start = start;
	}


	public String getEnd() {
		return end;
	}


	public void setEnd(String end) {
		this.end = end;
	}


	public String getAzimuth() {
		return azimuth;
	}


	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}


	public String getElevation() {
		return elevation;
	}


	public void setElevation(String elevation) {
		this.elevation = elevation;
	}


	public String getTrackAlong() {
		return trackAlong;
	}


	public void setTrackAlong(String trackAlong) {
		this.trackAlong = trackAlong;
	}


	public String getTrackAcross() {
		return trackAcross;
	}


	public void setTrackAcross(String trackAcross) {
		this.trackAcross = trackAcross;
	}


	public String getMinLuminosity() {
		return minLuminosity;
	}


	public void setMinLuminosity(String minLuminosity) {
		this.minLuminosity = minLuminosity;
	}


	public String getPolMode() {
		return polMode;
	}


	public void setPolMode(String polMode) {
		this.polMode = polMode;
	}


	public String getCloudCover() {
		return cloudCover;
	}


	public void setCloudCover(String cloudCover) {
		this.cloudCover = cloudCover;
	}


	public String getSnowCover() {
		return snowCover;
	}


	public void setSnowCover(String snowCover) {
		this.snowCover = snowCover;
	}


	public String getMaxSunGlint() {
		return maxSunGlint;
	}


	public void setMaxSunGlint(String maxSunGlint) {
		this.maxSunGlint = maxSunGlint;
	}


	public String getHaze() {
		return haze;
	}


	public void setHaze(String haze) {
		this.haze = haze;
	}


	public String getSandWind() {
		return sandWind;
	}


	public void setSandWind(String sandWind) {
		this.sandWind = sandWind;
	}


	public String getMaxNoiseLevel() {
		return maxNoiseLevel;
	}


	public void setMaxNoiseLevel(String maxNoiseLevel) {
		this.maxNoiseLevel = maxNoiseLevel;
	}


	public String getMaxAmbiguityLevel() {
		return maxAmbiguityLevel;
	}


	public void setMaxAmbiguityLevel(String maxAmbiguityLevel) {
		this.maxAmbiguityLevel = maxAmbiguityLevel;
	}


	public String getResolution() {
		return resolution;
	}


	public void setResolution(String resolution) {
		this.resolution = resolution;
	}


	public String getSensorMode() {
		return sensorMode;
	}


	public void setSensorMode(String sensorMode) {
		this.sensorMode = sensorMode;
	}


	public String getCompositeType() {
		return compositeType;
	}


	public void setCompositeType(String compositeType) {
		this.compositeType = compositeType;
	}


	public String getCoverageType() {
		return coverageType;
	}


	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}
	
	
	
	
	

}
