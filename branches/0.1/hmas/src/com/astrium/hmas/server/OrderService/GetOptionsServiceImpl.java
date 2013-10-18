package com.astrium.hmas.server.OrderService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
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

import com.astrium.hmas.bean.CatalogueBean.CatalogueResult;
import com.astrium.hmas.bean.DownloadBean.DownloadProduct;
import com.astrium.hmas.bean.OrderBean.Option;
import com.astrium.hmas.client.OrderService.GetOptionsService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetOptionsServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Get Options Service interface implementation
 *   																	Sends the list of the chosen options 
 *   																	for a product to order
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
@SuppressWarnings("serial")
public class GetOptionsServiceImpl extends RemoteServiceServlet implements GetOptionsService{

	@Override
	public Map<String, Option> getOptions(DownloadProduct downloadProduct) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		/*
		 * Jersey Client configuration
		 */
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, 30000);
		clientConfig.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 30000);

		/*
		 * Jersey Client creation
		 */
		Client client = new Client();
		/*
		 * Call to the server
		 */
		WebResource webResource = client.resource("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/order/getOptions");
		/*
		 * parameters map
		 */
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		
		/*
		 * Add the product identifier to the request as a KVP
		 */
		queryParams.add("ProductID", downloadProduct.getId());
		
		/*
		 * Get the response : the XML file
		 */
		String s = webResource.queryParams(queryParams).accept("application/atom+xml").get(String.class);
		
		/*
		 * The object where we will put the results from the XML response
		 */
		Map<String, Option> options = new HashMap<String, Option>();

		try {
			/*
			 * ********************** XML RESPONSE PARSING ********************
			 */
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(s.getBytes("utf-8"))));

			doc.getDocumentElement().normalize();
			
			/*
			 * Recover the options group
			 */
			Element optionsGroup = (Element) doc.getElementsByTagName("roseo:OrderOptionGroup").item(0);
			
			NodeList optionsList = optionsGroup.getElementsByTagName("oseo:option");
			
			if (optionsList != null && optionsList.getLength() > 0) {

				for (int i = 0; i < optionsList.getLength(); i++) {
					
					Option opt = new Option();
					
					Element optionElmt = (Element) optionsList.item(i);
					
					Element dataRecord = (Element) optionElmt.getElementsByTagName("swe:DataRecord").item(0);
					Element field = (Element) dataRecord.getElementsByTagName("swe:field").item(0);
					String name = field.getAttribute("name");
					
					opt.setName(name);
					
					Element category = (Element) field.getElementsByTagName("swe:Category").item(0);
					Element identifier = (Element) category.getElementsByTagName("swe:identifier").item(0);
					String idtxt = identifier.getChildNodes().item(0).getNodeValue();
					
					opt.setIdentifier(idtxt);
					
					Element constraint = (Element) category.getElementsByTagName("swe:constraint").item(0);
					Element  allowedTokens = (Element) constraint.getElementsByTagName("swe:AllowedTokens").item(0);
					NodeList tokenListElement = allowedTokens.getElementsByTagName("swe:value");
					
					List<String> tokenList = opt.getAllowedTokens();
					
					if (tokenListElement != null && tokenListElement.getLength() > 0) {

						for (int j = 0; j < tokenListElement.getLength(); j++) {
							
							Element token = (Element) tokenListElement.item(j);
							String tokentxt = token.getChildNodes().item(0).getNodeValue();
							
							tokenList.add(tokentxt);
							
						}
						
					}
					
					options.put(String.valueOf(i), opt);
				}
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

		

		
		
		return options;
	}

}
