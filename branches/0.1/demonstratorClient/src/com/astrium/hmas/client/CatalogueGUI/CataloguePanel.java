package com.astrium.hmas.client.CatalogueGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               CataloguePanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the interface of
 *   																	the panel which handles the Catalogue
 *   																	Search functionality
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TabPanel;

public class CataloguePanel extends Composite implements HasText {

	/*
	 * The Catalogue panel contains 3 sub-panels, each handling different tasks
	 * : search, results display and details of the products
	 */
	private static CatSubPanelUiBinder uiBinder = GWT.create(CatSubPanelUiBinder.class);
	@UiField
	public TabPanel catalogue_panel_tab;
	public CatalogueSearchPanel catalogueSearchPanel = new CatalogueSearchPanel();
	public CatalogueResultPanel catalogueResultPanel = new CatalogueResultPanel();
	public CatalogueDetailsPanel catalogueDetailsPanel = new CatalogueDetailsPanel();

	interface CatSubPanelUiBinder extends UiBinder<Widget, CataloguePanel> {
	}

	/*
	 * Constructor of the Catalogue panel : adding the sub-panels
	 */

	public CataloguePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		catalogueSearchPanel.setWidth("450px");
		// resultPanel.setWidth("450px");
		catalogueDetailsPanel.setWidth("450px");
		catalogue_panel_tab.add(catalogueSearchPanel, "Search");
		catalogue_panel_tab.add(catalogueResultPanel, "Results");
		catalogue_panel_tab.add(catalogueDetailsPanel, "Details");
		catalogue_panel_tab.getTabBar().setTabEnabled(1, false);
		catalogue_panel_tab.getTabBar().setTabEnabled(2, false);

	}

	public CataloguePanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void setText(String text) {
	}

	public String getText() {
		return null;
	}

}
