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

public class FeasibilityPanel extends Composite implements HasText {

	private static FeasSubPanelUiBinder uiBinder = GWT
			.create(FeasSubPanelUiBinder.class);
	@UiField TabPanel feasibility_panel_tab;
	public FeasibilitySearchPanel feasibilitySearchPanel = new FeasibilitySearchPanel();
	public FeasibilityResultPanel feasibilityResultPanel = new FeasibilityResultPanel();
	public FeasibilityDetailsPanel feasibilityDetailsPanel = new FeasibilityDetailsPanel();

	interface FeasSubPanelUiBinder extends UiBinder<Widget, FeasibilityPanel> {
	}

	public FeasibilityPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		feasibilitySearchPanel.setWidth("450px");
		feasibilityDetailsPanel.setWidth("450px");
		feasibility_panel_tab.add(feasibilitySearchPanel, "Search");
		feasibility_panel_tab.add(feasibilityResultPanel, "Results");
		feasibility_panel_tab.add(feasibilityDetailsPanel, "Details");
		//feasibility_panel_tab.getTabBar().setTabEnabled(1, false);
		//feasibility_panel_tab.getTabBar().setTabEnabled(2, false);
	}



	public FeasibilityPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}



	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
