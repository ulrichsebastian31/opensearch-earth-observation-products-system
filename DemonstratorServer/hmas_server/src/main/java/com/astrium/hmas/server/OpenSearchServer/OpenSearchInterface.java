package com.astrium.hmas.server.OpenSearchServer;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OpenSearchInterface.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class is a server which handles 
 *   																	the construction of the XML description
 *   																	file to do a Catalogue Search request
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

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * @author re-cetienne
 */
@Path("cat/os")
public class OpenSearchInterface {

	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;

	@GET
	public Response getMethodParser() {
		/*
		 * XML file creation
		 */
		Document document = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {

			dBuilder = dbFactory.newDocumentBuilder();
			/*
			 * Retrieve the description file on the computer
			 */
			String dd = System.getProperty("user.home")+File.separator + "hmas"+File.separator+"description.xml";
			System.out.println(dd);
			document = (Document) dBuilder.parse(new File(dd));
			OutputFormat format = new OutputFormat(document);
			/*
			 * The XML can be collect in String format
			 */
			StringWriter writer = new StringWriter();
			XMLSerializer serial = new XMLSerializer(writer, format);

			serial.serialize(document);
			/*
			 * Return the description file to the client
			 */
			return Response.ok(writer.toString(), "application/atom+xml").build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@POST
	@Consumes(MediaType.APPLICATION_ATOM_XML)
	// @GET
	public Response postRequestParser(FormDataMultiPart formDataMultiPart /*String xmlrequest*/) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
}