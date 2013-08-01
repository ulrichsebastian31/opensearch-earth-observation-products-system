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

public class CataloguePanel extends Composite implements HasText {

	private static CatSubPanelUiBinder uiBinder = GWT
			.create(CatSubPanelUiBinder.class);
	@UiField TabPanel catalogue_panel_tab;
	public CatalogueSearchPanel catalogueSearchPanel = new CatalogueSearchPanel();
	public CatalogueResultPanel catalogueResultPanel = new CatalogueResultPanel();
	public CatalogueDetailsPanel catalogueDetailsPanel = new CatalogueDetailsPanel();

	interface CatSubPanelUiBinder extends UiBinder<Widget, CataloguePanel> {
	}

	public CataloguePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		catalogueSearchPanel.setWidth("450px");
		//resultPanel.setWidth("450px");
		catalogueDetailsPanel.setWidth("450px");
		catalogue_panel_tab.add(catalogueSearchPanel, "Search");
		catalogue_panel_tab.add(catalogueResultPanel, "Results");
		catalogue_panel_tab.add(catalogueDetailsPanel, "Details");
		catalogue_panel_tab.getTabBar().setTabEnabled(1, false);
		catalogue_panel_tab.getTabBar().setTabEnabled(2, false);
		
		/****************TEST***************/
		
		/*catalogueSearchPanel.catalogue_search_begin.setValue("2009-01-01T00:00:00");
		catalogueSearchPanel.catalogue_search_end.setValue("2009-01-07T00:00:00");
		catalogueSearchPanel.catalogue_search_panel_cloud.setValue(100.0);
		//catalogueSearchPanel.spot_search_panel_incidence.setValue("30");
		catalogueSearchPanel.catalogue_search_panel_resolutionMin.setValue(2.5);
		catalogueSearchPanel.catalogue_search_panel_resolutionMax.setValue((double) 20);
		catalogueSearchPanel.catalogue_search_panel_entryType.setValue("rectangle");
		catalogueSearchPanel.catalogue_search_panel_nwlat.setValue((double) 52);
		catalogueSearchPanel.catalogue_search_panel_selat.setValue((double) 48);
		catalogueSearchPanel.catalogue_search_panel_nwlon.setValue((double) 9);
		catalogueSearchPanel.catalogue_search_panel_selon.setValue((double) 13);*/
		
		
	}

	public CataloguePanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}


	public void setText(String text) {
	}

	public String getText() {
		return null;
	}

}
