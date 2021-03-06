package com.astrium.hmas.client.CatalogueGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               CatalogueSearchPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the interface of
 *   																	the panel which handles the display of
 *   																	the form after getting the OS description
 *   																	file and the sending of the catalogue
 *   																	search request
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

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.core.Context;

//import org.apache.commons.validator.UrlValidator;

import com.astrium.hmas.client.CatalogueService.CatalogueService;
import com.astrium.hmas.client.CatalogueService.CatalogueServiceAsync;
import com.astrium.hmas.client.CatalogueService.OpenSearchService;
import com.astrium.hmas.client.CatalogueService.OpenSearchServiceAsync;
import com.astrium.hmas.shared.UrlValidator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.sun.jersey.api.core.HttpContext;

public class CatalogueSearchPanel extends Composite implements HasText {

	private static SearchPanelUiBinder uiBinder = GWT.create(SearchPanelUiBinder.class);
	@UiField
	public AbsolutePanel catalogue_search_param_panel;
	@UiField
	public AbsolutePanel catalogue_search_absolute_panel;
	/*
	 * Below are all the fields of the search form
	 */
	@UiField
	public Button catalogue_search_send_button;
	@UiField
	public Button catalogue_search_datepicker1_button;
	@UiField
	public TextBox catalogue_search_begin;
	@UiField
	public TextBox catalogue_search_end;
	@UiField
	public Button catalogue_search_datepicker2_button;
	@UiField
	public Button catalogue_search_draw_aoi_button;
	@UiField
	public Button catalogue_search_stop_drawing_button;
	@UiField
	public DoubleBox catalogue_search_panel_nwlon;
	@UiField
	public DoubleBox catalogue_search_panel_nwlat;
	@UiField
	public DoubleBox catalogue_search_panel_selon;
	@UiField
	public DoubleBox catalogue_search_panel_selat;
	@UiField
	public ListBox catalogue_search_panel_platform;
	@UiField
	public ListBox catalogue_search_panel_sensor;
	@UiField
	public ListBox catalogue_search_panel_sensortype;
	@UiField
	public DoubleBox catalogue_search_panel_resolutionMin;
	@UiField
	public DoubleBox catalogue_search_panel_resolutionMax;
	@UiField
	public DoubleBox catalogue_search_panel_orbitMin;
	@UiField
	public DoubleBox catalogue_search_panel_orbitMax;
	@UiField
	public DoubleBox catalogue_search_panel_trackAlong;
	@UiField
	public DoubleBox catalogue_search_panel_trackAccross;
	@UiField
	public TextBox catalogue_search_panel_os_textbox;
	@UiField
	public TextBox catalogue_search_panel_sensorMode;
	@UiField
	public TextBox catalogue_search_panel_swathId;
	@UiField
	public DoubleBox catalogue_search_panel_waveLenght1;
	@UiField
	public DoubleBox catalogue_search_panel_waveLenght2;
	@UiField
	public ListBox catalogue_search_panel_spectralRange;
	@UiField
	public DoubleBox catalogue_search_panel_frame1;
	@UiField
	public DoubleBox catalogue_search_panel_frame2;
	@UiField
	public TextBox catalogue_search_panel_identifier;
	@UiField
	public TextBox catalogue_search_panel_acquisitionType;
	@UiField
	public ListBox catalogue_search_panel_processingCenter;
	@UiField
	public TextBox catalogue_search_panel_processingSoftware;
	@UiField
	public TextBox catalogue_search_panel_processingLevel;
	@UiField
	public TextBox catalogue_search_panel_compositeType;
	@UiField
	public TextBox catalogue_search_panel_contents;
	@UiField
	public DoubleBox catalogue_search_panel_cloud;
	@UiField
	public DoubleBox catalogue_search_panel_snow;
	@UiField
	public TextBox catalogue_search_panel_entryType;
	@UiField
	public ListBox catalogue_search_panel_orbitType;
	@UiField
	public TextBox catalogue_search_panel_processingDate1;
	@UiField
	public TextBox catalogue_search_panel_processingDate2;
	@UiField
	public ListBox catalogue_search_panel_orbitDirection;
	@UiField
	public ListBox catalogue_search_panel_status;
	@UiField
	public TextBox catalogue_search_panel_archivingCenter;
	@UiField
	public TextBox catalogue_search_panel_archivingDate;
	@UiField
	public TextBox catalogue_search_panel_acquisitionStation;
	@UiField
	public Button catalogue_search_panel_send_request_button;
	@UiField
	Button catalogue_search_panel_see_description_button;

	/*
	 * OpenSearch service : parses the description file and sends back the
	 * available parameters
	 */
	private final OpenSearchServiceAsync opensearchService = GWT.create(OpenSearchService.class);

