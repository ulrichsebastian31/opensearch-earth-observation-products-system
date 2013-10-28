package com.astrium.hmas.server.OrderService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OrderGetURI.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class is a server which retrieve	
 *   																	The URI of the product to download 	
 *   																	thanks to its URI.
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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.api.core.HttpContext;

@Path("order")
public class OrderGetURI {
	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;

	@GET
	public Response getURI() {
		/*
		 * Get the parameters from the query
		 */
		MultivaluedMap<String, String> conf = ui.getQueryParameters();
		/*
		 * Get the important one : the product ID
		 */
		String product_id = conf.get("ProductID").get(0);

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

		try {
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/osresult", "postgres", "astrium1");

			st = connection.createStatement();
			/*
			 * SQL Query
			 */
			String query = "SELECT * FROM dwproduct where id = '" + product_id + "'";

			rs = st.executeQuery(query);
			System.out.println(query);

			if (connection != null)
				System.out.println("Connection succeded!");

			if (!rs.isBeforeFirst()) {
				/*
				 * If the product is missing, return 404 response
				 */
				System.out.println("no result");

				return Response.status(404).build();

			} else {

				while (rs.next()) {
					/*
					 * Construct the URI of the product thanks to its filename
					 */
					String name = rs.getString("filename");
					String uri = "Products/" + name;
					System.out.println(uri);
					/*
					 * Return the URI to the client
					 */
					return Response.ok(uri, "text/html").build();
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
