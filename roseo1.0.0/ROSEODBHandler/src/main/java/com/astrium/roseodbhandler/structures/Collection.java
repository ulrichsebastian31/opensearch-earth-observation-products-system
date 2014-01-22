/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.astrium.roseodbhandler.structures;

/**
 *
 * @author re-cetienne
 */
public class Collection {
    
    private String collectionID;
    private String options;

    public Collection(String collectionID, String options) {
        this.collectionID = collectionID;
        this.options = options;
    }

    public String getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(String collectionID) {
        this.collectionID = collectionID;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
    
    
    
}
