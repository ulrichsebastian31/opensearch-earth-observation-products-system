package com.astrium.hmas.server.DownloadService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DownloadProductServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               DownloadProduct Service interface 
 *   																	implementation
 *   																	Analyse the response from the Download
 *   																	Server and sends back a String to the 
 *   																	Client : either a url or a message according
 *   																	to the nature of the Download Server
 *   																	response
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.astrium.hmas.client.DownloadService.DownloadProductService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@SuppressWarnings("serial")
public class DownloadProductServiceImpl extends RemoteServiceServlet implements DownloadProductService {

	@Override
	public String getProducts(String uri) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		/*
		 * Jersey Client creation
		 */
		Client client = new Client();
		/*
		 * Call to the server
		 */
		WebResource webResource = client.resource("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/dw");
		/*
		 * parameters map
		 */
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		/*
		 * Add all the parameters to the request to the server : the parameter
		 * uri is obtained from the client
		 */
		queryParams.add("service", "DSEO");
		queryParams.add("request", "GetProduct");
		queryParams.add("version", "1.0.0");
		queryParams.add("ProductURI", uri);

		/*
		 * Get the mime Type of the response
		 */
		String type = webResource.queryParams(queryParams).head().getType().toString();
		System.out.println(type);
		/*
		 * If the server returned a file
		 */
		if (type.equals("application/octet-stream")) {
			/*
			 * Sends back its url to the client so it can download it directly
			 */
			String url = webResource.queryParams(queryParams).toString();
			return url;
			/*
			 * If the server returned a XML file
			 */
		} else if (type.equals("application/atom+xml")) {
			/*
			 * Sends back the XML as a string
			 */
			String xml = webResource.queryParams(queryParams).accept("application/atom+xml").get(String.class);

			/*
			 * ************ XML response PARSING *****************
			 */
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;

			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

				Element root = doc.getDocumentElement();
				root.normalize();

				String node = root.getNodeName();
				/*
				 * If the xml response is a metalink file
				 */
				if (node.equals("metalink")) {

					/*
					 * Map <ProductURI,Server>
					 */
					Map<String, String> metalink = new HashMap<String, String>();
					Element files = (Element) doc.getElementsByTagName("files").item(0);
					NodeList nlist = files.getElementsByTagName("file");

					/*
					 * If there are some files
					 */
					if (nlist != null && nlist.getLength() > 0) {
						/*
						 * Look at all of them and get the informations
						 */
						for (int i = 0; i < nlist.getLength(); i++) {

							Element file = (Element) nlist.item(i);
							/*
							 * Get the URI for each file
							 */
							String fileName = file.getAttribute("name");
							Element resources = (Element) file.getElementsByTagName("resources").item(0);

							NodeList urls = resources.getElementsByTagName("url");
							String servers = "";
							/*
							 * Get all the resources for each files
							 */
							if (urls != null && urls.getLength() > 0) {

								for (int j = 0; j < urls.getLength(); j++) {

									Element url = (Element) urls.item(j);
									String urltxt = url.getChildNodes().item(0).getNodeValue();
									/*
									 * The resources has to be separated by a
									 * comma
									 */
									if (nlist.getLength() > 1 && j < nlist.getLength() - 1) {
										servers += urltxt + ",";
									} else {
										servers += urltxt;
									}

								}
							}

							/*
							 * Add the entry to the map
							 */
							metalink.put(fileName, servers);
						}
					}

					/*
					 * Build the message sent to the user
					 */
					String reponse = null;
					reponse = "Your product is on-line available, but in several part and/or on several servers : \n";
					reponse += "	URI : \n";
					/*
					 * Iterator on the filename
					 */
					Iterator<String> iterator = metalink.keySet().iterator();
					while (iterator.hasNext()) {

						String productUri = (String) iterator.next();
						reponse += "		- " + productUri;
						reponse += " is available on the following servers : \n";
						String[] servers = metalink.get(productUri).split(",");

						for (int k = 0; k < servers.length; k++) {
							reponse += "			- " + servers[k] + "\n";
						}

					}

					return reponse;

				} else {
					/*
					 * If it's not a metalink response, it's a retry response
					 */
					String reponse = null;
					/*
					 * Retry XML parsing
					 */
					Element rep = (Element) doc.getElementsByTagName("dseo:ProductDownloadResponse").item(0);

					Element code = (Element) rep.getElementsByTagName("dseo:ResponseCode").item(0);
					String codetxt = code.getChildNodes().item(0).getNodeValue();

					Element retry = (Element) rep.getElementsByTagName("dseo:RetryAfter").item(0);
					String retrytxt = retry.getChildNodes().item(0).getNodeValue();

					/*
					 * The message to send to the user is built
					 */
					reponse = "Your product is not on-line available right now.\n";
					reponse += "	Status : " + codetxt + "\n";
					reponse += "	Retry after : " + retrytxt;
					/*
					 * Returned it
					 */
					return reponse;

				}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (type.equals("text/html; charset=utf-8")) {
			/*
			 * The product to download is missing!
			 */
			return "Your product is missing!";

		}

		return null;
	}

}
