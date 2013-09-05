package com.astrium.hmas.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
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
import javax.xml.bind.JAXBException;

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
		MultivaluedMap<String, String> conf = ui.getQueryParameters();
		String uri_get = conf.get("ProductURI").get(0);
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

			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/osresult", "postgres", "fightclub09");

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
				String urn = uri + rs.getString("uri");
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
