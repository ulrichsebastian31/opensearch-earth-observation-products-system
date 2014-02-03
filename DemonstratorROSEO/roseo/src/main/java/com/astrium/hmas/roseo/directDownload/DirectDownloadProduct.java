package com.astrium.hmas.roseo.directDownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionType;
import net.opengis.ows.x20.ExceptionReportDocument.ExceptionReport;

import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.astrium.roseodbhandler.structures.EOProduct;
import com.sun.jersey.api.core.HttpContext;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DirectDownloadProduct.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class allows a direct download
 *   																	of a EO product
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

@Path("1.0.0/product/{eoProductIdentifier}")
public class DirectDownloadProduct {

	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;

	private String serversAdress;

	@GET
	public Response downloadProduct() throws Exception {

		Response response = null;

		String eoProductIdentifier = ui.getPathParameters().getFirst("eoProductIdentifier");
		System.out.println("eoProductIdentifier = " + eoProductIdentifier);

		RoseoManagementHandler roseomh = new RoseoManagementHandler();
		EOProduct eoProduct = roseomh.getProduct(eoProductIdentifier);

		if(eoProduct==null){
			
			ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
			ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

			ExceptionType exception = exceptionReport.addNewException();
			exception.addExceptionText("Invalid value for parameter");
			exception.setExceptionCode("InvalidParameterValue");
			exception.setLocator(eoProductIdentifier);

			String exceptionString = exceptionReportDocument.toString();

			response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();

		}else{

			File product = new File(System.getProperty("user.home") + File.separator + "hmas" + File.separator + eoProductIdentifier + ".zip");

			FileInputStream prodStream = null;
			prodStream = new FileInputStream(product);

			byte[] fileContent = new byte[(int) product.length()];
			prodStream.read(fileContent);

			response = Response.ok(fileContent, "application/octet-stream").header("Content-Disposition", "attachment; filename=" + eoProductIdentifier).build();
		}
		
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
