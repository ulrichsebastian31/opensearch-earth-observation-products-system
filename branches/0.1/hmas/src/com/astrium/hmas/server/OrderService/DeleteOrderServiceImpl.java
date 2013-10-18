package com.astrium.hmas.server.OrderService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.util.PSQLException;

import com.astrium.hmas.bean.OrderBean.Order;
import com.astrium.hmas.client.OrderService.DeleteOrderService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * ----------------------------------------------------------------------------
 * ---------------------------- Project : HMA-S
 * ----------------------------------
 * ---------------------------------------------------------------------- File
 * Name : DeleteOrderServiceImpl.java File Type : Source Code Description :
 * Cancel an Order service implementation Delete order from the order database
 * 
 * ----------------------------------------------------------------------------
 * ----------------------------
 * 
 * ================================================================= (C)
 * COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved This software is
 * supplied by EADS Astrium Limited on the express terms that it is to be
 * treated as confidential and that it may not be copied, used or disclosed to
 * others for any purpose except as authorised in writing by this Company.
 * ------
 * ------------------------------------------------------------------------
 * --------------------------
 */
@SuppressWarnings("serial")
public class DeleteOrderServiceImpl extends RemoteServiceServlet implements DeleteOrderService {

	@Override
	public void deleteOrder(Order order) throws IllegalArgumentException {
		// TODO Auto-generated method stub
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

		try {
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/osresult", "postgres", "fightclub09");
			st = connection.createStatement();
			stbis = connection.createStatement();

			String query = "SELECT * FROM \"order\" WHERE id = '" + order.getId() + "';";

			rs = st.executeQuery(query);

			while (rs.next()) {
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
					try {
						String item_query = "DELETE FROM item WHERE item_id = '" + item_id + "';";
						stbis.executeQuery(item_query);
					} catch (PSQLException e) {
						if (e.getMessage().contains("No results were returned by the query")) {

						} else {
							throw e;
						}
					}

				}

			}

			try {
				String delete = "DELETE FROM \"order\" WHERE id = '" + order.getId() + "';";
				st.executeQuery(delete);
			} catch (PSQLException e) {
				if (e.getMessage().contains("No results were returned by the query")) {

				} else {
					throw e;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}
