package com.astrium.hmas.roseo.orderedProduct;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import net.eads.astrium.dream.util.DateHandler;
import net.opengis.oseo.x10.ItemURLType;
import net.opengis.oseo.x10.OnLineAccessAddressType;
import net.opengis.oseo.x10.ProductIdType;
import net.opengis.oseo.x10.OnLineAccessAddressType.ResourceAddress;
import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionType;
import net.opengis.ows.x20.ExceptionReportDocument.ExceptionReport;
import net.opengis.roseo.x10.OrderedProductsDocument;
import net.opengis.roseo.x10.OrderedProductsDocument.OrderedProducts;

import org.apache.xmlbeans.XmlCursor;

import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.astrium.roseodbhandler.structures.EOProduct;
import com.astrium.roseodbhandler.structures.Order;
import com.astrium.roseodbhandler.structures.OrderItem;
import com.sun.jersey.api.core.HttpContext;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetProductByItem.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the Product resource
 *   																	i.e. the EO product asked to the server
 *   																	by sending an Order request.
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

@Path("1.0.0/order/{orderIdentifier}/{orderItemIdentifier}/file")
public class GetProductByItem {
	
	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;
	
	private String serversAdress;
	
	@GET
	public Response getProductsByItemID() throws Exception {
		
		Response response = null;
		
		String orderIdentifier = ui.getPathParameters().getFirst("orderIdentifier");
		System.out.println("orderIdentifier = " + orderIdentifier);
		
		String orderItemIdentifier = ui.getPathParameters().getFirst("orderItemIdentifier");
		System.out.println("orderItemIdentifier = " + orderItemIdentifier);
		
		URI baseURI = httpContext.getUriInfo().getAbsolutePath();
		serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort();
		
		if (!serversAdress.endsWith("/")) serversAdress += "/";
		
		RoseoManagementHandler roseomh = new RoseoManagementHandler();
		Order order = roseomh.getOrder(orderIdentifier);
		
		if(order!=null){
			
			OrderItem orderItem = roseomh.getOrderItem(orderItemIdentifier);
			if(orderItem!=null){
				
				OrderedProductsDocument orderedProductsDocument = OrderedProductsDocument.Factory.newInstance();
				OrderedProducts orderedProducts = orderedProductsDocument.addNewOrderedProducts();
				
				XmlCursor cursor = orderedProducts.newCursor();
				cursor.toNextToken();
				cursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
				cursor.insertNamespace("oseo", "http://www.opengis.net/oseo/1.0");
				cursor.insertNamespace("roseo", "http://www.opengis.net/roseo/1.0");
				cursor.dispose();
				
				String eoProductID = orderItem.getEoProductID();
				
				EOProduct eoProduct = roseomh.getProduct(eoProductID);
				
				ItemURLType url = orderedProducts.addNewURL();
				
				url.setItemId(orderItemIdentifier);
				ProductIdType productID = url.addNewProductId();
				
				if(!eoProduct.getCollectionID().equals("null")){
					productID.setIdentifier("http://www.opengis.net/def/EOP/ESA/0/" + eoProduct.getCollectionID() + "/" + eoProduct.getProductID());
					productID.setCollectionId("http://www.opengis.net/def/EOP/ESA/0/" + eoProduct.getCollectionID());
				}else{
					productID.setIdentifier("http://www.opengis.net/def/EOP/ESA/0/" + eoProduct.getProductID());
				}
				
				OnLineAccessAddressType itemAddress = url.addNewItemAddress();
				
				ResourceAddress resourceAdress = itemAddress.addNewResourceAddress();
				resourceAdress.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/product/" + eoProductID);
				
				Date date = DateHandler.parseDate("2015-01-01T00:00:00Z");
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				
				url.setExpirationDate(cal);
				
				String getProductsXML = orderedProductsDocument.toString();
				
				response = Response.ok(getProductsXML, "text/xml").build();
				
			}else{
				
				ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
				ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

				ExceptionType exception = exceptionReport.addNewException();
				exception.addExceptionText("Invalid value for order");
				exception.setExceptionCode("InvalidOrderItemIdentifier");
				exception.setLocator(orderItemIdentifier);

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
