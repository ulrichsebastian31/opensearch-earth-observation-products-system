package com.astrium.hmas.bean;

import java.io.Serializable;

public class SpotSearch implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5273941212432218753L;
	
	public String key;
	public String output;
	//TODO : changer en Date
	public String start_date;
	public String end_date;
	public double max_cloud;
	public double max_snow;
	public double max_incidence;
	public double min_resolution;
	public double max_resolution;
	public String zone_type;
	public double circle_lon;
	public double circle_lat;
	public double circle_rad;
	public double nwlat;
	public double nwlon;
	public double selat;
	public double selon;
	public String points;
	public String station_code;
	public String satellite;
	public String sensor_family;
	public String scene_fullness;
	
	public SpotSearch() {
	}

	public SpotSearch(String key, String output, String start_date, String end_date,
			double max_cloud, double max_snow, double max_incidence,
			double min_resolution, double max_resolution, String zone_type,
			double circle_lon, double circle_lat, double circle_rad,
			double nwlat, double nwlon, double selat, double selon,
			String points, String station_code, String satellite,
			String sensor_family, String scene_fullness) {
		super();
		this.key = key;
		this.output = output;
		this.start_date = start_date;
		this.end_date = end_date;
		this.max_cloud = max_cloud;
		this.max_snow = max_snow;
		this.max_incidence = max_incidence;
		this.min_resolution = min_resolution;
		this.max_resolution = max_resolution;
		this.zone_type = zone_type;
		this.circle_lon = circle_lon;
		this.circle_lat = circle_lat;
		this.circle_rad = circle_rad;
		this.nwlat = nwlat;
		this.nwlon = nwlon;
		this.selat = selat;
		this.selon = selon;
		this.points = points;
		this.station_code = station_code;
		this.satellite = satellite;
		this.sensor_family = sensor_family;
		this.scene_fullness = scene_fullness;
	}
	
	

	public SpotSearch(String key, String output, String start_date, String end_date,
			double max_cloud, double max_incidence, double min_resolution,
			double max_resolution, String zone_type, double nwlat,
			double nwlon, double selat, double selon) {
		super();
		this.key = key;
		this.output = output;
		this.start_date = start_date;
		this.end_date = end_date;
		this.max_cloud = max_cloud;
		this.max_incidence = max_incidence;
		this.min_resolution = min_resolution;
		this.max_resolution = max_resolution;
		this.zone_type = zone_type;
		this.nwlat = nwlat;
		this.nwlon = nwlon;
		this.selat = selat;
		this.selon = selon;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public double getMax_cloud() {
		return max_cloud;
	}

	public void setMax_cloud(double max_cloud) {
		this.max_cloud = max_cloud;
	}

	public double getMax_snow() {
		return max_snow;
	}

	public void setMax_snow(double max_snow) {
		this.max_snow = max_snow;
	}

	public double getMax_incidence() {
		return max_incidence;
	}

	public void setMax_incidence(double max_incidence) {
		this.max_incidence = max_incidence;
	}

	public double getMin_resolution() {
		return min_resolution;
	}

	public void setMin_resolution(double min_resolution) {
		this.min_resolution = min_resolution;
	}

	public double getMax_resolution() {
		return max_resolution;
	}

	public void setMax_resolution(double max_resolution) {
		this.max_resolution = max_resolution;
	}

	public String getZone_type() {
		return zone_type;
	}

	public void setZone_type(String zone_type) {
		this.zone_type = zone_type;
	}

	public double getCircle_lon() {
		return circle_lon;
	}

	public void setCircle_lon(double circle_lon) {
		this.circle_lon = circle_lon;
	}

	public double getCircle_lat() {
		return circle_lat;
	}

	public void setCircle_lat(double circle_lat) {
		this.circle_lat = circle_lat;
	}

	public double getCircle_rad() {
		return circle_rad;
	}

	public void setCircle_rad(double circle_rad) {
		this.circle_rad = circle_rad;
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

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getStation_code() {
		return station_code;
	}

	public void setStation_code(String station_code) {
		this.station_code = station_code;
	}

	public String getSatellite() {
		return satellite;
	}

	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}

	public String getSensor_family() {
		return sensor_family;
	}

	public void setSensor_family(String sensor_family) {
		this.sensor_family = sensor_family;
	}

	public String getScene_fullness() {
		return scene_fullness;
	}

	public void setScene_fullness(String scene_fullness) {
		this.scene_fullness = scene_fullness;
	}
	
	
	
	

}
