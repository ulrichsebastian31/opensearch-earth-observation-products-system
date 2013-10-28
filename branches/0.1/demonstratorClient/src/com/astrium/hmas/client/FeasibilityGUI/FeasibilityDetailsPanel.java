package com.astrium.hmas.client.FeasibilityGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FeasibilityDetailsPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the interface of
 *   																	the Details panel after the Feasibility
 *   																	Tasking.
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

public class FeasibilityDetailsPanel extends Composite implements HasText {

	private static FeasibilityDetailsPanelUiBinder uiBinder = GWT.create(FeasibilityDetailsPanelUiBinder.class);

	/*
	 * The labels which will write the product information
	 */
	@UiField
	public Label feasibility_details_panel_id;
	@UiField
	public Label feasibility_details_panel_platform;
	@UiField
	public Label feasibility_details_panel_orbitType;
	@UiField
	public Label feasibility_details_panel_instrument;
	@UiField
	public Label feasibility_details_panel_sensorType;
	@UiField
	public Label feasibility_details_panel_sensorMode;
	@UiField
	public Label feasibility_details_panel_resolution;
	@UiField
	public Label feasibility_details_panel_acquisitionDate;
	@UiField
	public Label feasibility_details_panel_azimuth;
	@UiField
	public Label feasibility_details_panel_elevation;
	@UiField
	public Label feasibility_details_panel_alongTrack;
	@UiField
	public Label feasibility_details_panel_accrossTrack;
	@UiField
	public Label feasibility_details_panel_compositeType;
	@UiField
	public Label feasibility_details_panel_sandWind;
	@UiField
	public Label feasibility_details_panel_minLuminosity;
	@UiField
	public Label feasibility_details_panel_noiseLevel;
	@UiField
	public Label feasibility_details_panel_cloud;
	@UiField
	public Label feasibility_details_panel_snow;
	@UiField
	public Label feasibility_details_panel_haze;
	@UiField
	public Label feasibility_details_panel_sunGlint;
	@UiField
	public Label feasibility_details_panel_polarisationMode;

	interface FeasibilityDetailsPanelUiBinder extends UiBinder<Widget, FeasibilityDetailsPanel> {
	}

	public FeasibilityDetailsPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public FeasibilityDetailsPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
