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

public class FeasibilityDetailsPanel extends Composite implements HasText {

	private static FeasibilityDetailsPanelUiBinder uiBinder = GWT
			.create(FeasibilityDetailsPanelUiBinder.class);

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
