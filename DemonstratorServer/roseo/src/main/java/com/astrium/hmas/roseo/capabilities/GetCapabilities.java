package com.astrium.hmas.roseo.capabilities;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.xmlbeans.XmlCursor;

import net.opengis.oseo.x10.CollectionCapability;
import net.opengis.oseo.x10.CollectionCapability.DescribeResultAccessCapabilities;
import net.opengis.oseo.x10.EncodingType;
import net.opengis.oseo.x10.OrderingServiceContentsType.CancelCapabilities;
import net.opengis.oseo.x10.OrderingServiceContentsType.GetQuotationCapabilities;
import net.opengis.oseo.x10.OrderingServiceContentsType.GetStatusCapabilities;
import net.opengis.oseo.x10.OrderingServiceContentsType.ProductOrders;
import net.opengis.oseo.x10.OrderingServiceContentsType.ProgrammingOrders;
import net.opengis.oseo.x10.OrderingServiceContentsType.SubmitCapabilities;
import net.opengis.oseo.x10.OrderingServiceContentsType.SubscriptionOrders;
import net.opengis.ows.x20.CapabilitiesBaseType.Languages;
import net.opengis.ows.x20.CodeType;
import net.opengis.ows.x20.ContactType;
import net.opengis.ows.x20.KeywordsType;
import net.opengis.ows.x20.LanguageStringType;
import net.opengis.ows.x20.ResponsiblePartySubsetType;
import net.opengis.ows.x20.ServiceIdentificationDocument.ServiceIdentification;
import net.opengis.ows.x20.ServiceProviderDocument.ServiceProvider;
import net.opengis.ows.x20.TelephoneType;
import net.opengis.roseo.x10.CapabilitiesDocument;
import net.opengis.roseo.x10.CapabilitiesDocument.Capabilities;
import net.opengis.roseo.x10.OrderingServiceContentsType;
import net.opengis.roseo.x10.OrderingServiceContentsType.ResourceURL;
import net.opengis.roseo.x10.OrderingServiceContentsType.ResourceURL.Resource.Enum;

import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.astrium.roseodbhandler.structures.Collection;
import com.sun.jersey.api.core.HttpContext;


/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetCapabilities.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Capabilities resource contains metadata 
 *   																	about the ROSEO server reporting. This 
 *   																	class returns an XML encoding document 
 *   																	containing all this information.
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
@Path("1.0.0")
@SuppressWarnings("restriction")
public class GetCapabilities{
	
	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;
	
	private String serversAdress;
	
