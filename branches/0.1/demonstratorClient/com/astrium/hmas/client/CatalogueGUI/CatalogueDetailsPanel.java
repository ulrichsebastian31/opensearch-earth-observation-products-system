package com.astrium.hmas.client.CatalogueGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               CatalogueDetailsPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the interface of
 *   																	the Details panel after the Catalogue
 *   																	search.
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
import com.google.gwt.user.client.ui.Label;

public class CatalogueDetailsPanel extends Composite implements HasText {

	private static DetailsPanelUiBinder uiBinder = GWT.create(DetailsPanelUiBinder.class);
	/*
	 * The labels which will write the product information
	 */
	@UiField
	public Label catalogue_details_panel_platform;
	@UiField
	public Label catalogue_details_panel_orbitType;
	@UiField
	public Label catalogue_details_panel_instrument;
	@UiField
	public Label catalogue_details_panel_sensorType;
	@UiField
	public Label catalogue_details_panel_sensorMode;
	@UiField
	public Label catalogue_details_panel_resolution;
	@UiField
	public Label catalogue_details_panel_swathId;
	@UiField
	public Label catalogue_details_panel_waveLenght;
	@UiField
	public Label catalogue_details_panel_spectralRange;
	@UiField
	public Label catalogue_details_panel_orbitNumber;
	@UiField
	public Label catalogue_details_panel_orbitDirection;
	@UiField
	public Label catalogue_details_panel_track;
	@UiField
	public Label catalogue_details_panel_frame;
	@UiField
	public Label catalogue_details_panel_identifier;
	@UiField
	public Label catalogue_details_panel_entryType;
	@UiField
	public Label catalogue_details_panel_acquisitionType;
	@UiField
	public Label catalogue_details_panel_status;
	@UiField
	public Label catalogue_details_panel_archivingCenter;
	@UiField
	public Label catalogue_details_panel_archivingDate;
	@UiField
	public Label catalogue_details_panel_acquisitionStation;
	@UiField
	public Label catalogue_details_panel_processingCenter;
	@UiField
	public Label catalogue_details_panel_processingSoftware;
	@UiField
	public Label catalogue_details_panel_processingDate;
	@UiField
	public Label catalogue_details_panel_processingLevel;
	@UiField
	public Label catalogue_details_panel_compositeType;
	@UiField
	public Label catalogue_details_panel_cloudCover;
	@UiField
	public Label catalogue_details_panel_snowCover;
	@UiField
	public Label catalogue_details_panel_id;

	interface DetailsPanelUiBinder extends UiBinder<Widget, CatalogueDetailsPanel> {
	}

	public CatalogueDetailsPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public CatalogueDetailsPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {
	}

	public String getText() {
		return null;
	}

}
