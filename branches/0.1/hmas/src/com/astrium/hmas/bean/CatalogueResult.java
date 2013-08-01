/**
 * 
 */
package com.astrium.hmas.bean;

import java.io.Serializable;


/**
 * @author re-cetienne
 *
 */
public class CatalogueResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7491339815880537431L;
	public String xml;
	public String id;
	public String title;
	public String start;
	public String end;
	public String time;
	public String platform;
	public String orbitType;
	public String instrument;
	public String sensorType;
	public String sensorMode;
	public String resolution;
	public String swathId;
	public String waveLenght;
	public String spectralRange;
	public String orbitNumber;
	public String orbitDirection;
	public String track;
	public String frame;
	public String identifier;
	public Point upperRight;
	public Point upperLeft;
	public Point lowerRight;
	public Point lowerLeft;
	public String acquisitionType;
	public String productType;
	public String status;
	public String archivingCenter;
	public String archivingDate;
	public String acquisitionStation;
	public String processingCenter;
	public String processingSoftware;
	public String processingDate;
	public String processingLevel;
	public String compositeType;
	public String contents;
	public String cloudCover;
	public String snowCover;
	
	public CatalogueResult() {
		super();
	}
	
	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getOrbitType() {
		return orbitType;
	}

	public void setOrbitType(String orbitType) {
		this.orbitType = orbitType;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

	public String getSensorMode() {
		return sensorMode;
	}

	public void setSensorMode(String sensorMode) {
		this.sensorMode = sensorMode;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getSwathId() {
		return swathId;
	}

	public void setSwathId(String swathId) {
		this.swathId = swathId;
	}

	public String getWaveLenght() {
		return waveLenght;
	}

	public void setWaveLenght(String waveLenght) {
		this.waveLenght = waveLenght;
	}

	public String getSpectralRange() {
		return spectralRange;
	}

	public void setSpectralRange(String spectralRange) {
		this.spectralRange = spectralRange;
	}

	public String getOrbitNumber() {
		return orbitNumber;
	}

	public void setOrbitNumber(String orbitNumber) {
		this.orbitNumber = orbitNumber;
	}

	public String getOrbitDirection() {
		return orbitDirection;
	}

	public void setOrbitDirection(String orbitDirection) {
		this.orbitDirection = orbitDirection;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getFrame() {
		return frame;
	}

	public void setFrame(String frame) {
		this.frame = frame;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Point getUpperRight() {
		return upperRight;
	}

	public void setUpperRight(Point upperRight) {
		this.upperRight = upperRight;
	}

	public Point getUpperLeft() {
		return upperLeft;
	}

	public void setUpperLeft(Point upperLeft) {
		this.upperLeft = upperLeft;
	}

	public Point getLowerRight() {
		return lowerRight;
	}

	public void setLowerRight(Point lowerRight) {
		this.lowerRight = lowerRight;
	}

	public Point getLowerLeft() {
		return lowerLeft;
	}

	public void setLowerLeft(Point lowerLeft) {
		this.lowerLeft = lowerLeft;
	}

	public String getAcquisitionType() {
		return acquisitionType;
	}

	public void setAcquisitionType(String acquisitionType) {
		this.acquisitionType = acquisitionType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getArchivingCenter() {
		return archivingCenter;
	}

	public void setArchivingCenter(String archivingCenter) {
		this.archivingCenter = archivingCenter;
	}

	public String getArchivingDate() {
		return archivingDate;
	}

	public void setArchivingDate(String archivingDate) {
		this.archivingDate = archivingDate;
	}

	public String getAcquisitionStation() {
		return acquisitionStation;
	}

	public void setAcquisitionStation(String acquisitionStation) {
		this.acquisitionStation = acquisitionStation;
	}

	public String getProcessingCenter() {
		return processingCenter;
	}

	public void setProcessingCenter(String processingCenter) {
		this.processingCenter = processingCenter;
	}

	public String getProcessingSoftware() {
		return processingSoftware;
	}

	public void setProcessingSoftware(String processingSoftware) {
		this.processingSoftware = processingSoftware;
	}

	public String getProcessingDate() {
		return processingDate;
	}

	public void setProcessingDate(String processingDate) {
		this.processingDate = processingDate;
	}

	public String getProcessingLevel() {
		return processingLevel;
	}

	public void setProcessingLevel(String processingLevel) {
		this.processingLevel = processingLevel;
	}

	public String getCompositeType() {
		return compositeType;
	}

	public void setCompositeType(String compositeType) {
		this.compositeType = compositeType;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
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

	
	
	

	

}
