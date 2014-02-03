package com.astrium.hmas.roseo.order;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.codehaus.jettison.json.JSONArray;
import org.json.JSONObject;

import net.opengis.oseo.x10.DeliveryOptionsType;
import net.opengis.oseo.x10.ParameterDataType;
import net.opengis.oseo.x10.PaymentOptionSelectedValue;
import net.opengis.oseo.x10.ProductIdType;
import net.opengis.oseo.x10.StatusType;
import net.opengis.oseo.x10.CommonOrderItemType.Option;
import net.opengis.oseo.x10.DeliveryOptionsType.OnlineDataAccess;
import net.opengis.oseo.x10.ProtocolType.Enum;
import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionType;
import net.opengis.ows.x20.ExceptionReportDocument.ExceptionReport;
import net.opengis.roseo.x10.CommonOrderMonitorSpecification;
import net.opengis.roseo.x10.CommonOrderStatusItemType;
import net.opengis.roseo.x10.OrderDocument;

import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.astrium.roseodbhandler.structures.EOProduct;
import com.astrium.roseodbhandler.structures.Order;
import com.astrium.roseodbhandler.structures.OrderItem;
import com.sun.jersey.api.core.HttpContext;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OrderByID.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the OrderService resource
 *   																	i.e. the envelope sent from the client
 *   																	to order a set of EO products with the
 *   																	selected order options.
 *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (C) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 */

@Path("1.0.0/order/{orderIdentifier}")
public class OrderByID {

	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;
	
	private String serversAdress;
	
	@SuppressWarnings("unchecked")
	@GET
	public Response retrieveOrderByID() throws Exception {
		
		Response response = null;

		String orderIdentifier = ui.getPathParameters().getFirst("orderIdentifier");
		System.out.println("orderIdentifier = " + orderIdentifier);
		
		URI baseURI = httpContext.getUriInfo().getAbsolutePath();
		serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort();
		
		if (!serversAdress.endsWith("/")) serversAdress += "/";

		System.out.println("Server base address : " + serversAdress);
		
		RoseoManagementHandler roseomh = new RoseoManagementHandler();
		Order order = roseomh.getOrder(orderIdentifier);
		
		if(order!=null){
			
			OrderDocument orderDocument = OrderDocument.Factory.newInstance();
			CommonOrderMonitorSpecification xml_order = orderDocument.addNewOrder();
			
			XmlCursor cursor = xml_order.newCursor();
			cursor.toNextToken();
			cursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			cursor.insertNamespace("roseo", "http://www.opengis.net/roseo/1.0");
			cursor.insertNamespace("sps", "http://www.opengis.net/sps/2.0");
			cursor.insertNamespace("swe", "http://www.opengis.net/swe/2.0");
			cursor.dispose();
			
			xml_order.setOrderReference(order.getOrderRefence());
			xml_order.setOrderRemark("Retrieve OrderService By ID example");
			
			DeliveryOptionsType deliveryOptions = xml_order.addNewDeliveryOptions();
			OnlineDataAccess onlineDataAccess = deliveryOptions.addNewOnlineDataAccess();

			Enum protocol = Enum.forString("https");
			onlineDataAccess.setProtocol(protocol);
			
			net.opengis.oseo.x10.EnumOrderType.Enum orderType = net.opengis.oseo.x10.EnumOrderType.Enum.forString("PRODUCT_ORDER");
			xml_order.setOrderType(orderType);
			
			xml_order.setOrderId(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + order.getOrderID());
			
			StatusType statusInfo = StatusType.Factory.newInstance();
			String statusOrder = order.getStatus();
			net.opengis.oseo.x10.EnumStatusType.Enum status = net.opengis.oseo.x10.EnumStatusType.Enum.forString(statusOrder);
			statusInfo.setStatus(status);
			xml_order.setOrderStatusInfo(statusInfo);
			
			Calendar dateCalendar = Calendar.getInstance();
			dateCalendar.setTime(order.getLastUpdate());
			xml_order.setOrderDateTime(dateCalendar);
			
			String[] itemsID = order.getOrderItems().split(",");
			Map<String,OrderItem> orderItems = new HashMap<String, OrderItem>();
			
			xml_order.setDownloadURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + orderIdentifier + "/file");
			
			for(int i=0;i<itemsID.length;i++){
				
				OrderItem item = roseomh.getOrderItem(itemsID[i]);
				orderItems.put(item.getItemID(), item);
				
				String optSet = item.getSetOptions();
				
				JSONObject optSetJson = new JSONObject(optSet);
				
				CommonOrderStatusItemType orderItem = xml_order.addNewOrderItem();
				orderItem.setItemId(item.getItemID());
				orderItem.setProductOrderOptionsId(optSetJson.getString("productOrderOptionsId"));
				int nbItem = i + 1;
				orderItem.setOrderItemRemark("Product number " + nbItem);

				Option option = orderItem.addNewOption();
				ParameterDataType parameterData = option.addNewParameterData();

				net.opengis.oseo.x10.SWEEncoding.Enum encoding = net.opengis.oseo.x10.SWEEncoding.Enum.forString("XMLEncoding");
				parameterData.setEncoding(encoding);

				XmlObject values = parameterData.addNewValues();
				
				XmlCursor cur = values.newCursor();

				cur.toChild("values");
				cur.toEndToken();
				cur.insertNamespace("ns", "http://www.opengis.net/oseo/1.0/dataset");
				
				Iterator<String> keys = optSetJson.keys();
				Map<String,String> jsonObj = new HashMap<String,String>();
				
				while(keys.hasNext()) {
			        String key = keys.next();
			        
			        String currentDynamicValue = optSetJson.getString(key);
			        
			        if(key.contains(":")){
			        	key = key.split(":")[1];
			        }
			        
			        if(currentDynamicValue.startsWith("[")){
			        	
			        	JSONArray valuesJSON = new JSONArray(currentDynamicValue);
			        	String temp = "";
			        	for(int k=0;k<valuesJSON.length();k++){
			        		
			        		if(k==valuesJSON.length()-1){
			        			
			        			temp += valuesJSON.get(k);
			        			
			        		}else{
			        			
			        			temp += valuesJSON.get(k) + ",";
			        		}
			        		
			        	}
			        	currentDynamicValue = temp;
			        }
			        jsonObj.put(key, currentDynamicValue);
			        

			    }
				
				
				for (String key : jsonObj.keySet()) {

					if(!(key.equals("productOrderOptionsId"))){
			        	cur.insertElementWithText(key, jsonObj.get(key));
			        }

				}
				
				if (cur != null)
					cur.dispose();
				
				EOProduct product = roseomh.getProduct(item.getEoProductID());
				
				System.out.println(item.getEoProductID());
				System.out.println(product.getCollectionID());
				
				ProductIdType productId = orderItem.addNewProductId();
				if(!product.getCollectionID().equals("null")){
					productId.setIdentifier("http://www.opengis.net/def/EOP/ESA/0/" + product.getCollectionID() + "/" + product.getProductID());
					productId.setCollectionId("http://www.opengis.net/def/EOP/ESA/0/" + product.getCollectionID());
				}else{
					productId.setIdentifier("http://www.opengis.net/def/EOP/ESA/0/" + product.getProductID());
				}
				
				orderItem.setItemIdURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + orderIdentifier + "/" + item.getItemID());
				orderItem.setDownloadURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + orderIdentifier + "/" + item.getItemID() + "/file");
				
