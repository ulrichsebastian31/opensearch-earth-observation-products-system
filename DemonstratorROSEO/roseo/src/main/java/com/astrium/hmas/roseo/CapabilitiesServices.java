package com.astrium.hmas.roseo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionType;
import net.opengis.ows.x20.ExceptionReportDocument.ExceptionReport;


import com.astrium.hmas.roseo.capabilities.GenericCapabilitiesService;
import com.sun.jersey.api.core.HttpContext;

@Path("1.0.0/{serviceName}")
public class CapabilitiesServices {

	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;

	@Context
	volatile ServletContext servletContext;

	private String serversAdress;

	@GET
	public Response getCapabilities() throws Exception {

		Response response = null;

		String serviceName = ui.getPathParameters().getFirst("serviceName");
		System.out.println("servicename = " +serviceName);
		
		
		URI baseURI = httpContext.getUriInfo().getAbsolutePath();
		serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort();
		
		if (!serversAdress.endsWith("/")) serversAdress += "/";

		System.out.println("Server base address : " + serversAdress);

		Map<String, Class<? extends GenericCapabilitiesService>> services = CapabilitiesServicesLoader.getProcesses();
		if (services.containsKey(serviceName)) {
			Map<String, String> theInputs = new HashMap();
			for (String string : ui.getQueryParameters().keySet()) {
				theInputs.put(string, ui.getQueryParameters().getFirst(string));
			}
			GenericCapabilitiesService process = services.get(serviceName).newInstance();

			process.getCapabilities(theInputs,serversAdress);
			response = process.getOutput();

		} else {
			
			ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
			ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

			ExceptionType exception = exceptionReport.addNewException();
			exception.addExceptionText("Invalid value for Parameter");
			exception.setExceptionCode("InvalidParameterValue");
			exception.setLocator(serviceName);

			String exceptionString = exceptionReportDocument.toString();
			response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();

		}

		if (response == null){
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
