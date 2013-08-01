package com.astrium.hmas.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.astrium.hmas.bean.SpotResult;
import com.astrium.hmas.bean.SpotScene;
import com.astrium.hmas.bean.SpotSearch;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class SpotPanel extends Composite implements HasText {

	private static SpotSubPanelUiBinder uiBinder = GWT
			.create(SpotSubPanelUiBinder.class);
	@UiField TabPanel spot_panel_tab;
	public SpotSearchPanel spotSearchPanel = new SpotSearchPanel();
	public SpotResultPanel spotResultPanel = new SpotResultPanel();
	public SpotDetailsPanel spotDetailsPanel = new SpotDetailsPanel();

	interface SpotSubPanelUiBinder extends UiBinder<Widget, SpotPanel> {
	}

	public SpotPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		spotSearchPanel.setWidth("450px");
		spotResultPanel.setWidth("580px");
		spotResultPanel.setHeight("800px");
		spotDetailsPanel.setWidth("450px");
		spotResultPanel.spot_result_panel_cellTable.setHeight("800px");
		spot_panel_tab.add(spotSearchPanel, "Search");
		spot_panel_tab.add(spotResultPanel, "Results");
		spot_panel_tab.add(spotDetailsPanel,"Details");
		spot_panel_tab.getTabBar().setTabEnabled(1, false);
		spot_panel_tab.getTabBar().setTabEnabled(2, false);
		
		/****************TEST***************/
		
		spotSearchPanel.spot_search_panel_key.setValue("w9LEniuV7WlZUP9CgtMr9Je72gS0BwfsleDoNypGIGI:");
		spotSearchPanel.spot_search_panel_out.setValue("json");
		spotSearchPanel.spot_search_panel_start.setValue("2009-01-01T00:00:00");
		spotSearchPanel.spot_search_panel_end.setValue("2009-01-07T00:00:00");
    	spotSearchPanel.spot_search_panel_cloud.setValue("100");
    	spotSearchPanel.spot_search_panel_incidence.setValue("30");
    	spotSearchPanel.spot_search_panel_reso_min.setValue("2.5");
    	spotSearchPanel.spot_search_panel_reso_max.setValue("20");
    	spotSearchPanel.spot_search_panel_zone.setValue("rectangle");
    	spotSearchPanel.spot_search_panel_nwlat.setValue("52");
    	spotSearchPanel.spot_search_panel_selat.setValue("48");
    	spotSearchPanel.spot_search_panel_nwlon.setValue("9");
    	spotSearchPanel.spot_search_panel_selon.setValue("13");
				

		    
		}

	public SpotPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {
	}

	public String getText() {
		return null;
	}

}
