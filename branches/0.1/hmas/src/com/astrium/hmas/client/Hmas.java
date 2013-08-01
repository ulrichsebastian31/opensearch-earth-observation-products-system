package com.astrium.hmas.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Size;
import org.gwtopenmaps.openlayers.client.event.VectorAfterFeatureModifiedListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureAddedListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureModifiedListener;
import org.gwtopenmaps.openlayers.client.feature.Feature;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.GeometryImpl;
import org.gwtopenmaps.openlayers.client.geometry.LineString;
import org.gwtopenmaps.openlayers.client.geometry.LinearRing;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.Image;
import org.gwtopenmaps.openlayers.client.layer.ImageOptions;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.astrium.hmas.bean.CatalogueResult;
import com.astrium.hmas.bean.CatalogueSearch;
import com.astrium.hmas.bean.SpotResult;
import com.astrium.hmas.bean.SpotScene;
import com.astrium.hmas.bean.SpotSearch;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hmas implements EntryPoint {
	
	public MainPanel mainPanel = new MainPanel();
	public MapPanel mapPanel = new MapPanel();
    
    
    /**
     * This is the entry point method.
     */
	public void onModuleLoad() {
		
		/****************BASE APPLICATION BUILDING***************/
		
		/***********MAIN AND MAP PANELS ADDING***********/
    	mapPanel.buildMap();
    	MapWidget mapWidget = mapPanel.getMapWidget();
    	final org.gwtopenmaps.openlayers.client.Map map = mapWidget.getMap();
        DockPanel dockPanel = new DockPanel();
        dockPanel.add(mainPanel, DockPanel.WEST);
        dockPanel.add(mapWidget, DockPanel.CENTER);
        dockPanel.setWidth("100%");
        dockPanel.setHeight("100%");
        dockPanel.setCellWidth(dockPanel.getWidget(0), "400px");
        dockPanel.setCellHeight(dockPanel.getWidget(1), "900px");
        
        final Projection DEFAULT_PROJECTION = new Projection(
                "EPSG:4326");
        
        
        /****************EVENT HANDLER***************/
        
        /***********CATALOGUE SEARCH PANEL***********/
        mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_draw_aoi_button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				mapPanel.drawRectangleFeatureControl.activate();
				
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_stop_drawing_button.setVisible(true);
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_draw_aoi_button.setVisible(false);

			}
		});
        
        Vector vector = mapPanel.vectorLayer;
        vector.addVectorFeatureAddedListener(new VectorFeatureAddedListener(){

			@Override
			public void onFeatureAdded(FeatureAddedEvent eventObject) {
				// TODO Auto-generated method stub
				mapPanel.drawRectangleFeatureControl.deactivate();
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_draw_aoi_button.setEnabled(false);
				
			}});
		
        vector.addVectorFeatureModifiedListener(new VectorFeatureModifiedListener(){

			@Override
			public void onFeatureModified(
					FeatureModifiedEvent eventObject) {
				// TODO Auto-generated method stub
				Vector vector = mapPanel.vectorLayer;
				Feature[] features = vector.getFeatures();
				Feature feature = features[0];
				Bounds bounds = ((VectorFeature) feature).getGeometry().getBounds();
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlat.setValue(bounds.getLowerLeftY());
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlon.setValue(bounds.getUpperRightX());
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selat.setValue(bounds.getUpperRightY());
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selon.setValue(bounds.getLowerLeftX());
				
			}});
        mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_stop_drawing_button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Vector vector = mapPanel.vectorLayer;
				Feature[] features = vector.getFeatures();
				Feature feature = features[0];
				vector.removeFeature((VectorFeature) feature);
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_draw_aoi_button.setVisible(true);
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_draw_aoi_button.setEnabled(true);
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_stop_drawing_button.setVisible(false);

			}
		});
        
        
		mapPanel.mapWidget.addHandler(new DoubleClickHandler() {

	        @Override
	        public void onDoubleClick(DoubleClickEvent event) {
	        	mapPanel.drawRectangleFeatureControl.deactivate();
	        	mapPanel.drawRectangleFeatureControl.disable();
	        }
	    }, DoubleClickEvent.getType());
		
		mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_send_request_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				CatalogueSearch catalogueSearch = new CatalogueSearch();
				
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_begin.getValue() != null){
					catalogueSearch.setArchivingDate_start(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_begin.getValue());
					catalogueSearch.parameters.put("time:start", "start");
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_end.getValue() != null){
					catalogueSearch.setArchivingDate_stop(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_end.getValue());
					catalogueSearch.parameters.put("time:end", "stop");
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionStation.getValue() != null){
					catalogueSearch.setAcquisitionStation(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionStation.getValue());
					catalogueSearch.parameters.put("eo:acquisitionStation", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionStation.getName());
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionType.getValue() != null){
					catalogueSearch.setAcquisitionType(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionType.getValue());
					catalogueSearch.parameters.put("eo:acquisitionType", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionType.getName());
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_archivingCenter.getValue() != null){
					catalogueSearch.setArchivingCenter(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_archivingCenter.getValue());
					catalogueSearch.parameters.put("eo:archivingCenter", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_archivingCenter.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_cloud.getValue() != null){
					catalogueSearch.setCloudCover(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_cloud.getValue());
					catalogueSearch.parameters.put("eo:cloudCover", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_cloud.getName());
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_compositeType.getValue() != null){
					catalogueSearch.setCompositeType(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_compositeType.getValue());
					catalogueSearch.parameters.put("eo:compositeType", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_compositeType.getName());
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_contents.getValue() != null){
					catalogueSearch.setContents(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_contents.getValue());
					catalogueSearch.parameters.put("eo:contents", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_contents.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_entryType.getValue() != null){
					catalogueSearch.setEntryType(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_entryType.getValue());
					catalogueSearch.parameters.put("eo:type", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_entryType.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame1.getValue() != null){
					catalogueSearch.setFrame_min(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame1.getValue());
					catalogueSearch.parameters.put("eo:frame", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame1.getName());
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame2.getValue() != null){
					catalogueSearch.setFrame_max(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame2.getValue());
					catalogueSearch.parameters.put("eo:frame", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame2.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_identifier.getValue() != null){
					catalogueSearch.setIdentifier(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_identifier.getValue());
					catalogueSearch.parameters.put("eo:identifier", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_identifier.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlat.getValue() != null){
					catalogueSearch.setNwlat(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlat.getValue());
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlon.getValue() != null){
					catalogueSearch.setNwlon(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlon.getValue());
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMax.getValue() != null){
					catalogueSearch.setOrbitNumber_max(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMax.getValue());
					catalogueSearch.parameters.put("eo:orbitNumber", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMax.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMin.getValue() != null){
					catalogueSearch.setOrbitNumber_min(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMin.getValue());
					catalogueSearch.parameters.put("eo:orbitNumber", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMin.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitType.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitType.getSelectedIndex()) != "Select..."){
					catalogueSearch.setOrbitType(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitType.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitType.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:orbitType", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitType.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitDirection1.getValue() != null){
					catalogueSearch.setOrbitDirection("ASCENDING");
					catalogueSearch.parameters.put("eo:orbitDirection", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitDirection1.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitDirection2.getValue() != null){
					catalogueSearch.setOrbitDirection("DESCENDING");
					catalogueSearch.parameters.put("eo:orbitDirection", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitDirection2.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_platform.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_platform.getSelectedIndex()) != "Select..."){
					catalogueSearch.setPlatform(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_platform.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_platform.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:platform", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_platform.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingCenter.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingCenter.getSelectedIndex()) != "Select..."){
					catalogueSearch.setProcessingCenter(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingCenter.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingCenter.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:ProcessingCenter", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingCenter.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate1.getValue() != null){
					catalogueSearch.setProcessingDate_start(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate1.getValue());
					catalogueSearch.parameters.put("eo:ProcessingDate", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate1.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate2.getValue() != null){
					catalogueSearch.setProcessingDate_stop(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate2.getValue());
					catalogueSearch.parameters.put("eo:ProcessingDate", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate2.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingLevel.getValue() != null){
					catalogueSearch.setProcessingLevel(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingLevel.getValue());
					catalogueSearch.parameters.put("eo:ProcessingLevel", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingLevel.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingSoftware.getValue() != null){
					catalogueSearch.setProcessingSoftware(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingSoftware.getValue());
					catalogueSearch.parameters.put("eo:ProcessingSoftware", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingSoftware.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMax.getValue() != null){
					catalogueSearch.setResolution_max(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMax.getValue());
					catalogueSearch.parameters.put("eo:resolution", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMax.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMin.getValue() != null){
					catalogueSearch.setResolution_min(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMin.getValue());
					catalogueSearch.parameters.put("eo:resolution", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMin.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selat.getValue() != null){
					catalogueSearch.setSelat(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selat.getValue());
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selon.getValue() != null){
					catalogueSearch.setSelon(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selon.getValue());
				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensor.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensor.getSelectedIndex()) != "Select..."){
					catalogueSearch.setInstrument(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensor.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensor.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:instrument", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensor.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensorMode.getValue() != null){
					catalogueSearch.setSensorMode(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensorMode.getValue());
					catalogueSearch.parameters.put("eo:sensorMode", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensorMode.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensortype.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensortype.getSelectedIndex()) != "Select..."){
					catalogueSearch.setSensorType(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensortype.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensortype.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:sensorType", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensortype.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_snow.getValue() != null){
					catalogueSearch.setSnowCover(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_snow.getValue());
					catalogueSearch.parameters.put("eo:snowCover", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_snow.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_spectralRange.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_spectralRange.getSelectedIndex()) != "Select..."){
					catalogueSearch.setSpectralRange(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_spectralRange.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_spectralRange.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:spectralRange", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_spectralRange.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_status.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_status.getSelectedIndex()) != "Select..."){
					catalogueSearch.setStatus(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_status.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_status.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:status", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_status.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_swathId.getValue() != null){
					catalogueSearch.setSwathId(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_swathId.getValue());
					catalogueSearch.parameters.put("eo:swathId", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_swathId.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAccross.getValue() != null){
					catalogueSearch.setTrack_across(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAccross.getValue());
					catalogueSearch.parameters.put("eo:track", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAccross.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAlong.getValue() != null){
					catalogueSearch.setTrack_along(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAlong.getValue());
					catalogueSearch.parameters.put("eo:track", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAlong.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght1.getValue() != null){
					catalogueSearch.setWaveLenght_min(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght1.getValue());
					catalogueSearch.parameters.put("eo:wavelength", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght1.getName());

				}
				if(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght2.getValue() != null){
					catalogueSearch.setWaveLenght_max(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght2.getValue());
					catalogueSearch.parameters.put("eo:wavelength", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght2.getName());
				}

				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_send_request_button.setEnabled(false);
				mainPanel.cataloguePanel.catalogueSearchPanel.getCatalogueService().getResults(catalogueSearch, new AsyncCallback<Map<String, CatalogueResult>>(){
					
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("fail!");
					}

					@Override
					public void onSuccess(final Map<String, CatalogueResult> catalogueResult) {
						// TODO Auto-generated method stub
						System.out.println("success");
						//We go on the results Panel and disable the search Panel
						mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().setTabEnabled(1, true);
						mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().setTabEnabled(2, true);
						mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().selectTab(1);
						mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().setTabEnabled(0, false);
						
						//Recovering of the parameters and building of the Search object to send to the server
						
						mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_xml_show_button.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Iterator<String> iterator = catalogueResult.keySet().iterator();
								if(iterator.hasNext()){
									String key = (String) iterator.next();
									CatalogueResult value = (CatalogueResult) catalogueResult.get(key);
									Window.alert(value.getXml());
								}else {
									System.out.println("marche pas");
								}
								
								
							}
							
						});
						
						final ArrayList<CatalogueResult> result_list = new ArrayList<CatalogueResult>();
						
						AsyncDataProvider<CatalogueResult> provider = new AsyncDataProvider<CatalogueResult>() {							 
							 @SuppressWarnings("unchecked")
							@Override
							 protected void onRangeChanged(HasData<CatalogueResult> display) {
							          int start = display.getVisibleRange().getStart();
							          int end = start + display.getVisibleRange().getLength();
							          end = end >= catalogueResult.size() ? catalogueResult.size() : end;
							          Iterator iterator = catalogueResult.keySet().iterator(); 
							          
							          while(iterator.hasNext()){
							        	  
							        	 //add results in a table
							            String key = (String) iterator.next();
							            CatalogueResult value = (CatalogueResult) catalogueResult.get(key);
							            result_list.add(value);
							             
							             //Display footprint on the map
							            Vector polygon_layer = new Vector("polygon_layer" + value.id);
								     		
								     	Point p1 = new Point(value.upperLeft.longitude,value.upperLeft.latitude);
								     	Point p2 = new Point(value.lowerLeft.longitude,value.lowerLeft.latitude);
								     	Point p3 = new Point(value.lowerRight.longitude,value.lowerRight.latitude);
								     	Point p4 = new Point(value.upperRight.longitude,value.upperRight.latitude);
								     		
								     	p1.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
								     	p2.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
								     	p3.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
								     	p4.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
								     		
								     	Point[] points= {p1,p2,p3,p4};

								     	LinearRing linear_ring = new LinearRing(points); 
								     	VectorFeature polygon = new VectorFeature(linear_ring);
								     	polygon_layer.addFeature(polygon);
								     	polygon_layer.setIsVisible(true);
								     	((org.gwtopenmaps.openlayers.client.Map) map).addLayer(polygon_layer);
								     	
								     	
								     	map.setCenter(new LonLat(value.upperLeft.longitude,value.upperLeft.latitude),6);
								     	
								     	  //remove current object
							            iterator.remove(); 
							          }
							          updateRowData(start, result_list);
							 }
						};
						 provider.addDataDisplay(mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable);
						 provider.updateRowCount(result_list.size(), true);
						 
						 mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_showColumn.setFieldUpdater(new FieldUpdater<CatalogueResult, String>() {
							  @Override
							  public void update(int index, CatalogueResult object, String value) {
							    // The user clicked on the button for the passed auction.
								  if (value == "Hide"){
									  Layer vector = map.getLayerByName("polygon_layer" + object.id);
									  vector.setIsVisible(false);
									  mainPanel.cataloguePanel.catalogueResultPanel.hideButtonClicked = true;
									  mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable.redrawRow(index);
									  
								  } else if (value == "Show"){
									  Layer vector = map.getLayerByName("polygon_layer" + object.id);
									  vector.setIsVisible(true);
									  mainPanel.cataloguePanel.catalogueResultPanel.hideButtonClicked = false;
									  mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable.redrawRow(index);
									  
								  }
							  }
							});
						//Details according to the selected scene
						    Handler tableHandler = new SelectionChangeEvent.Handler() 
						    {
						        @Override
						        public void onSelectionChange(SelectionChangeEvent event) 
						        {
						             CatalogueResult selectedScene = mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_selectionModelDetails.getLastSelectedObject();
						             mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_id.setText(selectedScene.id);
						             if(selectedScene.acquisitionStation != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_acquisitionStation.setText(selectedScene.acquisitionStation);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_acquisitionStation.setText("not informed");
						             }
						             if(selectedScene.acquisitionType != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_acquisitionType.setText(selectedScene.acquisitionType);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_acquisitionType.setText("not informed");
						             }
						             if(selectedScene.archivingCenter != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_archivingCenter.setText(selectedScene.archivingCenter);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_archivingCenter.setText("not informed");
						             }
						             if(selectedScene.archivingDate != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_archivingDate.setText(selectedScene.archivingDate);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_archivingDate.setText("not informed");
						             }
						             if(selectedScene.cloudCover != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_cloudCover.setText(selectedScene.cloudCover);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_cloudCover.setText("not informed");
						             }
						             if(selectedScene.compositeType != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_compositeType.setText(selectedScene.compositeType);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_compositeType.setText("not informed");
						             }
						             if(selectedScene.frame != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_frame.setText(selectedScene.frame);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_frame.setText("not informed");
						             }
						             if(selectedScene.identifier != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_identifier.setText(selectedScene.identifier);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_identifier.setText("not informed");
						             }
						             if(selectedScene.instrument != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_instrument.setText(selectedScene.instrument);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_instrument.setText("not informed");
						             }
						             if(selectedScene.orbitDirection != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitDirection.setText(selectedScene.orbitDirection);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitDirection.setText("not informed");
						             }
						             if(selectedScene.orbitNumber != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitNumber.setText(selectedScene.orbitNumber);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitNumber.setText("not informed");
						             }
						             if(selectedScene.orbitType != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitType.setText(selectedScene.orbitType);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitType.setText("not informed");
						             }
						             if(selectedScene.platform != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_platform.setText(selectedScene.platform);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_platform.setText("not informed");
						             }
						             if(selectedScene.processingCenter != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingCenter.setText(selectedScene.processingCenter);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingCenter.setText("not informed");
						             }
						             if(selectedScene.processingDate != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingDate.setText(selectedScene.processingDate);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingDate.setText("not informed");
						             }
						             if(selectedScene.processingLevel != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingLevel.setText(selectedScene.processingLevel);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingLevel.setText("not informed");
						             }
						             if(selectedScene.processingSoftware != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingSoftware.setText(selectedScene.processingSoftware);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingSoftware.setText("not informed");
						             }
						             if(selectedScene.productType != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_entryType.setText(selectedScene.productType);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_entryType.setText("not informed");
						             }
						             if(selectedScene.resolution != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_resolution.setText(selectedScene.resolution);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_resolution.setText("not informed");
						             }
						             if(selectedScene.sensorMode != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_sensorMode.setText(selectedScene.sensorMode);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_sensorMode.setText("not informed");
						             }
						             if(selectedScene.sensorType != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_sensorType.setText(selectedScene.sensorType);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_sensorType.setText("not informed");
						             }
						             if(selectedScene.snowCover != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_snowCover.setText(selectedScene.snowCover);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_snowCover.setText("not informed");
						             }
						             if(selectedScene.spectralRange != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_spectralRange.setText(selectedScene.spectralRange);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_spectralRange.setText("not informed");
						             }
						             if(selectedScene.swathId != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_swathId.setText(selectedScene.swathId);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_swathId.setText("not informed");
						             }
						             if(selectedScene.status != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_status.setText(selectedScene.status);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_status.setText("not informed");
						             }
						             if(selectedScene.track != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_track.setText(selectedScene.track);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_track.setText("not informed");
						             }
						             if(selectedScene.waveLenght != null){
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_waveLenght.setText(selectedScene.waveLenght);
						             }else{
						            	 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_waveLenght.setText("not informed");
						             }
						    
						             
						        }
						    };
						    
						    // Add the handler to the selection model
						    mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_selectionModelDetails.addSelectionChangeHandler( tableHandler );
						    // Add the selection model to the table
						    mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable.setSelectionModel( mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_selectionModelDetails);
						
					}
					
				});

			}
		});
		
		//Recherche dans le catalogue SPOT (bouton submit par défaut invisible -> voir dans CatalogueSearchPanel)
		mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_submit_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				// TODO Auto-generated method stub
				//Recovering of the parameters and building of the Search object to send to the server
				SpotSearch searchSpot = new SpotSearch("w9LEniuV7WlZUP9CgtMr9Je72gS0BwfsleDoNypGIGI:", 
						"json", 
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_begin.getValue(), 
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_end.getValue(), 
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_cloud.getValue(),(double) 30,
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMin.getValue(), 
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMax.getValue(), 
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_entryType.getValue(), 
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlat.getValue(),
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlon.getValue(), 
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selat.getValue(),
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selon.getValue());
				
				//The submit button is not enabled anymore
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_submit_button.setEnabled(false);
				
				//Server action
				mainPanel.cataloguePanel.catalogueSearchPanel.getGreetingService().greetServer(searchSpot,
						new AsyncCallback<SpotResult>() {
							@Override
							public void onSuccess(SpotResult spotResult) {
								// TODO Auto-generated method stub
								System.out.println("success");
								//We go on the results Panel and disable the search Panel
								mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().setTabEnabled(1, true);
								mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().setTabEnabled(2, true);
								mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().selectTab(1);
								mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().setTabEnabled(0, false);
								
								final Map<String,SpotScene> spotScenes = spotResult.getScenes();
								final ArrayList<SpotScene> scene_list = new ArrayList();
								
								//Display results in a table
								 AsyncDataProvider<SpotScene> provider = new AsyncDataProvider<SpotScene>() {							 
								 @SuppressWarnings("unchecked")
								@Override
								 protected void onRangeChanged(HasData<SpotScene> display) {
								          int start = display.getVisibleRange().getStart();
								          int end = start + display.getVisibleRange().getLength();
								          end = end >= spotScenes.size() ? spotScenes.size() : end;
								          Iterator iterator = spotScenes.keySet().iterator(); 
								          
								          while(iterator.hasNext()){
								        	  
								        	 //add results in a table
								            String key = (String) iterator.next();
								            SpotScene value = (SpotScene) spotScenes.get(key);
								            scene_list.add(value);
								             
								             //Display footprint on the map
								           /* Vector polygon_layer = new Vector("polygon_layer" + value.id);
									     		
									     	Point p1 = new Point(value.upper_left.longitude,value.upper_left.latitude);
									     	Point p2 = new Point(value.lower_left.longitude,value.lower_left.latitude);
									     	Point p3 = new Point(value.lower_right.longitude,value.lower_right.latitude);
									     	Point p4 = new Point(value.upper_right.longitude,value.upper_right.latitude);
									     		
									     	p1.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
									     	p2.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
									     	p3.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
									     	p4.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
									     		
									     	Point[] points= {p1,p2,p3,p4};

									     	LinearRing linear_ring = new LinearRing(points); 
									     	VectorFeature polygon = new VectorFeature(linear_ring);
									     	polygon_layer.addFeature(polygon);
									     		
									     	map.addLayer(polygon_layer);*/
												

								             //Display quicklook
								             final Bounds extent = new Bounds(value.upper_left.latitude, value.lower_left.longitude, value.lower_right.latitude, value.upper_right.longitude);
								             LonLat scene_center_lonlat = new LonLat(value.scene_center.longitude, value.scene_center.latitude);
								             LonLat upper_left_lonlat = new LonLat(value.upper_left.longitude, value.upper_left.latitude);
								             LonLat lower_left_lonlat = new LonLat(value.lower_left.longitude, value.lower_left.latitude);
								             LonLat lower_right_lonlat = new LonLat(value.lower_right.longitude, value.lower_right.latitude);
								             LonLat upper_right_lonlat = new LonLat(value.upper_right.longitude, value.upper_right.latitude);
								             extent.extend(scene_center_lonlat);
								             extent.extend(upper_left_lonlat);
								             extent.extend(lower_left_lonlat);
								             extent.extend(lower_right_lonlat);
								             extent.extend(upper_right_lonlat);
								             extent.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
								             ImageOptions imageOptions = new ImageOptions();
								             imageOptions.setNumZoomLevels(18);
								             imageOptions.setLayerOpacity(0.8d);
								             Image imageLayer = new Image(value.id, value.image_url, extent, new Size(50,50), 
								                   imageOptions);
								             imageLayer.setIsBaseLayer(false);
								             
								             map.setCenter(new LonLat(10.3321,49.8959),6);
								         
								             //((org.gwtopenmaps.openlayers.client.Map) map).addLayer(imageLayer);
								             
								             //remove current object
								            iterator.remove(); 

								          }
								          updateRowData(start, scene_list);
								        }
								 };
								//add to the table
								// provider.addDataDisplay(mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable);
								 provider.updateRowCount(scene_list.size(), true);
								 
					             
								//Footprints handler
								 //TODO : ne marche pas super bien....
								 /*mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_showColumn.setFieldUpdater(new FieldUpdater<SpotScene, String>() {
									  @Override
									  public void update(int index, SpotScene object, String value) {
									    // The user clicked on the button for the passed auction.
										  if (value == "Hide"){
											  System.out.println(index);
											  Layer vector = map.getLayerByName("polygon_layer" + object.id);
											  System.out.println("polygon_layer" + object.id);
											  vector.setIsVisible(false);
											  System.out.println(vector.isVisible());
											  mainPanel.cataloguePanel.catalogueResultPanel.hideButtonClicked = true;
											  mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable.redrawRow(index);
											  System.out.println(value);
											  
											  
										  } else if (value == "Show"){
											  System.out.println(index);
											  Layer vector = map.getLayerByName("polygon_layer" + object.id);
											  System.out.println("polygon_layer" + object.id);
											  vector.setIsVisible(true);
											  System.out.println(vector.isVisible());
											  mainPanel.cataloguePanel.catalogueResultPanel.hideButtonClicked = false;
											  mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable.redrawRow(index);
											  System.out.println(value);
											  
										  }
									  }
									});*/
								 
								 	//Details according to the selected scene
								    /*Handler tableHandler = new SelectionChangeEvent.Handler() 
								    {
								        @Override
								        public void onSelectionChange(SelectionChangeEvent event) 
								        {
								            SpotScene selectedScene = mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_selectionModelDetails.getLastSelectedObject();
								            mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_image.setUrl(selectedScene.image_url);
											 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_date.setText(selectedScene.acquisition_date);
											 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_satellite.setText(selectedScene.satellite);
											 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_cloud.setText(String.valueOf(selectedScene.cloud_cover));
											 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_snow.setText(String.valueOf(selectedScene.snow_cover));
											 mainPanel.cataloguePanel.catalogueDetailsPanel.resolution.setText(String.valueOf(selectedScene.resolution));
											 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_shift.setText(selectedScene.shift);
											 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_sunAzimuth.setText(String.valueOf(selectedScene.sun_azimuth));
											 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_sunElevation.setText(String.valueOf(selectedScene.sun_elevation));
											 mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_tquality.setText(selectedScene.tquality);
											 //mainPanel.cataloguePanel.catalogueDetailsPanel.spot_panel_label_center.setText("[" + selectedScene.scene_center.latitude + ";" + selectedScene.scene_center.longitude + "]");
								        }
								    };
								    
								    // Add the handler to the selection model
								    mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_selectionModelDetails.addSelectionChangeHandler( tableHandler );*/
								    // Add the selection model to the table
								    //mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable.setSelectionModel( mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_selectionModelDetails);
	 

							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								System.out.println("fail!");
							}
						});
				
			}});
		
		
		/****************TEST DISPLAY IMAGE***************/

        
		/****************ROOT PANEL CONFIGURATION***************/
        dockPanel.setBorderWidth(1);
        RootPanel rootpanel = RootPanel.get();
        rootpanel.clear();
        rootpanel.add(dockPanel);

        

    }
}    