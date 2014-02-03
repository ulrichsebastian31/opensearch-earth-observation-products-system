package com.astrium.hmas.roseo.errors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionType;
import net.opengis.ows.x20.ExceptionReportDocument.ExceptionReport;

import com.sun.jersey.api.core.HttpContext;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetOptionsMissingParamterErrorReport.java
 *   File Type                                          :               Source Code
 *   Description                                        :               returns the error report XML file
 *   																	when the user try to access the 	
 *   																	GET /options service without specifying	
 *   																	a product or collection ID
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
@Path("1.0.0/options/{format}")
public class GetOptionsMissingParamterErrorReport {
	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;
	
	@GET
	public Response getErrorReport() throws Exception {
		
		Response response = null;
		
		ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
		ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

		ExceptionType exception = exceptionReport.addNewException();
		exception.addExceptionText("Missing value for parameter");
		exception.setExceptionCode("MissingParameterValue");
		exception.setLocator("Product or Collection ID missing");

		String exceptionString = exceptionReportDocument.toString();

		response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();
		
		return response;
		
	}

}
