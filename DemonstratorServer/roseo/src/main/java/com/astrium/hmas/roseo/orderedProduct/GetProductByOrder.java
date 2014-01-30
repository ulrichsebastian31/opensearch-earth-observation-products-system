package com.astrium.hmas.roseo.orderedProduct;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.xmlbeans.XmlCursor;

import net.eads.astrium.dream.util.DateHandler;
import net.opengis.oseo.x10.ItemURLType;
import net.opengis.oseo.x10.OnLineAccessAddressType;
import net.opengis.oseo.x10.OnLineAccessAddressType.ResourceAddress;
import net.opengis.oseo.x10.ProductIdType;
import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionType;
import net.opengis.ows.x20.ExceptionReportDocument.ExceptionReport;
import net.opengis.roseo.x10.OrderedProductsDocument;
import net.opengis.roseo.x10.OrderedProductsDocument.OrderedProducts;

import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.astrium.roseodbhandler.structures.EOProduct;
import com.astrium.roseodbhandler.structures.Order;
import com.astrium.roseodbhandler.structures.OrderItem;
import com.sun.jersey.api.core.HttpContext;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetProductByOrder.java
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

@Path("1.0.0/order/{orderIdentifier}/file")
public class GetProductByOrder {

	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;

	private String serversAdress;
	private String lastUpdate;

	@GET
	public Response getProductsByOrderID() throws Exception {

		Response response = null;
		lastUpdate=null;
		/*
		 * Get the parameters from the query
		 */
		MultivaluedMap<String, String> conf = ui.getQueryParameters();

		String getProductsXML=null;

		String orderIdentifier = ui.getPathParameters().getFirst("orderIdentifier");
		System.out.println("orderIdentifier = " + orderIdentifier);

		URI baseURI = httpContext.getUriInfo().getAbsolutePath();
		serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort();

		if (!serversAdress.endsWith("/")) serversAdress += "/";

		RoseoManagementHandler roseomh = new RoseoManagementHandler();
		Order order = roseomh.getOrder(orderIdentifier);

		if(order!=null){

			if(conf.size()==0){

				OrderedProductsDocument orderedProductsDocument = OrderedProductsDocument.Factory.newInstance();
				OrderedProducts orderedProducts = orderedProductsDocument.addNewOrderedProducts();

				XmlCursor cursor = orderedProducts.newCursor();
				cursor.toNextToken();
				cursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
				cursor.insertNamespace("oseo", "http://www.opengis.net/oseo/1.0");
				cursor.insertNamespace("roseo", "http://www.opengis.net/roseo/1.0");
				cursor.dispose();

				String orderItems = order.getOrderItems();
				String[] itemsID = orderItems.split(",");

				for(int i = 0; i<itemsID.length;i++){

					getProductsBuildXML(itemsID[i], roseomh, orderedProducts);

				}

				getProductsXML = orderedProductsDocument.toString();

				response = Response.ok(getProductsXML, "text/xml").build();

			}else{

				if(conf.size()>1 || conf.get("lu")==null){

					ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
					ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

					ExceptionType exception = exceptionReport.addNewException();
					exception.addExceptionText("Invalid value for filtering");
					exception.setExceptionCode("InvalidFilteringValue");
					exception.setLocator("wrong filtering parameters");

					String exceptionString = exceptionReportDocument.toString();

					response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();

				}else{

					lastUpdate = conf.get("lu").get(0);

					Map<String, EOProduct> products = new HashMap<String, EOProduct>();

					System.out.println(lastUpdate);

					if(!lastUpdate.contains("[") && !lastUpdate.contains("]")){

						ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
						ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

						ExceptionType exception = exceptionReport.addNewException();
						exception.addExceptionText("Invalid value for parameter");
						exception.setExceptionCode("InvalidParameterValue");
						exception.setLocator(lastUpdate);

						String exceptionString = exceptionReportDocument.toString();

						response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();

					}else{
						try{

							//Searching for all EO products compliant with the search parameter value
							if(lastUpdate.startsWith("[") && !lastUpdate.endsWith("]")){

								lastUpdate = lastUpdate.replaceAll("\\[", "");
								products = roseomh.getProductsLastUpdateGreater(lastUpdate);


							}else if(!lastUpdate.startsWith("[") && lastUpdate.endsWith("]")){

								String lastUpdateNew = lastUpdate.replaceAll("\\]", "");
								products = roseomh.getProductsLastUpdateLess(lastUpdateNew);

							}else if(lastUpdate.startsWith("[") && lastUpdate.endsWith("]")){

								String lastUpdateTemp = lastUpdate.replaceAll("\\]", "");
								String lastUpdateNew = lastUpdateTemp.replaceAll("\\[", "");
								String[] dates = lastUpdateNew.split(",");
								String date1 = dates[0];
								String date2 = dates[1];
								products = roseomh.getProductsLastUpdateBetween(date1, date2);

							}

							OrderedProductsDocument orderedProductsDocument = OrderedProductsDocument.Factory.newInstance();
							OrderedProducts orderedProducts = orderedProductsDocument.addNewOrderedProducts();

							XmlCursor cursor = orderedProducts.newCursor();
							cursor.toNextToken();
							cursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
							cursor.insertNamespace("oseo", "http://www.opengis.net/oseo/1.0");
							cursor.insertNamespace("roseo", "http://www.opengis.net/roseo/1.0");
							cursor.dispose();


							String orderItems = order.getOrderItems();
							String[] itemsID = orderItems.split(",");

							//Building of the list of the ordered products which are compliant with the search parameter
							Map<String,String> productsIDMap = new HashMap<String,String>();
							List<String> productsID = new ArrayList<String>();

							//list of all the ordered products
							for(int i = 0; i<itemsID.length;i++){

								OrderItem item = roseomh.getOrderItem(itemsID[i]);
								String productID = item.getEoProductID();

								productsIDMap.put(productID,itemsID[i]);
								productsID.add(productID);

							}

							//list of the ordered products that are not compliant with the search parameter value
							List<String> badProducts = new ArrayList<String>();

							for(int j = 0;j<productsID.size();j++){

								if(!products.containsKey(productsID.get(j))){

									badProducts.add(productsID.get(j));
								}
							}

							//we remove those "bad products" from the list of the ordered products
							for(int k =0;k<badProducts.size();k++){
								if(!products.containsKey(badProducts.get(k))){
									productsIDMap.remove(badProducts.get(k));
								}
							}

							//Response building
							for(int i = 0; i<itemsID.length;i++){

								OrderItem item = roseomh.getOrderItem(itemsID[i]);
								String eoProductID = item.getEoProductID();

								if(productsIDMap.containsKey(eoProductID)){

									getProductsBuildXML(itemsID[i], roseomh, orderedProducts);

								}

							}

							getProductsXML = orderedProductsDocument.toString();

							response = Response.ok(getProductsXML, "text/xml").build();
							
						}catch(ParseException e){
							ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
							ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

							ExceptionType exception = exceptionReport.addNewException();
							exception.addExceptionText("Invalid value for parameter");
							exception.setExceptionCode("InvalidParameterValue");
							exception.setLocator(lastUpdate);

							String exceptionString = exceptionReportDocument.toString();

							response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();
						}
					}
				}

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

	public void getProductsBuildXML(String itemID,RoseoManagementHandler roseomh,OrderedProducts orderedProducts) throws Exception{

		OrderItem item = roseomh.getOrderItem(itemID);
		String eoProductID = item.getEoProductID();

		EOProduct eoProduct = roseomh.getProduct(eoProductID);

		ItemURLType url = orderedProducts.addNewURL();

		url.setItemId(itemID);
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


	}

}
