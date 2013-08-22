package com.astrium.hmas.client;

import java.util.Date;
import java.util.Map;

import com.astrium.hmas.shared.UrlValidator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
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

	private static FeasibilitySearchPanelUiBinder uiBinder = GWT
			.create(FeasibilitySearchPanelUiBinder.class);
	@UiField AbsolutePanel feasibility_search_panel_absolute_panel;
	@UiField AbsolutePanel feasibility_search_panel_param_panel;
	@UiField TextBox feasibility_search_panel_osurl;
	@UiField Button feasibility_search_panel_send_button;
	@UiField DoubleBox feasibility_search_panel_nwlon;
	@UiField DoubleBox feasibility_search_panel_nwlat;
	@UiField DoubleBox feasibility_search_panel_selon;
	@UiField DoubleBox feasibility_search_panel_selat;
	@UiField Button feasibility_search_panel_drawaoiStop;
	@UiField Button feasibility_search_panel_drawaoi;
	@UiField TextBox feasibility_search_panel_datestart;
	@UiField TextBox feasibility_search_panel_dateend;
	@UiField ListBox feasibility_search_panel_platform;
	@UiField ListBox feasibility_search_panel_instrument;
	@UiField ListBox feasibility_search_panel_sensorType;
	@UiField TextBox feasibility_search_panel_sensorMode;
	@UiField DoubleBox feasibility_search_panel_azimuthMin;
	@UiField DoubleBox feasibility_search_panel_azimuthMax;
	@UiField DoubleBox feasibility_search_panel_elevationMin;
	@UiField DoubleBox feasibility_search_panel_elevationMax;
	@UiField DoubleBox feasibility_search_panel_trackalongMin;
	@UiField DoubleBox feasibility_search_panel_trackalongMax;
	@UiField DoubleBox feasibility_search_panel_trackacrossMin;
	@UiField DoubleBox feasibility_search_panel_trackacrossMax;
	@UiField DoubleBox feasibility_search_panel_minLuminosity;
	@UiField TextBox feasibility_search_panel_polarization;
	@UiField ListBox feasibility_search_panel_compositeType;
	@UiField ListBox feasibility_search_panel_coverageType;
	@UiField DoubleBox feasibility_search_panel_sunglint;
	@UiField ListBox feasibility_search_panel_haze;
	@UiField ListBox feasibility_search_panel_sandwind;
	@UiField DoubleBox feasibility_search_panel_noiseLevel;
	@UiField DoubleBox feasibility_search_panel_ambiguityLevel;
	@UiField DoubleBox feasibility_search_panel_resoMin;
	@UiField DoubleBox feasibility_search_panel_resoMax;
	@UiField DoubleBox feasibility_search_panel_cloud;
	@UiField DoubleBox feasibility_search_panel_snow;
	@UiField Button feasibility_search_panel_start_button;
	@UiField Button feasibility_search_panel_end_button;
	@UiField Button feasibility_search_panel_send_request_button;
	
	private final OpenSearchServiceAsync opensearchService = GWT
			.create(OpenSearchService.class);
	
	private final FeasibilityServiceAsync feasibilityService = GWT
			.create(FeasibilityService.class);
	

	public FeasibilityServiceAsync getFeasibilityService() {
		return feasibilityService;
	}


	interface FeasibilitySearchPanelUiBinder extends
			UiBinder<Widget, FeasibilitySearchPanel> {
	}

	public FeasibilitySearchPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		feasibility_search_panel_absolute_panel.getElement().getStyle().setOverflow(Overflow.AUTO);
		feasibility_search_panel_param_panel.getElement().getStyle().setOverflow(Overflow.AUTO);
		
		feasibility_search_panel_osurl.setValue("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/feas/os");
		
		feasibility_search_panel_param_panel.setVisible(false);
		feasibility_search_panel_drawaoiStop.setVisible(false);
		
		feasibility_search_panel_platform.addItem("Select...");
		feasibility_search_panel_platform.addItem("SPOT6");
		feasibility_search_panel_platform.addItem("SPOT7");
		feasibility_search_panel_platform.addItem("EUMETSAT");
		feasibility_search_panel_platform.addItem("PLEIADE");
		
		feasibility_search_panel_instrument.addItem("Select...");
		feasibility_search_panel_instrument.addItem("MERIS");
		feasibility_search_panel_instrument.addItem("AATSR");
		feasibility_search_panel_instrument.addItem("ASAR");
		feasibility_search_panel_instrument.addItem("HRVIR");
		
		feasibility_search_panel_haze.addItem("Select...");
		feasibility_search_panel_haze.addItem("true");
		feasibility_search_panel_haze.addItem("false");
		
		feasibility_search_panel_sandwind.addItem("Select...");
		feasibility_search_panel_sandwind.addItem("true");
		feasibility_search_panel_sandwind.addItem("false");
		
		feasibility_search_panel_sensorType.addItem("Select...");
		feasibility_search_panel_sensorType.addItem("OPT");
		feasibility_search_panel_sensorType.addItem("SAR");
		
		feasibility_search_panel_compositeType.addItem("Select...");
		feasibility_search_panel_compositeType.addItem("true");
		feasibility_search_panel_compositeType.addItem("false");
		
		feasibility_search_panel_coverageType.addItem("Select...");
		feasibility_search_panel_coverageType.addItem("SINGLE_SWATH");
		feasibility_search_panel_coverageType.addItem("MONOPASS");
		feasibility_search_panel_coverageType.addItem("MULTIPASS");
		
		
		
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
	void onFeasibility_search_panel_send_buttonClick(ClickEvent event) {
		String url = feasibility_search_panel_osurl.getValue();
		UrlValidator urlValidator = new UrlValidator();
		if(!urlValidator.isValidUrl(url, false)){
			Window.alert("ERROR : Opensearch URL not valid!");
		} else {
			opensearchService.getDescriptionFile(url, new AsyncCallback<Map<String, String>>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					System.out.println("fail!");
					
				}
				
				@Override
				public void onSuccess(Map<String, String> result) {

					feasibility_search_panel_param_panel.setVisible(true);
					if(!result.containsKey("eo:platform")){
						feasibility_search_panel_platform.setEnabled(false);
					}else{feasibility_search_panel_platform.setName(result.get("eo:platform"));}
					if(!result.containsKey("eo:sensorType")){
						feasibility_search_panel_sensorType.setEnabled(false);
					}else{feasibility_search_panel_sensorType.setName(result.get("eo:sensorType"));}
					if(!result.containsKey("eo:instrument")){
						feasibility_search_panel_instrument.setEnabled(false);
					}else{feasibility_search_panel_instrument.setName(result.get("eo:instrument"));}
					if(!result.containsKey("eo:resolution")){
						feasibility_search_panel_resoMax.setEnabled(false);
						feasibility_search_panel_resoMin.setEnabled(false);
					}else{feasibility_search_panel_resoMin.setName(result.get("eo:resolution"));
					feasibility_search_panel_resoMax.setName(result.get("eo:resolution"));}
					if(!result.containsKey("geo:box")){
						feasibility_search_panel_nwlat.setEnabled(false);
						feasibility_search_panel_nwlon.setEnabled(false);
						feasibility_search_panel_selat.setEnabled(false);
						feasibility_search_panel_selon.setEnabled(false);
					}else{feasibility_search_panel_nwlat.setName(result.get("geo:box"));}
					if(!result.containsKey("time:start")){
						feasibility_search_panel_datestart.setEnabled(false);
					}else{feasibility_search_panel_datestart.setName(result.get("time:start"));}
					if(!result.containsKey("time:end")){
						feasibility_search_panel_dateend.setEnabled(false);
					}else{feasibility_search_panel_dateend.setName(result.get("time:end"));}
					if(!result.containsKey("time:end")){
						feasibility_search_panel_dateend.setEnabled(false);
					}else{feasibility_search_panel_dateend.setName(result.get("time:end"));}
					if(!result.containsKey("time:end")){
						feasibility_search_panel_dateend.setEnabled(false);
					}else{feasibility_search_panel_dateend.setName(result.get("time:end"));}
					if(!result.containsKey("eosp:acquisitionAngleIncidenceAzimuth")){
						feasibility_search_panel_azimuthMax.setEnabled(false);
						feasibility_search_panel_azimuthMin.setEnabled(false);
					}else{feasibility_search_panel_azimuthMax.setName(result.get("eosp:acquisitionAngleIncidenceAzimuth"));
					feasibility_search_panel_azimuthMin.setName(result.get("eosp:acquisitionAngleIncidenceAzimuth"));}
					if(!result.containsKey("eosp:acquisitionAngleIncidenceElevation")){
						feasibility_search_panel_elevationMax.setEnabled(false);
						feasibility_search_panel_elevationMin.setEnabled(false);
					}else{feasibility_search_panel_elevationMax.setName(result.get("eosp:acquisitionAngleIncidenceElevation"));
					feasibility_search_panel_elevationMin.setName(result.get("eosp:acquisitionAngleIncidenceElevation"));}
					if(!result.containsKey("eosp:acquisitionAnglePointingRangeAlongTrack")){
						feasibility_search_panel_trackalongMax.setEnabled(false);
						feasibility_search_panel_trackalongMin.setEnabled(false);
					}else{feasibility_search_panel_trackalongMax.setName(result.get("eosp:acquisitionAnglePointingRangeAlongTrack"));
					feasibility_search_panel_trackalongMin.setName(result.get("eosp:acquisitionAnglePointingRangeAlongTrack"));}
					if(!result.containsKey("eosp:acquisitionAnglePointingRangeAcrossTrack")){
						feasibility_search_panel_trackacrossMax.setEnabled(false);
						feasibility_search_panel_trackacrossMin.setEnabled(false);
					}else{feasibility_search_panel_trackacrossMax.setName(result.get("eosp:acquisitionAnglePointingRangeAcrossTrack"));
					feasibility_search_panel_trackacrossMin.setName(result.get("eosp:acquisitionAnglePointingRangeAcrossTrack"));}
					if(!result.containsKey("eo:sensorMode")){
						feasibility_search_panel_sensorMode.setEnabled(false);
					}else{feasibility_search_panel_sensorMode.setName(result.get("eo:sensorMode"));}
					if(!result.containsKey("eo:compositeType")){
						feasibility_search_panel_compositeType.setEnabled(false);
					}else{feasibility_search_panel_compositeType.setName(result.get("eo:compositeType"));}
					if(!result.containsKey("eosp:acquisitionParametersOPTMinimumLuminosity")){
						feasibility_search_panel_minLuminosity.setEnabled(false);
					}else{feasibility_search_panel_minLuminosity.setName(result.get("eosp:acquisitionParametersOPTMinimumLuminosity"));}
					if(!result.containsKey("eosp:acquisitionParametersSARPolarizationMode")){
						feasibility_search_panel_polarization.setEnabled(false);
					}else{feasibility_search_panel_polarization.setName(result.get("eosp:acquisitionParametersSARPolarizationMode"));}
					if(!result.containsKey("eo:cloudCover")){
						feasibility_search_panel_cloud.setEnabled(false);
					}else{feasibility_search_panel_cloud.setName(result.get("eo:cloudCover"));}
					if(!result.containsKey("eo:snowCover")){
						feasibility_search_panel_snow.setEnabled(false);
					}else{feasibility_search_panel_snow.setName(result.get("eo:snowCover"));}
					if(!result.containsKey("eosp:validationParametersOPTmaxSunGlint")){
						feasibility_search_panel_sunglint.setEnabled(false);
					}else{feasibility_search_panel_sunglint.setName(result.get("eosp:validationParametersOPTmaxSunGlint"));}
					if(!result.containsKey("eosp:validationParametersOPTHazeAcepted")){
						feasibility_search_panel_haze.setEnabled(false);
					}else{feasibility_search_panel_haze.setName(result.get("eosp:validationParametersOPTHazeAccepted"));}
					if(!result.containsKey("eosp:validationParametersOPTSandWindAccepted")){
						feasibility_search_panel_sandwind.setEnabled(false);
					}else{feasibility_search_panel_sandwind.setName(result.get("eosp:validationParametersOPTSandWindAccepted"));}
					if(!result.containsKey("eosp:validationParametersSARMaxAmbiguityLevel")){
						feasibility_search_panel_ambiguityLevel.setEnabled(false);
					}else{feasibility_search_panel_ambiguityLevel.setName(result.get("eosp:validationParametersSARMaxAmbiguityLevel"));}
					if(!result.containsKey("eosp:validationParametersSARMaximumNoiseLevel")){
						feasibility_search_panel_noiseLevel.setEnabled(false);
					}else{feasibility_search_panel_noiseLevel.setName(result.get("eosp:validationParametersSARMaximumNoiseLevel"));}
					
				}
			});
		}
	}
	@UiHandler("feasibility_search_panel_start_button")
	void onFeasibility_search_panel_start_buttonClick(ClickEvent event) {
		// Create a basic date picker
	      // Create a basic popup widget
	      final PopupPanel simplePopup = new PopupPanel(true);
	      simplePopup.ensureDebugId("cwBasicPopup-simplePopup");

	      DatePicker datePicker = new DatePicker();
	      final Label text = new Label();
	      simplePopup.add(datePicker);
	      
	      simplePopup.show();
	   // Set the value in the text box when the user selects a date
	      datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
	         @Override
	         public void onValueChange(ValueChangeEvent<Date> event) {
	            Date date = event.getValue();
	            String dateString = 
	            DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
	            text.setText(dateString);
	            feasibility_search_panel_datestart.setValue(dateString);
	         }
	      });
	      
	      // Set the default value
	      datePicker.setValue(new Date(), true);
	}
	
	
	@UiHandler("feasibility_search_panel_end_button")
	void onFeasibility_search_panel_end_buttonClick(ClickEvent event) {
		// Create a basic date picker
	      // Create a basic popup widget
	      final PopupPanel simplePopup = new PopupPanel(true);
	      simplePopup.ensureDebugId("cwBasicPopup-simplePopup");

	      DatePicker datePicker = new DatePicker();
	      final Label text = new Label();
	      simplePopup.add(datePicker);
	      
	      simplePopup.show();
	   // Set the value in the text box when the user selects a date
	      datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
	         @Override
	         public void onValueChange(ValueChangeEvent<Date> event) {
	            Date date = event.getValue();
	            String dateString = 
	            DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
	            text.setText(dateString);
	            feasibility_search_panel_dateend.setValue(dateString);
	         }
	      });
	      
	      // Set the default value
	      datePicker.setValue(new Date(), true);
	}
}
