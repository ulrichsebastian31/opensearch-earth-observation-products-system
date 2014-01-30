package com.astrium.hmas.roseo.capabilities;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.xmlbeans.XmlCursor;

import net.opengis.ows.x20.ContactType;
import net.opengis.ows.x20.ResponsiblePartySubsetType;
import net.opengis.ows.x20.TelephoneType;
import net.opengis.ows.x20.ServiceProviderDocument.ServiceProvider;
import net.opengis.roseo.x10.CapabilitiesDocument;
import net.opengis.roseo.x10.CapabilitiesDocument.Capabilities;


/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetCapabilitiesServiceProvider.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Capabilities resource contains metadata 
 *   																	about the ROSEO server reporting. This 
 *   																	class returns an XML encoding document 
 *   																	containing just the information
 * 																		about the service provider.
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
public class GetCapabilitiesServiceProvider extends GenericCapabilitiesService {
	
	public GetCapabilitiesServiceProvider(){
		super();
	}

	@Override
	public void getCapabilities(Map<String, String> inputs, String serverAdress) throws Exception {

		/*
		 * XML file creation
		 */
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
		
		ServiceProvider serviceProvider = capabilities.addNewServiceProvider();
		serviceProvider.setProviderName("ESA EECF");
		
		ResponsiblePartySubsetType serviceContact = serviceProvider.addNewServiceContact();
		serviceContact.setIndividualName("John Smith");
		serviceContact.setPositionName("Help Desk");
		ContactType contactInfo = serviceContact.addNewContactInfo();
		TelephoneType phone = contactInfo.addNewPhone();
		phone.addVoice("+39 06 90 180 999");
		contactInfo.setHoursOfService("9:00 - 18:00");

		/*
		 * Return the capabilities file to the client
		 */
		this.output = Response.ok(capabilitiesDocument.toString(), "text/xml").build();

	}

}
