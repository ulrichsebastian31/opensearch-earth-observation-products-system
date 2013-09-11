package com.astrium.hmas.client.FeasibilityGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FeasibilityResultPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the interface of
 *   																	the panel which handles the display of
 *   																	the results after a feasibility tasking.
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

import com.astrium.hmas.bean.FeasibilityBean.FeasibilityResult;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.NoSelectionModel;

public class FeasibilityResultPanel extends Composite implements HasText {

	private static FeasibilityResultPanelUiBinder uiBinder = GWT.create(FeasibilityResultPanelUiBinder.class);
	@UiField(provided = true)
	/*
	 * Table in which the results will be displayed
	 */
	public CellTable<FeasibilityResult> feasibility_result_panel_cellTable = new CellTable<FeasibilityResult>();
	@UiField
	/*
	 * Button to show the xml file returned from the server
	 */
	public Button feasibility_result_panel_show_xml_button;
	/*
	 * Selection model to be able to select the desired products in the GWT
	 * interface
	 */
	public NoSelectionModel<FeasibilityResult> feasibility_result_panel_selectionModelDetails = new NoSelectionModel<FeasibilityResult>();
	/*
	 * Button column to be able to choose if you want the mesh of the future
	 * acquisition to be displayed on the map or not
	 */
	public Column<FeasibilityResult, String> feasibility_result_panel_showColumn;
	/*
	 * boolean which says if the mesh of the acquisition is hidden on the map or
	 * not
	 */
	public boolean hideButtonClicked = false;

	interface FeasibilityResultPanelUiBinder extends UiBinder<Widget, FeasibilityResultPanel> {
	}

	/*
	 * CatalogueResultPanel constructor : build the table and the different
	 * buttons
	 */
	public FeasibilityResultPanel() {

		initWidget(uiBinder.createAndBindUi(this));

		feasibility_result_panel_cellTable.getElement().getStyle().setOverflow(Overflow.AUTO);

		/*
		 * Checkbox column to select the products the user wants to add in his
		 * shopcart
		 */
		Column<FeasibilityResult, Boolean> shopcartColumn = new Column<FeasibilityResult, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(FeasibilityResult object) {
				// Default value = false
				return false;
			}
		};

		/*
		 * Name and size settings
		 */
		feasibility_result_panel_cellTable.addColumn(shopcartColumn, "Shopcart");
		feasibility_result_panel_cellTable.setColumnWidth(shopcartColumn, 40, Unit.PX);

		/*
		 * Button column to choose if you want to hide or show the mesh of the
		 * future acquisition on the map
		 */
		ButtonCell showButton = new ButtonCell();
		feasibility_result_panel_showColumn = new Column<FeasibilityResult, String>(showButton) {
			public String getValue(FeasibilityResult object) {
				if (object.upperLeft == null) {
					return "no footprint";
				} else {
					/*
					 * If the button "hide" is clicked, its value changes to
					 * become "show"
					 */
					return hideButtonClicked ? "Show" : "Hide";
				}

			}
		};

		/*
		 * Name and size settings
		 */
		feasibility_result_panel_cellTable.addColumn(feasibility_result_panel_showColumn, "Show");
		feasibility_result_panel_cellTable.setColumnWidth(feasibility_result_panel_showColumn, 40, Unit.PX);

		/*
		 * Column which informs on the name of the platform's product
		 */
		TextColumn<FeasibilityResult> nameColumn = new TextColumn<FeasibilityResult>() {
			@Override
			public String getValue(FeasibilityResult object) {
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
		feasibility_result_panel_cellTable.addColumn(nameColumn, "Platform");

		/*
		 * Column which informs on the name of the sensor's product
		 */
		TextColumn<FeasibilityResult> sensorColumn = new TextColumn<FeasibilityResult>() {
			@Override
			public String getValue(FeasibilityResult object) {
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
		feasibility_result_panel_cellTable.addColumn(sensorColumn, "Sensor");

		/*
		 * Column which informs on the date of the acquisition
		 */
		TextColumn<FeasibilityResult> dateColumn = new TextColumn<FeasibilityResult>() {
			@Override
			public String getValue(FeasibilityResult object) {
				if (object.acquisitionDate != null) {
					return object.acquisitionDate;
				} else {
					return "not informed";
				}

			}
		};

		/*
		 * Name setting
		 */
		feasibility_result_panel_cellTable.addColumn(dateColumn, "Date");

		/*
		 * Column which informs on the prevision of the percentage of cloud
		 * coverage when the acquisition will be taken
		 */
		TextColumn<FeasibilityResult> cloudColumn = new TextColumn<FeasibilityResult>() {
			@Override
			public String getValue(FeasibilityResult object) {
				if (object.cloudCover != null) {
					return object.cloudCover;
				} else {
					return "not informed";
				}
			}
		};

		/*
		 */
		feasibility_result_panel_cellTable.addColumn(cloudColumn, "Cloud coverage");

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
