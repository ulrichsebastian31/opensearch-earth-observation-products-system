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
import com.google.gwt.user.cellview.client.CellTable;

public class FeasibilityResultPanel extends Composite implements HasText {

	private static FeasibilityResultPanelUiBinder uiBinder = GWT
			.create(FeasibilityResultPanelUiBinder.class);
	@UiField(provided=true) CellTable<Object> cellTable = new CellTable<Object>();

	interface FeasibilityResultPanelUiBinder extends
			UiBinder<Widget, FeasibilityResultPanel> {
	}

	public FeasibilityResultPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}


	public FeasibilityResultPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
