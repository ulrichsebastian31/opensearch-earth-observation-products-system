package com.astrium.hmas.roseo;

import javax.ws.rs.core.MediaType;


import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class OrderByIDTest {

	@Test
	public void testCancelOrder() {
		
		Client client = new Client();
		
		/*
		 * Call to the server
		 */
		WebResource webResource = client.resource("http://localhost:8080/ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/");
		WebResource webResourceTest = webResource.path("order/ad6807775ea093bf1ea77c7c6adfbcea");
	    final String responseString = webResourceTest.type(MediaType.APPLICATION_XML).delete(String.class);
	    Assert.assertEquals("Request fulfilled.", responseString);
	}

}
