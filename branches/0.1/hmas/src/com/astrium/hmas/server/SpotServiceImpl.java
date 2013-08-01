package com.astrium.hmas.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import java_cup.parser;

import javax.ws.rs.core.MultivaluedMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.astrium.hmas.bean.Point;
import com.astrium.hmas.bean.SpotResult;
import com.astrium.hmas.bean.SpotScene;
import com.astrium.hmas.bean.SpotSearch;
import com.astrium.hmas.client.SpotService;
import com.astrium.hmas.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SpotServiceImpl extends RemoteServiceServlet implements
		SpotService {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SpotResult greetServer(SpotSearch spotSearch) throws IllegalArgumentException {
		// TODO Auto-generated method stub
			URLConnectionClientHandler ch  = new URLConnectionClientHandler(new ConnectionFactory());


		   //http://api.spotimage.com/catalog/spot/data/Dali.svc/search?of=json&sd=2009-01-01T00:00:00&ed=2009-01-07T00:00:00&mc=100&mi=30&minr=2.5&maxr=20&zt=rectangle&nwlat=52&selat=48&nwlon=9&selon=13&sk=w9LEniuV7WlZUP9CgtMr9Je72gS0BwfsleDoNypGIGI:
		    Client client = new Client(ch);
	        WebResource webResource = client.resource("http://api.spotimage.com/catalog/spot/data/Dali.svc/search");
	        
			MultivaluedMap queryParams = new MultivaluedMapImpl();
	        
	        queryParams.add("of", spotSearch.output);
	        queryParams.add("sd", spotSearch.start_date);
	        queryParams.add("ed", spotSearch.end_date);
	        queryParams.add("mc", String.valueOf(spotSearch.max_cloud));
	        queryParams.add("mi", String.valueOf(spotSearch.max_incidence));
	        queryParams.add("minr", String.valueOf(spotSearch.min_resolution));
	        queryParams.add("maxr", String.valueOf(spotSearch.max_resolution));
	        queryParams.add("zt", spotSearch.zone_type);
	        queryParams.add("nwlat", String.valueOf(spotSearch.nwlat));
	        queryParams.add("selat", String.valueOf(spotSearch.selat));
	        queryParams.add("nwlon", String.valueOf(spotSearch.nwlon));
	        queryParams.add("selon", String.valueOf(spotSearch.selon));
	        queryParams.add("sk", spotSearch.key);

	        String s = webResource.queryParams(queryParams).accept("application/json").get(String.class);
	        
	        JSONParser parser = new JSONParser();
	        SpotResult spotResult = new SpotResult();
	        
	    	try {
	    		
	    		
	    		Object obj = parser.parse(s);
	     
	    		JSONObject jsonObject = (JSONObject) obj;
	     
	    		String code = (String) jsonObject.get("Code");
	    		spotResult.setCode(code);
	    		System.out.println(code);
	     
	    		String msg = (String) jsonObject.get("Message");
	    		spotResult.setMessage(msg);
	    		System.out.println(msg);
	     
	    		// loop array
	    		JSONArray scenes = (JSONArray) jsonObject.get("Scenes");
	    		Iterator<JSONObject> iterator = scenes.iterator();
	    		while (iterator.hasNext()) {
	    			
	    			JSONObject jsonObj = iterator.next();
	    			
	    			JSONObject center = (JSONObject) jsonObj.get("SceneCenter");
	    			double lat_ctr = (double) center.get("Latitude");
	    			double lon_ctr = (double) center.get("Longitude");
	    			Point scene_center = new Point(lat_ctr,lon_ctr);
	    			JSONObject upperleft = (JSONObject) jsonObj.get("UpperLeft");
	    			double lat_ul = (double) upperleft.get("Latitude");
	    			double lon_ul = (double) upperleft.get("Longitude");
	    			Point upper_left = new Point(lat_ul,lon_ul);
	    			JSONObject upperright = (JSONObject) jsonObj.get("UpperRight");
	    			double lat_ur = (double) upperright.get("Latitude");
	    			double lon_ur = (double) upperright.get("Longitude");
	    			Point upper_right = new Point(lat_ur,lon_ur);
	    			JSONObject lowerleft = (JSONObject) jsonObj.get("LowerLeft");
	    			double lat_ll = (double) lowerleft.get("Latitude");
	    			double lon_ll = (double) lowerleft.get("Longitude");
	    			Point lower_left = new Point(lat_ll,lon_ll);
	    			JSONObject lowerright = (JSONObject) jsonObj.get("LowerRight");
	    			double lat_lr = (double) lowerright.get("Latitude");
	    			double lon_lr = (double) lowerright.get("Longitude");
	    			Point lower_right = new Point(lat_lr,lon_lr);

	    			SpotScene spotScene = new SpotScene((String) jsonObj.get("Id"), (String) jsonObj.get("MetadataUrl"), (String) jsonObj.get("ImageUrl"),
	    					(String) jsonObj.get("AcquisitionDate"), (String) jsonObj.get("Satellite"), (long) jsonObj.get("Resolution"), (String) jsonObj.get("ArchivingStation"),
	    					(long) jsonObj.get("CloudCoverPercentage"),(long) jsonObj.get("SnowCoverPercentage"), (String) jsonObj.get("Shift"), 
	    					(String) jsonObj.get("MinShift"), (String) jsonObj.get("MaxShift"),	scene_center,upper_left, upper_right, lower_left, lower_right, 
	    					(long) jsonObj.get("SunAzimuth"), (long) jsonObj.get("SunElevation"), (String) jsonObj.get("TQuality"));
	    			
	    			spotResult.getScenes().put((String) iterator.next().get("Id"), spotScene);
	    		}
	     
	    	} catch (ParseException e) {
	    		e.printStackTrace();
	    	}

	        return spotResult;
	}
}
