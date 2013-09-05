package com.astrium.hmas.server;

import javax.ws.rs.core.MultivaluedMap;

import com.astrium.hmas.bean.DownloadProduct;
import com.astrium.hmas.client.OrderService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@SuppressWarnings("serial")
public class OrderServiceImpl extends RemoteServiceServlet implements OrderService {


	@Override
	public String goDownload(DownloadProduct downloadProduct) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Client client = new Client();
		WebResource webResource = client.resource("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/order");
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		
		String identifier = downloadProduct.getId();
		queryParams.add("ProductID", identifier);
		
		String uri = webResource.queryParams(queryParams).accept("text/html").get(String.class);
		
		return uri;
	}

}
