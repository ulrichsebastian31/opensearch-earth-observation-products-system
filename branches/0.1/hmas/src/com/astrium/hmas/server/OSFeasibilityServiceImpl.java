package com.astrium.hmas.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.astrium.hmas.bean.Parameter;
import com.astrium.hmas.client.OSFeasibilityService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

@SuppressWarnings("serial")
public class OSFeasibilityServiceImpl extends RemoteServiceServlet implements OSFeasibilityService {

	@Override
	public Map<String, Parameter> getParameters(String url) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Client client = new Client();
		WebResource webResource = client.resource(url);

		String s = webResource.accept("text/xml").get(String.class);

		Map<String, Parameter> parameters = new HashMap<String, Parameter>();
		String rep = "";

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;

			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(s.getBytes("utf-8"))));

			doc.getDocumentElement().normalize();

			NodeList nlist = doc.getElementsByTagName("Url");

			Node nNode = nlist.item(1);

			Element eElement = (Element) nNode;
			//String template = eElement.getAttribute("template");

			NodeList params = eElement.getElementsByTagName("param:Parameter");

			if (params != null && params.getLength() > 0) {
				System.out.println("Hello");
				for (int i = 0; i < params.getLength(); i++) {
					Parameter parameter = new Parameter();
					Element param = (Element) params.item(i);
					String name = param.getAttribute("name");
					parameter.setName(name);
					System.out.println(name);
					String key = param.getAttribute("value");
					System.out.println(key + "?");
					parameter.setKey(key);
					rep += key + " : " + name + "\n";
					System.out.println(rep);
					if(param.getAttribute("minInclusive") != null && !param.getAttribute("minInclusive").equals("")){
						String min = param.getAttribute("minInclusive");
						parameter.setMin(min);
					}

					if(param.getAttribute("maxInclusive") != null && !param.getAttribute("maxInclusive").equals("")){
						String max = param.getAttribute("maxInclusive");
						parameter.setMax(max);
					}

					List<String> options_list = new ArrayList<String>();

					NodeList options = param.getElementsByTagName("param:option");
					if (options != null && options.getLength() > 0) {
						for (int j = 0; j < options.getLength(); j++) {

							Element option = (Element) options.item(j);
							String opt = option.getAttribute("value");
							options_list.add(opt);
						}
						parameter.setOptions(options_list);
					}

					parameters.put(key, parameter);
				}
				System.out.println(rep);

			}

			return parameters;

		} catch (ParserConfigurationException e) {// TODO Auto-generated catch
													// block
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
