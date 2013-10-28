package com.astrium.hmas.bean.FeasibilityBean;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FeasibilityResult.java
 *   File Type                                          :               Source Code
 *   Description                                        :               FeasibilityResult bean : this class 
 *   																	describes the returned object after
 *   																	a feasibility tasking.
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

import com.astrium.hmas.bean.Point;

public class FeasibilityResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8559240970772638747L;
	public String xml;
	public String title;
	public String identifier;
	public String platform;
	public String sensor;
	public String instrument;
	public String resolution;
	public String orbitType;
	public List<Point> upperRight;
	public List<Point> upperLeft;
	public List<Point> lowerRight;
	public List<Point> lowerLeft;
	public String acquisitionDate;
	public String azimuth;
	public String elevation;
	public String alongTrack;
	public String acrossTrack;
	public String instrumentMode;
	public String compositeType;
	public String minLuminosity;
	public String polarizationMode;
	public String cloudCover;
	public String snowCover;
	public String maxNoiseLevel;
	public String statusMsg;
	public String maxSunGlint;
	public String haze;
	public String sandWind;
	
	public FeasibilityResult() {
		upperLeft = new ArrayList<Point>();
		upperRight = new ArrayList<Point>();
		lowerLeft = new ArrayList<Point>();
		lowerRight = new ArrayList<Point>();
	}
	

	public String getXml() {
		return xml;
	}



	public void setXml(String xml) {
		this.xml = xml;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getOrbitType() {
		return orbitType;
	}

	public void setOrbitType(String orbitType) {
		this.orbitType = orbitType;
	}

	public List<Point> getUpperRight() {
		return upperRight;
	}


	public void setUpperRight(List<Point> upperRight) {
		this.upperRight = upperRight;
	}


	public List<Point> getUpperLeft() {
		return upperLeft;
	}


	public void setUpperLeft(List<Point> upperLeft) {
		this.upperLeft = upperLeft;
	}


	public List<Point> getLowerRight() {
		return lowerRight;
	}


	public void setLowerRight(List<Point> lowerRight) {
		this.lowerRight = lowerRight;
	}


	public List<Point> getLowerLeft() {
		return lowerLeft;
	}


	public void setLowerLeft(List<Point> lowerLeft) {
		this.lowerLeft = lowerLeft;
	}


	public String getAcquisitionDate() {
		return acquisitionDate;
	}

	public void setAcquisitionDate(String acquisitionDate) {
		this.acquisitionDate = acquisitionDate;
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

	public String getAlongTrack() {
		return alongTrack;
	}

	public void setAlongTrack(String alongTrack) {
		this.alongTrack = alongTrack;
	}

	public String getAcrossTrack() {
		return acrossTrack;
	}

	public void setAcrossTrack(String acrossTrack) {
		this.acrossTrack = acrossTrack;
	}

	public String getInstrumentMode() {
		return instrumentMode;
	}

	public void setInstrumentMode(String instrumentMode) {
		this.instrumentMode = instrumentMode;
	}

	public String getCompositeType() {
		return compositeType;
	}

	public void setCompositeType(String compositeType) {
		this.compositeType = compositeType;
	}

	public String getMinLuminosity() {
		return minLuminosity;
	}

	public void setMinLuminosity(String minLuminosity) {
		this.minLuminosity = minLuminosity;
	}

	public String getPolarizationMode() {
		return polarizationMode;
	}

	public void setPolarizationMode(String polarizationMode) {
		this.polarizationMode = polarizationMode;
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

	public String getMaxNoiseLevel() {
		return maxNoiseLevel;
	}

	public void setMaxNoiseLevel(String maxNoiseLevel) {
		this.maxNoiseLevel = maxNoiseLevel;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
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
	
	
	
	

}
