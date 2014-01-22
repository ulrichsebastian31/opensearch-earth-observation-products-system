/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.astrium.roseodbhandler;

import com.astrium.roseodbhandler.structures.OrderItem;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author re-cetienne
 */
public class OrderItemHandler extends RoseoDatabaseLoader{
    
    public OrderItemHandler(){
        super("ROSEODatabase");
    }

    public OrderItemHandler(RoseoDBOperations dboperations) {
        super(dboperations);
    }

    public OrderItemHandler(String databaseURL, String user, String pass) {
        super(databaseURL, user, pass);
    }
    
    public void addOrderItem(String orderItemID, String eoProductID, String optionsSet, String status) throws SQLException {

        String table = "orderItem";

        List<String> fields = new ArrayList<String>();
        
        fields.add("itemID");
        fields.add("eoProductID");
        fields.add("optionsSet");
        fields.add("status");

        List<String> depl1 = new ArrayList<String>();
        depl1.add("'" + orderItemID + "'");
        depl1.add("'" + eoProductID + "'");
        depl1.add("'" + optionsSet + "'");
        depl1.add("'" + status + "'");

        List<List<String>> values = new ArrayList<List<String>>();
        values.add(depl1);

        this.getDboperations().insert(
                table,
                fields,
                values);
    }
    
     /**
     * UPDATE OrderItem SET status=<status> WHERE orderItemID =
     * <orderItemID>
     *
     * @param orderItemID
     * @param status
     * @throws SQLException
     */
    public void setOrderItemStatus(String orderItemID, String status) throws SQLException {

        String table = "orderItem";

        List<String> fields = new ArrayList<>();
        fields.add("status");

        List<String> values = new ArrayList<>();
        values.add("'" + status + "'");

        String condition = "itemID='" + orderItemID + "'";

        this.getDboperations().update(table, fields, values, condition);
    }
    
    public OrderItem getOrderItem(String orderItemID) throws ParseException, SQLException {

        OrderItem item = null;

        List<String> fields = new ArrayList<String>();
        fields.add("eoProductID");
        fields.add("optionsSet");
        fields.add("status");
        

        String table = "orderItem";

        //Filtering the DB results by app server
        List<String> conditions = new ArrayList<String>();
        conditions.add("itemID='" + orderItemID + "'");

        List<List<String>> result = this.getDboperations().select(fields, table, conditions);

        if (result != null && result.size() > 0) {

            item = new OrderItem(
                    orderItemID,
                    result.get(0).get(0),
                    result.get(0).get(1),
                    result.get(0).get(2));

        }
        
         return item;
    }
    
    
    
}
