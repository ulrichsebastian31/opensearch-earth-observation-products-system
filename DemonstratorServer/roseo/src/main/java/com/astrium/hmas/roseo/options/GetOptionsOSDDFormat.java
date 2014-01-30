package com.astrium.hmas.roseo.options;

import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.opengis.oseo.x10.ParameterDescriptorType;
import net.opengis.oseo.x10.ProtocolType;
import net.opengis.oseo.x10.CommonOrderOptionsType.ProductDeliveryOptions;
import net.opengis.oseo.x10.CommonOrderOptionsType.ProductDeliveryOptions.OnlineDataAccess;
import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.ows.x20.ExceptionReportDocument.ExceptionReport;
import net.opengis.ows.x20.ExceptionType;
import net.opengis.roseo.x10.OrderOptionsDocument;
import net.opengis.roseo.x10.OrderOptionsGroupType;
import net.opengis.roseo.x10.OrderOptionsType;
import net.opengis.swe.x20.AllowedTokensPropertyType;
import net.opengis.swe.x20.AllowedTokensType;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.DataRecordType;
import net.opengis.swe.x20.DataRecordType.Field;

import org.apache.xmlbeans.XmlString;
import org.json.JSONArray;
import org.json.JSONObject;

import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.astrium.roseodbhandler.structures.Collection;
import com.astrium.roseodbhandler.structures.EOProduct;
import com.sun.jersey.api.core.HttpContext;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetOptionsOSDDFormat.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Sends back the XML file which describes
 *   																	all the available options for a 
 *   																	product in a Open Search Description
 *   																	Document format
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

@Path("1.0.0/options/{identifier}/osdd")
public class GetOptionsOSDDFormat {

	/**
	 * Map containing the URL parameters
	 */
	@Context
	volatile UriInfo ui;

	@Context
	volatile HttpContext httpContext;
	
	private String serversAdress;

	public Document getDocument() {
		return document;
	}

	private Document document;
	private Element description; // root element

