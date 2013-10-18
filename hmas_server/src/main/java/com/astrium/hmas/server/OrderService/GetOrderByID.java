package com.astrium.hmas.server.OrderService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetOrderByID.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Sends back the XML file which describes 
 *   																	one Order thanks to its ID, and gives 
 *   																	the status of this order
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import net.opengis.oseo.x10.CommonOrderItemType.Option;
import net.opengis.oseo.x10.DeliveryOptionsType;
import net.opengis.oseo.x10.DeliveryOptionsType.OnlineDataAccess;
import net.opengis.oseo.x10.ParameterDataType;
import net.opengis.oseo.x10.PaymentOptionSelectedValue;
import net.opengis.oseo.x10.ProductIdType;
import net.opengis.oseo.x10.ProtocolType.Enum;
import net.opengis.oseo.x10.StatusType;
import net.opengis.roseo.x10.CommonOrderMonitorSpecification;
import net.opengis.roseo.x10.CommonOrderStatusItemType;
import net.opengis.roseo.x10.OrderDocument;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

import com.sun.jersey.api.core.HttpContext;

@Path("order/getOrderByID")
public class GetOrderByID {
	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;

	@GET
	public Response getOrderByID() throws SQLException {

		String getOrderXML = null;

		/*
		 * Get the parameters from the query
		 */
		MultivaluedMap<String, String> conf = ui.getQueryParameters();
		/*
		 * Order ID
		 */
		String orderIdentifier = conf.get("orderIdentifier").get(0);

		try {
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();
		}

		/*
		 * Connection to the database
		 */
		System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");

		System.out.println("PostgreSQL JDBC Driver Registered!");

		Connection connection = null;
		Statement st = null;
		Statement stbis = null;
		ResultSet rs = null;
		ResultSet rsbis = null;

		connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/osresult", "postgres", "fightclub09");
		st = connection.createStatement();
		stbis = connection.createStatement();

		String query = "SELECT * FROM \"order\" WHERE id = '" + orderIdentifier + "';";

		rs = st.executeQuery(query);

		while (rs.next()) {

			OrderDocument doc = OrderDocument.Factory.newInstance();
			CommonOrderMonitorSpecification xml_order = doc.addNewOrder();
			xml_order.setOrderReference("example_0001");
			xml_order.setOrderRemark("example");
			DeliveryOptionsType deliveryOptions = xml_order.addNewDeliveryOptions();
			OnlineDataAccess onlineDataAccess = deliveryOptions.addNewOnlineDataAccess();

			Enum protocol = Enum.forString("https");
			onlineDataAccess.setProtocol(protocol);

			xml_order.setDeliveryOptions(deliveryOptions);

			net.opengis.oseo.x10.EnumOrderType.Enum orderType = net.opengis.oseo.x10.EnumOrderType.Enum.forString("PRODUCT_ORDER");
			xml_order.setOrderType(orderType);
			xml_order.setOrderId("http://roseo.example.server/ROSEO/1.0.0/order/" + orderIdentifier);

			StatusType statusInfo = StatusType.Factory.newInstance();
			net.opengis.oseo.x10.EnumStatusType.Enum status = net.opengis.oseo.x10.EnumStatusType.Enum.forString(rs.getString("status"));
			statusInfo.setStatus(status);
			xml_order.setOrderStatusInfo(statusInfo);

			Calendar time = Calendar.getInstance();
			time.set(2013, 04, 29, 00, 00, 00);
			xml_order.setOrderDateTime(time);

			/*
			 * List of the items ID stored in the db in String
			 */
			String items_id = rs.getString("items_id");

			String[] items_string = items_id.split(",");

			for (int i = 0; i < items_string.length; i++) {

				String item_id = items_string[i];
				/*
				 * search in the item table to find the item related to the
				 * order
				 */
				String item_query = "SELECT * FROM item WHERE item_id = '" + item_id + "';";
				rsbis = stbis.executeQuery(item_query);

				while (rsbis.next()) {

					CommonOrderStatusItemType orderItem = xml_order.addNewOrderItem();
					orderItem.setItemId(rsbis.getString("item_id"));
					orderItem.setProductOrderOptionsId("Level 1,Product SLC (ASA_IMS)");
					orderItem.setOrderItemRemark("Product number " + i);

					Option option = orderItem.addNewOption();
					ParameterDataType parameterData = option.addNewParameterData();

					net.opengis.oseo.x10.SWEEncoding.Enum encoding = net.opengis.oseo.x10.SWEEncoding.Enum.forString("XMLEncoding");
					parameterData.setEncoding(encoding);

					XmlObject values = parameterData.addNewValues();

					XmlCursor cur = values.newCursor();
					// We could use the convenient xobj.selectPath() or
					// cur.selectPath()
					// to position the cursor on the <person> element, but let's
					// use the
					// cursor's toChild() instead.
					cur.toChild("values");
					cur.toEndToken();

					Map<String, String> options = new HashMap<String, String>();
					options.put("processingLevel", rsbis.getString("processing_level"));
					options.put("productType", rsbis.getString("product_type"));
					options.put("qualityOfService", rsbis.getString("quality_of_service"));

					for (String key : options.keySet()) {

						cur.insertElementWithText(key, options.get(key));

					}
					if (cur != null)
						cur.dispose();

					ProductIdType productId = orderItem.addNewProductId();
					productId.setIdentifier(rsbis.getString("product_id"));
					productId.setCollectionId("http://www.opengis.net/def/EOP/ESA/0/ESA.EECF.ENVISAT_ASA_IMx_xS");
					
					PaymentOptionSelectedValue paymentMethod = PaymentOptionSelectedValue.Factory.newInstance();
					paymentMethod.setPaymentMethod("quota");
					paymentMethod.setOrderAccount("project_10000");
					orderItem.setPayment(paymentMethod );
					
					StatusType itemStatusInfo = StatusType.Factory.newInstance();
					net.opengis.oseo.x10.EnumStatusType.Enum itemStatus = net.opengis.oseo.x10.EnumStatusType.Enum.forString(rsbis.getString("status"));
					itemStatusInfo.setStatus(itemStatus);
					orderItem.setOrderItemStatusInfo(itemStatusInfo);

				}
			}
			getOrderXML = doc.toString();

		}
		return Response.ok(getOrderXML, "text/xml").build();

	}

}
