package com.astrium.hmas.roseo.order;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import org.w3c.dom.Document;
import net.opengis.oseo.x10.ParameterDataType;
import net.opengis.oseo.x10.PaymentOptionSelectedValue;
import net.opengis.oseo.x10.ProductIdType;
import net.opengis.oseo.x10.StatusType;
import net.opengis.oseo.x10.CommonOrderItemType.Option;
import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionType;
import net.opengis.ows.x20.ExceptionReportDocument.ExceptionReport;
import net.opengis.roseo.x10.CommonOrderStatusItemType;
import net.opengis.roseo.x10.OrderItemDocument;
import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.astrium.roseodbhandler.structures.EOProduct;
import com.astrium.roseodbhandler.structures.Order;
import com.astrium.roseodbhandler.structures.OrderItem;
import com.sun.jersey.api.core.HttpContext;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OrderItemByID.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class returns the OrderService Item within
 *   																	the specified OrderService.
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

@Path("1.0.0/order/{orderIdentifier}/{orderItemIndentifier}")
public class OrderItemByID {
	
	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;
	
	private String serversAdress;
	
	public Document getDocument() {
		return document;
	}

	private Document document;
	
	@SuppressWarnings({ "unchecked" })
	@GET
	public Response retrieveOrderItemByID() throws Exception {
		
		Response response = null;
		String orderIdentifier = ui.getPathParameters().getFirst("orderIdentifier");
		System.out.println("orderIndentifier = " + orderIdentifier);
		String orderItemIndentifier = ui.getPathParameters().getFirst("orderItemIndentifier");
		System.out.println("orderItemIndentifier = " + orderItemIndentifier);
		
		URI baseURI = httpContext.getUriInfo().getAbsolutePath();
		serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort();
		
		if (!serversAdress.endsWith("/")) serversAdress += "/";
		
		RoseoManagementHandler roseomh = new RoseoManagementHandler();
		
		Order order = roseomh.getOrder(orderIdentifier);
		OrderItem orderItem = roseomh.getOrderItem(orderItemIndentifier);

		if(order!=null){
			if(orderItem!=null){
				
				String optSet = orderItem.getSetOptions();
				
				JSONObject optSetJson = new JSONObject(optSet);
				
				OrderItemDocument orderItemDocument = OrderItemDocument.Factory.newInstance();
				CommonOrderStatusItemType orderItemXml = orderItemDocument.addNewOrderItem();
				
				XmlCursor cursor = orderItemXml.newCursor();
				cursor.toNextToken();
				cursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
				cursor.insertNamespace("roseo", "http://www.opengis.net/roseo/1.0");
				cursor.dispose();
				
				orderItemXml.setItemId(orderItem.getItemID());
				orderItemXml.setProductOrderOptionsId(optSetJson.getString("productOrderOptionsId"));
				orderItemXml.setOrderItemRemark("OrderService item example");

				Option option = orderItemXml.addNewOption();
				
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
				
				EOProduct product = roseomh.getProduct(orderItem.getEoProductID());
				
				ProductIdType productId = ProductIdType.Factory.newInstance();
				
				if(!product.getCollectionID().equals("null")){
					productId.setIdentifier("http://www.opengis.net/def/EOP/ESA/0/" + product.getCollectionID() + "/" + product.getProductID());
					productId.setCollectionId("http://www.opengis.net/def/EOP/ESA/0/" + product.getCollectionID());
				}else{
					productId.setIdentifier("http://www.opengis.net/def/EOP/ESA/0/" + product.getProductID());
				}
				
				orderItemXml.setProductId(productId);
				
				PaymentOptionSelectedValue paymentMethod = PaymentOptionSelectedValue.Factory.newInstance();
				paymentMethod.setPaymentMethod("quota");
				paymentMethod.setOrderAccount("project_10000");
				
				orderItemXml.setPayment(paymentMethod);
				
				orderItemXml.setItemIdURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + orderIdentifier + "/" + orderItem.getItemID());
				orderItemXml.setDownloadURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + orderIdentifier + "/" + orderItem.getItemID() + "/file");
				
				StatusType itemStatusInfo = orderItemXml.addNewOrderItemStatusInfo();
				net.opengis.oseo.x10.EnumStatusType.Enum itemStatus = net.opengis.oseo.x10.EnumStatusType.Enum.forString(orderItem.getStatus());
				itemStatusInfo.setStatus(itemStatus);	
				
				response = Response.ok(orderItemDocument.toString(), "text/xml").build();
				
			}else{

				ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
				ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

				ExceptionType exception = exceptionReport.addNewException();
				exception.addExceptionText("Invalid value for order");
				exception.setExceptionCode("InvalidOrderItemIdentifier");
				exception.setLocator(orderItemIndentifier);

				String exceptionString = exceptionReportDocument.toString();

				response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();
			}

			

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

}
