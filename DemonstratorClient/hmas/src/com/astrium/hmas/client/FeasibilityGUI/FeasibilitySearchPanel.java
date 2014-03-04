package com.astrium.hmas.client.FeasibilityGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FeasibilitySearchPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the interface of
 *   																	the panel which handles the display of
 *   																	the form after getting the OS description
 *   																	file and the sending of the feasibility
 *   																	tasking request
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

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.astrium.hmas.bean.FeasibilityBean.Parameter;
import com.astrium.hmas.client.FeasibilityService.FeasibilityService;
import com.astrium.hmas.client.FeasibilityService.FeasibilityServiceAsync;
import com.astrium.hmas.client.FeasibilityService.OSFeasibilityService;
import com.astrium.hmas.client.FeasibilityService.OSFeasibilityServiceAsync;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class FeasibilitySearchPanel extends Composite implements HasText {

	private static FeasibilitySearchPanelUiBinder uiBinder = GWT.create(FeasibilitySearchPanelUiBinder.class);
	@UiField
	public AbsolutePanel feasibility_search_panel_absolute_panel;
	@UiField
	public AbsolutePanel feasibility_search_panel_param_panel;
	@UiField
	public TextBox feasibility_search_panel_osurl;
	@UiField
	public Button feasibility_search_panel_send_button;
	/*
	 * Below are all the fields of the search form
	 */
	@UiField
	public DoubleBox feasibility_search_panel_nwlon;
	@UiField
	public DoubleBox feasibility_search_panel_nwlat;
	@UiField
	public DoubleBox feasibility_search_panel_selon;
	@UiField
	public DoubleBox feasibility_search_panel_selat;
	@UiField
	public Button feasibility_search_panel_drawaoiStop;
	@UiField
	public Button feasibility_search_panel_drawaoi;
	@UiField
	public TextBox feasibility_search_panel_datestart;
	@UiField
	public TextBox feasibility_search_panel_dateend;
	@UiField
	public ListBox feasibility_search_panel_platform;
	@UiField
	public ListBox feasibility_search_panel_instrument;
	@UiField
	public ListBox feasibility_search_panel_sensorType;
	@UiField
	public DoubleBox feasibility_search_panel_azimuthMin;
	@UiField
	public DoubleBox feasibility_search_panel_azimuthMax;
	@UiField
	public DoubleBox feasibility_search_panel_elevationMin;
	@UiField
	public DoubleBox feasibility_search_panel_elevationMax;
	@UiField
	public DoubleBox feasibility_search_panel_trackalongMin;
	@UiField
	public DoubleBox feasibility_search_panel_trackalongMax;
	@UiField
	public DoubleBox feasibility_search_panel_trackacrossMin;
	@UiField
	public DoubleBox feasibility_search_panel_trackacrossMax;
	@UiField
	public DoubleBox feasibility_search_panel_minLuminosity;
	@UiField
	public ListBox feasibility_search_panel_compositeType;
	@UiField
	public ListBox feasibility_search_panel_coverageType;
	@UiField
	public DoubleBox feasibility_search_panel_sunglint;
	@UiField
	public ListBox feasibility_search_panel_haze;
	@UiField
	public ListBox feasibility_search_panel_sandwind;
	@UiField
	public DoubleBox feasibility_search_panel_noiseLevel;
	@UiField
	public DoubleBox feasibility_search_panel_ambiguityLevel;
	@UiField
	public DoubleBox feasibility_search_panel_cloud;
	@UiField
	public DoubleBox feasibility_search_panel_snow;
	@UiField
	public Button feasibility_search_panel_start_button;
	@UiField
	public Button feasibility_search_panel_end_button;
	@UiField
	public Button feasibility_search_panel_send_request_button;
	@UiField
	public ListBox feasibility_search_panel_sensorMode;
	@UiField
	public ListBox feasibility_search_panel_resolution;
	@UiField
	public ListBox feasibility_search_panel_polarization;
	@UiField
	public Button feasibility_search_panel_see_description_button;

	/*
	 * OpenSearch service for the feasibility analysis
	 */
	private final OSFeasibilityServiceAsync osFeasibilityService = GWT.create(OSFeasibilityService.class);

	/*
	 * Feasibility analysis service and its getter
	 */
	private final FeasibilityServiceAsync feasibilityService = GWT.create(FeasibilityService.class);

	public FeasibilityServiceAsync getFeasibilityService() {
		return feasibilityService;
	}

	interface FeasibilitySearchPanelUiBinder extends UiBinder<Widget, FeasibilitySearchPanel> {
	}

	/*
	 * FeasibilitySearchPanel constructor
	 */
	public FeasibilitySearchPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		feasibility_search_panel_absolute_panel.getElement().getStyle().setOverflow(Overflow.AUTO);
		feasibility_search_panel_param_panel.getElement().getStyle().setOverflow(Overflow.AUTO);

		feasibility_search_panel_osurl.setValue("http://localhost:8080/HMAS-FAS");
		
		feasibility_search_panel_see_description_button.setVisible(false);

		/*
		 * The form is not visible yet
		 */
		feasibility_search_panel_param_panel.setVisible(false);
		feasibility_search_panel_drawaoiStop.setVisible(false);

		/*
		 * Adding defaults options to the several list box of the form
		 */
		feasibility_search_panel_platform.addItem("Select...");

		feasibility_search_panel_instrument.addItem("Select...");

		feasibility_search_panel_haze.addItem("Select...");

		feasibility_search_panel_sandwind.addItem("Select...");

		feasibility_search_panel_sensorType.addItem("Select...");

		feasibility_search_panel_compositeType.addItem("Select...");

		feasibility_search_panel_coverageType.addItem("Select...");

		feasibility_search_panel_resolution.addItem("Select...");

		feasibility_search_panel_sensorMode.addItem("Select...");

		feasibility_search_panel_polarization.addItem("Select...");

	}

	public FeasibilitySearchPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

	@UiHandler("feasibility_search_panel_send_button")
	/*
	 * Call to the OSFeasibility service to get the list of the available
	 * parameters
	 */
	void onFeasibility_search_panel_send_buttonClick(ClickEvent event) {
		/*
		 * We test if the OpenSearch Url is well-formed
		 */
		final String url = feasibility_search_panel_osurl.getValue();
		UrlValidator urlValidator = new UrlValidator();
		if (!urlValidator.isValidUrl(url, false)) {

			Window.alert("ERROR : Opensearch URL not valid!");

		} else {

			osFeasibilityService.getParameters(url, new AsyncCallback<Map<String, Parameter>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					System.out.println("Fail!");

				}

				@Override
				public void onSuccess(Map<String, Parameter> result) {

					/*
					 * Form building according to the list of the available
					 * parameters (result)
					 */
					feasibility_search_panel_param_panel.setVisible(true);
					
					feasibility_search_panel_see_description_button.setVisible(true);
					
					/*
					 * Click on this button to see the description file
					 */
					feasibility_search_panel_see_description_button.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							Window.open(url + "/hmas/fas/os/description", "description file", url);
							
						}
					});

					/*
					 * If the map doesn't contain the parameter then the field
					 * is disabled. Otherwise, the parameter's key is register
					 * to build the request url later. For the list boxes, the
					 * different options are set thanks to the attribute
					 * "options" of the Parameter object
					 */
					if (!result.containsKey("eo:platform")) {
						feasibility_search_panel_platform.setEnabled(false);
					} else {
						List<String> options = result.get("eo:platform").getOptions();
						for (int i = 0; i < options.size(); i++) {
							feasibility_search_panel_platform.addItem(options.get(i));
						}
						feasibility_search_panel_platform.setName(result.get("eo:platform").getName());
					}
					if (!result.containsKey("eo:sensorType")) {
						feasibility_search_panel_sensorType.setEnabled(false);
					} else {
						List<String> options = result.get("eo:sensorType").getOptions();
						for (int i = 0; i < options.size(); i++) {
							feasibility_search_panel_sensorType.addItem(options.get(i));
						}
						feasibility_search_panel_sensorType.setName(result.get("eo:sensorType").getName());
					}
					if (!result.containsKey("eo:instrument")) {
						feasibility_search_panel_instrument.setEnabled(false);
					} else {
						List<String> options = result.get("eo:instrument").getOptions();
						for (int i = 0; i < options.size(); i++) {
							feasibility_search_panel_instrument.addItem(options.get(i));
						}
						feasibility_search_panel_instrument.setName(result.get("eo:instrument").getName());
					}
					if (!result.containsKey("eo:resolution")) {
						feasibility_search_panel_resolution.setEnabled(false);
					} else {
						List<String> options = result.get("eo:resolution").getOptions();
						for (int i = 0; i < options.size(); i++) {
							feasibility_search_panel_resolution.addItem(options.get(i));
						}
						feasibility_search_panel_resolution.setName(result.get("eo:resolution").getName());
					}
					if (!result.containsKey("geo:box")) {
						feasibility_search_panel_nwlat.setEnabled(false);
						feasibility_search_panel_nwlon.setEnabled(false);
						feasibility_search_panel_selat.setEnabled(false);
						feasibility_search_panel_selon.setEnabled(false);
					} else {
						feasibility_search_panel_nwlat.setName(result.get("geo:box").getName());
					}
					if (!result.containsKey("time:start")) {
						feasibility_search_panel_datestart.setEnabled(false);
					} else {
						feasibility_search_panel_datestart.setName(result.get("time:start").getName());
					}
					if (!result.containsKey("time:end")) {
						feasibility_search_panel_dateend.setEnabled(false);
					} else {
						feasibility_search_panel_dateend.setName(result.get("time:end").getName());
					}
					if (!result.containsKey("time:end")) {
						feasibility_search_panel_dateend.setEnabled(false);
					} else {
						feasibility_search_panel_dateend.setName(result.get("time:end").getName());
					}
					if (!result.containsKey("time:end")) {
						feasibility_search_panel_dateend.setEnabled(false);
					} else {
						feasibility_search_panel_dateend.setName(result.get("time:end").getName());
					}
					if (!result.containsKey("eosp:incidenceAzimuthAngle")) {
						feasibility_search_panel_azimuthMax.setEnabled(false);
						feasibility_search_panel_azimuthMin.setEnabled(false);
					} else {
						feasibility_search_panel_azimuthMax.setName(result.get("eosp:incidenceAzimuthAngle").getName());
						feasibility_search_panel_azimuthMin.setName(result.get("eosp:incidenceAzimuthAngle").getName());
					}
					if (!result.containsKey("eosp:incidenceElevationAngle")) {
						feasibility_search_panel_elevationMax.setEnabled(false);
						feasibility_search_panel_elevationMin.setEnabled(false);
					} else {
						feasibility_search_panel_elevationMax.setName(result.get("eosp:incidenceElevationAngle").getName());
						feasibility_search_panel_elevationMin.setName(result.get("eosp:incidenceElevationAngle").getName());
					}
					if (!result.containsKey("eosp:pointingAlongTrackAngle")) {
						feasibility_search_panel_trackalongMax.setEnabled(false);
						feasibility_search_panel_trackalongMin.setEnabled(false);
					} else {
						feasibility_search_panel_trackalongMax.setName(result.get("eosp:pointingAlongTrackAngle").getName());
						feasibility_search_panel_trackalongMin.setName(result.get("eosp:pointingAlongTrackAngle").getName());
					}
					if (!result.containsKey("eosp:pointingAcrossTrackAngle")) {
						feasibility_search_panel_trackacrossMax.setEnabled(false);
						feasibility_search_panel_trackacrossMin.setEnabled(false);
					} else {
						feasibility_search_panel_trackacrossMax.setName(result.get("eosp:pointingAcrossTrackAngle").getName());
						feasibility_search_panel_trackacrossMin.setName(result.get("eosp:pointingAcrossTrackAngle").getName());
					}
					if (!result.containsKey("eo:sensorMode")) {
						feasibility_search_panel_sensorMode.setEnabled(false);
					} else {
						List<String> options = result.get("eo:sensorMode").getOptions();
						for (int i = 0; i < options.size(); i++) {
							feasibility_search_panel_sensorMode.addItem(options.get(i));
						}
						feasibility_search_panel_sensorMode.setName(result.get("eo:sensorMode").getName());
					}
					if (!result.containsKey("eo:compositeType")) {
						feasibility_search_panel_compositeType.setEnabled(false);
					} else {
						List<String> options = result.get("eo:compositeType").getOptions();
						for (int i = 0; i < options.size(); i++) {
							feasibility_search_panel_compositeType.addItem(options.get(i));
						}
						feasibility_search_panel_compositeType.setName(result.get("eo:compositeType").getName());
					}
					if (!result.containsKey("eosp:minimumLuminosity")) {
						feasibility_search_panel_minLuminosity.setEnabled(false);
					} else {
						feasibility_search_panel_minLuminosity.setName(result.get("eosp:minimumLuminosity").getName());
					}
					if (!result.containsKey("eosp:polarizationMode")) {
						feasibility_search_panel_polarization.setEnabled(false);
					} else {
						List<String> options = result.get("eosp:polarizationMode").getOptions();
						for (int i = 0; i < options.size(); i++) {
							feasibility_search_panel_polarization.addItem(options.get(i));
						}
						feasibility_search_panel_polarization.setName(result.get("eosp:polarizationMode").getName());
					}
					if (!result.containsKey("eo:cloudCover")) {
						feasibility_search_panel_cloud.setEnabled(false);
					} else {
						feasibility_search_panel_cloud.setName(result.get("eo:cloudCover").getName());
					}
					if (!result.containsKey("eo:snowCover")) {
						feasibility_search_panel_snow.setEnabled(false);
					} else {
						feasibility_search_panel_snow.setName(result.get("eo:snowCover").getName());
					}
					if (!result.containsKey("eosp:maxSunGlint")) {
						feasibility_search_panel_sunglint.setEnabled(false);
					} else {
						feasibility_search_panel_sunglint.setName(result.get("eosp:maxSunGlint").getName());
					}
					if (!result.containsKey("eosp:hazeAcepted")) {
						feasibility_search_panel_haze.setEnabled(false);
					} else {
						List<String> options = result.get("eosp:hazeAcepted").getOptions();
						for (int i = 0; i < options.size(); i++) {
							feasibility_search_panel_haze.addItem(options.get(i));
						}
						feasibility_search_panel_haze.setName(result.get("eosp:hazeAcepted").getName());
					}
					if (!result.containsKey("eosp:validationParametersOPTSandWindAccepted")) {
						feasibility_search_panel_sandwind.setEnabled(false);
					} else {
						List<String> options = result.get("eosp:sandWindAccepted").getOptions();
						for (int i = 0; i < options.size(); i++) {
							feasibility_search_panel_sandwind.addItem(options.get(i));
						}
						feasibility_search_panel_sandwind.setName(result.get("eosp:sandWindAccepted").getName());
					}
					if (!result.containsKey("eosp:maxAmbiguityLevel")) {
						feasibility_search_panel_ambiguityLevel.setEnabled(false);
					} else {
						feasibility_search_panel_ambiguityLevel.setName(result.get("eosp:maxAmbiguityLevel").getName());
					}
					if (!result.containsKey("eosp:maxNoiseLevel")) {
						feasibility_search_panel_noiseLevel.setEnabled(false);
					} else {
						feasibility_search_panel_noiseLevel.setName(result.get("eosp:maxNoiseLevel").getName());
					}

				}
			});
		}
	}

	@UiHandler("feasibility_search_panel_start_button")
	/*
	 * Date picker for the future acquisition date settings - start date
	 */
	void onFeasibility_search_panel_start_buttonClick(ClickEvent event) {

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
				 * Set the date format
				 */
				String dateString = DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
				text.setText(dateString);
				feasibility_search_panel_datestart.setValue(dateString);
			}
		});

		/*
		 * Set the default value
		 */
		datePicker.setValue(new Date(), true);
	}

	@UiHandler("feasibility_search_panel_end_button")
	/*
	 * Date picker for the future acquisition date settings - end date
	 */
	void onFeasibility_search_panel_end_buttonClick(ClickEvent event) {

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
				 * Set the date format
				 */
				String dateString = DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
				text.setText(dateString);
				feasibility_search_panel_dateend.setValue(dateString);
			}
		});

		/*
		 * Set the default value
		 */
		datePicker.setValue(new Date(), true);
	}
}
