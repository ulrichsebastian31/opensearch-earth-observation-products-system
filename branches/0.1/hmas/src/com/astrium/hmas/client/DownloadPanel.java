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

public class DownloadPanel extends Composite implements HasText {

	private static DownloadSubPanelUiBinder uiBinder = GWT
			.create(DownloadSubPanelUiBinder.class);
	@UiField TabPanel download_panel_tab;
	public DownloadProductPanel downloadProductPanel = new DownloadProductPanel();

	interface DownloadSubPanelUiBinder extends
			UiBinder<Widget, DownloadPanel> {
	}

	public DownloadPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		downloadProductPanel.setWidth("580px");
		download_panel_tab.add(downloadProductPanel, "Products");
	}


	public DownloadPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
