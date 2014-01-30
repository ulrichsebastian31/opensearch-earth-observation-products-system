package com.astrium.hmas.roseo.capabilities;

import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.xmlbeans.XmlCursor;

import net.opengis.oseo.x10.CollectionCapability;
import net.opengis.oseo.x10.EncodingType;
import net.opengis.oseo.x10.CollectionCapability.DescribeResultAccessCapabilities;
import net.opengis.oseo.x10.OrderingServiceContentsType.CancelCapabilities;
import net.opengis.oseo.x10.OrderingServiceContentsType.GetQuotationCapabilities;
import net.opengis.oseo.x10.OrderingServiceContentsType.GetStatusCapabilities;
import net.opengis.oseo.x10.OrderingServiceContentsType.ProductOrders;
import net.opengis.oseo.x10.OrderingServiceContentsType.ProgrammingOrders;
import net.opengis.oseo.x10.OrderingServiceContentsType.SubmitCapabilities;
import net.opengis.oseo.x10.OrderingServiceContentsType.SubscriptionOrders;
import net.opengis.roseo.x10.CapabilitiesDocument;
import net.opengis.roseo.x10.OrderingServiceContentsType;
import net.opengis.roseo.x10.CapabilitiesDocument.Capabilities;
import net.opengis.roseo.x10.OrderingServiceContentsType.ResourceURL;
import net.opengis.roseo.x10.OrderingServiceContentsType.ResourceURL.Resource.Enum;

import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.astrium.roseodbhandler.structures.Collection;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetCapabilitiesContents.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Capabilities resource contains metadata 
 *   																	about the ROSEO server reporting. This 
 *   																	class returns an XML encoding document 
 *   																	containing just the information
 * 																		about the capabilities contents.
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

@SuppressWarnings("restriction")
public class GetCapabilitiesContents extends GenericCapabilitiesService {
	
	public GetCapabilitiesContents(){
		super();
	}

