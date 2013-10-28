package com.astrium.hmas.server.DownloadServer;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DownloadGetProductBis.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class is a server providing
 *  																	direct access to the products the user 
 *  																	wants to download, but which are not 
 *  																	available from the main Download Server
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

@Path("dw_bis")
public class DownloadGetProductBis {
	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;

	@GET
	public Response getProduct() {
		/*
		 * Get the parameters from the query
		 */
		MultivaluedMap<String, String> conf = ui.getQueryParameters();

		/*
		 * Get the important one : the product URI
		 */
		String uri_get = conf.get("ProductURI").get(0);
		/*
		 * Split the string and retrieve the file name
		 */
		String filename = uri_get.split("/")[uri_get.split("/").length - 1];

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
		try {
			System.out.println("PostgreSQL JDBC Driver Registered!");

			Connection connection = null;
			Statement st = null;
			ResultSet rs = null;

			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/osresult", "postgres", "astrium1");

			st = connection.createStatement();
			/*
			 * SQL Query
			 */
			String query = "SELECT * FROM dwproduct WHERE filename = '" + filename + "'";

			rs = st.executeQuery(query);
			System.out.println(query);

			if (connection != null)
				System.out.println("Connection succeded!");

			while (rs.next()) {
				/*
				 * Direct Download
				 */
				String uri = "C:/";
				/*
				 * Create the full product URI to collect the file on the
				 * computer
				 */
				String urn = uri + rs.getString("uri");
				/*
				 * Create a File object
				 */
				File product = new File(urn);

				FileInputStream prodStream = null;
				prodStream = new FileInputStream(product);

				byte[] fileContent = new byte[(int) product.length()];
				prodStream.read(fileContent);

				/*
				 * The file is returned as byte stream
				 */
				return Response.ok(fileContent, "application/octet-stream").header("Content-Disposition", "attachment; filename=" + rs.getString("uri"))
						.build();

			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
