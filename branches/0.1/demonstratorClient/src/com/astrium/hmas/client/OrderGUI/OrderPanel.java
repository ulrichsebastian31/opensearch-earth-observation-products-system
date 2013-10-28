package com.astrium.hmas.client.OrderGUI;
/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OrderPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the panel which
 *   																	contains the Ordering feature
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TabPanel;

public class OrderPanel extends Composite implements HasText {

	private static OrderPanelUiBinder uiBinder = GWT.create(OrderPanelUiBinder.class);
	@UiField public TabPanel order_panel_tab;
	
	/*
	 * ShopcartPanel contains one tab -> ShopcartListPanel
	 */
	public OrderListPanel orderListPanel = new OrderListPanel();

	interface OrderPanelUiBinder extends UiBinder<Widget, OrderPanel> {
	}

	/*
	 * Order Panel constructor
	 */
	public OrderPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		/*
		 * tab settings
		 */
		order_panel_tab.add(orderListPanel, "List");
		order_panel_tab.setWidth("450px");
	}



	public OrderPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}


	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
