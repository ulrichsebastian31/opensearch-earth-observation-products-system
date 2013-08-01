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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class SpotDetailsPanel extends Composite implements HasText {

	private static SpotDetailsPanelUiBinder uiBinder = GWT
			.create(SpotDetailsPanelUiBinder.class);
	@UiField Image spot_details_panel_image;
	@UiField Label spot_panel_label_satellite;
	@UiField Label spot_panel_label_acquisition_date;
	@UiField Label spot_panel_label_resolution;
	@UiField Label spot_panel_label_station;
	@UiField Label spot_panel_label_cloud;
	@UiField Label spot_panel_label_snow;
	@UiField Label spot_panel_label_shift;
	@UiField Label spot_panel_label_sun_azimuth;
	@UiField Label spot_panel_label_sun_elevation;
	@UiField Label spot_panel_label_tquality;
	@UiField Label spot_panel_label_center;

	interface SpotDetailsPanelUiBinder extends
			UiBinder<Widget, SpotDetailsPanel> {
	}

	public SpotDetailsPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}


	public SpotDetailsPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
