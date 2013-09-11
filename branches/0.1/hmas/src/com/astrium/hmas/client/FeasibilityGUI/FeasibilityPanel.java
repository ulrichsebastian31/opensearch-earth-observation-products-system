package com.astrium.hmas.client.FeasibilityGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FeasibilityPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the interface of
 *   																	the panel which handles the Feasibility
 *   																	tasking functionality
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

public class FeasibilityPanel extends Composite implements HasText {

	private static FeasSubPanelUiBinder uiBinder = GWT.create(FeasSubPanelUiBinder.class);

	@UiField
	public TabPanel feasibility_panel_tab;

	/*
	 * The Feasibility panel contains 3 sub-panels, each handling different
	 * tasks : search, results display and details of the products
	 */
	public FeasibilitySearchPanel feasibilitySearchPanel = new FeasibilitySearchPanel();
	public FeasibilityResultPanel feasibilityResultPanel = new FeasibilityResultPanel();
	public FeasibilityDetailsPanel feasibilityDetailsPanel = new FeasibilityDetailsPanel();

	interface FeasSubPanelUiBinder extends UiBinder<Widget, FeasibilityPanel> {
	}

	/*
	 * Constructor of the Feasibility panel : adding the sub-panels
	 */
	public FeasibilityPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		feasibilitySearchPanel.setWidth("450px");
		feasibilityDetailsPanel.setWidth("450px");
		feasibility_panel_tab.add(feasibilitySearchPanel, "Search");
		feasibility_panel_tab.add(feasibilityResultPanel, "Results");
		feasibility_panel_tab.add(feasibilityDetailsPanel, "Details");
		feasibility_panel_tab.getTabBar().setTabEnabled(1, false);
		feasibility_panel_tab.getTabBar().setTabEnabled(2, false);
	}

	public FeasibilityPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
