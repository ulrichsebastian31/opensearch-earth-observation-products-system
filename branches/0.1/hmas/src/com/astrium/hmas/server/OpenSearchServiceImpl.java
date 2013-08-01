package com.astrium.hmas.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.astrium.hmas.client.OpenSearchService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;

@SuppressWarnings("serial")
public class OpenSearchServiceImpl extends RemoteServiceServlet implements
OpenSearchService {

	public Map<String, String> getDescriptionFile(String url) throws IllegalArgumentException {
		   URLConnectionClientHandler ch  = new URLConnectionClientHandler(new ConnectionFactory());
		   
		    Client client = new Client(ch);
		    //TODO mettre l'url direct -> ne pas passer par la servlet distante
	        WebResource webResource = client.resource(url);

	        String s = webResource.accept("application/atom+xml").get(String.class);

	    	Map<String, String> parameters = new HashMap<String, String>();
	    	
	     try {
	    	 
	        //File fXmlFile = new File();
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder;
			
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(s.getBytes("utf-8"))));
	     
	    	//optional, but recommended
	    	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	    	doc.getDocumentElement().normalize();
	    	
	    	NodeList nlist = doc.getElementsByTagName("Url");
	    	
	    	Node nNode = nlist.item(0);
	    	
	    	Element eElement = (Element) nNode;
	    	String template = eElement.getAttribute("template");
	    	
	    	
	    	Pattern pattern = Pattern.compile("(.*?)*\\&(.*?)=\\{(.*?)\\?\\}.*?"); 
	    	Matcher m = pattern.matcher(template); 
	    	
	    	while(m.find()){ 
	    		parameters.put(m.group(3), m.group(2));
	    	} 
	   
	    	
	    	/*if (nlist != null && nlist.getLength() > 0){
	    		for(int i = 0 ; i < nlist.getLength();i++) {
	    			Element param = (Element)nlist.item(i);
	    			String value = param.getAttribute("value");
	    			String[] names = value.split(":");
	    			String name = names[1];
	    			if (name.endsWith("}")){
	    				name = name.substring(0,name.length()-1);
	    			}
	    			parameters.add(name);
	    			rep += name + "\n";
	    			
	    		}
	    	} else {
	    		rep = "null";
	    	}*/

	    	
	   } catch (ParserConfigurationException e) {// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	     return parameters;
	   }

}
