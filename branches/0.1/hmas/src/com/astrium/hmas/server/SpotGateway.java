package com.astrium.hmas.server;

import java.awt.Window;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.astrium.hmas.bean.CatalogueResult;
import com.astrium.hmas.bean.Point;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import org.gwtopenmaps.openlayers.client.LonLat;

@SuppressWarnings("serial")
public class SpotGateway extends HttpServlet {

	   public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{


	    	response.setContentType("application/atom+xml");
	        PrintWriter out = response.getWriter();
	        
	        Map<String, CatalogueResult> reponse = parseXML();
	        
	        out.println(reponse);

	        
	    }

	   public static Map<String, CatalogueResult> parseXML() {
		   //URLConnectionClientHandler ch  = new URLConnectionClientHandler(new ConnectionFactory());

		    Client client = new Client();
	        WebResource webResource = client.resource("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/os");

	        String s = webResource.accept("application/atom+xml").get(String.class);

	    	//String rep = "";
	    	Map<String, CatalogueResult> results = new HashMap<String, CatalogueResult>();
	     try {

	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder;
			
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(s.getBytes("utf-8"))));
	     
	    	//optional, but recommended
	    	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	    	doc.getDocumentElement().normalize();
	    	
	    	NodeList nlist = doc.getElementsByTagName("entry");
	    	
