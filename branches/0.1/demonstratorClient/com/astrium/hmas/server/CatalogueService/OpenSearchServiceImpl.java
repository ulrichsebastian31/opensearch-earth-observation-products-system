package com.astrium.hmas.server.CatalogueService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OpenSearchServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Opensearch Service interface 
 *   																	implementation
 *   																	Parses the XML file description returned
 *   																	from the OpenSearch Server for the 
 *   																	Catalogue Search feature and returns 
 *   																	a map with all the available parameters
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
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Context;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.astrium.hmas.client.Hmas;
import com.astrium.hmas.client.CatalogueService.OpenSearchService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;


@SuppressWarnings("serial")
public class OpenSearchServiceImpl extends RemoteServiceServlet implements OpenSearchService {


	public Map<String, String> getDescriptionFile(String url) throws IllegalArgumentException {
		// URLConnectionClientHandler ch = new URLConnectionClientHandler(new
		// ConnectionFactory());
		
		Hmas.baseURLCatalogue.add(url);
		/*
		 * Jersey Client creation
		 */
		Client client = new Client();
		/*
		 * Call to the server
		 */
		WebResource webResource = client.resource(url + "/hmas/cat/os");
		/*
		 * Get the response : the XML description file
		 */
		String s = webResource.accept("application/atom+xml").get(String.class);

		/*
		 * The object where we will put the results from the XML response
		 */
		Map<String, String> parameters = new HashMap<String, String>();
		
		/*
		 * We register the description file for the user to see it if he wants
		 */
		parameters.put("description", url + "/hmas/cat/os");

		String rep = "";

		try {
			/*
			 * ********************** XML RESPONSE PARSING ********************
			 */
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;

			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(s.getBytes("utf-8"))));

			doc.getDocumentElement().normalize();

			NodeList nlist = doc.getElementsByTagName("Url");

			Node nNode = nlist.item(0);

			Element eElement = (Element) nNode;
			String template = eElement.getAttribute("template");

			/*
			 * Parse the template url attribute to get all the parameters ->
			 * regex
			 */
			Pattern pattern = Pattern.compile("(.*?)*\\&(.*?)=\\{(.*?)\\?\\}.*?");
			Matcher m = pattern.matcher(template);

			while (m.find()) {
				/*
				 * put all the parameters in a map, where the key is the OGC
				 * element name and the value is the key of the future Catalogue
				 * Search request
				 */
				parameters.put(m.group(3), m.group(2));
				rep += m.group(3) + " : " + m.group(2) + "\n";
			}
			System.out.println(rep);
			return parameters;

			/*
			 * if (nlist != null && nlist.getLength() > 0){ for(int i = 0 ; i <
			 * nlist.getLength();i++) { Element param = (Element)nlist.item(i);
			 * String value = param.getAttribute("value"); String[] names =
			 * value.split(":"); String name = names[1]; if
			 * (name.endsWith("}")){ name = name.substring(0,name.length()-1); }
			 * parameters.add(name); rep += name + "\n";
			 * 
			 * } } else { rep = "null"; }
			 */

		} catch (ParserConfigurationException e) {// TODO Auto-generated catch
													// block
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
