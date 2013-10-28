package com.astrium.hmas.client;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               MainPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Main Panel which handles all the sub-
 *   																	panels and features of the application
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

import com.astrium.hmas.client.CatalogueGUI.CataloguePanel;
import com.astrium.hmas.client.DownloadGUI.DownloadPanel;
import com.astrium.hmas.client.FeasibilityGUI.FeasibilityPanel;
import com.astrium.hmas.client.OrderGUI.OrderPanel;
import com.astrium.hmas.client.ShopcartGUI.ShopcartPanel;
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

public class MainPanel extends Composite implements HasText {

	private static MainPanelUiBinder uiBinder = GWT
			.create(MainPanelUiBinder.class);
	@UiField TabPanel mainTab;
	/*
	 * the MainPanel contains all the features panels
	 */
	public CataloguePanel cataloguePanel = new CataloguePanel();
	public FeasibilityPanel feasibilityPanel = new FeasibilityPanel();
	public ShopcartPanel shopcartPanel = new ShopcartPanel();
	public DownloadPanel downloadPanel = new DownloadPanel();
	public OrderPanel orderPanel = new OrderPanel();
	

	interface MainPanelUiBinder extends UiBinder<Widget, MainPanel> {
	}

	/*
	 * MainPanel constructor
	 */
	public MainPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		/*
		 * Give the different tabs a name
		 */
		mainTab.add(cataloguePanel, "Catalogue Search");
		mainTab.add(feasibilityPanel, "Tasking Feasibility");
		mainTab.add(shopcartPanel, "Shopcart");
		mainTab.add(orderPanel, "Order");
		mainTab.add(downloadPanel, "Download");
		
	}


	public MainPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {
	}

	public String getText() {
		return null;
	}

}