	/*
	 * Catalogue service : parses the results file and sends them back in
	 * CatalogueResult objects
	 */
	private final CatalogueServiceAsync catalogueService = GWT.create(CatalogueService.class);

	/*
	 * Catalogue service getter
	 */
	public CatalogueServiceAsync getCatalogueService() {
		return catalogueService;
	}

	interface SearchPanelUiBinder extends UiBinder<Widget, CatalogueSearchPanel> {
	}

	/*
	 * CatalogueSearchPanel constructor
	 */
	public CatalogueSearchPanel() {

		initWidget(uiBinder.createAndBindUi(this));
		catalogue_search_absolute_panel.getElement().getStyle().setOverflow(Overflow.AUTO);
		catalogue_search_param_panel.getElement().getStyle().setOverflow(Overflow.AUTO);

		catalogue_search_panel_see_description_button.setVisible(false);

		/*
		 * The form is not visible yet
		 */
		catalogue_search_param_panel.setVisible(false);
		catalogue_search_stop_drawing_button.setVisible(false);
		/*
		 * The server address is already setting in the OpenSearch URL field
		 */
		catalogue_search_panel_os_textbox.setValue("http://localhost:8080/hmas_server-1.0-SNAPSHOT");

		/*
		 * Adding different options to the several list box of the form
		 */
		catalogue_search_panel_platform.addItem("Select...");
		catalogue_search_panel_platform.addItem("ENVISAT");
		catalogue_search_panel_platform.addItem("Sentinel-1");

		catalogue_search_panel_sensor.addItem("Select...");
		catalogue_search_panel_sensor.addItem("MERIS");
		catalogue_search_panel_sensor.addItem("AATSR");
		catalogue_search_panel_sensor.addItem("ASAR");
		catalogue_search_panel_sensor.addItem("HRVIR");
		catalogue_search_panel_sensor.addItem("SAR");

		catalogue_search_panel_sensortype.addItem("Select...");
		catalogue_search_panel_sensortype.addItem("OPTICAL");
		catalogue_search_panel_sensortype.addItem("RADAR");
		catalogue_search_panel_sensortype.addItem("ALTIMETRIC");
		catalogue_search_panel_sensortype.addItem("ATMOSPHERIC");
		catalogue_search_panel_sensortype.addItem("LIMB");

		catalogue_search_panel_spectralRange.addItem("Select...");
		catalogue_search_panel_spectralRange.addItem("INFRARED");
		catalogue_search_panel_spectralRange.addItem("NEAR-INFRARED");
		catalogue_search_panel_spectralRange.addItem("UV");
		catalogue_search_panel_spectralRange.addItem("VISIBLE");

		catalogue_search_panel_orbitType.addItem("Select...");
		catalogue_search_panel_orbitType.addItem("LEO");
		catalogue_search_panel_orbitType.addItem("MEO");
		catalogue_search_panel_orbitType.addItem("HEO");
		catalogue_search_panel_orbitType.addItem("GEO");
		catalogue_search_panel_orbitType.addItem("GSO");
		catalogue_search_panel_orbitType.addItem("ASO");

		catalogue_search_panel_status.addItem("Select...");
		catalogue_search_panel_status.addItem("ARCHIVED");
		catalogue_search_panel_status.addItem("ACQUIRED");
		catalogue_search_panel_status.addItem("CANCELLED");

		catalogue_search_panel_processingCenter.addItem("Select...");
		catalogue_search_panel_processingCenter.addItem("PDHS-E");
		catalogue_search_panel_processingCenter.addItem("PDHS-K");
		catalogue_search_panel_processingCenter.addItem("DPA");
		catalogue_search_panel_processingCenter.addItem("F-ACRI");

		catalogue_search_panel_orbitDirection.addItem("Select...");
		catalogue_search_panel_orbitDirection.addItem("ASCENDING");
		catalogue_search_panel_orbitDirection.addItem("DESCENDING");
	}

	public CatalogueSearchPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		catalogue_search_param_panel.setVisible(false);
		catalogue_search_stop_drawing_button.setVisible(false);
	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

	@UiHandler("catalogue_search_send_button")
	/*
	 * Call to the OpenSearch service to get the list of the available
	 * parameters
	 */
	void onCatalogue_search_send_buttonClick(ClickEvent event) {
		/*
		 * We test if the OpenSearch Url is well-formed
		 */
		String url = catalogue_search_panel_os_textbox.getValue();
		UrlValidator urlValidator = new UrlValidator();
		if (!urlValidator.isValidUrl(url, false)) {
			Window.alert("ERROR : Opensearch URL not valid!");
		} else {
			opensearchService.getDescriptionFile(url, new AsyncCallback<Map<String, String>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					System.out.println("fail!");

				}

				@Override
				public void onSuccess(final Map<String, String> result) {

					/*
					 * Form building according to the available parameters
					 * (result)
					 */
					catalogue_search_param_panel.setVisible(true);

					catalogue_search_panel_see_description_button.setVisible(true);

					/*
					 * Click on this button to see the description file
					 */
					catalogue_search_panel_see_description_button.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							Window.open(result.get("description"), "description", result.get("description"));

						}
					});

