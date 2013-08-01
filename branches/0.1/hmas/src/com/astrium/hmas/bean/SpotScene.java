package com.astrium.hmas.bean;

import java.io.Serializable;

public class SpotScene implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8400333461169347750L;
	public String id;
	public String metadata_url;
	public String image_url;
	public String acquisition_date;
	public String satellite;
	public double resolution;
	public String archiving_station;
	public double cloud_cover;
	public double snow_cover;
	public String shift;
	public String min_shift;
	public String max_shift;
	public Point scene_center;
	public Point upper_left;
	public Point upper_right;
	public Point lower_left;
	public Point lower_right;
	public double sun_azimuth;
	public double sun_elevation;
	public String tquality;
	
	public SpotScene() {
	}
	
	public SpotScene(String id, String metadata_url, String image_url,
			String acquisition_date, String satellite, double resolution,
			String archiving_resolution, double cloud_cover, double snow_cover,
			String shift, String min_shift, String max_shift,
			Point scene_center, Point upper_left, Point upper_right,
			Point lower_left, Point lower_right, double sun_azimuth,
			double sun_elevation, String tquality) {
		super();
		this.id = id;
		this.metadata_url = metadata_url;
		this.image_url = image_url;
		this.acquisition_date = acquisition_date;
		this.satellite = satellite;
		this.resolution = resolution;
		this.archiving_station = archiving_resolution;
		this.cloud_cover = cloud_cover;
		this.snow_cover = snow_cover;
		this.shift = shift;
		this.min_shift = min_shift;
		this.max_shift = max_shift;
		this.scene_center = scene_center;
		this.upper_left = upper_left;
		this.upper_right = upper_right;
		this.lower_left = lower_left;
		this.lower_right = lower_right;
		this.sun_azimuth = sun_azimuth;
		this.sun_elevation = sun_elevation;
		this.tquality = tquality;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMetadata_url() {
		return metadata_url;
	}
	public void setMetadata_url(String metadata_url) {
		this.metadata_url = metadata_url;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getAcquisition_date() {
		return acquisition_date;
	}
	public void setAcquisition_date(String acquisition_date) {
		this.acquisition_date = acquisition_date;
	}
	public String getSatellite() {
		return satellite;
	}
	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}
	public double getResolution() {
		return resolution;
	}
	public void setResolution(double resolution) {
		this.resolution = resolution;
	}
	public String getArchiving_resolution() {
		return archiving_station;
	}
	public void setArchiving_resolution(String archiving_resolution) {
		this.archiving_station = archiving_resolution;
	}
	public double getCloud_cover() {
		return cloud_cover;
	}
	public void setCloud_cover(double cloud_cover) {
		this.cloud_cover = cloud_cover;
	}
	public double getSnow_cover() {
		return snow_cover;
	}
	public void setSnow_cover(double snow_cover) {
		this.snow_cover = snow_cover;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getMin_shift() {
		return min_shift;
	}
	public void setMin_shift(String min_shift) {
		this.min_shift = min_shift;
	}
	public String getMax_shift() {
		return max_shift;
	}
	public void setMax_shift(String max_shift) {
		this.max_shift = max_shift;
	}
	public Point getScene_center() {
		return scene_center;
	}
	public void setScene_center(Point scene_center) {
		this.scene_center = scene_center;
	}
	public Point getUpper_left() {
		return upper_left;
	}
	public void setUpper_left(Point upper_left) {
		this.upper_left = upper_left;
	}
	public Point getUpper_right() {
		return upper_right;
	}
	public void setUpper_right(Point upper_right) {
		this.upper_right = upper_right;
	}
	public Point getLower_left() {
		return lower_left;
	}
	public void setLower_left(Point lower_left) {
		this.lower_left = lower_left;
	}
	public Point getLower_right() {
		return lower_right;
	}
	public void setLower_right(Point lower_right) {
		this.lower_right = lower_right;
	}
	public double getSun_azimuth() {
		return sun_azimuth;
	}
	public void setSun_azimuth(double sun_azimuth) {
		this.sun_azimuth = sun_azimuth;
	}
	public double getSun_elevation() {
		return sun_elevation;
	}
	public void setSun_elevation(double sun_elevation) {
		this.sun_elevation = sun_elevation;
	}
	public String getTquality() {
		return tquality;
	}
	public void setTquality(String tquality) {
		this.tquality = tquality;
	}
	
	

}
