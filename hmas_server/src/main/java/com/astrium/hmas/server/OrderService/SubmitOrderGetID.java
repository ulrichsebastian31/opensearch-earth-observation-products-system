package com.astrium.hmas.server.OrderService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SubmitOrderGetID.java
 *   File Type                                          :               Source Code
 *   Description                                        :               receives the XML request to submit an
 *   																	order, registers this order in a database
 *   																	and sends back an acceptance response
 *   																	with the order ID
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.jersey.api.core.HttpContext;

/**
 * 
 * @author re-cetienne
 */
@Path("order/submit")
public class SubmitOrderGetID {

	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;

	@POST
	@Consumes(MediaType.TEXT_XML)
	public Response postRequestParser(String xmlrequest) throws SQLException, ParserConfigurationException, UnsupportedEncodingException, SAXException,
			IOException {

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
		ResultSet rs = null;

		connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/osresult", "postgres", "fightclub09");

		st = connection.createStatement();
		/*try {
			String delete = "DELETE from item;";
			st.executeQuery(delete);
		} catch (SQLException e) {
			if (e.getMessage().contains("No results were returned by the query")) {

			} else {
				throw e;
			}
		}*/

		/*
		 * ********************** XML REQUEST PARSING ********************
		 */
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xmlrequest.getBytes("utf-8"))));

		doc.getDocumentElement().normalize();

		/*
		 * Get the "orderItem" element
		 */
		NodeList nlist = doc.getElementsByTagName("orderItem");

		List<String> items = new ArrayList<>();

		if (nlist != null && nlist.getLength() > 0) {

			for (int i = 0; i < nlist.getLength(); i++) {

				Element item = (Element) nlist.item(i);

				Element id = (Element) item.getElementsByTagName("ns:itemId").item(0);
				String idtxt = id.getChildNodes().item(0).getNodeValue();
				items.add(idtxt);

				Element productID = (Element) item.getElementsByTagName("ns:productId").item(0);

				Element identifier = (Element) productID.getElementsByTagName("ns:identifier").item(0);
				String identifiertxt = identifier.getChildNodes().item(0).getNodeValue();

				Element option = (Element) item.getElementsByTagName("ns:option").item(0);

				Element parameterData = (Element) option.getElementsByTagName("ns:ParameterData").item(0);

				Element values = (Element) parameterData.getElementsByTagName("ns:values").item(0);

				Element processingLevel = (Element) values.getElementsByTagName("processingLevel").item(0);
				String processingLeveltxt = processingLevel.getChildNodes().item(0).getNodeValue();

				Element qualityOfService = (Element) values.getElementsByTagName("qualityOfService").item(0);
				String qualityOfServicetxt = qualityOfService.getChildNodes().item(0).getNodeValue();

				Element productType = (Element) values.getElementsByTagName("productType").item(0);
				String productTypetxt = productType.getChildNodes().item(0).getNodeValue();

				/*
				 * SQL query to add a line in the table item
				 */
				try {
					String query = "INSERT INTO item(item_id, processing_level, product_type, quality_of_service, status, product_id) VALUES ('" + idtxt
							+ "', '" + processingLeveltxt + "', '" + productTypetxt + "', '" + qualityOfServicetxt + "', 'Completed', '" + identifiertxt
							+ "');";

					st.executeQuery(query);
					System.out.println(query);
				} catch (SQLException e) {
					if (e.getMessage().contains("No results were returned by the query")) {

					} else {
						throw e;
					}
				}

			}

		}
		String itemsID = "";

		for (String id : items) {

			if (id.equals(items.get(items.size() - 1))) {
				itemsID += id;
			} else {
				itemsID += id + ",";
			}
		}

		int lower = 10;
		int higher = 1000;

		int random = (int) (Math.random() * (higher - lower)) + lower;

		String orderID = "ID_" + random;
		try {
			String query2 = "INSERT INTO \"order\" (id, status, items_id) VALUES ('" + orderID + "', 'Completed', '" + itemsID + "');";
			System.out.println(query2);
			st.executeQuery(query2);

		} catch (SQLException e) {
			if (e.getMessage().contains("No results were returned by the query")) {

			} else {
				throw e;
			}
		}

		/*
		 * XML RESPONSE CREATION
		 */

		String query3 = "SELECT * FROM \"order\" WHERE id = '" + orderID + "';";

		rs = st.executeQuery(query3);
		System.out.println(query3);
		String xmlResponse = null;
		while (rs.next()) {
			xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
					+ "<oseo:orderId xsi:schemaLocation=\"http://www.opengis.net/roseo/1.0 roseo.xsd\" \n"
					+ "xmlns:oseo=\"http://www.opengis.net/oseo/1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
					+ "http://roseo.example.com/ROSEO/1.0.0/order/" + rs.getString("id") + "</oseo:orderId>";
		}

		System.out.println(xmlResponse);
		return Response.ok(xmlResponse.toString(), "text/xml").build();
	}
}
