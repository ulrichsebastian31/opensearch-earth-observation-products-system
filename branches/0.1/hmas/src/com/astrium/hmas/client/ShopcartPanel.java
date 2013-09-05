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

public class ShopcartPanel extends Composite implements HasText {

	private static ShopcartPanelUiBinder uiBinder = GWT
			.create(ShopcartPanelUiBinder.class);
	@UiField TabPanel shopcart_panel_tab;
	public ShopcartListPanel shopcartListPanel = new ShopcartListPanel();

	interface ShopcartPanelUiBinder extends UiBinder<Widget, ShopcartPanel> {
	}

	public ShopcartPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		shopcart_panel_tab.add(shopcartListPanel, "List");
		shopcartListPanel.setWidth("450px");
	}

	public ShopcartPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setText(String text) {
	}

	public String getText() {
		return null;
	}

}
