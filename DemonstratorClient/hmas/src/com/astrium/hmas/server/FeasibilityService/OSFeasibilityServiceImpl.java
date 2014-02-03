package com.astrium.hmas.server.FeasibilityService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OSFeasibilityServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Opensearch for the feasibility Service 
 *   																	interface implementation
 *   																	Parses the XML file description returned
 *   																	from the OpenSearch Server for the 
 *   																	Feasibility Search feature and returned the 
 *   																	available parameters to the client
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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.astrium.hmas.bean.FeasibilityBean.Parameter;
import com.astrium.hmas.client.Hmas;
import com.astrium.hmas.client.FeasibilityService.OSFeasibilityService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

@SuppressWarnings("serial")
public class OSFeasibilityServiceImpl extends RemoteServiceServlet implements OSFeasibilityService {

	@Override
	public Map<String, Parameter> getParameters(String url) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		/*
		 * Jersey client creation
		 */
		Client client = new Client();
		if (Hmas.baseURLFeasibility.size() == 0)
			Hmas.baseURLFeasibility.add(url);
		else {
			String u = Hmas.baseURLFeasibility.get(0);
			if (u.contains("localhost") || u.contains("127.0.0.1")) {
				if (!url.contains("localhost") && !url.contains("127.0.0.1")) {

					Hmas.baseURLFeasibility.remove(0);
					Hmas.baseURLFeasibility.add(url);
				}
			}
		}
		/*
		 * Call to the server
		 */
		WebResource webResource = client.resource(url + "/hmas/fas/os/description");
		/*
		 * Get the response : the XML description file
		 */
		String s = webResource.accept("text/xml").get(String.class);
		/*
		 * The object where we will put the results from the XML response
		 */
		Map<String, Parameter> parameters = new HashMap<String, Parameter>();

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

			Node nNode = nlist.item(1);

			Element eElement = (Element) nNode;
			// String template = eElement.getAttribute("template");

			NodeList params = eElement.getElementsByTagName("param:Parameter");

			if (params != null && params.getLength() > 0) {

				for (int i = 0; i < params.getLength(); i++) {
					/*
					 * create the Parameter with all its options
					 */
					Parameter parameter = new Parameter();

					Element param = (Element) params.item(i);
					/*
					 * OGC parameter name
					 */
					String name = param.getAttribute("name");
					parameter.setName(name);

					/*
					 * Key for the future request URL tasking Feasibility
					 */
					String key = param.getAttribute("value");
					parameter.setKey(key);

					rep += key + " : " + name + "\n";
					System.out.println(rep);

					/*
					 * Min and Max values
					 */
					if (param.getAttribute("minInclusive") != null && !param.getAttribute("minInclusive").equals("")) {
						String min = param.getAttribute("minInclusive");
						parameter.setMin(min);
					}

					if (param.getAttribute("maxInclusive") != null && !param.getAttribute("maxInclusive").equals("")) {
						String max = param.getAttribute("maxInclusive");
						parameter.setMax(max);
					}

					/*
					 * Parameter's options list
					 */
					List<String> options_list = new ArrayList<String>();

					NodeList options = param.getElementsByTagName("param:option");

					if (options != null && options.getLength() > 0) {

						for (int j = 0; j < options.getLength(); j++) {

							Element option = (Element) options.item(j);
							String opt = option.getAttribute("value");
							options_list.add(opt);

						}
						parameter.setOptions(options_list);
					}

					/*
					 * Put the build Parameter in the map to return to the
					 * client
					 */
					parameters.put(key, parameter);
				}

				System.out.println(rep);

			}

			return parameters;

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
