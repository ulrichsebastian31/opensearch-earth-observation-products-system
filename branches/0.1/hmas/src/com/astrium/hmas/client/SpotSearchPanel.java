package com.astrium.hmas.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.astrium.hmas.bean.Point;
import com.astrium.hmas.bean.SpotResult;
import com.astrium.hmas.bean.SpotScene;
import com.astrium.hmas.bean.SpotSearch;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class SpotSearchPanel extends Composite implements HasText {

	private static SpotPanelUiBinder uiBinder = GWT
			.create(SpotPanelUiBinder.class);
	@UiField Button spot_search_panel_submit_button;
	@UiField TextBox spot_search_panel_key;
	@UiField TextBox spot_search_panel_out;
	@UiField TextBox spot_search_panel_start;
	@UiField TextBox spot_search_panel_end;
	@UiField TextBox spot_search_panel_cloud;
	@UiField TextBox spot_search_panel_snow;
	@UiField TextBox spot_search_panel_incidence;
	@UiField TextBox spot_search_panel_reso_min;
	@UiField TextBox spot_search_panel_reso_max;
	@UiField TextBox spot_search_panel_zone;
	@UiField TextBox spot_search_panel_circle_lon;
	@UiField TextBox spot_search_panel_circle_lat;
	@UiField TextBox spot_search_panel_circle_rad;
	@UiField TextBox spot_search_panel_nwlat;
	@UiField TextBox spot_search_panel_nwlon;
	@UiField TextBox spot_search_panel_selat;
	@UiField TextBox spot_search_panel_selon;
	@UiField TextBox spot_search_panel_points;
	@UiField TextBox spot_search_panel_station;
	@UiField TextBox spot_search_panel_satellite;
	@UiField TextBox spot_search_panel_sensor;
	@UiField TextBox spot_search_panel_fullness;
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final SpotServiceAsync greetingService = GWT
			.create(SpotService.class);

	public SpotServiceAsync getGreetingService() {
		return greetingService;
	}

	interface SpotPanelUiBinder extends UiBinder<Widget, SpotSearchPanel> {
	}

	public SpotSearchPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SpotSearchPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}


	public void setText(String text) {
	}

	public String getText() {
		return null;
	}
	

	}
