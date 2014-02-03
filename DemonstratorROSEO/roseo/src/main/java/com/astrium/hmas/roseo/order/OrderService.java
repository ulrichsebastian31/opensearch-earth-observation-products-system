package com.astrium.hmas.roseo.order;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlObject;
import org.codehaus.jettison.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Node;

import net.opengis.oseo.x10.DeliveryOptionsType;
import net.opengis.oseo.x10.OrderIdDocument;
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
import net.opengis.roseo.x10.OrdersDocument;
import net.opengis.roseo.x10.OrdersDocument.Orders;

import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.astrium.roseodbhandler.structures.EOProduct;
import com.astrium.roseodbhandler.structures.Order;
import com.astrium.roseodbhandler.structures.OrderItem;
import com.sun.jersey.api.core.HttpContext;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OrderService.java
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

@Path("1.0.0/order")
public class OrderService {

	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;

	private String serversAdress;

	private String lastUpdate;
	private String orderStatus;
	private String orderReference;

	public static int PRETTY_PRINT_INDENT_FACTOR = 4;

	@GET
	public Response retrieveOrdersByFilter() throws Exception {

		Response response = null;
		lastUpdate=null;
		orderReference=null;
		orderStatus=null;

		RoseoManagementHandler roseomh = new RoseoManagementHandler();

		/*
		 * Get the parameters from the query
		 */
		MultivaluedMap<String, String> conf = ui.getQueryParameters();
		List<String> parameters = new ArrayList<String>();
		parameters.add("lu");
		parameters.add("orr");
		parameters.add("ost");

		int count = 0;
		for(String key : conf.keySet()){
			if(!parameters.contains(key)){
				count++;
			}
		}

		if(count>0){

			ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
			ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

			ExceptionType exception = exceptionReport.addNewException();
			exception.addExceptionText("Invalid value for filtering");
			exception.setExceptionCode("InvalidFilteringValue");
			exception.setLocator("wrong filtering parameters");

			String exceptionString = exceptionReportDocument.toString();

			response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();

		}else{

			if(conf.get("lu")!=null){
				lastUpdate = conf.get("lu").get(0);
			}
			if(conf.get("ost")!=null){
				orderStatus=conf.get("ost").get(0);
			}
			if(conf.get("orr")!=null){
				orderReference=conf.get("orr").get(0);
			}

			Map<String, Order> orders = new HashMap<String, Order>();
			String getOrdersXML = null;

			System.out.println(orderStatus);
			System.out.println(lastUpdate);
			System.out.println(orderReference);

			if((lastUpdate==null) && (orderReference==null) && (orderStatus==null)){

				ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
				ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

				ExceptionType exception = exceptionReport.addNewException();
				exception.addExceptionText("Missing value for parameter");
				exception.setExceptionCode("MissingParameterValue");
				exception.setLocator("filtering parameters missing");

				String exceptionString = exceptionReportDocument.toString();

				response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();

			}else{

				//lu
				if((lastUpdate!=null) && (orderReference==null) && (orderStatus==null)){

					if(lastUpdate.startsWith("[") && !lastUpdate.endsWith("]")){

						lastUpdate = lastUpdate.replaceAll("\\[", "");
						orders = roseomh.getOrdersLastUpdateGreater(lastUpdate);
						getOrdersXML = getOrdersBuildXML(orders,roseomh);


					}else if(!lastUpdate.startsWith("[") && lastUpdate.endsWith("]")){

						String lastUpdateNew = lastUpdate.replaceAll("\\]", "");
						orders = roseomh.getOrdersLastUpdateLess(lastUpdateNew);
						getOrdersXML = getOrdersBuildXML(orders,roseomh);

					}else if(lastUpdate.startsWith("[") && lastUpdate.endsWith("]")){

						String lastUpdateTemp = lastUpdate.replaceAll("\\]", "");
						String lastUpdateNew = lastUpdateTemp.replaceAll("\\[", "");
						String[] dates = lastUpdateNew.split(",");
						String date1 = dates[0];
						String date2 = dates[1];
						orders = roseomh.getOrdersLastUpdateBetween(date1, date2);
						getOrdersXML = getOrdersBuildXML(orders, roseomh);

					}

					//lu and orr
				}else if((lastUpdate!=null) && (orderReference!=null) && (orderStatus==null)){

					if(lastUpdate.startsWith("[") && !lastUpdate.endsWith("]")){

						String lastUpdateNew = lastUpdate.replaceAll("\\[", "");
						orders = roseomh.getOrdersLastUpdateGreaterAndReference(lastUpdateNew,orderReference);
						getOrdersXML = getOrdersBuildXML(orders,roseomh);

					}else if(!lastUpdate.startsWith("[") && lastUpdate.endsWith("]")){

						String lastUpdateNew = lastUpdate.replaceAll("\\]", "");
						orders = roseomh.getOrdersLastUpdateLessAndReference(lastUpdateNew, orderReference);
						getOrdersXML = getOrdersBuildXML(orders,roseomh);

					}else if(lastUpdate.startsWith("[") && lastUpdate.endsWith("]")){

						String lastUpdateTemp = lastUpdate.replaceAll("\\]", "");
						String lastUpdateNew = lastUpdateTemp.replaceAll("\\[", "");
						String[] dates = lastUpdateNew.split(",");
						String date1 = dates[0];
						String date2 = dates[1];
						orders = roseomh.getOrdersLastUpdateBetweenAndReference(date1, date2, orderReference);
						getOrdersXML = getOrdersBuildXML(orders, roseomh);

					}

					//lu and ost
				}else if((lastUpdate!=null) && (orderReference==null) && (orderStatus!=null)){

					if(lastUpdate.startsWith("[") && !lastUpdate.endsWith("]")){

						String lastUpdateNew = lastUpdate.replaceAll("\\[", "");
						orders = roseomh.getOrdersLastUpdateGreaterAndStatus(lastUpdateNew, orderStatus);
						getOrdersXML = getOrdersBuildXML(orders, roseomh);

					}else if(!lastUpdate.startsWith("[") && lastUpdate.endsWith("]")){

						String lastUpdateNew = lastUpdate.replaceAll("\\]", "");
						orders = roseomh.getOrdersLastUpdateLessAndStatus(lastUpdateNew, orderStatus);
						getOrdersXML = getOrdersBuildXML(orders, roseomh);

					}else if(lastUpdate.startsWith("[") && lastUpdate.endsWith("]")){

						String lastUpdateTemp = lastUpdate.replaceAll("\\]", "");
						String lastUpdateNew = lastUpdateTemp.replaceAll("\\[", "");
						String[] dates = lastUpdateNew.split(",");
						String date1 = dates[0];
						String date2 = dates[1];
						orders = roseomh.getOrdersLastUpdateBetweenAndStatus(date1, date2, orderStatus);
						getOrdersXML = getOrdersBuildXML(orders, roseomh);

					}

					//lu and ost and orr
				}else if((lastUpdate!=null) && (orderReference!=null) && (orderStatus!=null)){

					if(lastUpdate.startsWith("[") && !lastUpdate.endsWith("]")){

						String lastUpdateNew = lastUpdate.replaceAll("\\[", "");
						orders = roseomh.getOrdersLastUpdateGreaterAndStatusAndReference(lastUpdateNew, orderStatus, orderReference);
						getOrdersXML = getOrdersBuildXML(orders, roseomh);

					}else if(!lastUpdate.startsWith("[") && lastUpdate.endsWith("]")){

						String lastUpdateNew = lastUpdate.replaceAll("\\]", "");
						orders = roseomh.getOrdersLastUpdateLessAndStatusAndReference(lastUpdateNew, orderStatus, orderReference);
						getOrdersXML = getOrdersBuildXML(orders, roseomh);

					}else if(lastUpdate.startsWith("[") && lastUpdate.endsWith("]")){

						String lastUpdateTemp = lastUpdate.replaceAll("\\]", "");
						String lastUpdateNew = lastUpdateTemp.replaceAll("\\[", "");
						String[] dates = lastUpdateNew.split(",");
						String date1 = dates[0];
						String date2 = dates[1];
						orders = roseomh.getOrdersLastUpdateBetweenAndStatusAndReference(date1, date2, orderStatus, orderReference);
						getOrdersXML = getOrdersBuildXML(orders, roseomh);

					}

					//orr
				}else if((lastUpdate==null) && (orderReference!=null) && (orderStatus==null)){

					orders = roseomh.getOrdersReference(orderReference);
					getOrdersXML = getOrdersBuildXML(orders, roseomh);

					//ost and orr
				}else if((lastUpdate==null) && (orderReference!=null) && (orderStatus!=null)){

					orders = roseomh.getOrdersStatusAndReference(orderStatus, orderReference);
					getOrdersXML = getOrdersBuildXML(orders, roseomh);

					//ost
				}else if((lastUpdate==null) && (orderReference==null) && (orderStatus!=null)){

					orders = roseomh.getOrdersStatus(orderStatus);
					getOrdersXML = getOrdersBuildXML(orders, roseomh);

				}

				if(orders.size()==0){

					ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
					ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

					ExceptionType exception = exceptionReport.addNewException();
					exception.addExceptionText("No Orders found");
					exception.setExceptionCode("NoOrdersFound");
					exception.setExceptionCode("");

					String exceptionString = exceptionReportDocument.toString();

					response = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_XML).entity(exceptionString).build();

				}else if(orders.size()>100){

					ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
					ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

					ExceptionType exception = exceptionReport.addNewException();
					exception.addExceptionText("Too many hits");
					exception.setExceptionCode("TooManyHits");
					exception.setExceptionCode("");

					String exceptionString = exceptionReportDocument.toString();

					response = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_XML).entity(exceptionString).build();

				}else{

					response = Response.ok(getOrdersXML, "text/xml").build();
				}
			}



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