					/*
					 * If the map doesn't contain the parameter then the field
					 * is disabled. Otherwise, the parameter's key is register
					 * to build the request url later
					 */
					if (!result.containsKey("eo:platform")) {
						catalogue_search_panel_platform.setEnabled(false);
					} else {
						catalogue_search_panel_platform.setName(result.get("eo:platform"));
					}
					if (!result.containsKey("eo:orbitType")) {
						catalogue_search_panel_orbitType.setEnabled(false);
					} else {
						catalogue_search_panel_orbitType.setName(result.get("eo:orbitType"));
					}
					if (!result.containsKey("eo:instrument")) {
						catalogue_search_panel_sensor.setEnabled(false);
					} else {
						catalogue_search_panel_sensor.setName(result.get("eo:instrument"));
					}
					if (!result.containsKey("eo:sensorType")) {
						catalogue_search_panel_sensortype.setEnabled(false);
					} else {
						catalogue_search_panel_sensortype.setName(result.get("eo:sensorType"));
					}
					if (!result.containsKey("eo:sensorMode")) {
						catalogue_search_panel_sensorMode.setEnabled(false);
					} else {
						catalogue_search_panel_sensorMode.setName(result.get("eo:sensorMode"));
					}
					if (!result.containsKey("eo:resolution")) {
						catalogue_search_panel_resolutionMax.setEnabled(false);
						catalogue_search_panel_resolutionMin.setEnabled(false);
					} else {
						catalogue_search_panel_resolutionMax.setName(result.get("eo:resolution"));
						catalogue_search_panel_resolutionMin.setName(result.get("eo:resolution"));
					}
					if (!result.containsKey("eo:swathId")) {
						catalogue_search_panel_swathId.setEnabled(false);
					} else {
						catalogue_search_panel_swathId.setName(result.get("eo:swathId"));
					}
					if (!result.containsKey("eo:wavelength")) {
						catalogue_search_panel_waveLenght1.setEnabled(false);
						catalogue_search_panel_waveLenght2.setEnabled(false);
					} else {
						catalogue_search_panel_waveLenght1.setName(result.get("eo:wavelength"));
						catalogue_search_panel_waveLenght2.setName(result.get("eo:wavelength"));
					}
					if (!result.containsKey("eo:spectralRange")) {
						catalogue_search_panel_spectralRange.setEnabled(false);
					} else {
						catalogue_search_panel_spectralRange.setName(result.get("eo:spectralRange"));
					}
					if (!result.containsKey("eo:orbitNumber")) {
						catalogue_search_panel_orbitMax.setEnabled(false);
						catalogue_search_panel_orbitMin.setEnabled(false);
					} else {
						catalogue_search_panel_orbitMax.setName(result.get("eo:orbitNumber"));
						catalogue_search_panel_orbitMin.setName(result.get("eo:orbitNumber"));
					}
					if (!result.containsKey("eo:orbitDirection")) {
						catalogue_search_panel_orbitDirection.setEnabled(false);
					} else {
						catalogue_search_panel_orbitDirection.setName(result.get("eo:orbitDirection"));
					}
					if (!result.containsKey("eo:track")) {
						catalogue_search_panel_trackAccross.setEnabled(false);
						catalogue_search_panel_trackAlong.setEnabled(false);
					} else {
						catalogue_search_panel_trackAccross.setName(result.get("eo:track"));
						catalogue_search_panel_trackAlong.setName(result.get("eo:track"));
					}
					if (!result.containsKey("eo:frame")) {
						catalogue_search_panel_frame1.setEnabled(false);
						catalogue_search_panel_frame2.setEnabled(false);
					} else {
						catalogue_search_panel_frame1.setName(result.get("eo:frame"));
						catalogue_search_panel_frame2.setName(result.get("eo:frame"));
					}
					if (!result.containsKey("eo:identifier")) {
						catalogue_search_panel_identifier.setEnabled(false);
					} else {
						catalogue_search_panel_identifier.setName(result.get("eo:identifier"));
					}
					if (!result.containsKey("eo:type")) {
						catalogue_search_panel_entryType.setEnabled(false);
					} else {
						catalogue_search_panel_entryType.setName(result.get("eo:type"));
					}
					if (!result.containsKey("eo:acquisitionType")) {
						catalogue_search_panel_acquisitionType.setEnabled(false);
					} else {
						catalogue_search_panel_acquisitionType.setName(result.get("eo:acquisitionType"));
					}
					if (!result.containsKey("eo:status")) {
						catalogue_search_panel_status.setEnabled(false);
					} else {
						catalogue_search_panel_status.setName(result.get("eo:status"));
					}
					if (!result.containsKey("eo:archivingCenter")) {
						catalogue_search_panel_archivingCenter.setEnabled(false);
					} else {
						catalogue_search_panel_archivingCenter.setName(result.get("eo:archivingCenter"));
					}
					if (!result.containsKey("eo:acquisitionStation")) {
						catalogue_search_panel_acquisitionStation.setEnabled(false);
					} else {
						catalogue_search_panel_acquisitionStation.setName(result.get("eo:acquisitionStation"));
					}
					if (!result.containsKey("eo:processingCenter")) {
						catalogue_search_panel_processingCenter.setEnabled(false);
					} else {
						catalogue_search_panel_processingCenter.setName(result.get("eo:processingCenter"));
					}
					if (!result.containsKey("eo:processingSoftware")) {
						catalogue_search_panel_processingSoftware.setEnabled(false);
					} else {
						catalogue_search_panel_processingSoftware.setName(result.get("eo:processingSoftware"));
					}
					if (!result.containsKey("eo:processingDate")) {
						catalogue_search_panel_processingDate1.setEnabled(false);
						catalogue_search_panel_processingDate2.setEnabled(false);
					} else {
						catalogue_search_panel_processingDate1.setName(result.get("eo:processingDate"));
						catalogue_search_panel_processingDate2.setName(result.get("eo:processingDate"));
					}
					if (!result.containsKey("eo:processingLevel")) {
						catalogue_search_panel_processingLevel.setEnabled(false);
					} else {
						catalogue_search_panel_processingLevel.setName(result.get("eo:processingLevel"));
					}
					if (!result.containsKey("eo:compositeType")) {
						catalogue_search_panel_compositeType.setEnabled(false);
					} else {
						catalogue_search_panel_compositeType.setName(result.get("eo:compositeType"));
					}
					if (!result.containsKey("eo:contents")) {
						catalogue_search_panel_contents.setEnabled(false);
					} else {
						catalogue_search_panel_contents.setName(result.get("eo:contents"));
					}
					if (!result.containsKey("eo:cloudCover")) {
						catalogue_search_panel_cloud.setEnabled(false);
					} else {
						catalogue_search_panel_cloud.setName(result.get("eo:cloudCover"));
					}
					if (!result.containsKey("eo:snowCover")) {
						catalogue_search_panel_snow.setEnabled(false);
					} else {
						catalogue_search_panel_snow.setName(result.get("eo:snowCover"));
					}

