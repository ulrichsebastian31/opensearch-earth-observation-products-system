/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DatabaseLoader.java
 *   File Type                                          :               Source Code
 *   Description                                        :                *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (coffee) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astrium.roseodbhandler;

import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author re-sulrich
 */
public class RoseoDatabaseLoader {
    
    private final RoseoDBOperations dboperations;

    public void closeConnection() throws SQLException {
        this.dboperations.connection.commit();
        this.dboperations.connection.close();
    }
    
    public RoseoDatabaseLoader(RoseoDBOperations dboperations) {
        
        this.dboperations = dboperations;
    }
    
    public RoseoDatabaseLoader(String databaseName) {
        
        this.dboperations = new RoseoDBOperations(databaseName);
    }
    
    public RoseoDatabaseLoader(String databaseURL, String user, String pass) {
        
        this.dboperations = new RoseoDBOperations(databaseURL, user, pass);
    }

    public RoseoDBOperations getDboperations() {
        return dboperations;
    }
    
    public List<List<String>> select(List<String> fields, String table, List<String> conditions) throws SQLException {
        
        return dboperations.select(fields, table, conditions);
    }
    
    public int count(String table, List<String> conditions) throws SQLException {
        
        return dboperations.count(table, conditions);
    }
}
