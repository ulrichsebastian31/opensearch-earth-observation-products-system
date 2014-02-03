package com.astrium.hmas.roseo.capabilities;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.xmlbeans.XmlCursor;

import net.opengis.ows.x20.CodeType;
import net.opengis.ows.x20.KeywordsType;
import net.opengis.ows.x20.LanguageStringType;
import net.opengis.ows.x20.ServiceIdentificationDocument.ServiceIdentification;
import net.opengis.roseo.x10.CapabilitiesDocument;
import net.opengis.roseo.x10.CapabilitiesDocument.Capabilities;



/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetCapabilitiesServiceIdentification.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Capabilities resource contains metadata 
 *   																	about the ROSEO server reporting. This 
 *   																	class returns an XML encoding document 
 *   																	containing just the information
 * 																		about the service identification.
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
public class GetCapabilitiesServiceIdentification extends GenericCapabilitiesService {

	public GetCapabilitiesServiceIdentification() {
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
		/*
		 * Return the capabilities file to the client
		 */
		this.output = Response.ok(capabilitiesDocument.toString(), "text/xml").build();

	}

}
