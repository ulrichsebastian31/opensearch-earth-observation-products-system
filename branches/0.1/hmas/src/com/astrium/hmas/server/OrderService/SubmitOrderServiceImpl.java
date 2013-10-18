package com.astrium.hmas.server.OrderService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SubmitOrderServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Submit Order Service interface implementation
 *   																	Sends an XML file with all the information
 *   																	to submit an order and get back the ID
 *   																	of this order
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

import java.util.List;
import java.util.Map;

import net.opengis.oseo.x10.CommonOrderItemType.Option;
import net.opengis.oseo.x10.DeliveryOptionsType;
import net.opengis.oseo.x10.DeliveryOptionsType.OnlineDataAccess;
import net.opengis.oseo.x10.ParameterDataType;
import net.opengis.oseo.x10.ProductIdType;
import net.opengis.oseo.x10.ProtocolType.Enum;
import net.opengis.roseo.x10.CommonOrderMonitorSpecification;
import net.opengis.roseo.x10.CommonOrderStatusItemType;
import net.opengis.roseo.x10.OrderDocument;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

import com.astrium.hmas.bean.OrderBean.Order;
import com.astrium.hmas.bean.OrderBean.OrderItem;
import com.astrium.hmas.client.OrderService.SubmitOrderService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

@SuppressWarnings("serial")
public class SubmitOrderServiceImpl extends RemoteServiceServlet implements SubmitOrderService {
	
	/*
	 * Jersey Client creation
	 */
	Client client = new Client();
	/*
	 * Call to the server
	 */
	WebResource webResource = client.resource("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/order/submit");

	@Override
	public String submitOrder(Order order) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		OrderDocument doc = OrderDocument.Factory.newInstance();
		CommonOrderMonitorSpecification xml_order = doc.addNewOrder();
		xml_order.setOrderReference("example_0001");
		xml_order.setOrderRemark("example");
		DeliveryOptionsType deliveryOptions = xml_order.addNewDeliveryOptions();
		OnlineDataAccess onlineDataAccess = deliveryOptions.addNewOnlineDataAccess();

		Enum protocol = Enum.forString("https");
		onlineDataAccess.setProtocol(protocol);

		xml_order.setDeliveryOptions(deliveryOptions);

		net.opengis.oseo.x10.EnumOrderType.Enum orderType = net.opengis.oseo.x10.EnumOrderType.Enum.forString("PRODUCT_ORDER");
		xml_order.setOrderType(orderType);

		List<OrderItem> items = order.getOrderItemList();
		System.out.println("order list size :" + items.size());


		for (int i = 0; i < items.size(); i++) {

			OrderItem item = new OrderItem();
			item = items.get(i);
			CommonOrderStatusItemType orderItem = xml_order.addNewOrderItem();
			orderItem.setItemId(item.getItemID());
			orderItem.setProductOrderOptionsId("Level 1,Product SLC (ASA_IMS)");
			orderItem.setOrderItemRemark("Product number " + i);

			Option option = orderItem.addNewOption();
			ParameterDataType parameterData = option.addNewParameterData();

			net.opengis.oseo.x10.SWEEncoding.Enum encoding = net.opengis.oseo.x10.SWEEncoding.Enum.forString("XMLEncoding");
			parameterData.setEncoding(encoding);
			
			Map<String, String> options = item.getOptions();

			XmlObject values = parameterData.addNewValues();

			XmlCursor cur = values.newCursor();
			// We could use the convenient xobj.selectPath() or cur.selectPath()
			// to position the cursor on the <person> element, but let's use the
			// cursor's toChild() instead.
			cur.toChild("values");
			cur.toEndToken();

			for (String key : options.keySet()) {

				cur.insertElementWithText(key, options.get(key));

			}
			if (cur != null)
				cur.dispose();

			ProductIdType productId = orderItem.addNewProductId();
			productId.setIdentifier(item.getProductID());
			productId.setCollectionId("http://www.opengis.net/def/EOP/ESA/0/ESA.EECF.ENVISAT_ASA_IMx_xS");

		}

		String submitOrderXML = doc.toString();
		
		String xmlresponse = webResource.entity(submitOrderXML, "text/xml").accept("text/xml").post(String.class);

		return xmlresponse;
	}
	
	

}