	@SuppressWarnings("unchecked")
	@POST
	@Consumes(MediaType.TEXT_XML)
	public Response submitOrder(String xmlrequest) throws Exception{

		Response response = null;

		URI baseURI = httpContext.getUriInfo().getAbsolutePath();
		serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort();

		if (!serversAdress.endsWith("/")) serversAdress += "/";

		System.out.println("Server base address : " + serversAdress);

		RoseoManagementHandler roseomh = new RoseoManagementHandler();

		OrderDocument orderDocument = OrderDocument.Factory.parse(xmlrequest);

		CommonOrderMonitorSpecification xml_order = orderDocument.getOrder();

		UUID id = UUID.randomUUID();
		String orderID = id.toString();
		String orderReference = xml_order.getOrderReference();

		CommonOrderStatusItemType[] items = xml_order.getOrderItemArray();
		String orderItems = "";

		for(int i=0;i<items.length;i++){

			String itemID = items[i].getItemId();
			String[] identifier = items[i].getProductId().getIdentifier().split("/");
			String eoProductID = identifier[identifier.length-1];
			
			EOProduct eoProduct = roseomh.getProduct(eoProductID);

			Option options = items[i].getOptionArray(0);
			XmlObject values = options.getParameterData().getValues();

			JSONObject optionsSet = XML.toJSONObject(values.toString());
			optionsSet = optionsSet.getJSONObject("xml-fragment");

			Iterator<String> keys = optionsSet.keys();
			List<String> keyList = new ArrayList<String>();

			while(keys.hasNext()){
				String key = keys.next();
				if(key.contains("xmlns")){
					keyList.add(key);
				}
			}

			for(int j=0;j<keyList.size();j++){
				optionsSet.remove(keyList.get(j));
			}

			optionsSet.put("productOrderOptionsId", items[i].getProductOrderOptionsId());

			String jsonPrettyPrintOptionsSet = optionsSet.toString(PRETTY_PRINT_INDENT_FACTOR);

			roseomh.addOrderItem(itemID, eoProductID, jsonPrettyPrintOptionsSet, "Submitted");

			if(i==(items.length-1)){

				orderItems += itemID;

			}else{

				orderItems += itemID +",";

			}

		}

		Calendar dateTime = xml_order.getOrderDateTime();
		Date lastUpdateDate = dateTime.getTime();
		roseomh.addOrder(orderID, "Accepted", lastUpdateDate.toString(), orderReference, orderItems);

		OrderIdDocument orderIdDocument = OrderIdDocument.Factory.newInstance();
		orderIdDocument.setOrderId(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + orderID);

		XmlCursor cursor = orderIdDocument.newCursor();
		cursor.toNextToken();
		cursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		cursor.insertNamespace("oseo", "http://www.opengis.net/oseo/1.0");
		cursor.dispose();

		response = Response.status(Response.Status.CREATED).type(MediaType.TEXT_XML).entity(orderIdDocument.toString()).build();

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

	@SuppressWarnings("unchecked")
	public String getOrdersBuildXML(Map<String,Order> orders, RoseoManagementHandler roseomh) throws Exception{

		String getOrdersXML="";

		URI baseURI = httpContext.getUriInfo().getAbsolutePath();
		serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort();

		if (!serversAdress.endsWith("/")) serversAdress += "/";

		System.out.println("Server base address : " + serversAdress);

		OrdersDocument ordersDocument = OrdersDocument.Factory.newInstance();
		Orders xml_orders = ordersDocument.addNewOrders();

		XmlCursor cursor = xml_orders.newCursor();
		cursor.toNextToken();
		cursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		cursor.insertNamespace("roseo", "http://www.opengis.net/roseo/1.0");
		cursor.insertNamespace("sps", "http://www.opengis.net/sps/2.0");
		cursor.insertNamespace("swe", "http://www.opengis.net/swe/2.0");
		cursor.insertNamespace("gml", "http://www.opengis.net/gml");
		cursor.dispose();

		Map<String,Order> sortedOrders = sortByComparator(orders);

		for (String key : sortedOrders.keySet()) {

			Order order = sortedOrders.get(key);
			CommonOrderMonitorSpecification xml_order = xml_orders.addNewOrder();

			xml_order.setOrderReference(order.getOrderRefence());
			xml_order.setOrderRemark("Retrieve Order with filter expression example");

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

			xml_order.setDownloadURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + order.getOrderID() + "/file");

			String[] itemsID = order.getOrderItems().split(",");
			Map<String,OrderItem> orderItems = new HashMap<String, OrderItem>();

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
					String currentDynamicKey = keys.next();

					String currentDynamicValue = optSetJson.getString(currentDynamicKey);

					if(currentDynamicKey.contains(":")){
						currentDynamicKey = currentDynamicKey.split(":")[1];
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
					jsonObj.put(currentDynamicKey, currentDynamicValue);


				}


				for (String keyTemp : jsonObj.keySet()) {

					if(!(keyTemp.equals("productOrderOptionsId"))){
						cur.insertElementWithText(keyTemp, jsonObj.get(keyTemp));
					}

				}

				if (cur != null)
					cur.dispose();

				EOProduct product = roseomh.getProduct(item.getEoProductID());

				ProductIdType productId = orderItem.addNewProductId();
				if(!product.getCollectionID().equals("null")){
					productId.setIdentifier("http://www.opengis.net/def/EOP/ESA/0/" + product.getCollectionID() + "/" + product.getProductID());
					productId.setCollectionId("http://www.opengis.net/def/EOP/ESA/0/" + product.getCollectionID());
				}else{
					productId.setIdentifier("http://www.opengis.net/def/EOP/ESA/0/" + product.getProductID());
				}

				orderItem.setItemIdURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + order.getOrderID() + "/" + item.getItemID());
				orderItem.setDownloadURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + order.getOrderID() + "/" + item.getItemID() + "/file");

				PaymentOptionSelectedValue paymentMethod = PaymentOptionSelectedValue.Factory.newInstance();
				paymentMethod.setPaymentMethod("quota");
				paymentMethod.setOrderAccount("project_10000");
				orderItem.setPayment(paymentMethod);

				StatusType itemStatusInfo = StatusType.Factory.newInstance();
				net.opengis.oseo.x10.EnumStatusType.Enum itemStatus = net.opengis.oseo.x10.EnumStatusType.Enum.forString(item.getStatus());
				itemStatusInfo.setStatus(itemStatus);
				orderItem.setOrderItemStatusInfo(itemStatusInfo);

			}


		}

		getOrdersXML = ordersDocument.toString();

		return getOrdersXML;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map<String,Order> sortByComparator(Map<String,Order> unsortMap) {

		List<Order> list = new LinkedList(unsortMap.entrySet());

		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		Collections.reverse(list);

		// put sorted list into map again
		//LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}


}
