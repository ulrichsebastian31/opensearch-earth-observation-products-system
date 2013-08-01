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
import com.google.gwt.user.client.ui.TabPanel;

public class MainPanel extends Composite implements HasText {

	private static MainPanelUiBinder uiBinder = GWT
			.create(MainPanelUiBinder.class);
	@UiField TabPanel mainTab;
	public CataloguePanel cataloguePanel = new CataloguePanel();
	public FeasibilityPanel feasibilityPanel = new FeasibilityPanel();
	public ShopcartPanel shopcartPanel = new ShopcartPanel();
	public DownloadPanel downloadPanel = new DownloadPanel();
	public SpotPanel spotPanel = new SpotPanel();
	

	interface MainPanelUiBinder extends UiBinder<Widget, MainPanel> {
	}

	public MainPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		mainTab.add(cataloguePanel, "Catalogue Search");
		mainTab.add(feasibilityPanel, "Tasking Feasibility");
		mainTab.add(shopcartPanel, "Shopcart");
		mainTab.add(downloadPanel, "Download");
		//mainTab.add(spotPanel, "Spot Research API");
		
	}


	public MainPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {
	}

	public String getText() {
		return null;
	}

}
