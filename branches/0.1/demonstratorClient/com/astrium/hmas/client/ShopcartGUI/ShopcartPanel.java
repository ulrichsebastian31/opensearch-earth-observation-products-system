package com.astrium.hmas.client.ShopcartGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               ShopcartPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the panel which
 *   																	contains the Shopcart feature
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

public class ShopcartPanel extends Composite implements HasText {

	private static ShopcartPanelUiBinder uiBinder = GWT.create(ShopcartPanelUiBinder.class);
	@UiField
	public TabPanel shopcart_panel_tab;
	/*
	 * ShopcartPanel contains one tab -> ShopcartListPanel
	 */
	public ShopcartListPanel shopcartListPanel = new ShopcartListPanel();

	interface ShopcartPanelUiBinder extends UiBinder<Widget, ShopcartPanel> {
	}

	/*
	 * Shopcart Panel constructor
	 */
	public ShopcartPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		/*
		 * tab settings
		 */
		shopcart_panel_tab.add(shopcartListPanel, "List");
		shopcartListPanel.setWidth("450px");
	}

	public ShopcartPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {
	}

	public String getText() {
		return null;
	}

}