	@SuppressWarnings("restriction")
	@GET
	public Response getOptionsOSDDFormat() throws Exception {

		Response response = null;

		String identifier = ui.getPathParameters().getFirst("identifier");
		System.out.println("identifier = " + identifier);

		URI baseURI = httpContext.getUriInfo().getAbsolutePath();
		serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort();
		
		if (!serversAdress.endsWith("/")) serversAdress += "/";

		System.out.println("Server base address : " + serversAdress);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;

		document = null;

		// Creating document
		dBuilder = dbFactory.newDocumentBuilder();
		document = dBuilder.newDocument();
		document.setXmlVersion("1.0");
		description = document.createElement("OpenSearchDescription");

		// Adds namespaces
		addNamespaces();
		// Adds name, description, ...
		appendFixedElements();
		// Adds URL from this page
		appendURL(identifier);

		OrderOptionsDocument orderOptionsDocument = OrderOptionsDocument.Factory.newInstance();
		OrderOptionsType xml_orderOptions = orderOptionsDocument.addNewOrderOptions();
		Node orderOptionsNode = xml_orderOptions.getDomNode();

		OrderOptionsGroupType optionsGroup = xml_orderOptions.addNewOrderOptionGroup();
		

		RoseoManagementHandler roseomh = new RoseoManagementHandler();
		EOProduct eoProduct = roseomh.getProduct(identifier);

		if (eoProduct != null) {

			String options = "";

			if(eoProduct.getOptions().equals("null")){
				String collectionID = eoProduct.getCollectionID();
				Collection collection = roseomh.getCollection(collectionID);
				options += collection.getOptions();
			}else{
				options += eoProduct.getOptions();
			}

			JSONObject optjson = new JSONObject(options);
			JSONObject option = (JSONObject) optjson.get("oseo:option");
			String productOrderOptionsId = (String) option.get("productOrderOptionsId");

			optionsGroup.setProductOrderOptionsId(productOrderOptionsId);
			optionsGroup.setIdentifier(eoProduct.getProductID());
			optionsGroup.setDescription("OrderService options to produce " + eoProduct.getProductID() + " product");

			net.opengis.oseo.x10.EnumOrderType.Enum productOrder = net.opengis.oseo.x10.EnumOrderType.Enum.forString("PRODUCT_ORDER");
			optionsGroup.setOrderType(productOrder);

			JSONArray optListJson = option.getJSONArray("options");

			Map<String, String> templateParameters = new HashMap<String, String>();

			if (optListJson != null && optListJson.length() > 0) {
				for (int i = 0; i < optListJson.length(); i++) {

					JSONObject optJSON = (JSONObject) optListJson.get(i);

					ParameterDescriptorType opt = optionsGroup.addNewOption();

					DataRecordType dataRecord = DataRecordType.Factory.newInstance();
					Field field = dataRecord.addNewField();

					JSONObject dataRecordJSON = optJSON.getJSONObject("swe:DataRecord");
					JSONObject fieldJSON = dataRecordJSON.getJSONObject("swe:field");

					templateParameters.put(fieldJSON.getString("templateParameter"), fieldJSON.getString("-name"));

					field.setName(fieldJSON.getString("-name"));
					CategoryType categoryType = CategoryType.Factory.newInstance();

					JSONObject categoryJSON = fieldJSON.getJSONObject("swe:Category");

					categoryType.setUpdatable(categoryJSON.getBoolean("-updatable"));
					categoryType.setOptional(categoryJSON.getBoolean("-optional"));
					categoryType.setDefinition(categoryJSON.getString("-definition"));
					categoryType.setIdentifier(categoryJSON.getString("swe:identifier"));

					AllowedTokensPropertyType constraint = categoryType.addNewConstraint();

					AllowedTokensType allowedTokens = constraint.addNewAllowedTokens();

					JSONObject constraintJSON = categoryJSON.getJSONObject("swe:constraint");
					JSONObject allowedTokensJSON = constraintJSON.getJSONObject("swe:AllowedTokens");
					JSONArray valuesJSON = allowedTokensJSON.getJSONArray("swe:value");

					if (valuesJSON != null && valuesJSON.length() > 0) {
						for (int j = 0; j < valuesJSON.length(); j++) {

							XmlString value = allowedTokens.addNewValue();
							value.setStringValue((String) valuesJSON.get(j));

						}

					}
					field.setAbstractDataComponent(categoryType);
					opt.setAbstractDataComponent(dataRecord);

					opt.setGrouping(optJSON.getString("oseo:grouping"));
				}

			}

			ProductDeliveryOptions productDeliveryOptions = optionsGroup.addNewProductDeliveryOptions();
			OnlineDataAccess onlineDataAccess = productDeliveryOptions.addNewOnlineDataAccess();
			ProtocolType protocol = onlineDataAccess.addNewProtocol();

			protocol.setStringValue("https");

			optionsGroup.setDownloadURL(getTemplateParameters(eoProduct.getProductID()));

			description.appendChild(document.importNode(orderOptionsNode, true));
			document.appendChild(description);
			OutputFormat format = new OutputFormat(document);
			/*
			 * The XML can be collect in String format
			 */
			StringWriter writer = new StringWriter();
			XMLSerializer serial = new XMLSerializer(writer, format);

			serial.serialize(document);

			String getOptionsOSDDFormat = writer.toString();

			response = Response.ok(getOptionsOSDDFormat, "text/xml").build();
		} else {

			Collection collection = roseomh.getCollection(identifier);

			if (collection != null) {

				String options = collection.getOptions();

				JSONObject optjson = new JSONObject(options);
				JSONObject option = (JSONObject) optjson.get("oseo:option");
				String productOrderOptionsId = (String) option.get("productOrderOptionsId");

				System.out.println(productOrderOptionsId);

				optionsGroup.setProductOrderOptionsId(productOrderOptionsId);
				optionsGroup.setDescription("OrderService options to produce " + collection.getCollectionID() + " collection");

				net.opengis.oseo.x10.EnumOrderType.Enum productOrder = net.opengis.oseo.x10.EnumOrderType.Enum.forString("PRODUCT_ORDER");
				optionsGroup.setOrderType(productOrder);

				JSONArray optListJson = option.getJSONArray("options");

				if (optListJson != null && optListJson.length() > 0) {
					for (int i = 0; i < optListJson.length(); i++) {

						JSONObject optJSON = (JSONObject) optListJson.get(i);

						ParameterDescriptorType opt = optionsGroup.addNewOption();

						DataRecordType dataRecord = DataRecordType.Factory.newInstance();
						Field field = dataRecord.addNewField();

						JSONObject dataRecordJSON = optJSON.getJSONObject("swe:DataRecord");
						JSONObject fieldJSON = dataRecordJSON.getJSONObject("swe:field");

						field.setName(fieldJSON.getString("-name"));
						CategoryType categoryType = CategoryType.Factory.newInstance();

						JSONObject categoryJSON = fieldJSON.getJSONObject("swe:Category");

						categoryType.setUpdatable(categoryJSON.getBoolean("-updatable"));
						categoryType.setOptional(categoryJSON.getBoolean("-optional"));
						categoryType.setDefinition(categoryJSON.getString("-definition"));
						categoryType.setIdentifier(categoryJSON.getString("swe:identifier"));

						AllowedTokensPropertyType constraint = categoryType.addNewConstraint();

						AllowedTokensType allowedTokens = constraint.addNewAllowedTokens();

						JSONObject constraintJSON = categoryJSON.getJSONObject("swe:constraint");
						JSONObject allowedTokensJSON = constraintJSON.getJSONObject("swe:AllowedTokens");
						JSONArray valuesJSON = allowedTokensJSON.getJSONArray("swe:value");

						if (valuesJSON != null && valuesJSON.length() > 0) {
							for (int j = 0; j < valuesJSON.length(); j++) {

								XmlString value = allowedTokens.addNewValue();
								value.setStringValue((String) valuesJSON.get(j));

							}

						}
						field.setAbstractDataComponent(categoryType);
						opt.setAbstractDataComponent(dataRecord);

						opt.setGrouping(optJSON.getString("oseo:grouping"));
					}

				}

				ProductDeliveryOptions productDeliveryOptions = optionsGroup.addNewProductDeliveryOptions();
				OnlineDataAccess onlineDataAccess = productDeliveryOptions.addNewOnlineDataAccess();
				ProtocolType protocol = onlineDataAccess.addNewProtocol();

				protocol.setStringValue("https");

				optionsGroup.setDownloadURL(serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/options/" + collection.getCollectionID() + "/osdd");

				description.appendChild(document.importNode(orderOptionsNode, true));
				document.appendChild(description);
				OutputFormat format = new OutputFormat(document);
				/*
				 * The XML can be collect in String format
				 */
				StringWriter writer = new StringWriter();
				XMLSerializer serial = new XMLSerializer(writer, format);

				serial.serialize(document);

				String getOptionsOSDDFormat = writer.toString();

				response = Response.ok(getOptionsOSDDFormat, "text/xml").build();
				
			} else{

				ExceptionReportDocument exceptionReportDocument = ExceptionReportDocument.Factory.newInstance();
				ExceptionReport exceptionReport = exceptionReportDocument.addNewExceptionReport();

				ExceptionType exception = exceptionReport.addNewException();
				exception.addExceptionText("Invalid value for parameter");
				exception.setExceptionCode("InvalidParameterValue");
				exception.setLocator(identifier);

				String exceptionString = exceptionReportDocument.toString();

				response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(exceptionString).build();

			}
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

	private void addNamespaces() {

		description.setAttribute("xmlns:mstns", "http://a9.com/-/spec/opensearch/1.1/");
		description.setAttribute("xmlns:oseo", "http://www.opengis.net/oseo/1.0");
		description.setAttribute("xmlns:roseo", "http://www.opengis.net/roseo/1.0");
		description.setAttribute("xmlns:swe", "http://www.opengis.net/swe/2.0");
		description.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
		description.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	}

	private void appendFixedElements() {

		// Adding description of the service
		Element sn = document.createElement("ShortName");
		sn.setTextContent("OrderService Options");
		Element d = document.createElement("Description");
		d.setTextContent("OSDD for OrderService Options");
		Element c = document.createElement("SyndicationRight");
		c.setTextContent("open");
		Element ac = document.createElement("AdultContent");
		ac.setTextContent("false");
		Element l = document.createElement("Language");
		l.setTextContent("*");
		Element ie = document.createElement("InputEncoding");
		ie.setTextContent("UTF-8");
		Element oe = document.createElement("OutputEncoding");
		oe.setTextContent("UTF-8");

		description.appendChild(sn);
		description.appendChild(d);
		description.appendChild(c);
		description.appendChild(ac);
		description.appendChild(l);
		description.appendChild(ie);
		description.appendChild(oe);
	}

	private void appendURL(String id) throws Exception {

		Element d = document.createElement("Url");

		d.setAttribute("type", "application/atom+xml");

		d.setAttribute("template", getTemplateParameters(id));

		description.appendChild(d);
	}

	private String getTemplateParameters(String id) throws Exception {

		RoseoManagementHandler roseomh = new RoseoManagementHandler();
		EOProduct eoProduct = roseomh.getProduct(id);

		Map<String, String> templateParameters = new HashMap<String, String>();

		if (eoProduct != null) {

			String options = "";

			if(eoProduct.getOptions().equals("null")){
				String collectionID = eoProduct.getCollectionID();
				Collection collection = roseomh.getCollection(collectionID);
				options += collection.getOptions();
			}else{
				options += eoProduct.getOptions();
			}
			
			OptionsJSONParser(options, templateParameters);

			String osdd = "";

			Iterator<String> iterator = templateParameters.keySet().iterator();

			while (iterator.hasNext()) {

				String key = (String) iterator.next();
				osdd += key + "={roseo:" + templateParameters.get(key) + "}&";

			}

			return serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/product/" + id + "?" + osdd;

		} else {

			Collection collection = roseomh.getCollection(id);
			if(collection == null){
				return "";
			}else{
				String options = collection.getOptions();
				OptionsJSONParser(options, templateParameters);

				String osdd = "";

				Iterator<String> iterator = templateParameters.keySet().iterator();

				while (iterator.hasNext()) {

					String key = (String) iterator.next();
					osdd += key + "&{roseo:" + templateParameters.get(key) + "}";

				}

				return serversAdress + "ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/product/" + id + "?q={searchTerm}&count={count}&startIndex={startIndex?}&"
				+ "startPage={startPage?}&language={language?}&inputEncoding={inputEncoding?}&outputEncoding={outputEncoding?}&"
				+ "bbox={geo:box?}&geom={geo:geometry?}&id={geo:uid?}&lat={geo:lat?}&lon={geo:lon?}&radius={geo:radius?}&"
				+ "rel={geo:relation?}&loc={geo:name?}&startdate={time:start?}&stopdate={time:end?}" + osdd;
			}
		}

	}

	private void OptionsJSONParser(String json, Map<String, String> templateParameters) throws Exception {

		JSONObject optjson = new JSONObject(json);
		JSONObject option = (JSONObject) optjson.get("oseo:option");

		JSONArray optListJson = option.getJSONArray("options");

		if (optListJson != null && optListJson.length() > 0) {
			for (int i = 0; i < optListJson.length(); i++) {

				JSONObject optJSON = (JSONObject) optListJson.get(i);
				JSONObject dataRecordJSON = optJSON.getJSONObject("swe:DataRecord");
				JSONObject fieldJSON = dataRecordJSON.getJSONObject("swe:field");

				templateParameters.put(fieldJSON.getString("templateParameter"), fieldJSON.getString("-name"));
			}
		}

	}

}
