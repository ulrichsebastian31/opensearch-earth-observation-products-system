/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.astrium.roseodbhandler;

import com.astrium.roseodbhandler.structures.Collection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author re-cetienne
 */
public class CollectionHandler extends RoseoDatabaseLoader {
    
    public CollectionHandler(){
        super("ROSEODatabase");
    }

    public CollectionHandler(RoseoDBOperations dboperations) {
        super(dboperations);
    }

    public CollectionHandler(String databaseURL, String user, String pass) {
        super(databaseURL, user, pass);
    }
    
    public void addCollection(String collectionID, String options) throws SQLException {


        String table = "collection";

        List<String> fields = new ArrayList<String>();
        fields.add("collectionID");
        fields.add("options");

        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + collectionID + "'");
        depl1.add("'" + options + "'");

        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);

        this.getDboperations().insert(
                table,
                fields,
                values);

    }
    
    public Collection getCollection(String collectionID) throws ParseException, SQLException {

        Collection collection = null;

        List<String> fields = new ArrayList<String>();
        fields.add("options");

        String table = "collection";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<String>();
        conditions.add("collectionID='" + collectionID + "'");

        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {

            collection = new Collection(
                    collectionID,
                    result.get(0).get(0));

        }
        
         return collection;
    }
    
    public Map<String,Collection> getCollections() throws ParseException, SQLException {

        Map<String,Collection> collections = new HashMap<>();

        List<String> fields = new ArrayList<String>();
        fields.add("collectionID");
        fields.add("options");

        String table = "collection";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<String>();

        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                
                Collection collection = new Collection(
                    result.get(i).get(0),
                    result.get(i).get(1));
                
                collections.put(result.get(i).get(0), collection);
            }

        }
        
         return collections;
    }
    
}
