package com.astrium.hmas.server.DownloadServer;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DownloadGetProduct.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class is a server providing
 *  																	access to the products the user 
 *  																	wants to download
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
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.astrium.hmas.bean.metalink.FileType;
import com.astrium.hmas.bean.metalink.FilesType;
import com.astrium.hmas.bean.metalink.MetalinkType;
import com.astrium.hmas.bean.metalink.ObjectFactory;
import com.astrium.hmas.bean.metalink.ResourcesType;
import com.astrium.hmas.bean.metalink.ResourcesType.Url;
import com.sun.jersey.api.core.HttpContext;

@Path("dw")
public class DownloadGetProduct {

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

			/*
			 * Check status : If the product is on-line available, there are the
			 * following sub-cases : - Direct : The product is returned as byte
			 * stream - Forward : The product is on-line, but at different site
			 * -> the server returns a forwarded download message - Metalink :
			 * The product is on-line, but composed of multiple files -> the
			 * server returns a Metalink XML file If the product is not
			 * available, the server sends back a XML file giving the time to
			 * wait to retry
			 */
			if (!rs.isBeforeFirst()) {
				/*
				 * If the product is missing, return 404 response
				 */
				System.out.println("no result");
				return Response.status(404).build();

			} else {
				while (rs.next()) {
					/*
					 * Direct Download
					 */

					if (rs.getString("status").equals("Direct")) {

						String uri = "C:/";
						/*
						 * Create the full product URI to collect the file on
						 * the computer
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
						return Response.ok(fileContent, "application/octet-stream")
								.header("Content-Disposition", "attachment; filename=" + rs.getString("uri")).build();

					} else
					/*
					 * Metalink
					 */
					if (rs.getString("status").equals("Metalink")) {
						/*
						 * ******XML RESPONSE CREATION*******
						 */
						ObjectFactory objectFactory = new ObjectFactory();
						MetalinkType metalinkType = objectFactory.createMetalinkType();

						FilesType filesType = objectFactory.createFilesType();

						List<FileType> files = filesType.getFile();
						/*
						 * Get back all the URI from all the parts of the
						 * product
						 */
						String[] uris = rs.getString("uri").split(",");

						/*
						 * For all the items
						 */
						for (int i = 0; i < uris.length; i++) {

							ResourcesType resourcesType = objectFactory.createResourcesType();
							List<Url> urls = resourcesType.getUrl();
							String[] servers = rs.getString("server").split(",");
							/*
							 * If the product to download has one item but has
							 * several resources
							 */
							if (uris.length == 1 && servers.length > 1) {

								for (int j = 0; j < servers.length; j++) {

									int nb = j + 1;
									FileType fileType = objectFactory.createFileType();
									/*
									 * Change the name of the file to retrieve
									 * it in the database
									 */
									fileType.setName(uris[i] + "_" + nb);

									Url url = objectFactory.createResourcesTypeUrl();
									url.setValue(servers[j]);
									url.setType("http");
									urls.add(url);

									fileType.setResources(resourcesType);
									fileType.setCopyright("European Space Agency");

									files.add(fileType);

								}

							} else {
								/*
								 * There are several items in the EO product
								 */
								FileType fileType = objectFactory.createFileType();
								fileType.setName(uris[i]);

								for (int j = 0; j < servers.length; j++) {
									Url url = objectFactory.createResourcesTypeUrl();
									url.setValue(servers[j]);
									url.setType("http");
									urls.add(url);
								}

								fileType.setResources(resourcesType);
								fileType.setCopyright("European Space Agency");
								files.add(fileType);
							}

						}

						metalinkType.setFiles(filesType);

						/*
						 * Creation of the XML file thanks to JAXB api
						 */
						JAXBContext jaxbContext = JAXBContext.newInstance("com.astrium.hmas.bean.metalink");
						Marshaller marshaller = jaxbContext.createMarshaller();
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));

						marshaller.marshal(metalinkType, System.out);
						/*
						 * Return the response
						 */
						return Response.ok(metalinkType, "application/atom+xml").build();

					} else
					/*
					 * The file is not on-line yet
					 */
					if (rs.getString("status").equals("Not Available")) {
						/*
						 * Retry response
						 */
						String retryResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								+ "<dseo:ProductDownloadResponse xmlns:dseo=\"http://www.opengis.net/dseo/1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/dseo/1.0 ./dseo.xsd\">"
								+ "	<dseo:ResponseCode>IN_PROGRESS</dseo:ResponseCode>" + "	<dseo:RetryAfter>30</dseo:RetryAfter>"
								+ "</dseo:ProductDownloadResponse>";
						/*
						 * return it to the client
						 */
						return Response.ok(retryResponse, "application/atom+xml").build();

					} else
					/*
					 * Forwarded product
					 */
					if (rs.getString("status").equals("Forwarded")) {

						System.out.println("forwarded");
						/*
						 * Creation of the URL of the other server
						 */
						URI url = new URI(rs.getString("server") + "?service=DSEO&request=GetProduct&version=1.0.0&ProductURI=" + rs.getString("uri"));

						/*
						 * Forward to this url and get its response
						 */
						return Response.seeOther(url).build();

					} else {

						return null;

					}
				}
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * Server error
		 */
		return Response.serverError().build();
	}

}