	    	if (nlist != null && nlist.getLength() > 0){
	    		for(int i = 0 ; i < nlist.getLength();i++) {
	    			Element param = (Element)nlist.item(i);
	    			
	    			Element id = (Element)param.getElementsByTagName("id").item(0);
	    			String idtxt = id.getChildNodes().item(0).getNodeValue();
	    			
	    			Element title = (Element)param.getElementsByTagName("title").item(0);
	    			String titletxt = title.getChildNodes().item(0).getNodeValue();
	    			
	    			Element earthObs = (Element)param.getElementsByTagName("eop:EarthObservation").item(0);
	    			
	    			Element time = (Element)earthObs.getElementsByTagName("om:phenomenonTime").item(0);
	    			
	    			Element timePeriod = (Element)time.getElementsByTagName("gml:TimePeriod").item(0);
	    			
	    			Element start = (Element)timePeriod.getElementsByTagName("gml:beginPosition").item(0);
	    			String starttxt = start.getChildNodes().item(0).getNodeValue();
	    			
	    			Element end = (Element)timePeriod.getElementsByTagName("gml:endPosition").item(0);
	    			String endtxt = end.getChildNodes().item(0).getNodeValue();
	    			
	    			Element resultTime = (Element)earthObs.getElementsByTagName("om:resultTime").item(0);
	    			
	    			Element instantTime = (Element)resultTime.getElementsByTagName("gml:TimeInstant").item(0);
	    			
	    			Element positionTime = (Element)instantTime.getElementsByTagName("gml:timePosition").item(0);
	    			String positionTimetxt = positionTime.getChildNodes().item(0).getNodeValue();
	    			
	    			Element procedure = (Element)earthObs.getElementsByTagName("om:procedure").item(0);
	    			
	    			Element eoEquipment = (Element)procedure.getElementsByTagName("eop:EarthObservationEquipment").item(0);
	    			
	    			Element platform = (Element)eoEquipment.getElementsByTagName("eop:platform").item(0);
	    			
	    			Element platformElmt = (Element)platform.getElementsByTagName("eop:Platform").item(0);
	    			
	    			Element platformName = (Element)platformElmt.getElementsByTagName("eop:shortName").item(0);
	    			String platformNametxt = platformName.getChildNodes().item(0).getNodeValue();
	    			
	    			Element instrument = (Element)eoEquipment.getElementsByTagName("eop:instrument").item(0);
	    			
	    			Element instrumentElmt = (Element)instrument.getElementsByTagName("eop:Instrument").item(0);
	    			
	    			Element instrumentName = (Element)instrumentElmt.getElementsByTagName("eop:shortName").item(0);
	    			String instrumentNametxt = instrumentName.getChildNodes().item(0).getNodeValue();
	    			
	    			Element sensor = (Element)eoEquipment.getElementsByTagName("eop:sensor").item(0);
	    			
	    			Element sensorElmt = (Element)sensor.getElementsByTagName("eop:Sensor").item(0);
	    			
	    			Element sensorType = (Element)sensorElmt.getElementsByTagName("eop:sensorType").item(0);
	    			String sensorTypetxt = sensorType.getChildNodes().item(0).getNodeValue();
	    			
	    			Element featureOfInterest = (Element)earthObs.getElementsByTagName("om:featureOfInterest").item(0);
	    			
	    			Element footprint = (Element)featureOfInterest.getElementsByTagName("eop:Footprint").item(0);
	    			
	    			Element multiExtentof = (Element)footprint.getElementsByTagName("eop:multiExtentOf").item(0);
	    			
	    			Element multiSurface = (Element)multiExtentof.getElementsByTagName("gml:MultiSurface").item(0);
	    			
	    			Element polygon = (Element)multiSurface.getElementsByTagName("gml:Polygon").item(0);
	    			
	    			Element exterior = (Element)polygon.getElementsByTagName("gml:exterior").item(0);
	    			
	    			Element linearRing = (Element)exterior.getElementsByTagName("gml:LinearRing").item(0);
	    			
	    			Element position = (Element)linearRing.getElementsByTagName("gml:posList").item(0);
	    			//TODO spliter le string pour en faire des coordonnées
	    			String positiontxt = position.getChildNodes().item(0).getNodeValue();
	    			String[] coords = positiontxt.split(" ");
	    			Point upperRight = new Point(Double.parseDouble(coords[1]), Double.parseDouble(coords[0]));
	    			Point upperLeft = new Point(Double.parseDouble(coords[3]), Double.parseDouble(coords[2]));
	    			Point lowerRight = new Point(Double.parseDouble(coords[5]), Double.parseDouble(coords[4]));
	    			Point lowerLeft = new Point(Double.parseDouble(coords[7]), Double.parseDouble(coords[6]));
	    			
	    			Element metadataProperty = (Element)earthObs.getElementsByTagName("eop:metaDataProperty").item(0);
	    			
	    			Element eoMetadata = (Element)metadataProperty.getElementsByTagName("eop:EarthObservationMetaData").item(0);
	    			
	    			Element identifier = (Element)eoMetadata.getElementsByTagName("eop:identifier").item(0);
	    			String identifiertxt = identifier.getChildNodes().item(0).getNodeValue();
	    			
	    			Element acquisitionType = (Element)eoMetadata.getElementsByTagName("eop:acquisitionType").item(0);
	    			String acquisitionTypetxt = acquisitionType.getChildNodes().item(0).getNodeValue();
	    			
	    			Element productType = (Element)eoMetadata.getElementsByTagName("eop:productType").item(0);
	    			String productTypetxt = productType.getChildNodes().item(0).getNodeValue();
	    			
	    			Element status = (Element)eoMetadata.getElementsByTagName("eop:status").item(0);
	    			String statustxt = status.getChildNodes().item(0).getNodeValue();
	    			
	    			Element link = (Element)eoMetadata.getElementsByTagName("eop:downlinkedTo").item(0);
	    			
	    			Element linkInfo = (Element)link.getElementsByTagName("eop:DownlinkInformation").item(0);
	    			
	    			Element acquisitionStation = (Element)linkInfo.getElementsByTagName("eop:acquisitionStation").item(0);
	    			String acquisitionStationtxt = acquisitionStation.getChildNodes().item(0).getNodeValue();
	    			
	    			Element processing = (Element)eoMetadata.getElementsByTagName("eop:processing").item(0);
	    			
	    			Element processingInfo = (Element)processing.getElementsByTagName("eop:ProcessingInformation").item(0);
	    			
	    			Element processingCenter = (Element)processingInfo.getElementsByTagName("eop:processingCenter").item(0);
	    			String processingCentertxt = processingCenter.getChildNodes().item(0).getNodeValue();
	    			
	    			CatalogueResult entry = new CatalogueResult();
	    			entry.setId(idtxt);
	    			entry.setAcquisitionStation(acquisitionStationtxt);
	    			entry.setAcquisitionType(acquisitionTypetxt);
	    			entry.setEnd(endtxt);
	    			entry.setInstrument(instrumentNametxt);
	    			entry.setLowerLeft(lowerLeft);
	    			entry.setLowerRight(lowerRight);
	    			entry.setPlatform(platformNametxt);
	    			entry.setProcessingCenter(processingCentertxt);
	    			entry.setProductType(productTypetxt);
	    			entry.setSensorType(sensorTypetxt);
	    			entry.setStart(starttxt);
	    			entry.setStatus(statustxt);
	    			entry.setTime(positionTimetxt);
	    			entry.setTitle(titletxt);
	    			entry.setUpperLeft(upperLeft);
	    			entry.setUpperRight(upperRight);
	    			
	    			results.put(idtxt, entry);
	    			/*rep += idtxt + "\n";
	    			rep += titletxt + "\n";
	    			rep += starttxt + "\n";
	    			rep += endtxt + "\n";
	    			rep += positionTimetxt + "\n";
	    			rep += platformNametxt + "\n";
	    			rep += instrumentNametxt + "\n";
	    			rep += sensorTypetxt + "\n";
	    			rep += positiontxt + "\n";
	    			rep += upperRight.getLatitude() + "\n";
	    			rep += upperLeft.getLatitude() + "\n";
	    			rep += lowerRight.getLatitude() + "\n";
	    			rep += lowerLeft.getLatitude() + "\n";
	    			rep += identifiertxt + "\n";
	    			rep += acquisitionTypetxt + "\n";
	    			rep += productTypetxt + "\n";
	    			rep += statustxt + "\n";
	    			rep += acquisitionStationtxt + "\n";
	    			rep += processingCentertxt + "\n";*/
	    			
	    		}
	    	} else {
	    		System.out.println("fail");
	    	}
	    	
	    	//if url...
	    	/*Node nNode = nlist.item(0);
	    	
	    	Element eElement = (Element) nNode;
	    	String url = eElement.getAttribute("template");
	    	
	    	String rep = "";

	    	List<NameValuePair> params = URLEncodedUtils.parse(new URI(url), "UTF-8");

	    	for (NameValuePair param : params) {
	    	  rep += param.getName() + " : " + param.getValue() + "\n";
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
	     return results;
	   }
}
