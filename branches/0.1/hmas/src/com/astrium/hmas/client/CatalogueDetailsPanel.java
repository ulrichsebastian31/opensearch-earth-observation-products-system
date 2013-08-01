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
import com.google.gwt.user.client.ui.Image;

public class CatalogueDetailsPanel extends Composite implements HasText {

	private static DetailsPanelUiBinder uiBinder = GWT
			.create(DetailsPanelUiBinder.class);
	@UiField Label catalogue_details_panel_platform;
	@UiField Label catalogue_details_panel_orbitType;
	@UiField Label catalogue_details_panel_instrument;
	@UiField Label catalogue_details_panel_sensorType;
	@UiField Label catalogue_details_panel_sensorMode;
	@UiField Label catalogue_details_panel_resolution;
	@UiField Label catalogue_details_panel_swathId;
	@UiField Label catalogue_details_panel_waveLenght;
	@UiField Label catalogue_details_panel_spectralRange;
	@UiField Label catalogue_details_panel_orbitNumber;
	@UiField Label catalogue_details_panel_orbitDirection;
	@UiField Label catalogue_details_panel_track;
	@UiField Label catalogue_details_panel_frame;
	@UiField Label catalogue_details_panel_identifier;
	@UiField Label catalogue_details_panel_entryType;
	@UiField Label catalogue_details_panel_acquisitionType;
	@UiField Label catalogue_details_panel_status;
	@UiField Label catalogue_details_panel_archivingCenter;
	@UiField Label catalogue_details_panel_archivingDate;
	@UiField Label catalogue_details_panel_acquisitionStation;
	@UiField Label catalogue_details_panel_processingCenter;
	@UiField Label catalogue_details_panel_processingSoftware;
	@UiField Label catalogue_details_panel_processingDate;
	@UiField Label catalogue_details_panel_processingLevel;
	@UiField Label catalogue_details_panel_compositeType;
	@UiField Label catalogue_details_panel_cloudCover;
	@UiField Label catalogue_details_panel_snowCover;
	@UiField Label catalogue_details_panel_id;

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
