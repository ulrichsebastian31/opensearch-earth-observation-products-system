/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas.bdd.objects;

/**
 *
 * @author re-sulrich
 */
public class SatellitePlatform {

    private String id;
    private String name;
    private String description;
    private String href;
    private String osf;
    private String tle;
    
    public SatellitePlatform(String id, String name, String description, String href) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.href = href;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getOsf() {
        return osf;
    }

    public void setOsf(String osf) {
        this.osf = osf;
    }

    public String getTle() {
        return tle;
    }

    public void setTle(String tle) {
        this.tle = tle;
    }
    
    
}