				PaymentOptionSelectedValue paymentMethod = PaymentOptionSelectedValue.Factory.newInstance();
				paymentMethod.setPaymentMethod("quota");
				paymentMethod.setOrderAccount("project_10000");
				orderItem.setPayment(paymentMethod);
				
				StatusType itemStatusInfo = StatusType.Factory.newInstance();
				net.opengis.oseo.x10.EnumStatusType.Enum itemStatus = net.opengis.oseo.x10.EnumStatusType.Enum.forString(item.getStatus());
				itemStatusInfo.setStatus(itemStatus);
				orderItem.setOrderItemStatusInfo(itemStatusInfo);
				
			}
			
			String getOrderXML = orderDocument.toString();
			
			response = Response.ok(getOrderXML, "text/xml").build();
			
		}else{
			
			ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
			ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

			ExceptionType exception = exceptionReport.addNewException();
			exception.addExceptionText("Invalid value for order");
			exception.setExceptionCode("InvalidOrderIdentifier");
			exception.setLocator(orderIdentifier);

			String exceptionString = exceptionReportDocument.toString();

			response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();
			
		}
		
		if(response == null){
			
			ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
			ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

			ExceptionType exception = exceptionReport.addNewException();
			exception.addExceptionText("ServerError");
			exception.setExceptionCode("NoApplicableCode");

			String exceptionString = exceptionReportDocument.toString();

			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(exceptionString).build();
		}
		
		
		
		return response;
	}
	
	@DELETE
	public Response cancelOrderByID() throws Exception {
		
		Response response = null;
		
		String orderIdentifier = ui.getPathParameters().getFirst("orderIdentifier");
		System.out.println("orderIdentifier = " + orderIdentifier);
		
		RoseoManagementHandler roseomh = new RoseoManagementHandler();
		Order order = roseomh.getOrder(orderIdentifier);
		
		if(order!=null){
		
		roseomh.setOrderStatus(orderIdentifier, "Cancelled");
		String[] itemsID = order.getOrderItems().split(",");
		roseomh.setOrderItemStatus(itemsID[0], "Cancelled");
		
		response = Response.status(Response.Status.OK).build();
		
		}else{
			
			ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
			ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

			ExceptionType exception = exceptionReport.addNewException();
			exception.addExceptionText("Invalid value for parameter");
			exception.setExceptionCode("InvalidValueParameter");
			exception.setLocator(orderIdentifier);

			String exceptionString = exceptionReportDocument.toString();

			response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();
		}
		
		if(response==null){
			
			ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
			ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

			ExceptionType exception = exceptionReport.addNewException();
			exception.addExceptionText("ServerError");
			exception.setExceptionCode("NoApplicableCode");

			String exceptionString = exceptionReportDocument.toString();

			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(exceptionString).build();
			
		}
		
		return response;
		
	}

}
