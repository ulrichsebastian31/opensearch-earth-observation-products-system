/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astrium.roseodbhandler;

import com.astrium.roseodbhandler.structures.Collection;
import com.astrium.roseodbhandler.structures.EOProduct;
import com.astrium.roseodbhandler.structures.Order;
import com.astrium.roseodbhandler.structures.OrderItem;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author re-cetienne
 */
public class RoseoManagementHandler extends RoseoDatabaseLoader {

    private EOProductHandler eOProductHandler;
    private CollectionHandler collectionHandler;
    private OrderHandler orderHandler;
    private OrderItemHandler orderItemHandler;

    public final void initHandlers() {
        eOProductHandler = new EOProductHandler(this.getDboperations());
        collectionHandler = new CollectionHandler(this.getDboperations());
        orderHandler = new OrderHandler(this.getDboperations());
        orderItemHandler = new OrderItemHandler(this.getDboperations());
    }

    public RoseoManagementHandler() {
        super("ROSEODatabase");
        initHandlers();
    }

    public RoseoManagementHandler(RoseoDBOperations dboperations) {
        super(dboperations);
        initHandlers();
    }

    public RoseoManagementHandler(String databaseURL, String user, String pass) {
        super(databaseURL, user, pass);
        initHandlers();

    }

    /**
     * Functions that insert data in the database
     * @param productID
     * @param collectionID
     * @param options
     * @throws java.sql.SQLException
     */
    public void addEOProduct(String productID, String collectionID, String options,String lastUpdate) throws SQLException {
        eOProductHandler.addProduct(productID, collectionID, options,lastUpdate);
    }
    
    public void addCollection(String collectionID, String options) throws SQLException{
        collectionHandler.addCollection(collectionID, options);
    }
    
    public void addOrder(String orderID, String status,String lastUpdate,String orderRefence, String orderItems) throws Exception {
        orderHandler.addOrder(orderID, status, lastUpdate, orderRefence, orderItems);
    }
    
    public void addOrderItem(String itemID, String eoProductID, String optionsSet, String status) throws SQLException {
        orderItemHandler.addOrderItem(itemID, eoProductID, optionsSet, status);
    }
    
    public void setProductCollectionID(String productID, String collectionID) throws SQLException{
        eOProductHandler.setProductCollectionID(productID, collectionID);    
    }
    
    public EOProduct getProduct(String productID) throws ParseException, SQLException{
        return eOProductHandler.getProduct(productID);
    }
    
    public Map<String, EOProduct> getProductsLastUpdateLess(String date) throws ParseException, SQLException {
        return eOProductHandler.getProductsLastUpdateLess(date);
    }
    
    public Map<String, EOProduct> getProductsLastUpdateGreater(String date) throws ParseException, SQLException {
        return eOProductHandler.getProductsLastUpdateGreater(date);
    }
    
    public Map<String, EOProduct> getProductsLastUpdateBetween(String date1,String date2) throws ParseException, SQLException {
        return eOProductHandler.getProductsLastUpdateBetween(date1, date2);
    }
    
    public List<String> getProductIDs(String collectionID) throws SQLException {
        return eOProductHandler.getProductIDs(collectionID);
    }
    
    public Collection getCollection(String collectionID) throws ParseException, SQLException {
        return collectionHandler.getCollection(collectionID);
    }
    
    public Map<String,Collection> getCollections() throws ParseException, SQLException {
        return collectionHandler.getCollections();
    }
    
    public void setOrderStatus(String orderID, String status) throws SQLException {
        orderHandler.setOrderStatus(orderID, status);
    }
    
    public Order getOrder(String orderID) throws ParseException, SQLException {
        return orderHandler.getOrder(orderID);
    }
    
    public Map<String,Order> getOrdersLastUpdateLess(String date) throws Exception{
        return orderHandler.getOrdersLastUpdateLess(date);
    }
    
    public Map<String,Order> getOrdersLastUpdateLessAndStatus(String date, String status) throws Exception{
        return orderHandler.getOrdersLastUpdateLessAndStatus(date, status);
    }
    
    public Map<String,Order> getOrdersLastUpdateLessAndReference(String date, String reference) throws Exception{
        return orderHandler.getOrdersLastUpdateLessAndReference(date, reference);
    }
    
    public Map<String,Order> getOrdersLastUpdateLessAndStatusAndReference(String date,String status, String reference) throws Exception{
        return orderHandler.getOrdersLastUpdateLessAndStatusAndReference(date, status, reference);
    }
    
    public Map<String,Order> getOrdersLastUpdateGreater(String date) throws Exception{
        return orderHandler.getOrdersLastUpdateGreater(date);
    }
    
    public Map<String,Order> getOrdersLastUpdateGreaterAndStatus(String date, String status) throws Exception{
        return orderHandler.getOrdersLastUpdateGreaterAndStatus(date, status);
    }
    
    public Map<String,Order> getOrdersLastUpdateGreaterAndReference(String date, String reference) throws Exception{
        return orderHandler.getOrdersLastUpdateGreaterAndReference(date, reference);
    }
    
    public Map<String,Order> getOrdersLastUpdateGreaterAndStatusAndReference(String date,String status, String reference) throws Exception{
        return orderHandler.getOrdersLastUpdateGreaterAndStatusAndReference(date, status, reference);
    }
    
    public Map<String,Order> getOrdersLastUpdateBetween(String date1,String date2) throws Exception{
        return orderHandler.getOrdersLastUpdateBetween(date1, date2);
    }
    
    public Map<String,Order> getOrdersLastUpdateBetweenAndStatus(String date1,String date2, String status) throws Exception{
        return orderHandler.getOrdersLastUpdateBetweenAndStatus(date1, date2, status);
    }
    
    public Map<String,Order> getOrdersLastUpdateBetweenAndReference(String date1,String date2, String reference) throws Exception{
        return orderHandler.getOrdersLastUpdateBetweenAndReference(date1, date2, reference);
    }
    
    public Map<String,Order> getOrdersLastUpdateBetweenAndStatusAndReference(String date1,String date2,String status, String reference) throws Exception{
        return orderHandler.getOrdersLastUpdateBetweenAndStatusAndReference(date1, date2, status, reference);
    }
    
    public Map<String,Order> getOrdersStatus(String status) throws Exception{
        return orderHandler.getOrdersStatus(status);
    }
    
    public Map<String,Order> getOrdersStatusAndReference(String status,String reference) throws Exception{
        return orderHandler.getOrdersStatusAndReference(status, reference);
    }
    
    public Map<String,Order> getOrdersReference(String orderReference) throws Exception{
        return orderHandler.getOrdersReference(orderReference);
    }
    
    public String getOrderItems(String orderID) throws SQLException {
        return orderHandler.getOrderItems(orderID);
    }
    
    public void setOrderItemStatus(String orderItemID, String status) throws SQLException {
        orderItemHandler.setOrderItemStatus(orderItemID, status);
    }
    
    public OrderItem getOrderItem(String orderItemID) throws ParseException, SQLException {
        return orderItemHandler.getOrderItem(orderItemID);
    }

}
