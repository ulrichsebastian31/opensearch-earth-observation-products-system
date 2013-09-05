package com.astrium.hmas.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.astrium.hmas.client.DownloadProductService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@SuppressWarnings("serial")
public class DownloadProductServiceImpl extends RemoteServiceServlet implements DownloadProductService {

	@Override
	public String getProducts(String uri) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Client client = new Client();
		WebResource webResource = client.resource("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/dw");
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();

		queryParams.add("service", "DSEO");
		queryParams.add("request", "GetProduct");
		queryParams.add("version", "1.0.0");
		queryParams.add("ProductURI", uri);

		String type = webResource.queryParams(queryParams).head().getType().toString();
		System.out.println(type);
		if (type.equals("application/octet-stream")) {
			String url = webResource.queryParams(queryParams).toString();
			return url;
		} else if (type.equals("application/atom+xml")) {
			String xml = webResource.queryParams(queryParams).accept("application/atom+xml").get(String.class);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;

			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

				Element root = doc.getDocumentElement();
				root.normalize();

				String node = root.getNodeName();
				if (node.equals("metalink")) {
					
					Map<String, String> metalink = new HashMap<String, String>();
					Element files = (Element) doc.getElementsByTagName("files").item(0);
					NodeList nlist = files.getElementsByTagName("file");

					if (nlist != null && nlist.getLength() > 0) {
						for (int i = 0; i < nlist.getLength(); i++) {

							Element file = (Element) nlist.item(i);
							String fileName = file.getAttribute("name");
							Element resources = (Element) file.getElementsByTagName("resources").item(0);

							NodeList urls = resources.getElementsByTagName("url");
							String servers = "";
							if (urls != null && urls.getLength() > 0) {
								
								for (int j = 0; j < urls.getLength(); j++) {

									Element url = (Element) urls.item(j);
									String urltxt = url.getChildNodes().item(0).getNodeValue();
									if (nlist.getLength() > 1 && j < nlist.getLength() - 1) {
										servers += urltxt + ",";
									} else {
										servers += urltxt;
									}

								}
							}
							
							metalink.put(fileName, servers);
						}
					}
					
					String reponse = null;
					reponse = "Your product is on-line available, but in several part and/or on several servers : \n";
					reponse += "	URI : \n";
					Iterator<String> iterator = metalink.keySet().iterator();
					while (iterator.hasNext()) {
						String productUri = (String) iterator.next();
						reponse += "		- " + productUri;
						reponse += " is available on the following servers : \n";
						String[] servers = metalink.get(productUri).split(",");
						for(int k=0;k<servers.length;k++){
							reponse += "			- " + servers[k] + "\n";
						}
						
								
					}
					 
					return reponse;
				} else {
					String reponse = null;
					Element rep = (Element) doc.getElementsByTagName("dseo:ProductDownloadResponse").item(0);
					
					Element code = (Element) rep.getElementsByTagName("dseo:ResponseCode").item(0);
					String codetxt = code.getChildNodes().item(0).getNodeValue();
					
					Element retry = (Element) rep.getElementsByTagName("dseo:RetryAfter").item(0);
					String retrytxt = retry.getChildNodes().item(0).getNodeValue();
					
					reponse = "Your product is not on-line available right now.\n";
					reponse += "	Status : " + codetxt + "\n";
					reponse += "	Retry after : " + retrytxt;
					return reponse;
				}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else if (type.equals("text/html; charset=utf-8")){
			return "Your product is missing!";
			
		}

		return null;
	}

}