					System.out.println("success!");
				}
			});

		}

	}

	@SuppressWarnings("unused")
	@UiHandler("catalogue_search_datepicker1_button")
	/*
	 * Date picker for the acquisition date settings - start date
	 */
	void onCatalogue_search_datepicker1_buttonClick(ClickEvent event) {

		final PopupPanel simplePopup = new PopupPanel(true);
		simplePopup.ensureDebugId("cwBasicPopup-simplePopup");

		DatePicker datePicker = new DatePicker();
		final Label text = new Label();
		simplePopup.add(datePicker);

		/*
		 * Display the date picker
		 */
		simplePopup.show();

		/*
		 * Set the value in the text box when the user selects a date
		 */
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();
				/*
				 * Set the format
				 */
				String dateString = DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
				text.setText(dateString);
				catalogue_search_begin.setValue(dateString);
			}
		});

		// Set the default value
		datePicker.setValue(new Date(), true);

		// Create a DateBox
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
		// catalogue_search_begin.setFormat(new
		// DateBox.DefaultFormat(dateFormat));
		// catalogue_search_end.setFormat(new
		// DateBox.DefaultFormat(dateFormat));

	}

	@UiHandler("catalogue_search_datepicker2_button")
	/*
	 * Date picker for the acquisition date settings - end date
	 */
	void onCatalogue_search_datepicker2_buttonClick(ClickEvent event) {

		final PopupPanel simplePopup = new PopupPanel(true);
		simplePopup.ensureDebugId("cwBasicPopup-simplePopup");

		DatePicker datePicker = new DatePicker();
		final Label text = new Label();
		simplePopup.add(datePicker);

		/*
		 * Display the date picker
		 */
		simplePopup.show();

		/*
		 * Set the value in the text box when the user selects a date
		 */
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();

				/*
				 * Set the format
				 */
				String dateString = DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
				text.setText(dateString);
				catalogue_search_end.setValue(dateString);
			}
		});

		// Set the default value
		datePicker.setValue(new Date(), true);

		// Create a DateBox
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
		// catalogue_search_begin.setFormat(new
		// DateBox.DefaultFormat(dateFormat));
		// catalogue_search_end.setFormat(new
		// DateBox.DefaultFormat(dateFormat));
	}

}
