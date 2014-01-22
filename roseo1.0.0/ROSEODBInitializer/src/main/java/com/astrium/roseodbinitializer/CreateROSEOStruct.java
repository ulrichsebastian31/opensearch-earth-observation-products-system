/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astrium.roseodbinitializer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author re-cetienne
 */
public class CreateROSEOStruct {

    public static void createROSEOStruct() throws SQLException {

        ConnexionParameter.setRoseoDatabase("ROSEODatabase");
        Connexion.conn = Connexion.createConnexion();
        Connexion.testDrop();
        
        createEOProductTable();
        createCollectionTable();
        createOrderTable();
        createOrderItemTable();
    }
    
     public static void createEOProductTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"productID", "varchar(100) primary key"});
        fields.add(new String[]{"collectionID", "varchar(100)"});
        fields.add(new String[]{"options", "text"});
        fields.add(new String[]{"lastUpdate", "timestamp"});
        
        Connexion.create("EOProduct", fields);
    }
     
     public static void createCollectionTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"collectionID", "varchar(100) primary key"});
        fields.add(new String[]{"options", "text"});
        
        Connexion.create("collection", fields);
    }
     
     public static void createOrderTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"orderID", "varchar(100) primary key"});
        fields.add(new String[]{"status", "varchar(100)"});
        fields.add(new String[]{"lastUpdate", "timestamp"});
        fields.add(new String[]{"orderReference", "varchar(100)"});
        fields.add(new String[]{"orderItems", "varchar(1024)"});
        
        Connexion.create("\"order\"", fields);
    }
     
     public static void createOrderItemTable() throws SQLException {
        List<String[]> fields = new ArrayList<String[]>();
        
        fields.add(new String[]{"itemID", "varchar(100) primary key"});
        fields.add(new String[]{"eoProductID", "varchar(100)"});
        fields.add(new String[]{"status", "varchar(100)"});
        fields.add(new String[]{"optionsSet", "text"});
        
        Connexion.create("orderItem", fields);
    }
}