	@GET
	public Response getCapabilities() throws Exception {

		/*
		 * XML file creation
		 */
		
		URI baseURI = httpContext.getUriInfo().getAbsolutePath();
		serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort();
		
		if (!serversAdress.endsWith("/")) serversAdress += "/";

		System.out.println("Server base address : " + serversAdress);
		
		RoseoManagementHandler roseomh = new RoseoManagementHandler();
		
		CapabilitiesDocument capabilitiesDocument = CapabilitiesDocument.Factory.newInstance();
		Capabilities capabilities = capabilitiesDocument.addNewCapabilities();
		
		XmlCursor cursor = capabilities.newCursor();
		cursor.toNextToken();
		cursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		cursor.insertNamespace("roseo", "http://www.opengis.net/roseo/1.0");
		cursor.insertNamespace("oseo", "http://www.opengis.net/oseo/1.0");
		cursor.insertNamespace("xlink", "http://www.w3.org/1999/xlink");
		cursor.insertNamespace("ows", "http://www.opengis.net/ows/2.0");
		cursor.dispose();
		
		capabilities.setVersion("0.0.0");
		capabilities.setUpdateSequence("String");
		
		ServiceIdentification serviceIdentification = capabilities.addNewServiceIdentification();
		LanguageStringType title = serviceIdentification.addNewTitle();
		
		title.setLang("en-us");
		title.setStringValue("ROSEO Test Server");
		
		LanguageStringType abstract_var = serviceIdentification.addNewAbstract();
		abstract_var.setLang("en-us");
		abstract_var.setStringValue("Test Server of ROSEO protocol");
		
		KeywordsType keywords = serviceIdentification.addNewKeywords();
		LanguageStringType keyword = keywords.addNewKeyword();
		keyword.setLang("en-us");
		keyword.setStringValue("roseo");
		
		CodeType serviceType = serviceIdentification.addNewServiceType();
		serviceType.setCodeSpace("http://www.altova.com");
		serviceType.setStringValue("String");
		
		serviceIdentification.addServiceTypeVersion("1.0.0");
		
		serviceIdentification.addProfile("http://www.opengis.net/def/bp/roseo/1.0");
		
		serviceIdentification.setFees("String");
		
		serviceIdentification.addAccessConstraints("String");
		
		ServiceProvider serviceProvider = capabilities.addNewServiceProvider();
		serviceProvider.setProviderName("ESA EECF");
		
		ResponsiblePartySubsetType serviceContact = serviceProvider.addNewServiceContact();
		serviceContact.setIndividualName("John Smith");
		serviceContact.setPositionName("Help Desk");
		ContactType contactInfo = serviceContact.addNewContactInfo();
		TelephoneType phone = contactInfo.addNewPhone();
		phone.addVoice("+39 06 90 180 999");
		contactInfo.setHoursOfService("9:00 - 18:00");
		
		Languages languages = capabilities.addNewLanguages();
		languages.addLanguage("en-us");
		
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
		resourceURLCapabilities.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/");
		
		ResourceURL resourceURLOptions = contents.addNewResourceURL();
		OrderingServiceContentsType.ResourceURL.Resource.Enum resourceOptions= Enum.forString("order options");
		resourceURLOptions.setResource(resourceOptions);
		resourceURLOptions.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/options/");
		
		ResourceURL resourceURLOptionsProduct = contents.addNewResourceURL();
		resourceURLOptionsProduct.setResource(resourceOptions);
		resourceURLOptionsProduct.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/options/{product identifier}/{format}");
		
		ResourceURL resourceURLOptionsCollection = contents.addNewResourceURL();
		resourceURLOptionsCollection.setResource(resourceOptions);
		resourceURLOptionsCollection.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/options/{collection identifier}/{format}");
		
		ResourceURL resourceURLOrder = contents.addNewResourceURL();
		OrderingServiceContentsType.ResourceURL.Resource.Enum resourceOrder = Enum.forString("order");
		resourceURLOrder.setResource(resourceOrder);
		resourceURLOrder.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/");
		
		ResourceURL resourceURLOrderID = contents.addNewResourceURL();
		resourceURLOrderID.setResource(resourceOrder);
		resourceURLOrderID.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/{order identifier}");
		
		ResourceURL resourceURLOrderFilter = contents.addNewResourceURL();
		resourceURLOrderFilter.setResource(resourceOrder);
		resourceURLOrderFilter.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order?{filter expression}");
		
		ResourceURL resourceURLOrderItem = contents.addNewResourceURL();
		resourceURLOrderItem.setResource(resourceOrder);
		resourceURLOrderItem.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/{order identifier}/{order item identifier}");
		
		ResourceURL resourceURLProduct = contents.addNewResourceURL();
		OrderingServiceContentsType.ResourceURL.Resource.Enum resourceProduct= Enum.forString("ordered product");
		resourceURLProduct.setResource(resourceProduct);
		resourceURLProduct.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/{order identifier}/file");
		
		ResourceURL resourceURLProductFilter = contents.addNewResourceURL();
		resourceURLProductFilter.setResource(resourceProduct);
		resourceURLProductFilter.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/{order identifier}/file?{filter expression}");
		
		ResourceURL resourceURLProductItem = contents.addNewResourceURL();
		resourceURLProductItem.setResource(resourceProduct);
		resourceURLProductItem.setURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/{order identifier}/{order item identifier}/file");
		/*
		 * Return the capabilities file to the client
		 */
		return Response.ok(capabilitiesDocument.toString(), "text/xml").build();

	}

}
