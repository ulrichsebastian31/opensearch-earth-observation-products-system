package com.astrium.hmas.client.DownloadGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DownloadPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the interface of
 *   																	the panel which handles the Download 
 *   																	functionality
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

public class DownloadPanel extends Composite implements HasText {

	private static DownloadSubPanelUiBinder uiBinder = GWT.create(DownloadSubPanelUiBinder.class);

	@UiField
	public TabPanel download_panel_tab;
	/*
	 * The download Panel contains a sub-panel which allows user to download
	 * desired products
	 */
	public DownloadProductPanel downloadProductPanel = new DownloadProductPanel();

	interface DownloadSubPanelUiBinder extends UiBinder<Widget, DownloadPanel> {
	}

	/*
	 * DownloadPanel constructor
	 */
	public DownloadPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		downloadProductPanel.setWidth("580px");
		download_panel_tab.add(downloadProductPanel, "Products");
	}

	public DownloadPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
