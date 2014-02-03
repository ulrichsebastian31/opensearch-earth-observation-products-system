/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas.bdd.objects;

/**
 *
 * @author re-sulrich
 */
public class GroundStation {

    private String id;
    private String name;
    private String intId;
    private String antennaType;
    private double lon;
    private double lat;
    private double alt;
    
    
    /**
     * Default constructor
     */
    public GroundStation() {
        
    }
    
    /**
     * Constructor with parameters
     * @param id
     * @param name
     * @param intId
     * @param antennaType
     * @param lon
     * @param lat
     * @param alt 
     */
    public GroundStation(String id, String name, String intId, String antennaType, double lon, double lat, double alt) {
        this.id = id;
        this.name = name;
        this.intId = intId;
        this.antennaType = antennaType;
        this.lon = lon;
        this.lat = lat;
        this.alt = alt;
    }

    /**
     * Getters and setters 
     */
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }

    public String getAntennaType() {
        return antennaType;
    }

    public void setAntennaType(String antennaType) {
        this.antennaType = antennaType;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }
    
    
}
