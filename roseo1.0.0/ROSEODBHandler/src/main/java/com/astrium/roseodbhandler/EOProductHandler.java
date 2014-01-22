/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astrium.roseodbhandler;

import com.astrium.roseodbhandler.structures.EOProduct;
import com.astrium.roseodbhandler.structures.Order;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.eads.astrium.dream.util.DateHandler;

/**
 *
 * @author re-cetienne
 */
public class EOProductHandler extends RoseoDatabaseLoader {

    public EOProductHandler() {
        super("ROSEODatabase");
    }

    public EOProductHandler(RoseoDBOperations dboperations) {
        super(dboperations);
    }

    public EOProductHandler(String databaseURL, String user, String pass) {
        super(databaseURL, user, pass);
    }

    public void addProduct(String productID, String collectionID, String options, String lastUpdate) throws SQLException {

        String table = "EOProduct";

        List<String> fields = new ArrayList<String>();

        fields.add("productID");
        fields.add("collectionID");
        fields.add("options");
        fields.add("lastUpdate");

        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + productID + "'");
        depl1.add("'" + collectionID + "'");
        depl1.add("'" + options + "'");
        depl1.add("'" + lastUpdate + "'");

        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);

        this.getDboperations().insert(
                table,
                fields,
                values);
    }

    /**
     * UPDATE Product SET collectionID=<collectionID> WHERE productID =
     * <productID>
     *
     * @param productID
     * @param collectionID
     * @throws SQLException
     */
    public void setProductCollectionID(String productID, String collectionID) throws SQLException {

        String table = "EOProduct";

        List<String> fields = new ArrayList<>();
        fields.add("collectionID");

        List<String> values = new ArrayList<>();
        values.add("'" + collectionID + "'");

        String condition = "productID='" + productID + "'";

        this.getDboperations().update(table, fields, values, condition);
    }

    public EOProduct getProduct(String productID) throws ParseException, SQLException {

        EOProduct prod = null;

        List<String> fields = new ArrayList<String>();
        fields.add("collectionID");
        fields.add("options");
        fields.add("lastUpdate");

        String table = "EOProduct";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<String>();
        conditions.add("productID='" + productID + "'");

        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {

            Date dateObj;
            dateObj = DateHandler.parseDate(result.get(0).get(2));
            prod = new EOProduct(
                    productID,
                    result.get(0).get(0),
                    result.get(0).get(1),
                    dateObj);

        }

        return prod;
    }

    public Map<String, EOProduct> getProductsLastUpdateLess(String date) throws ParseException, SQLException {

        Map<String, EOProduct> products = new HashMap<>();

        List<String> fields = new ArrayList<String>();
        fields.add("productID");
        fields.add("collectionID");
        fields.add("options");
        fields.add("lastUpdate");

        String table = "EOProduct";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<String>();
        Date dateBDD;
        dateBDD = DateHandler.parseDate(date);
        conditions.add("lastUpdate <= '" + dateBDD + "'");

        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Date dateObj;
                dateObj = DateHandler.parseDate(result.get(i).get(3));
                EOProduct prod = new EOProduct(
                        result.get(i).get(0),
                        result.get(i).get(1),
                        result.get(i).get(2),
                        dateObj);
                
                products.put(result.get(i).get(0), prod);
            }

        }

        return products;
    }
    
    public Map<String, EOProduct> getProductsLastUpdateGreater(String date) throws ParseException, SQLException {

        Map<String, EOProduct> products = new HashMap<>();

        List<String> fields = new ArrayList<String>();
        fields.add("productID");
        fields.add("collectionID");
        fields.add("options");
        fields.add("lastUpdate");

        String table = "EOProduct";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<String>();
        Date dateBDD;
        dateBDD = DateHandler.parseDate(date);
        conditions.add("lastUpdate >= '" + dateBDD + "'");

        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Date dateObj;
                dateObj = DateHandler.parseDate(result.get(i).get(3));
                EOProduct prod = new EOProduct(
                        result.get(i).get(0),
                        result.get(i).get(1),
                        result.get(i).get(2),
                        dateObj);
                
                products.put(result.get(i).get(0), prod);
            }

        }

        return products;
    }
    
    public Map<String, EOProduct> getProductsLastUpdateBetween(String date1,String date2) throws ParseException, SQLException {

        Map<String, EOProduct> products = new HashMap<>();

        List<String> fields = new ArrayList<String>();
        fields.add("productID");
        fields.add("collectionID");
        fields.add("options");
        fields.add("lastUpdate");

        String table = "EOProduct";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<String>();
        Date dateBDD1;
        Date dateBDD2;
        dateBDD1 = DateHandler.parseDate(date1);
        dateBDD2 = DateHandler.parseDate(date2);
        conditions.add("lastUpdate >= '"+dateBDD1+"' AND lastUpdate <= '"+dateBDD2+"'");

        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Date dateObj;
                dateObj = DateHandler.parseDate(result.get(i).get(3));
                EOProduct prod = new EOProduct(
                        result.get(i).get(0),
                        result.get(i).get(1),
                        result.get(i).get(2),
                        dateObj);
                
                products.put(result.get(i).get(0), prod);
            }

        }

        return products;
    }

    public List<String> getProductIDs(String collectionID) throws SQLException {

        List<String> products = new ArrayList<>();

        List<String> fields = new ArrayList<String>();
        fields.add("productID");

        String table = "EOProduct";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<String>();
        conditions.add("collectionID='" + collectionID + "'");

        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {
            for (List<String> list : result) {
                products.add(list.get(0));
            }
        }

        return products;
    }

}