	@Override
	public void getCapabilities(Map<String, String> inputs, String serverAdress) throws Exception {

		/*
		 * XML file creation
		 */
		RoseoManagementHandler roseomh = new RoseoManagementHandler();
		
		CapabilitiesDocument capabilitiesDocument = CapabilitiesDocument.Factory.newInstance();
		Capabilities capabilities = capabilitiesDocument.addNewCapabilities();
		
		XmlCursor cursor = capabilities.newCursor();
		cursor.toNextToken();
		cursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		cursor.insertNamespace("roseo", "http://www.opengis.net/roseo/1.0");
		cursor.insertNamespace("oseo", "http://www.opengis.net/oseo/1.0");
		cursor.dispose();
		
		capabilities.setVersion("0.0.0");
		capabilities.setUpdateSequence("String");
		
		OrderingServiceContentsType contents = capabilities.addNewContents();
		
		EncodingType contentsType = contents.addNewContentsType();
		net.opengis.oseo.x10.SWEEncoding.Enum supportedEncoding = net.opengis.oseo.x10.SWEEncoding.Enum.forString("XMLEncoding");
		contentsType.addSupportedEncoding(supportedEncoding);
		
		ProductOrders productOrders = contents.addNewProductOrders();
		productOrders.setSupported(true);
		
		SubscriptionOrders subscriptionOrders = contents.addNewSubscriptionOrders();
		subscriptionOrders.setSupported(false);
		
		ProgrammingOrders programmingOrders = contents.addNewProgrammingOrders();
		programmingOrders.setSupported(false);
		
		GetQuotationCapabilities quotationCapabilities = contents.addNewGetQuotationCapabilities();
		quotationCapabilities.setSupported(false);
		quotationCapabilities.setSynchronous(false);
		quotationCapabilities.setAsynchronous(false);
		quotationCapabilities.setMonitoring(false);
		quotationCapabilities.setOffLine(false);
		
		SubmitCapabilities submitCapabilities = contents.addNewSubmitCapabilities();
		submitCapabilities.setAsynchronous(false);
		submitCapabilities.setGlobalDeliveryOptions(true);
		submitCapabilities.setLocalDeliveryOptions(true);
		submitCapabilities.setGlobalOrderOptions(true);
		submitCapabilities.setLocalOrderOptions(true);
		
		GetStatusCapabilities statusCapabilities = contents.addNewGetStatusCapabilities();
		statusCapabilities.setOrderSearch(true);
		statusCapabilities.setOrderRetrieve(true);
		statusCapabilities.setFull(true);
		
		net.opengis.oseo.x10.OrderingServiceContentsType.DescribeResultAccessCapabilities describeResultAccessCapabilities = contents.addNewDescribeResultAccessCapabilities();
		describeResultAccessCapabilities.setSupported(true);
		
		CancelCapabilities cancelCapabilities = contents.addNewCancelCapabilities();
		cancelCapabilities.setSupported(true);
		cancelCapabilities.setAsynchronous(false);
		
		Map<String,Collection> collections = roseomh.getCollections();
		
		Iterator<String> iterator = collections.keySet().iterator();

		while (iterator.hasNext()) {
			
			String key = (String) iterator.next();
			CollectionCapability supportedCollection = contents.addNewSupportedCollection();
			supportedCollection.setCollectionId("http://www.opengis.net/def/EOP/ESA/0/" + key);
			DescribeResultAccessCapabilities DRACollection = supportedCollection.addNewDescribeResultAccessCapabilities();
			DRACollection.setSupported(false);
		}
		
		ResourceURL resourceURLCapabilities = contents.addNewResourceURL();
		OrderingServiceContentsType.ResourceURL.Resource.Enum resourceCapabilities = Enum.forString("capabilities");
		resourceURLCapabilities.setResource(resourceCapabilities);
		resourceURLCapabilities.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/");
		
		ResourceURL resourceURLOptions = contents.addNewResourceURL();
		OrderingServiceContentsType.ResourceURL.Resource.Enum resourceOptions= Enum.forString("order options");
		resourceURLOptions.setResource(resourceOptions);
		resourceURLOptions.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/options/");
		
		ResourceURL resourceURLOptionsProduct = contents.addNewResourceURL();
		resourceURLOptionsProduct.setResource(resourceOptions);
		resourceURLOptionsProduct.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/options/{product identifier}/{format}");
		
		ResourceURL resourceURLOptionsCollection = contents.addNewResourceURL();
		resourceURLOptionsCollection.setResource(resourceOptions);
		resourceURLOptionsCollection.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/options/{collection identifier}/{format}");
		
		ResourceURL resourceURLOrder = contents.addNewResourceURL();
		OrderingServiceContentsType.ResourceURL.Resource.Enum resourceOrder = Enum.forString("order");
		resourceURLOrder.setResource(resourceOrder);
		resourceURLOrder.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/");
		
		ResourceURL resourceURLOrderID = contents.addNewResourceURL();
		resourceURLOrderID.setResource(resourceOrder);
		resourceURLOrderID.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/{order identifier}");
		
		ResourceURL resourceURLOrderFilter = contents.addNewResourceURL();
		resourceURLOrderFilter.setResource(resourceOrder);
		resourceURLOrderFilter.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order?{filter expression}");
		
		ResourceURL resourceURLOrderItem = contents.addNewResourceURL();
		resourceURLOrderItem.setResource(resourceOrder);
		resourceURLOrderItem.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/{order identifier}/{order item identifier}");
		
		ResourceURL resourceURLProduct = contents.addNewResourceURL();
		OrderingServiceContentsType.ResourceURL.Resource.Enum resourceProduct= Enum.forString("ordered product");
		resourceURLProduct.setResource(resourceProduct);
		resourceURLProduct.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/{order identifier}/file");
		
		ResourceURL resourceURLProductFilter = contents.addNewResourceURL();
		resourceURLProductFilter.setResource(resourceProduct);
		resourceURLProductFilter.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/{order identifier}/file?{filter expression}");
		
		ResourceURL resourceURLProductItem = contents.addNewResourceURL();
		resourceURLProductItem.setResource(resourceProduct);
		resourceURLProductItem.setURL(serverAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/{order identifier}/{order item identifier}/file");
		
		
		this.output = Response.ok(capabilitiesDocument.toString(), "text/xml").build();

	}

}
