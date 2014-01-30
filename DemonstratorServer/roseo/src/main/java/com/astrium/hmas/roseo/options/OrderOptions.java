package com.astrium.hmas.roseo.options;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionType;
import net.opengis.ows.x20.ExceptionReportDocument.ExceptionReport;
import net.opengis.roseo.x10.OrderOptionsDocument;
import net.opengis.roseo.x10.OrderOptionsType;

import com.sun.jersey.api.core.HttpContext;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetOptionsXMLFormat.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Sends back the XML file which describes
 *   																	all the available options for a 
 *   																	product in a XML canonical format
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

@Path("1.0.0/options")
public class OrderOptions {
	
	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;

	@GET
	public Response getOptions() throws Exception {
		
		Response response = null;
		
		OrderOptionsDocument orderOptionsDocument = OrderOptionsDocument.Factory.newInstance();
		OrderOptionsType xml_orderOptions = orderOptionsDocument.addNewOrderOptions();
		
		XmlCursor cur = xml_orderOptions.newCursor();
		// We could use the convenient xobj.selectPath() or cur.selectPath()
		// to position the cursor on the <person> element, but let's use the
		// cursor's toChild() instead.
		cur.toChild("OrderOptions");
		cur.toEndToken();

		cur.insertElementWithText("Service", "OrderService Options resource is accessible from this root path. You have now to specify a product or a collection ID as well as a format."
				+ "Here is an example:\n"
				+ "http://<hostname>:<port>/ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/options/{identifier}/xml");

		
		if (cur != null)
			cur.dispose();
		
		String getOptions = orderOptionsDocument.toString();
		
		response = Response.ok(getOptions, "text/xml").build();
		
		if(response == null){
			
			ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
			ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

			ExceptionType exception = exceptionReport.addNewException();
			exception.addExceptionText("ServerError");
			exception.setExceptionCode("NoApplicableCode");

			String exceptionString = exceptionReportDocument.toString();

			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(exceptionString).build();
		}
		
		return response;
	}

}
