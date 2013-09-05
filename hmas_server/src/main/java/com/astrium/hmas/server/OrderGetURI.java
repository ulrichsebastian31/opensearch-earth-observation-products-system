package com.astrium.hmas.server;

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

		MultivaluedMap<String, String> conf = ui.getQueryParameters();

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
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/osresult", "postgres", "fightclub09");

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
					String name = rs.getString("filename");
					String uri = "Products/" + name;
					System.out.println(uri);
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
