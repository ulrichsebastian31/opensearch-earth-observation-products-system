/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas.bdd.objects;

/**
 * This class permits to load sensor 
 * @author re-sulrich
 */
public class Sensor {

    @Override
    public String toString() {
        return "Sensor{" + "sensorId=" + sensorId + ", sensorName=" + sensorName + ", sensorDescription=" + sensorDescription + ", sensorType=" + sensorType + ", bandType=" + bandType + ", groundResolution=" + groundResolution + ", minLatitude=" + minLatitude + ", maxLatitude=" + maxLatitude + ", minLongitude=" + minLongitude + ", maxLongitude=" + maxLongitude + ", sdfFile=" + sdfFile + '}';
    }

    
    private String sensorId;
    private String sensorName;
    private String sensorDescription;
    private String sensorType;
    private String bandType;
    private String groundResolution;
    private String minLatitude;
    private String maxLatitude;
    private String minLongitude;
    private String maxLongitude;
    private String sdfFile;
    
    /**
     * Constructor
     * Doesn't include the SDF file
     * @param sensorId
     * @param sensorName
     * @param sensorDescription
     * @param sensorType
     * @param bandType
     * @param groundResolution
     * @param minLatitude
     * @param maxLatitude
     * @param minLongitude
     * @param maxLongitude 
     */
    public Sensor(String sensorId, String sensorName, String sensorDescription, String sensorType, String bandType, String groundResolution, String minLatitude, String maxLatitude, String minLongitude, String maxLongitude) {
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.sensorDescription = sensorDescription;
        this.sensorType = sensorType;
        this.bandType = bandType;
        this.groundResolution = groundResolution;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
    }

    /**
     * Getters and setters
     */
    
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorDescription() {
        return sensorDescription;
    }

    public void setSensorDescription(String sensorDescription) {
        this.sensorDescription = sensorDescription;
    }
    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getBandType() {
        return bandType;
    }

    public void setBandType(String bandType) {
        this.bandType = bandType;
    }

    public String getGroundResolution() {
        return groundResolution;
    }

    public void setGroundResolution(String groundResolution) {
        this.groundResolution = groundResolution;
    }

    public String getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(String minLatitude) {
        this.minLatitude = minLatitude;
    }

    public String getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(String maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public String getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(String minLongitude) {
        this.minLongitude = minLongitude;
    }

    public String getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(String maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    public String getSdfFile() {
        return sdfFile;
    }

    public void setSdfFile(String sdfFile) {
        this.sdfFile = sdfFile;
    }
    
    /**
     * END Getters and setters
     */
}
