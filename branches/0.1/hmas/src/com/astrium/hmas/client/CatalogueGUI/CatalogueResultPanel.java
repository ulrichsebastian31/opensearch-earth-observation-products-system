/**
 * 
 */
package com.astrium.hmas.client.CatalogueGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               CatalogueResultPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the interface of
 *   																	the panel which handles the display of
 *   																	the results after a catalogue search
 *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (C) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 */

import java.util.ArrayList;
import java.util.List;

import com.astrium.hmas.bean.CatalogueBean.CatalogueResult;
import com.astrium.hmas.client.ShopcartService.ShopcartService;
import com.astrium.hmas.client.ShopcartService.ShopcartServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;

/**
 * @author re-cetienne
 * 
 */
public class CatalogueResultPanel extends Composite implements HasText {

	private static CatalogueResultPanelUiBinder uiBinder = GWT.create(CatalogueResultPanelUiBinder.class);
	@UiField(provided = true)
	/*
	 * Table in which the results will be displayed
	 */
	public CellTable<CatalogueResult> catalogue_result_panel_celltable = new CellTable<CatalogueResult>();
	@UiField
	/*
	 * Button to show the xml file returned from the server
	 */
	public Button catalogue_result_panel_xml_show_button;
	@UiField
	/*
	 * Button which sends the list of selected products to the shopcart
	 */
	public Button catalogue_result_panel_add_to_shopcart;
	/*
	 * Different selection models to be able to select the desired products in
	 * the GWT interface
	 */
	public MultiSelectionModel<CatalogueResult> catalogue_result_panel_selectionModelShopcart = new MultiSelectionModel<CatalogueResult>();
	public NoSelectionModel<CatalogueResult> catalogue_result_panel_selectionModelDetails = new NoSelectionModel<CatalogueResult>();
	/*
	 * Button column to be able to choose if you want the mesh of the
	 * acquisition to be displayed on the map or not
	 */
	public Column<CatalogueResult, String> catalogue_result_panel_showColumn;
	/*
	 * The list of the selected products to add to the shopcart
	 */
	public List<CatalogueResult> selected = new ArrayList<CatalogueResult>();
	/*
	 * boolean which says if the mesh of the acquisition is hidden on the map or
	 * not
	 */
	public boolean hideButtonClicked = false;

	/*
	 * Shopcart service and its getter
	 */
	private final ShopcartServiceAsync shopcartService = GWT.create(ShopcartService.class);

	public ShopcartServiceAsync getShopcartService() {
		return shopcartService;
	}

	interface CatalogueResultPanelUiBinder extends UiBinder<Widget, CatalogueResultPanel> {
	}

	/*
	 * CatalogueResultPanel constructor : build the table and the different
	 * buttons
	 */
	public CatalogueResultPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		catalogue_result_panel_celltable.getElement().getStyle().setOverflow(Overflow.AUTO);

		/*
		 * Checkbox column to select the products the user wants to add in his
		 * shopcart
		 */
		final CheckboxCell cbCell = new CheckboxCell();
		Column<CatalogueResult, Boolean> shopcartColumn = new Column<CatalogueResult, Boolean>(cbCell) {
			@Override
			public Boolean getValue(CatalogueResult object) {
				// Default value = false
				return false;
			}
		};

		/*
		 * Name and size settings
		 */
		catalogue_result_panel_celltable.addColumn(shopcartColumn, "Shopcart");
		catalogue_result_panel_celltable.setColumnWidth(shopcartColumn, 40, Unit.PX);

		/*
		 * Update on the shopcart column : when a product is selected, it's
		 * added to the list attribute "selected"
		 */
		shopcartColumn.setFieldUpdater(new FieldUpdater<CatalogueResult, Boolean>() {
			@Override
			public void update(int index, CatalogueResult object, Boolean value) {
				// TODO Auto-generated method stub
				if (value == true) {
					// the product is selected
					selected.add(object);
				} else {
					// the product is unselected
					selected.remove(object);
				}
			}
		});

		/*
		 * Button column to choose if you want to hide or show the mesh of the
		 * acquisition on the map
		 */
		ButtonCell downloadButton = new ButtonCell();
		catalogue_result_panel_showColumn = new Column<CatalogueResult, String>(downloadButton) {

			public String getValue(CatalogueResult object) {
				/*
				 * If the button "hide" is clicked, its value changes to become
				 * "show"
				 */
				return hideButtonClicked ? "Show" : "Hide";
			}
		};

		/*
		 * Name and size settings
		 */
		catalogue_result_panel_celltable.addColumn(catalogue_result_panel_showColumn, "Show");
		catalogue_result_panel_celltable.setColumnWidth(catalogue_result_panel_showColumn, 40, Unit.PX);

		/*
		 * Column<CatalogueResult, String> quickLookColumn = new
		 * Column<CatalogueResult, String>( new ClickableTextCell() { public
		 * void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		 * sb.appendHtmlConstant("<img width=\"50\" src=" + value.asString() +
		 * "\">"); } }) {
		 * 
		 * @Override public String getValue(CatalogueResult object) { return
		 * object.image_url; } };
		 * 
		 * catalogue_result_panel_celltable.addColumn(quickLookColumn,
		 * "Quicklook");
		 * catalogue_result_panel_celltable.setColumnWidth(quickLookColumn, 40,
		 * Unit.PX);
		 */

		/*
		 * Column which informs on the name of the platform's product
		 */
		TextColumn<CatalogueResult> nameColumn = new TextColumn<CatalogueResult>() {
			@Override
			public String getValue(CatalogueResult object) {
				if (object.platform != null) {
					// if the object has got a platform attribute, the name is
					// returned
					return object.platform;
				} else {
					return "not informed";
				}

			}
		};
		/*
		 * Name setting
		 */
		catalogue_result_panel_celltable.addColumn(nameColumn, "Platform");

		/*
		 * Column which informs on the name of the sensor's product
		 */
		TextColumn<CatalogueResult> sensorColumn = new TextColumn<CatalogueResult>() {
			@Override
			public String getValue(CatalogueResult object) {
				if (object.instrument != null) {
					// if the object has got a sensor attribute, the name is
					// returned
					return object.instrument;
				} else {
					return "not informed";
				}
			}
		};

		/*
		 * Name setting
		 */
		catalogue_result_panel_celltable.addColumn(sensorColumn, "Sensor");

		/*
		 * Column which informs on the date of the acquisition
		 */
		TextColumn<CatalogueResult> dateColumn = new TextColumn<CatalogueResult>() {
			@Override
			public String getValue(CatalogueResult object) {
				return object.start + " - " + object.end;
			}
		};

		/*
		 * Name setting
		 */
		catalogue_result_panel_celltable.addColumn(dateColumn, "Date");

		/*
		 * Column which informs on the percentage of cloud coverage when the
		 * acquisition was taken
		 */
		TextColumn<CatalogueResult> cloudColumn = new TextColumn<CatalogueResult>() {
			@Override
			public String getValue(CatalogueResult object) {
				if (object.cloudCover != null) {
					return object.cloudCover;
				} else {
					return "not informed";
				}
			}
		};

		/*
		 * Name setting
		 */
		catalogue_result_panel_celltable.addColumn(cloudColumn, "Cloud coverage");

	}

	public CatalogueResultPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

		// Can access @UiField after calling createAndBindUi
	}

	public void setText(String text) {

	}

	/**
	 * Gets invoked when the default constructor is called and a string is
	 * provided in the ui.xml file.
	 */
	public String getText() {
		return null;
	}

}
