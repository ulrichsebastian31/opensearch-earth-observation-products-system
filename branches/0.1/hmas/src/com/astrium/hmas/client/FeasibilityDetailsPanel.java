package com.astrium.hmas.client;

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
import com.google.gwt.user.client.ui.Label;

public class FeasibilityDetailsPanel extends Composite implements HasText {

	private static FeasibilityDetailsPanelUiBinder uiBinder = GWT
			.create(FeasibilityDetailsPanelUiBinder.class);
	@UiField Label feasibility_details_panel_id;
	@UiField Label feasibility_details_panel_platform;
	@UiField Label feasibility_details_panel_orbitType;
	@UiField Label feasibility_details_panel_instrument;
	@UiField Label feasibility_details_panel_sensorType;
	@UiField Label feasibility_details_panel_sensorMode;
	@UiField Label feasibility_details_panel_resolution;
	@UiField Label feasibility_details_panel_acquisitionDate;
	@UiField Label feasibility_details_panel_azimuth;
	@UiField Label feasibility_details_panel_elevation;
	@UiField Label feasibility_details_panel_alongTrack;
	@UiField Label feasibility_details_panel_accrossTrack;
	@UiField Label feasibility_details_panel_compositeType;
	@UiField Label feasibility_details_panel_sandWind;
	@UiField Label feasibility_details_panel_minLuminosity;
	@UiField Label feasibility_details_panel_noiseLevel;
	@UiField Label feasibility_details_panel_cloud;
	@UiField Label feasibility_details_panel_snow;
	@UiField Label feasibility_details_panel_haze;
	@UiField Label feasibility_details_panel_sunGlint;
	@UiField Label feasibility_details_panel_polarisationMode;

	interface FeasibilityDetailsPanelUiBinder extends
			UiBinder<Widget, FeasibilityDetailsPanel> {
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
