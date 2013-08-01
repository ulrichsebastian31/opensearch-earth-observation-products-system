package com.astrium.hmas.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CatalogueSearch implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7110663649768591236L;
	public Map<String, String> parameters;
	public String platform;
	public String orbitType;
	public String instrument;
	public String sensorType;
	public String sensorMode;
	public double resolution_min;
	public double resolution_max;
	public String swathId;
	public double waveLenght_min;
	public double waveLenght_max;
	public String spectralRange;
	public double orbitNumber_max;
	public double orbitNumber_min;
	public String orbitDirection;
	public double track_along;
	public double track_across;
	public double frame_min;
	public double frame_max;
	public String identifier;
	public String entryType;
	public String acquisitionType;
	public String status;
	public String archivingCenter;
	public String archivingDate_start;
	public String archivingDate_stop;
	public String acquisitionStation;
	public String processingCenter;
	public String processingSoftware;
	public String processingDate_start;
	public String processingDate_stop;
	public String processingLevel;
	public String compositeType;
	public String contents;
	public double cloudCover;
	public double snowCover;
	public double nwlat;
	public double nwlon;
	public double selat;
	public double selon;
	
	public CatalogueSearch() {
		parameters = new HashMap<String, String>();
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

	public Double getResolution_min() {
		return resolution_min;
	}

	public void setResolution_min(Double resolution_min) {
		this.resolution_min = resolution_min;
	}

	public Double getResolution_max() {
		return resolution_max;
	}

	public void setResolution_max(Double resolution_max) {
		this.resolution_max = resolution_max;
	}

	public String getSwathId() {
		return swathId;
	}

	public void setSwathId(String swathId) {
		this.swathId = swathId;
	}

	public Double getWaveLenght_min() {
		return waveLenght_min;
	}

	public void setWaveLenght_min(Double waveLenght_min) {
		this.waveLenght_min = waveLenght_min;
	}

	public Double getWaveLenght_max() {
		return waveLenght_max;
	}

	public void setWaveLenght_max(Double waveLenght_max) {
		this.waveLenght_max = waveLenght_max;
	}

	public String getSpectralRange() {
		return spectralRange;
	}

	public void setSpectralRange(String spectralRange) {
		this.spectralRange = spectralRange;
	}

	public double getOrbitNumber_max() {
		return orbitNumber_max;
	}

	public void setOrbitNumber_max(double orbitNumber_max) {
		this.orbitNumber_max = orbitNumber_max;
	}

	public double getOrbitNumber_min() {
		return orbitNumber_min;
	}

	public void setOrbitNumber_min(double orbitNumber_min) {
		this.orbitNumber_min = orbitNumber_min;
	}

	public String getOrbitDirection() {
		return orbitDirection;
	}

	public void setOrbitDirection(String orbitDirection) {
		this.orbitDirection = orbitDirection;
	}

	public Double getTrack_along() {
		return track_along;
	}

	public void setTrack_along(Double track_along) {
		this.track_along = track_along;
	}

	public Double getTrack_across() {
		return track_across;
	}

	public void setTrack_across(Double track_across) {
		this.track_across = track_across;
	}

	public Double getFrame_min() {
		return frame_min;
	}

	public void setFrame_min(Double frame_min) {
		this.frame_min = frame_min;
	}

	public Double getFrame_max() {
		return frame_max;
	}

	public void setFrame_max(Double frame_max) {
		this.frame_max = frame_max;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public String getAcquisitionType() {
		return acquisitionType;
	}

	public void setAcquisitionType(String acquisitionType) {
		this.acquisitionType = acquisitionType;
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

	public String getArchivingDate_start() {
		return archivingDate_start;
	}

	public void setArchivingDate_start(String archivingDate_start) {
		this.archivingDate_start = archivingDate_start;
	}

	public String getArchivingDate_stop() {
		return archivingDate_stop;
	}

	public void setArchivingDate_stop(String archivingDate_stop) {
		this.archivingDate_stop = archivingDate_stop;
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

	public String getProcessingDate_start() {
		return processingDate_start;
	}

	public void setProcessingDate_start(String processingDate_start) {
		this.processingDate_start = processingDate_start;
	}

	public String getProcessingDate_stop() {
		return processingDate_stop;
	}

	public void setProcessingDate_stop(String processingDate_stop) {
		this.processingDate_stop = processingDate_stop;
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

	public Double getCloudCover() {
		return cloudCover;
	}

	public void setCloudCover(Double cloudCover) {
		this.cloudCover = cloudCover;
	}

	public Double getSnowCover() {
		return snowCover;
	}

	public void setSnowCover(Double snowCover) {
		this.snowCover = snowCover;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public double getNwlat() {
		return nwlat;
	}

	public void setNwlat(double nwlat) {
		this.nwlat = nwlat;
	}

	public double getNwlon() {
		return nwlon;
	}

	public void setNwlon(double nwlon) {
		this.nwlon = nwlon;
	}

	public double getSelat() {
		return selat;
	}

	public void setSelat(double selat) {
		this.selat = selat;
	}

	public double getSelon() {
		return selon;
	}

	public void setSelon(double selon) {
		this.selon = selon;
	}
	
	
	
	

}
