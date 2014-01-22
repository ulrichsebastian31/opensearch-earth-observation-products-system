/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.astrium.roseodbhandler;

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
public class OrderHandler extends RoseoDatabaseLoader{

    public OrderHandler() {
        super("ROSEODatabase");
    }

    public OrderHandler(RoseoDBOperations dboperations) {
        super(dboperations);
    }

    public OrderHandler(String databaseURL, String user, String pass) {
        super(databaseURL, user, pass);
    }
    
    public void addOrder(String orderID, String status,String lastUpdate,String orderRefence, String orderItems) throws SQLException, ParseException {

        String table = "\"order\"";

        List<String> fields = new ArrayList<String>();
        
        fields.add("orderID");
        fields.add("status");
        fields.add("lastUpdate");
        fields.add("orderReference");
        fields.add("orderItems");
        
//        Date lastUpdateBDD = DateHandler.parseBDDDate(lastUpdate);
        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + orderID + "'");
        depl1.add("'" + status + "'");
        depl1.add("'" + lastUpdate + "'");
        depl1.add("'" + orderRefence + "'");
        depl1.add("'" + orderItems + "'");

        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);

        this.getDboperations().insert(
                table,
                fields,
                values);
    }
    
    /**
     * UPDATE Order SET status=<status> WHERE orderID =
     * <orderID>
     *
     * @param orderID
     * @param status
     * @throws SQLException
     */
    public void setOrderStatus(String orderID, String status) throws SQLException {

        String table = "\"order\"";

        List<String> fields = new ArrayList<>();
        fields.add("status");

        List<String> values = new ArrayList<>();
        values.add("'" + status + "'");

        String condition = "orderID='" + orderID + "'";

        this.getDboperations().update(table, fields, values, condition);
    }
    
    public Order getOrder(String orderID) throws ParseException, SQLException {

        Order order = null;

        List<String> fields = new ArrayList<String>();
        fields.add("status");
        fields.add("lastUpdate");
        fields.add("orderReference");
        fields.add("orderItems");

        String table = "\"order\"";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<String>();
        conditions.add("orderID='" + orderID + "'");

        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {
            
            Date d;
            
            d = DateHandler.parseDate(result.get(0).get(1));

            order = new Order(
                    orderID,
                    result.get(0).get(0),
                    d, 
                    result.get(0).get(2),
                    result.get(0).get(3));

        }
        
         return order;
    }
    
    public Map<String,Order> getOrdersLastUpdateLess(String date) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("status");
        fields.add("lastUpdate");
        fields.add("orderReference");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD = null;
        dateBDD = DateHandler.parseDate(date);
        conditions.add("lastUpdate <= '"+dateBDD+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(2));
                Order order = new Order(result.get(i).get(0), result.get(i).get(1), dateObj, result.get(i).get(3), result.get(i).get(4));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateLessAndStatus(String date, String status) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("lastUpdate");
        fields.add("orderReference");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD = null;
        dateBDD = DateHandler.parseDate(date);
        conditions.add("lastUpdate <= '"+dateBDD+"'");
        conditions.add("status='"+status+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(1));
                Order order = new Order(result.get(i).get(0), status, dateObj, result.get(i).get(2), result.get(i).get(3));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateLessAndReference(String date, String reference) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("status");
        fields.add("lastUpdate");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD = null;
        dateBDD = DateHandler.parseDate(date);
        conditions.add("lastUpdate <= '"+dateBDD+"'");
        conditions.add("orderReference='"+reference+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(2));
                Order order = new Order(result.get(i).get(0), result.get(i).get(1), dateObj, reference, result.get(i).get(3));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateLessAndStatusAndReference(String date,String status, String reference) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("lastUpdate");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD = null;
        dateBDD = DateHandler.parseDate(date);
        conditions.add("lastUpdate <= '"+dateBDD+"'");
        conditions.add("status='"+status+"'");
        conditions.add("orderReference='"+reference+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(1));
                Order order = new Order(result.get(i).get(0), status, dateObj, reference, result.get(i).get(2));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateGreater(String date) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("status");
        fields.add("lastUpdate");
        fields.add("orderReference");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD = null;
        dateBDD = DateHandler.parseDate(date);
        conditions.add("lastUpdate >= '"+dateBDD+"'");
        
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(2));
                Order order = new Order(result.get(i).get(0), result.get(i).get(1), dateObj, result.get(i).get(3), result.get(i).get(4));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateGreaterAndStatus(String date, String status) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("lastUpdate");
        fields.add("orderReference");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD = null;
        dateBDD = DateHandler.parseDate(date);
        conditions.add("lastUpdate >= '"+dateBDD+"'");
        conditions.add("status='"+status+"'");
        
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(1));
                Order order = new Order(result.get(i).get(0), status, dateObj, result.get(i).get(2), result.get(i).get(3));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateGreaterAndReference(String date, String reference) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("status");
        fields.add("lastUpdate");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD = null;
        dateBDD = DateHandler.parseDate(date);
        conditions.add("lastUpdate >= '"+dateBDD+"'");
        conditions.add("orderReference='"+reference+"'");
        
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(2));
                Order order = new Order(result.get(i).get(0), result.get(i).get(1), dateObj, reference, result.get(i).get(3));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateGreaterAndStatusAndReference(String date,String status, String reference) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("lastUpdate");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD = null;
        dateBDD = DateHandler.parseDate(date);
        conditions.add("lastUpdate >= '"+dateBDD+"'");
        conditions.add("status='"+status+"'");
        conditions.add("orderReference='"+reference+"'");
        
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(1));
                Order order = new Order(result.get(i).get(0), status, dateObj, reference, result.get(i).get(2));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateBetween(String date1,String date2) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("status");
        fields.add("lastUpdate");
        fields.add("orderReference");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD1 = null;
        Date dateBDD2 = null;
        dateBDD1 = DateHandler.parseDate(date1);
        dateBDD2 = DateHandler.parseDate(date2);
        conditions.add("lastUpdate >= '"+dateBDD1+"' AND lastUpdate <= '"+dateBDD2+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(2));
                Order order = new Order(result.get(i).get(0), result.get(i).get(1), dateObj, result.get(i).get(3), result.get(i).get(4));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateBetweenAndStatus(String date1,String date2, String status) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("lastUpdate");
        fields.add("orderReference");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD1 = null;
        Date dateBDD2 = null;
        dateBDD1 = DateHandler.parseDate(date1);
        dateBDD2 = DateHandler.parseDate(date2);
        conditions.add("lastUpdate >= '"+dateBDD1+"' AND lastUpdate <= '"+dateBDD2+"'");
        conditions.add("status='"+status+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(1));
                Order order = new Order(result.get(i).get(0), status, dateObj, result.get(i).get(2), result.get(i).get(3));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateBetweenAndReference(String date1,String date2, String reference) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("status");
        fields.add("lastUpdate");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD1 = null;
        Date dateBDD2 = null;
        dateBDD1 = DateHandler.parseDate(date1);
        dateBDD2 = DateHandler.parseDate(date2);
        conditions.add("lastUpdate >= '"+dateBDD1+"' AND lastUpdate <= '"+dateBDD2+"'");
        conditions.add("orderReference='"+reference+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(2));
                Order order = new Order(result.get(i).get(0), result.get(i).get(1), dateObj, reference, result.get(i).get(3));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersLastUpdateBetweenAndStatusAndReference(String date1,String date2,String status, String reference) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("lastUpdate");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        Date dateBDD1 = null;
        Date dateBDD2 = null;
        dateBDD1 = DateHandler.parseDate(date1);
        dateBDD2 = DateHandler.parseDate(date2);
        conditions.add("lastUpdate >= '"+dateBDD1+"' AND lastUpdate <= '"+dateBDD2+"'");
        conditions.add("status='"+status+"'");
        conditions.add("orderReference='"+reference+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(1));
                Order order = new Order(result.get(i).get(0), status, dateObj, reference, result.get(i).get(2));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
        
    public Map<String,Order> getOrdersStatus(String status) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("lastUpdate");
        fields.add("orderReference");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        conditions.add("status='"+status+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(1));
                Order order = new Order(result.get(i).get(0), status, dateObj, result.get(i).get(2), result.get(i).get(3));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public Map<String,Order> getOrdersStatusAndReference(String status,String reference) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("lastUpdate");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        conditions.add("status='"+status+"'");
        conditions.add("orderReference='"+reference+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(1));
                Order order = new Order(result.get(i).get(0), status, dateObj, reference, result.get(i).get(2));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
     public Map<String,Order> getOrdersReference(String orderReference) throws Exception{
        
        String table = "\"order\"";
        
        Map<String,Order> orders = new HashMap<>();
        
        //Fields needed to fill in the Order structures
        List<String> fields = new ArrayList<String>();
        fields.add("orderID");
        fields.add("status");
        fields.add("lastUpdate");
        fields.add("orderItems");
        
        //Filtering the DB results by last update date
        List<String> conditions = new ArrayList<String>();
        conditions.add("orderReference='"+orderReference+"'");
        
         //Creating the result from the DB results
        List<List<String>> result = getDboperations().select(fields, table, conditions);
        
        if (result != null && result.size() > 0) {
            for(int i=0;i<result.size();i++){
                Date dateObj;
                dateObj=DateHandler.parseDate(result.get(i).get(2));
                Order order = new Order(result.get(i).get(0), result.get(i).get(1), dateObj, orderReference, result.get(i).get(3));
                
                orders.put(result.get(i).get(0), order);
            }
        }
        
        return orders;
        
    }
    
    public String getOrderItems(String orderID) throws SQLException {
        
        String orderItems = null;
        
        List<String> fields = new ArrayList<String>();
        fields.add("orderItems");
        
        String table = "\"order\"";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<String>();
        conditions.add("orderID='" + orderID + "'");

        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {
            orderItems = result.get(0).get(0);
        }
        
        return orderItems;
    }

    
    
}
