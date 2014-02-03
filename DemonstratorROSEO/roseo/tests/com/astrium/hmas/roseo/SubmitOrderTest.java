package com.astrium.hmas.roseo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.opengis.oseo.x10.DeliveryOptionsType;
import net.opengis.oseo.x10.ParameterDataType;
import net.opengis.oseo.x10.PaymentOptionSelectedValue;
import net.opengis.oseo.x10.ProductIdType;
import net.opengis.oseo.x10.CommonOrderItemType.Option;
import net.opengis.oseo.x10.DeliveryOptionsType.OnlineDataAccess;
import net.opengis.oseo.x10.ProtocolType.Enum;
import net.opengis.roseo.x10.CommonOrderMonitorSpecification;
import net.opengis.roseo.x10.CommonOrderStatusItemType;
import net.opengis.roseo.x10.OrderDocument;

import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.codehaus.jettison.json.JSONArray;
import org.json.JSONObject;
import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.astrium.roseodbhandler.structures.EOProduct;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class SubmitOrderTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	
	public static String url = "jdbc:postgresql://localhost:5432/ROSEODatabase";
    public static String user = "postgres";
    public static String passwd = "password";
    
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		OrderDocument orderDoc = OrderDocument.Factory.newInstance();
		CommonOrderMonitorSpecification xml_order = orderDoc.addNewOrder();
		
		XmlCursor cursor = xml_order.newCursor();
		cursor.toNextToken();
		cursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		cursor.insertNamespace("roseo", "http://www.opengis.net/roseo/1.0");
		cursor.insertNamespace("sps", "http://www.opengis.net/sps/2.0");
		cursor.insertNamespace("swe", "http://www.opengis.net/swe/2.0");
		cursor.dispose();

		xml_order.setOrderReference("Order_example");
		xml_order.setOrderRemark("Submit Order example");
		
		DeliveryOptionsType deliveryOptions = xml_order.addNewDeliveryOptions();
		OnlineDataAccess onlineDataAccess = deliveryOptions.addNewOnlineDataAccess();

		Enum protocol = Enum.forString("https");
		onlineDataAccess.setProtocol(protocol);

		xml_order.setDeliveryOptions(deliveryOptions);

		net.opengis.oseo.x10.EnumOrderType.Enum orderType = net.opengis.oseo.x10.EnumOrderType.Enum.forString("PRODUCT_ORDER");
		xml_order.setOrderType(orderType);
		
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		xml_order.setOrderDateTime(cal);
		RoseoManagementHandler roseomh = new RoseoManagementHandler(url,user,passwd);
		
		for(int i=0;i<args.length;i++){

			String eoProductID = args[i];
			
			System.out.println(eoProductID);

			//Fake set options -> for the example
			InputStream optSetFile = new FileInputStream(System.getProperty("user.home") + File.separator + "hmas" + File.separator + "opt3Set.json");
			String optionsSet = IOUtils.toString(optSetFile,"UTF-8");

			UUID id = UUID.randomUUID();
			String itemID = id.toString();
			
			CommonOrderStatusItemType orderItemXML = xml_order.addNewOrderItem();
			
			orderItemXML.setItemId("item_" + itemID);
			
			JSONObject optSetJson = new JSONObject(optionsSet);
			
			orderItemXML.setProductOrderOptionsId(optSetJson.getString("productOrderOptionsId"));
			orderItemXML.setOrderItemRemark("Product nb" + (i+1));
			
			Option option = orderItemXML.addNewOption();
			
			ParameterDataType parameterData = option.addNewParameterData();

			net.opengis.oseo.x10.SWEEncoding.Enum encoding = net.opengis.oseo.x10.SWEEncoding.Enum.forString("XMLEncoding");
			parameterData.setEncoding(encoding);

			XmlObject values = parameterData.addNewValues();
			
			XmlCursor cur = values.newCursor();

			cur.toChild("values");
			cur.toEndToken();
			cur.insertNamespace("ns", "http://www.opengis.net/oseo/1.0/dataset");
			
			Iterator<String> keys = optSetJson.keys();
			Map<String,String> jsonObj = new HashMap<String,String>();
			
			while(keys.hasNext()) {
		        String key = keys.next();
		        
		        String currentDynamicValue = optSetJson.getString(key);
		        
		        if(key.contains(":")){
		        	key = key.split(":")[1];
		        }
		        
		        if(currentDynamicValue.startsWith("[")){
		        	
		        	JSONArray valuesJSON = new JSONArray(currentDynamicValue);
		        	String temp = "";
		        	for(int k=0;k<valuesJSON.length();k++){
		        		
		        		if(k==valuesJSON.length()-1){
		        			
		        			temp += valuesJSON.get(k);
		        			
		        		}else{
		        			
		        			temp += valuesJSON.get(k) + ",";
		        		}
		        		
		        	}
		        	currentDynamicValue = temp;
		        }
		        jsonObj.put(key, currentDynamicValue);
		        

		    }
			
			
			for (String key : jsonObj.keySet()) {

				if(!(key.equals("productOrderOptionsId"))){
		        	cur.insertElementWithText(key, jsonObj.get(key));
		        }

			}
			
			if (cur != null)
				cur.dispose();
			
			EOProduct product = roseomh.getProduct(eoProductID);
			
			ProductIdType productId = ProductIdType.Factory.newInstance();
			
			if(!product.getCollectionID().equals("null")){
				productId.setIdentifier("http://www.opengis.net/def/EOP/ESA/0/" + product.getCollectionID() + "/" + product.getProductID());
				productId.setCollectionId("http://www.opengis.net/def/EOP/ESA/0/" + product.getCollectionID());
			}else{
				productId.setIdentifier("http://www.opengis.net/def/EOP/ESA/0/" + product.getProductID());
			}
			
			orderItemXML.setProductId(productId);
			
			PaymentOptionSelectedValue paymentMethod = PaymentOptionSelectedValue.Factory.newInstance();
			paymentMethod.setPaymentMethod("quota");
			paymentMethod.setOrderAccount("project_10000");
			
			orderItemXML.setPayment(paymentMethod);
		}


		String submitOrderXML = orderDoc.toString();
		
		System.out.println(submitOrderXML);

		Client client = new Client();

		WebResource webResource = client.resource("http://localhost:8080/ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/");
		WebResource webResourceTest = webResource.path("order");
		final String responseString = webResourceTest.entity(submitOrderXML, "text/xml").accept("text/xml").post(String.class);

		System.out.println(responseString);

	}

}
