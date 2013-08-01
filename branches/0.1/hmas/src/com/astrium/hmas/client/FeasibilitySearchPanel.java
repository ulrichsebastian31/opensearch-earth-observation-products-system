package com.astrium.hmas.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class FeasibilitySearchPanel extends Composite implements HasText {

	private static FeasibilitySearchPanelUiBinder uiBinder = GWT
			.create(FeasibilitySearchPanelUiBinder.class);
	@UiField AbsolutePanel feasibility_search_panel_absolute_panel;
	@UiField AbsolutePanel feasibility_search_panel_param_panel;

	interface FeasibilitySearchPanelUiBinder extends
			UiBinder<Widget, FeasibilitySearchPanel> {
	}

	public FeasibilitySearchPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		feasibility_search_panel_absolute_panel.getElement().getStyle().setOverflow(Overflow.AUTO);
		feasibility_search_panel_param_panel.getElement().getStyle().setOverflow(Overflow.AUTO);
	}


	public FeasibilitySearchPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
