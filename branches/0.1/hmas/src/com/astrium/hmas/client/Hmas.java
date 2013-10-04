package com.astrium.hmas.client;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               Hmas.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Entry point : main application class
 *   																	where we do the most important pro-
 *   																	-cessing, listeners and handlers
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

import java.awt.Checkbox;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureAddedListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureModifiedListener;
import org.gwtopenmaps.openlayers.client.feature.Feature;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.LinearRing;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.astrium.hmas.bean.CatalogueBean.CatalogueResult;
import com.astrium.hmas.bean.CatalogueBean.CatalogueSearch;
import com.astrium.hmas.bean.DownloadBean.DownloadProduct;
import com.astrium.hmas.bean.FeasibilityBean.FeasibilityResult;
import com.astrium.hmas.bean.FeasibilityBean.FeasibilitySearch;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hmas implements EntryPoint {

	/*
	 * The GUI is divided in 2 parts : the map (MapPanel) and the main panel
	 * which includes all the features
	 */
	public MainPanel mainPanel = new MainPanel();
	public MapPanel mapPanel = new MapPanel();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		/************************************************ BASE APPLICATION BUILDING ***************************************************/

		/*********** MAIN AND MAP PANELS ADDING ***********/
		/*
		 * We build the map
		 */
		mapPanel.buildMap();
		MapWidget mapWidget = mapPanel.getMapWidget();
		final org.gwtopenmaps.openlayers.client.Map map = mapWidget.getMap();
		/*
		 * We need a "dockPanel" in GWT interface to place the different parts
		 * of the GUI
		 */
		DockPanel dockPanel = new DockPanel();
		/*
		 * MainPanel is placed on the left
		 */
		dockPanel.add(mainPanel, DockPanel.WEST);
		/*
		 * The map panel is placed on the center
		 */
		dockPanel.add(mapWidget, DockPanel.CENTER);
		/*
		 * dockPanel settings
		 */
		dockPanel.setWidth("100%");
		dockPanel.setHeight("100%");
		dockPanel.setCellWidth(dockPanel.getWidget(0), "400px");
		dockPanel.setCellHeight(dockPanel.getWidget(1), "900px");

		/*
		 * Defaults projection of the map : SRID 4326
		 */
		final Projection DEFAULT_PROJECTION = new Projection("EPSG:4326");

		/************************************************ EVENT HANDLER ***************************************************/

		/*********** CATALOGUE SEARCH PANEL ***********/

		/*
		 * Draw an AOI : click on the draw AOI button
		 */
		mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_draw_aoi_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				/*
				 * activation of the drawfeature control for the catalogue
				 */
				mapPanel.drawRectangleFeatureControl.activate();
				/*
				 * activation of the modify feature control handling the vector
				 * features drawn thanks to the control above
				 */
				mapPanel.mod.activate();
				/*
				 * the drawing button become invisible and the stop drawing
				 * become visible
				 */
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_stop_drawing_button.setVisible(true);
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_draw_aoi_button.setVisible(false);

			}
		});

		/*
		 * Get the vector handling the AOI and add a "vector feature added"
		 * listener on it
		 */
		Vector vector = mapPanel.vectorLayer;
		vector.addVectorFeatureAddedListener(new VectorFeatureAddedListener() {

			@Override
			public void onFeatureAdded(FeatureAddedEvent eventObject) {
				// TODO Auto-generated method stub
				/*
				 * Once a feature is added, the draw feature control is
				 * deactivated
				 */
				mapPanel.drawRectangleFeatureControl.deactivate();
				/*
				 * the draw aoi button is not enabled anymore
				 */
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_draw_aoi_button.setEnabled(false);
				Vector vector = mapPanel.vectorLayer;
				/*
				 * Get all the features of the vector and take the first
				 */
				Feature[] features = vector.getFeatures();
				Feature feature = features[0];
				/*
				 * We need the bounds of the vector drawn to register them in
				 * the form
				 */
				Bounds bounds = ((VectorFeature) feature).getGeometry().getBounds();
				/*
				 * If we are using the WMS as base map, the project is not the
				 * same than Google Map
				 */
				if (map.getBaseLayer().getName().equals("Global Imagery")) {
					/*
					 * Put the bounds value in the AOI fields in the form
					 */
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlat.setValue(bounds.getUpperRightY());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlon.setValue(bounds.getLowerLeftX());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selat.setValue(bounds.getLowerLeftY());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selon.setValue(bounds.getUpperRightX());

				} else {
					/*
					 * Same thing but we need to transform the projection to fit
					 * with Google Map
					 */
					LonLat nw = new LonLat(bounds.getLowerLeftX(), bounds.getUpperRightY());
					LonLat se = new LonLat(bounds.getUpperRightX(), bounds.getLowerLeftY());
					/*
					 * Projection transformation
					 */
					nw.transform("EPSG:900913", "EPSG:4326");
					se.transform("EPSG:900913", "EPSG:4326");
					/*
					 * Put the bounds value in the AOI fields in the form
					 */
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlat.setValue(nw.lat());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlon.setValue(nw.lon());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selat.setValue(se.lat());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selon.setValue(se.lon());
				}

			}
		});

		/*
		 * Now add to the vector a "feature modified" listener
		 */
		vector.addVectorFeatureModifiedListener(new VectorFeatureModifiedListener() {

			@Override
			public void onFeatureModified(FeatureModifiedEvent eventObject) {
				// TODO Auto-generated method stub

				Vector vector = mapPanel.vectorLayer;
				/*
				 * Get all the features of the vector and take the first
				 */
				Feature[] features = vector.getFeatures();
				Feature feature = features[0];
				/*
				 * We need the bounds of the vector drawn to register them in
				 * the form
				 */
				Bounds bounds = ((VectorFeature) feature).getGeometry().getBounds();
				/*
				 * If we are using the WMS as base map, the project is not the
				 * same than Google Map
				 */
				if (map.getBaseLayer().getName().equals("Global Imagery")) {

					LonLat nw = new LonLat(bounds.getLowerLeftX(), bounds.getUpperRightY());
					LonLat se = new LonLat(bounds.getUpperRightX(), bounds.getLowerLeftY());
					/*
					 * Put the bounds value in the AOI fields in the form
					 */
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlat.setValue(nw.lat());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlon.setValue(nw.lon());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selat.setValue(se.lat());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selon.setValue(se.lon());

				} else {
					/*
					 * Same thing but we need to transform the projection to fit
					 * with Google Map
					 */
					LonLat nw = new LonLat(bounds.getLowerLeftX(), bounds.getUpperRightY());
					LonLat se = new LonLat(bounds.getUpperRightX(), bounds.getLowerLeftY());
					/*
					 * Projection transformation
					 */
					nw.transform("EPSG:900913", "EPSG:4326");
					se.transform("EPSG:900913", "EPSG:4326");
					/*
					 * Put the bounds value in the AOI fields in the form
					 */
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlat.setValue(nw.lat());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlon.setValue(nw.lon());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selat.setValue(se.lat());
					mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selon.setValue(se.lon());
				}

			}
		});

		/*
		 * Add a click handler on the stop drawing button : the feature
		 * disappears
		 */
		mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_stop_drawing_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Vector vector = mapPanel.vectorLayer;
				/*
				 * Get all the features of the vector and take the first
				 */
				Feature[] features = vector.getFeatures();
				Feature feature = features[0];
				/*
				 * Remove the feature so it cannot appear on the map anymore
				 */
				vector.removeFeature((VectorFeature) feature);
				/*
				 * The button draw AOI is enabled and visible again
				 */
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_draw_aoi_button.setVisible(true);
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_draw_aoi_button.setEnabled(true);
				mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_stop_drawing_button.setVisible(false);

			}
		});

		/*
		 * ******* Submit the catalogue search request ********
		 */
		mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_send_request_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				/*
				 * New search : reset the table
				 */
				mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable.setRowCount(0);
				/*
				 * TODO remove old meshs when new search
				 */
				// mapPanel.buildMap();

				/*
				 * Search object building
				 */
				CatalogueSearch catalogueSearch = new CatalogueSearch();

				/*
				 * If the field in the request form is not empty so we set the
				 * attribute to the CatalogueSearch object Moreover,
				 * registration of the parameter key for the request url in the
				 * map attribute "parameter" of the CatalogueSearch object
				 */
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_begin.getValue().length() > 0) {

					String start = mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_begin.getValue();
					catalogueSearch.setStart(start);
					catalogueSearch.parameters.put("time:start", "start");

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_end.getValue().length() > 0) {

					String stop = mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_end.getValue();
					catalogueSearch.parameters.put("time:end", "stop");
					catalogueSearch.setStop(stop);

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionStation.getValue().length() > 0) {

					catalogueSearch.setAcquisitionStation(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionStation.getValue());
					catalogueSearch.parameters.put("eo:acquisitionStation",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionStation.getName());

				} else {
					// empty
				}

				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionType.getValue().length() > 0) {

					catalogueSearch.setAcquisitionType(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionType.getValue());
					catalogueSearch.parameters.put("eo:acquisitionType",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_acquisitionType.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_archivingCenter.getValue().length() > 0) {

					catalogueSearch.setArchivingCenter(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_archivingCenter.getValue());
					catalogueSearch.parameters.put("eo:archivingCenter",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_archivingCenter.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_cloud.getValue() != null) {

					catalogueSearch.setCloudCover(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_cloud.getValue());
					catalogueSearch.parameters.put("eo:cloudCover", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_cloud.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_compositeType.getValue().length() > 0) {

					catalogueSearch.setCompositeType(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_compositeType.getValue());
					catalogueSearch.parameters.put("eo:compositeType",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_compositeType.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_contents.getValue().length() > 0) {

					catalogueSearch.setContents(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_contents.getValue());
					catalogueSearch.parameters.put("eo:contents", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_contents.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_entryType.getValue().length() > 0) {

					catalogueSearch.setEntryType(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_entryType.getValue());
					catalogueSearch.parameters.put("eo:type", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_entryType.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame1.getValue() != null) {

					catalogueSearch.setFrame_min(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame1.getValue());
					catalogueSearch.parameters.put("eo:frame", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame1.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame2.getValue() != null) {

					catalogueSearch.setFrame_max(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame2.getValue());
					catalogueSearch.parameters.put("eo:frame", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_frame2.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_identifier.getValue().length() > 0) {

					catalogueSearch.setIdentifier(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_identifier.getValue());
					catalogueSearch.parameters.put("eo:identifier", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_identifier.getName());

				} else {
					// empty
				}

				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMax.getValue() != null) {

					catalogueSearch.setOrbitNumber_max(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMax.getValue());
					catalogueSearch.parameters.put("eo:orbitNumber", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMax.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMin.getValue() != null) {

					catalogueSearch.setOrbitNumber_min(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMin.getValue());
					catalogueSearch.parameters.put("eo:orbitNumber", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitMin.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitType.getValue(
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitType.getSelectedIndex()).equals("Select...")) {
					// empty

				} else {
					catalogueSearch.setOrbitType(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitType
							.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitType.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:orbitType", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitType.getName());
				}
				if (!mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitDirection.getValue(
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitDirection.getSelectedIndex()).equals("Select...")) {

					catalogueSearch.setOrbitDirection(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitDirection
							.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitDirection.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:orbitDirection",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_orbitDirection.getName());

				} else {
					// empty
				}

				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_platform.getValue(
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_platform.getSelectedIndex()).equals("Select...")) {
					// empty
				} else {

					catalogueSearch.setPlatform(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_platform
							.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_platform.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:platform", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_platform.getName());

				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingCenter.getValue(
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingCenter.getSelectedIndex()).equals("Select...")) {
					// empty
				} else {

					catalogueSearch.setProcessingCenter(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingCenter
							.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingCenter.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:ProcessingCenter",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingCenter.getName());

				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate1.getValue().length() > 0) {

					catalogueSearch.setProcessingDate_start(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate1.getValue());
					catalogueSearch.parameters.put("eo:ProcessingDate",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate1.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate2.getValue().length() > 0) {

					catalogueSearch.setProcessingDate_stop(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate2.getValue());
					catalogueSearch.parameters.put("eo:ProcessingDate",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingDate2.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingLevel.getValue().length() > 0) {

					catalogueSearch.setProcessingLevel(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingLevel.getValue());
					catalogueSearch.parameters.put("eo:ProcessingLevel",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingLevel.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingSoftware.getValue().length() > 0) {

					catalogueSearch.setProcessingSoftware(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingSoftware.getValue());
					catalogueSearch.parameters.put("eo:ProcessingSoftware",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_processingSoftware.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMax.getValue() != null) {

					catalogueSearch.setResolution_max(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMax.getValue());
					catalogueSearch.parameters.put("eo:resolution",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMax.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMin.getValue() != null) {

					catalogueSearch.setResolution_min(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMin.getValue());
					catalogueSearch.parameters.put("eo:resolution",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_resolutionMin.getName());

				} else {
					// empty
				}

				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensor.getValue(
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensor.getSelectedIndex()).equals("Select...")) {
					// empty
				} else {

					catalogueSearch.setInstrument(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensor
							.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensor.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:instrument", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensor.getName());

				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensorMode.getValue().length() > 0) {

					catalogueSearch.setSensorMode(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensorMode.getValue());
					catalogueSearch.parameters.put("eo:sensorMode", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensorMode.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensortype.getValue(
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensortype.getSelectedIndex()).equals("Select...")) {
					// empty
				} else {

					catalogueSearch.setSensorType(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensortype
							.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensortype.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:sensorType", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_sensortype.getName());

				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_snow.getValue() != null) {

					catalogueSearch.setSnowCover(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_snow.getValue());
					catalogueSearch.parameters.put("eo:snowCover", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_snow.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_spectralRange.getValue(
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_spectralRange.getSelectedIndex()).equals("Select...")) {
					// empty
				} else {

					catalogueSearch.setSpectralRange(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_spectralRange
							.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_spectralRange.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:spectralRange",
							mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_spectralRange.getName());

				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_status.getValue(
						mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_status.getSelectedIndex()).equals("Select...")) {
					// empty
				} else {

					catalogueSearch.setStatus(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_status
							.getValue(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_status.getSelectedIndex()));
					catalogueSearch.parameters.put("eo:status", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_status.getName());

				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_swathId.getValue().length() > 0) {

					catalogueSearch.setSwathId(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_swathId.getValue());
					catalogueSearch.parameters.put("eo:swathId", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_swathId.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAccross.getValue() != null) {

					catalogueSearch.setTrack_across(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAccross.getValue());
					catalogueSearch.parameters.put("eo:track", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAccross.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAlong.getValue() != null) {

					catalogueSearch.setTrack_along(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAlong.getValue());
					catalogueSearch.parameters.put("eo:track", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_trackAlong.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght1.getValue() != null) {

					catalogueSearch.setWaveLenght_min(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght1.getValue());
					catalogueSearch.parameters.put("eo:wavelength", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght1.getName());

				} else {
					// empty
				}
				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght2.getValue() != null) {

					catalogueSearch.setWaveLenght_max(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght2.getValue());
					catalogueSearch.parameters.put("eo:wavelength", mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_waveLenght2.getName());

				} else {
					// empty
				}

				if (mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlat.getValue() == null
						|| mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlon.getValue() == null
						|| mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selat.getValue() == null
						|| mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selon.getValue() == null) {
					/*
					 * The AOI has to be filled
					 */
					Window.alert("You have to fill an AOI");
				} else {
					/*
					 * AOI setting
					 */
					catalogueSearch.setNwlat(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlat.getValue());
					catalogueSearch.setNwlon(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_nwlon.getValue());
					catalogueSearch.setSelat(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selat.getValue());
					catalogueSearch.setSelon(mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_selon.getValue());

					// mainPanel.cataloguePanel.catalogueSearchPanel.catalogue_search_panel_send_request_button
					// .setEnabled(false);

					/*
					 * Catalogue service call -> get the results of the
					 * Catalogue Search
					 */
					mainPanel.cataloguePanel.catalogueSearchPanel.getCatalogueService().getResults(catalogueSearch,
							new AsyncCallback<Map<String, CatalogueResult>>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method
									// stub
									System.out.println("fail Catalogue Search!");
								}

								@Override
								public void onSuccess(final Map<String, CatalogueResult> catalogueResult) {
									// TODO Auto-generated method stub
									System.out.println("success");

									/*
									 * We go on the results Panel and disable
									 * the search Panel
									 */

									mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().setTabEnabled(1, true);
									mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().setTabEnabled(2, true);
									/*
									 * Select the result tab
									 */
									mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().selectTab(1);
									mainPanel.cataloguePanel.catalogue_panel_tab.getTabBar().setTabEnabled(0, false);

									/*
									 * We can get the XMl returned from the
									 * server by clicking on the "See XML file"
									 * button
									 */

									if (catalogueResult.get("0") != null) {
										/*
										 * We have to have at least one result
										 * in the response
										 */
										mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_xml_show_button.setEnabled(true);
										final String xml = catalogueResult.get("0").getXml();

										mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_xml_show_button
												.addClickHandler(new ClickHandler() {

													@Override
													public void onClick(ClickEvent event) {
														// TODO
														// Auto-generated
														// method stub
														/*
														 * Show XML file in an
														 * other window
														 */

														Window.open(xml, "show xml", xml);
													}

												});

										mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_see_request_button
												.addClickHandler(new ClickHandler() {

													@Override
													public void onClick(ClickEvent event) {
														// TODO
														// Auto-generated
														// method stub
														/*
														 * Show search request
														 * url in an other
														 * window
														 */

														Window.alert(xml);
													}

												});

									} else {
										/*
										 * There is no result
										 */
										Window.alert("no result found corresponding to your request");
										mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_xml_show_button.setEnabled(false);
									}

									/*
									 * Add the selected product to the shopcart
									 */
									mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_add_to_shopcart.addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {

											/*
											 * ********************* Call to the
											 * shopcart service
											 * **********************
											 */
											mainPanel.cataloguePanel.catalogueResultPanel.getShopcartService().getShopcart(
													mainPanel.cataloguePanel.catalogueResultPanel.selected, new AsyncCallback<Map<String, DownloadProduct>>() {

														@Override
														public void onFailure(Throwable caught) {

															System.out.println("Fail!");

														}

														@Override
														public void onSuccess(final Map<String, DownloadProduct> shop_result) {

															System.out.println("success!");
															/*
															 * Select the
															 * shopcart tab
															 */
															mainPanel.mainTab.getTabBar().selectTab(2);
															mainPanel.shopcartPanel.shopcart_panel_tab.getTabBar().selectTab(0);
															/*
															 * List of the
															 * products -
															 * DownloadProduct -
															 * to place in the
															 * shopcart
															 */
															final ArrayList<DownloadProduct> shopcart_list = new ArrayList<DownloadProduct>();

															/*
															 * Displays the
															 * products in a
															 * table
															 */
															AsyncDataProvider<DownloadProduct> provider = new AsyncDataProvider<DownloadProduct>() {
																@Override
																protected void onRangeChanged(HasData<DownloadProduct> display) {
																	/*
																	 * Get start
																	 * and end
																	 * of the
																	 * list to
																	 * display
																	 * in the
																	 * table
																	 */
																	int start = display.getVisibleRange().getStart();
																	int end = start + display.getVisibleRange().getLength();
																	end = end >= shop_result.size() ? shop_result.size() : end;
																	/*
																	 * Iterator
																	 * on the
																	 * server
																	 * response
																	 * : we want
																	 * to put
																	 * all the
																	 * objects
																	 * contained
																	 * in
																	 * shop_result
																	 * in the
																	 * list
																	 * shopcart_list
																	 */
																	Iterator<String> iterator = shop_result.keySet().iterator();

																	while (iterator.hasNext()) {

																		String key = (String) iterator.next();
																		/*
																		 * Get
																		 * each
																		 * object
																		 * of
																		 * the
																		 * response
																		 */
																		DownloadProduct value = (DownloadProduct) shop_result.get(key);
																		/*
																		 * Put
																		 * them
																		 * in
																		 * the
																		 * shopcart_list
																		 * list
																		 */
																		shopcart_list.add(value);

																		/*
																		 * Destroy
																		 * the
																		 * current
																		 * element
																		 */
																		iterator.remove();
																	}
																	updateRowData(start, shopcart_list);
																}
															};
															/*
															 * Display the list
															 * in the cell table
															 * in the
															 * ShopcartList
															 * panel
															 */
															provider.addDataDisplay(mainPanel.shopcartPanel.shopcartListPanel.shopcart_list_panel_cellTable);
															provider.updateRowCount(shopcart_list.size(), true);

														}
													});

										}
									});

									/*
									 * ***************** Display the Catalogue
									 * result in a table in the
									 * CatalogueResultPanel ****************
									 */

									/*
									 * List of the results - CatalogueResult -
									 * to place in the table
									 */
									final ArrayList<CatalogueResult> result_list = new ArrayList<CatalogueResult>();

									/*
									 * Displays the products in a table
									 */
									AsyncDataProvider<CatalogueResult> provider = new AsyncDataProvider<CatalogueResult>() {
										@Override
										protected void onRangeChanged(HasData<CatalogueResult> display) {
											/*
											 * Get start and end of the list to
											 * display in the table
											 */
											int start = display.getVisibleRange().getStart();
											int end = start + display.getVisibleRange().getLength();
											end = end >= catalogueResult.size() ? catalogueResult.size() : end;
											/*
											 * Iterator on the server response :
											 * we want to put all the objects
											 * contained in catalogueResult in
											 * the list result_list
											 */
											Iterator<String> iterator = catalogueResult.keySet().iterator();

											while (iterator.hasNext()) {

												String key = (String) iterator.next();
												/*
												 * Get each object of the
												 * response
												 */
												CatalogueResult value = (CatalogueResult) catalogueResult.get(key);
												/*
												 * Put them in the result_list
												 * list
												 */
												result_list.add(value);

												/*
												 * Display the mesh for each
												 * result on the map
												 */
												Vector polygon_layer = new Vector("polygon_layer" + value.identifier);

												if (value.upperLeft != null && value.lowerLeft != null && value.lowerRight != null && value.upperRight != null) {
													/*
													 * Points creation to build
													 * the mesh polygon
													 */
													Point p1 = new Point(value.upperLeft.longitude, value.upperLeft.latitude);
													Point p2 = new Point(value.lowerLeft.longitude, value.lowerLeft.latitude);
													Point p3 = new Point(value.lowerRight.longitude, value.lowerRight.latitude);
													Point p4 = new Point(value.upperRight.longitude, value.upperRight.latitude);

													/*
													 * The points have to
													 * respect the default
													 * project of the map
													 */
													p1.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
													p2.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
													p3.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
													p4.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));

													Point[] points = { p1, p2, p3, p4 };

													/*
													 * Polygon feature building
													 * thanks to the points
													 * defined above
													 */
													LinearRing linear_ring = new LinearRing(points);
													VectorFeature polygon = new VectorFeature(linear_ring);
													/*
													 * Add the feature to the
													 * vector layer
													 */
													polygon_layer.addFeature(polygon);
													/*
													 * Make it visible
													 */
													polygon_layer.setIsVisible(true);
													/*
													 * Add the layer to the map
													 */
													((org.gwtopenmaps.openlayers.client.Map) map).addLayer(polygon_layer);

													/*
													 * Zoom and center on the
													 * last feature built
													 */
													LonLat center = new LonLat(value.upperLeft.longitude, value.upperLeft.latitude);
													center.transform("EPSG:4326", map.getProjection());
													map.setCenter(center, 6);
												}

												/*
												 * Remove current object
												 */
												iterator.remove();
											}
											updateRowData(start, result_list);
										}
									};
									/*
									 * Display the list in the cell table in the
									 * CatalogueResult panel
									 */
									provider.addDataDisplay(mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable);
									provider.updateRowCount(result_list.size(), true);

									/*
									 * Display or not the mesh on the map ->
									 * FieldUpdater
									 */
									mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_showColumn
											.setFieldUpdater(new FieldUpdater<CatalogueResult, String>() {
												@Override
												public void update(int index, CatalogueResult object, String value) {
													/*
													 * if the button value is
													 * Hide when the user click
													 * on it, the mesh disappear
													 * from the map
													 */
													if (value.equals("Hide")) {
														/*
														 * Get the good layer
														 * thanks to the object
														 * identifier
														 */
														Layer vector = map.getLayerByName("polygon_layer" + object.identifier);
														/*
														 * The layer become
														 * invisible
														 */
														vector.setIsVisible(false);
														/*
														 * We change the value
														 * of the button by
														 * setting the boolean
														 * "hideButtonClicked"
														 * to true -> the value
														 * become "show"
														 */
														mainPanel.cataloguePanel.catalogueResultPanel.hideButtonClicked = true;
														/*
														 * Redraw the row with
														 * the new Button value
														 */
														mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable.redrawRow(index);

														/*
														 * if the button value
														 * is Show when the user
														 * click on it, the mesh
														 * is displayed on the
														 * map
														 */
													} else if (value.equals("Show")) {
														/*
														 * Get the good layer
														 * thanks to the object
														 * identifier
														 */
														Layer vector = map.getLayerByName("polygon_layer" + object.identifier);
														/*
														 * The layer become
														 * visible
														 */
														vector.setIsVisible(true);
														/*
														 * We change the value
														 * of the button by
														 * setting the boolean
														 * "hideButtonClicked"
														 * to false -> the value
														 * become "hide"
														 */
														mainPanel.cataloguePanel.catalogueResultPanel.hideButtonClicked = false;
														/*
														 * Redraw the row with
														 * the new Button value
														 */
														mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable.redrawRow(index);

													}
												}
											});

									/*
									 * ********** Details displayed according to
									 * the selected product **********
									 */
									Handler tableHandler = new SelectionChangeEvent.Handler() {
										@Override
										/*
										 * According to the selection, the
										 * details panel change
										 */
										public void onSelectionChange(SelectionChangeEvent event) {
											/*
											 * Get the selected object
											 */
											CatalogueResult selectedScene = mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_selectionModelDetails
													.getLastSelectedObject();
											/*
											 * Fill all the panel informations
											 * with the object attributes, if
											 * there are no null
											 */
											mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_id.setText(selectedScene.id);

											if (selectedScene.acquisitionStation != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_acquisitionStation
														.setText(selectedScene.acquisitionStation);
											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_acquisitionStation
														.setText("not informed");

											}
											if (selectedScene.acquisitionType != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_acquisitionType
														.setText(selectedScene.acquisitionType);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_acquisitionType.setText("not informed");

											}
											if (selectedScene.archivingCenter != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_archivingCenter
														.setText(selectedScene.archivingCenter);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_archivingCenter.setText("not informed");

											}
											if (selectedScene.archivingDate != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_archivingDate
														.setText(selectedScene.archivingDate);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_archivingDate.setText("not informed");

											}
											if (selectedScene.cloudCover != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_cloudCover
														.setText(selectedScene.cloudCover);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_cloudCover.setText("not informed");

											}
											if (selectedScene.compositeType != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_compositeType
														.setText(selectedScene.compositeType);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_compositeType.setText("not informed");

											}
											if (selectedScene.frame != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_frame.setText(selectedScene.frame);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_frame.setText("not informed");

											}
											if (selectedScene.identifier != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_identifier
														.setText(selectedScene.identifier);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_identifier.setText("not informed");

											}
											if (selectedScene.instrument != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_instrument
														.setText(selectedScene.instrument);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_instrument.setText("not informed");

											}
											if (selectedScene.orbitDirection != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitDirection
														.setText(selectedScene.orbitDirection);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitDirection.setText("not informed");

											}
											if (selectedScene.orbitNumber != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitNumber
														.setText(selectedScene.orbitNumber);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitNumber.setText("not informed");

											}
											if (selectedScene.orbitType != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitType
														.setText(selectedScene.orbitType);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_orbitType.setText("not informed");

											}
											if (selectedScene.platform != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_platform.setText(selectedScene.platform);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_platform.setText("not informed");

											}
											if (selectedScene.processingCenter != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingCenter
														.setText(selectedScene.processingCenter);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingCenter.setText("not informed");

											}
											if (selectedScene.processingDate != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingDate
														.setText(selectedScene.processingDate);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingDate.setText("not informed");

											}
											if (selectedScene.processingLevel != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingLevel
														.setText(selectedScene.processingLevel);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingLevel.setText("not informed");

											}
											if (selectedScene.processingSoftware != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingSoftware
														.setText(selectedScene.processingSoftware);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_processingSoftware
														.setText("not informed");

											}
											if (selectedScene.productType != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_entryType
														.setText(selectedScene.productType);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_entryType.setText("not informed");

											}
											if (selectedScene.resolution != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_resolution
														.setText(selectedScene.resolution);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_resolution.setText("not informed");

											}
											if (selectedScene.sensorMode != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_sensorMode
														.setText(selectedScene.sensorMode);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_sensorMode.setText("not informed");

											}
											if (selectedScene.sensorType != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_sensorType
														.setText(selectedScene.sensorType);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_sensorType.setText("not informed");

											}
											if (selectedScene.snowCover != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_snowCover
														.setText(selectedScene.snowCover);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_snowCover.setText("not informed");

											}
											if (selectedScene.spectralRange != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_spectralRange
														.setText(selectedScene.spectralRange);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_spectralRange.setText("not informed");

											}
											if (selectedScene.swathId != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_swathId.setText(selectedScene.swathId);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_swathId.setText("not informed");

											}
											if (selectedScene.status != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_status.setText(selectedScene.status);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_status.setText("not informed");

											}
											if (selectedScene.track != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_track.setText(selectedScene.track);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_track.setText("not informed");

											}
											if (selectedScene.waveLenght != null) {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_waveLenght
														.setText(selectedScene.waveLenght);

											} else {

												mainPanel.cataloguePanel.catalogueDetailsPanel.catalogue_details_panel_waveLenght.setText("not informed");

											}

										}
									};

									/*
									 * Add the handler to the selection model
									 */
									mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_selectionModelDetails
											.addSelectionChangeHandler(tableHandler);
									/*
									 * Add the selection model to the table
									 */
									mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_celltable
											.setSelectionModel(mainPanel.cataloguePanel.catalogueResultPanel.catalogue_result_panel_selectionModelDetails);

								}

							});

				}
			}
		});

		/*********** TASKING FEASIBILITY SEARCH PANEL ***********/

		/*
		 * Draw an AOI : click on the draw AOI button
		 */
		mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_drawaoi.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				/*
				 * activation of the drawfeature control for the feasibility
				 * tasking
				 */
				mapPanel.drawRectangleFeatureControlFeas.activate();
				/*
				 * activation of the modify feature control handling the vector
				 * features drawn thanks to the control above
				 */
				mapPanel.modFeas.activate();

				/*
				 * the drawing button become invisible and the stop drawing
				 * button become visible
				 */
				mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_drawaoiStop.setVisible(true);
				mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_drawaoi.setVisible(false);

			}
		});

		/*
		 * Get the vector handling the AOI and add a "vector feature added"
		 * listener on it
		 */
		Vector vectorFeas = mapPanel.vectorLayerFeas;
		vectorFeas.addVectorFeatureAddedListener(new VectorFeatureAddedListener() {

			@Override
			public void onFeatureAdded(FeatureAddedEvent eventObject) {
				// TODO Auto-generated method stub
				/*
				 * Once a feature is added, the draw feature control is
				 * deactivated
				 */
				mapPanel.drawRectangleFeatureControlFeas.deactivate();
				/*
				 * the draw aoi button is not enabled anymore
				 */
				mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_drawaoi.setEnabled(false);
				Vector vectorFeas = mapPanel.vectorLayerFeas;
				/*
				 * Get all the features of the vector and take the first
				 */
				Feature[] features = vectorFeas.getFeatures();
				Feature feature = features[0];
				/*
				 * We need the bounds of the vector drawn to register them in
				 * the form
				 */
				Bounds bounds = ((VectorFeature) feature).getGeometry().getBounds();
				/*
				 * If we are using the WMS as base map, the project is not the
				 * same than Google Map
				 */
				if (map.getBaseLayer().getName().equals("Global Imagery")) {
					/*
					 * Put the bounds value in the AOI fields in the form
					 */
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlat.setValue(bounds.getUpperRightY());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlon.setValue(bounds.getLowerLeftX());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selat.setValue(bounds.getLowerLeftY());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selon.setValue(bounds.getUpperRightX());

				} else {
					/*
					 * Same thing but we need to transform the projection to fit
					 * with Google Map
					 */
					LonLat nw = new LonLat(bounds.getLowerLeftX(), bounds.getUpperRightY());
					LonLat se = new LonLat(bounds.getUpperRightX(), bounds.getLowerLeftY());
					/*
					 * Projection transformation
					 */
					nw.transform("EPSG:900913", "EPSG:4326");
					se.transform("EPSG:900913", "EPSG:4326");
					/*
					 * Put the bounds value in the AOI fields in the form
					 */
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlat.setValue(nw.lat());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlon.setValue(nw.lon());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selat.setValue(se.lat());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selon.setValue(se.lon());
				}

			}
		});

		/*
		 * Now add to the vector a "feature modified" listener
		 */
		vectorFeas.addVectorFeatureModifiedListener(new VectorFeatureModifiedListener() {

			@Override
			public void onFeatureModified(FeatureModifiedEvent eventObject) {
				// TODO Auto-generated method stub
				Vector vectorFeas = mapPanel.vectorLayerFeas;
				/*
				 * Get all the features of the vector and take the first
				 */
				Feature[] features = vectorFeas.getFeatures();
				Feature feature = features[0];
				/*
				 * We need the bounds of the vector drawn to register them in
				 * the form
				 */
				Bounds bounds = ((VectorFeature) feature).getGeometry().getBounds();
				/*
				 * If we are using the WMS as base map, the project is not the
				 * same than Google Map
				 */
				if (map.getBaseLayer().getName().equals("Global Imagery")) {

					LonLat nw = new LonLat(bounds.getLowerLeftX(), bounds.getUpperRightY());
					LonLat se = new LonLat(bounds.getUpperRightX(), bounds.getLowerLeftY());
					/*
					 * Put the bounds value in the AOI fields in the form
					 */
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlat.setValue(nw.lat());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlon.setValue(nw.lon());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selat.setValue(se.lat());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selon.setValue(se.lon());

				} else {
					/*
					 * Same thing but we need to transform the projection to fit
					 * with Google Map
					 */
					LonLat nw = new LonLat(bounds.getLowerLeftX(), bounds.getUpperRightY());
					LonLat se = new LonLat(bounds.getUpperRightX(), bounds.getLowerLeftY());
					/*
					 * Projection transformation
					 */
					nw.transform("EPSG:900913", "EPSG:4326");
					se.transform("EPSG:900913", "EPSG:4326");
					/*
					 * Put the bounds value in the AOI fields in the form
					 */
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlat.setValue(nw.lat());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlon.setValue(nw.lon());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selat.setValue(se.lat());
					mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selon.setValue(se.lon());

				}

			}
		});

		/*
		 * Add a click handler on the stop drawing button : the feature
		 * disappears
		 */
		mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_drawaoiStop.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Vector vectorFeas = mapPanel.vectorLayerFeas;
				/*
				 * Get all the features of the vector and take the first
				 */
				Feature[] features = vectorFeas.getFeatures();
				Feature feature = features[0];
				/*
				 * Remove the feature so it cannot appear on the map anymore
				 */
				vectorFeas.removeFeature((VectorFeature) feature);
				/*
				 * The button draw AOI is enabled and visible again
				 */
				mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_drawaoi.setVisible(true);
				mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_drawaoi.setEnabled(true);
				mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_drawaoiStop.setVisible(false);

			}
		});

		/*
		 * ******* Submit the feasibility tasking request ********
		 */
		mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_send_request_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

				/*
				 * FeasibilitySearch object building
				 */
				FeasibilitySearch feasibilitySearch = new FeasibilitySearch();

				/*
				 * If the field in the request form is not empty so we set the
				 * attribute to the FeasibilitySearch object. Moreover,
				 * registration of the parameter key for the request url in the
				 * map attribute "parameter" of the FeasibilitySearch object
				 */
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_ambiguityLevel.getValue() != null) {

					feasibilitySearch.setMaxAmbiguityLevel(String
							.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_ambiguityLevel.getValue()));

					feasibilitySearch.parameters.put("eosp:validationParametersSARMaxAmbiguityLevel",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_ambiguityLevel.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_azimuthMax.getValue() != null
						&& mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_azimuthMin.getValue() != null) {

					feasibilitySearch.setAzimuth(String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_azimuthMin
							.getValue())
							+ ","
							+ String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_azimuthMax.getValue()));

					feasibilitySearch.parameters.put("eosp:acquisitionAngleIncidenceAzimuth",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_azimuthMin.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_cloud.getValue() != null) {

					feasibilitySearch.setCloudCover(String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_cloud.getValue()));

					feasibilitySearch.parameters.put("eo:cloudCover",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_cloud.getName());

				} else {
					// empty
				}
				if (!mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_compositeType.getValue(
						mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_compositeType.getSelectedIndex()).equals("Select...")) {

					feasibilitySearch.setCompositeType(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_compositeType
							.getValue(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_compositeType.getSelectedIndex()));

					feasibilitySearch.parameters.put("eo:compositeType",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_compositeType.getName());

				} else {
					// empty
				}
				if (!mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_coverageType.getValue(
						mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_coverageType.getSelectedIndex()).equals("Select...")) {

					feasibilitySearch.setCoverageType(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_coverageType
							.getValue(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_coverageType.getSelectedIndex()));

					feasibilitySearch.parameters.put("eo:coverageType",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_coverageType.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_dateend.getValue().length() > 0) {

					feasibilitySearch.setEnd(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_dateend.getValue());

					feasibilitySearch.parameters.put("time:end", mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_dateend.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_datestart.getValue().length() > 0) {

					feasibilitySearch.setStart(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_datestart.getValue());

					feasibilitySearch.parameters.put("time:start",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_datestart.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_elevationMax.getValue() != null
						&& mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_elevationMin.getValue() != null) {

					feasibilitySearch.setElevation(String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_elevationMin
							.getValue())
							+ ","
							+ String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_elevationMax.getValue()));

					feasibilitySearch.parameters.put("eosp:acquisitionAngleIncidenceElevation",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_elevationMin.getName());

				} else {
					// empty
				}
				if (!mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_haze.getValue(
						mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_haze.getSelectedIndex()).equals("Select...")) {

					feasibilitySearch.setHaze(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_haze
							.getValue(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_haze.getSelectedIndex()));

					feasibilitySearch.parameters.put("eosp:validationParametersOPTHazeAccepted",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_haze.getName());

				} else {
					// empty
				}
				if (!mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_instrument.getValue(
						mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_instrument.getSelectedIndex()).equals("Select...")) {

					feasibilitySearch.setInstrument(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_instrument
							.getValue(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_instrument.getSelectedIndex()));

					feasibilitySearch.parameters.put("eo:instrument",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_instrument.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_minLuminosity.getValue() != null) {

					feasibilitySearch.setMinLuminosity(String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_minLuminosity
							.getValue()));

					feasibilitySearch.parameters.put("eosp:acquisitionParametersOPTMinimumLuminosity",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_minLuminosity.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_noiseLevel.getValue() != null) {

					feasibilitySearch.setMaxNoiseLevel(String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_noiseLevel
							.getValue()));

					feasibilitySearch.parameters.put("eosp:validationParametersSARMaximumNoiseLevel",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_noiseLevel.getName());

				} else {
					// empty
				}
				/*
				 * AOI definition
				 */
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlat.getValue() != null
						&& mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlon.getValue() != null
						&& mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selat.getValue() != null
						&& mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selon.getValue() != null) {

					feasibilitySearch.setNwlat(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlat.getValue());
					feasibilitySearch.setNwlon(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlon.getValue());
					feasibilitySearch.setSelat(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selat.getValue());
					feasibilitySearch.setSelon(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_selon.getValue());
					feasibilitySearch.parameters.put("geo:box", mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_nwlat.getName());

				} else {
					// empty
				}
				if (!mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_platform.getValue(
						mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_platform.getSelectedIndex()).equals("Select...")) {

					feasibilitySearch.setPlatform(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_platform
							.getValue(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_platform.getSelectedIndex()));

					feasibilitySearch.parameters.put("eo:platform",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_platform.getName());

				} else {
					// empty
				}
				if (!mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_polarization.getValue(
						mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_polarization.getSelectedIndex()).equals("Select...")) {

					feasibilitySearch.setPolMode(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_polarization
							.getValue(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_polarization.getSelectedIndex()));

					feasibilitySearch.parameters.put("eosp:acquisitionParametersSARPolarizationMode",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_polarization.getName());

				} else {
					// empty
				}
				if (!mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_resolution.getValue(
						mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_resolution.getSelectedIndex()).equals("Select...")) {

					feasibilitySearch.setResolution(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_resolution
							.getValue(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_resolution.getSelectedIndex()));

					feasibilitySearch.parameters.put("eo:resolution",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_resolution.getName());

				} else {
					// empty
				}
				if (!mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sandwind.getValue(
						mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sandwind.getSelectedIndex()).equals("Select...")) {

					feasibilitySearch.setSandWind(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sandwind
							.getValue(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sandwind.getSelectedIndex()));

					feasibilitySearch.parameters.put("eosp:validationParametersOPTSandWindAccepted",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sandwind.getName());

				} else {
					// empty
				}
				if (!mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sensorMode.getValue(
						mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sensorMode.getSelectedIndex()).equals("Select...")) {

					feasibilitySearch.setSensorMode(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sensorMode
							.getValue(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sensorMode.getSelectedIndex()));

					feasibilitySearch.parameters.put("eo:sensorMode",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sensorMode.getName());

				} else {
					// empty
				}
				if (!mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sensorType.getValue(
						mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sensorType.getSelectedIndex()).equals("Select...")) {

					feasibilitySearch.setSensorType(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sensorType
							.getValue(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sensorType.getSelectedIndex()));

					feasibilitySearch.parameters.put("eo:sensorType",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sensorType.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_snow.getValue() != null) {

					feasibilitySearch.setSnowCover(String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_snow.getValue()));

					feasibilitySearch.parameters.put("eo:snowCover", mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_snow.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sunglint.getValue() != null) {

					feasibilitySearch.setMaxSunGlint(String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sunglint
							.getValue()));

					feasibilitySearch.parameters.put("eo:snowCover",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sunglint.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sunglint.getValue() != null) {

					feasibilitySearch.setMaxSunGlint(String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sunglint
							.getValue()));

					feasibilitySearch.parameters.put("eosp:validationParametersOPTmaxSunGlint",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_sunglint.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_trackacrossMax.getValue() != null
						&& mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_trackacrossMin.getValue() != null) {

					feasibilitySearch.setTrackAcross(String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_trackacrossMin
							.getValue())
							+ ","
							+ String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_trackacrossMax.getValue()));

					feasibilitySearch.parameters.put("eosp:acquisitionAnglePointingRangeAcrossTrack",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_trackacrossMax.getName());

				} else {
					// empty
				}
				if (mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_trackalongMax.getValue() != null
						&& mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_trackalongMin.getValue() != null) {

					feasibilitySearch.setTrackAlong(String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_trackalongMin
							.getValue())
							+ ","
							+ String.valueOf(mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_trackalongMax.getValue()));

					feasibilitySearch.parameters.put("eosp:acquisitionAnglePointingRangeAlongTrack",
							mainPanel.feasibilityPanel.feasibilitySearchPanel.feasibility_search_panel_trackalongMin.getName());

				} else {
					// empty
				}

				/*
				 * Feasibility service call -> get the results of the
				 * Feasibility tasking request
				 */
				mainPanel.feasibilityPanel.feasibilitySearchPanel.getFeasibilityService().getResults(feasibilitySearch,
						new AsyncCallback<Map<String, FeasibilityResult>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								System.out.println("fail Feasibility Tasking");

							}

							@Override
							public void onSuccess(final Map<String, FeasibilityResult> feasibilityResult) {
								// TODO Auto-generated method stub
								System.out.println("success");

								/*
								 * We go on the results Panel and disable the
								 * search Panel
								 */
								mainPanel.feasibilityPanel.feasibility_panel_tab.getTabBar().setTabEnabled(1, true);
								mainPanel.feasibilityPanel.feasibility_panel_tab.getTabBar().setTabEnabled(2, true);
								/*
								 * Select the result tab
								 */
								mainPanel.feasibilityPanel.feasibility_panel_tab.getTabBar().selectTab(1);
								mainPanel.feasibilityPanel.feasibility_panel_tab.getTabBar().setTabEnabled(0, false);

								/*
								 * We can get the XMl returned from the server
								 * by clicking on the "See XML file" button
								 */

								if (feasibilityResult.get("0") == null) {

									/*
									 * There is no result
									 */
									mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_show_xml_button.setEnabled(false);
									Window.alert("no result found corresponding to your request");

								} else {
									/*
									 * We have to have at least one result in
									 * the response to show the XML file
									 */
									mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_show_xml_button.setEnabled(true);
									final String xml = feasibilityResult.get("0").getXml();
									mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_show_xml_button
											.addClickHandler(new ClickHandler() {

												@Override
												public void onClick(ClickEvent event) {

													/*
													 * Show XML file in an other
													 * window
													 */
													Window.open(xml, "show xml", xml);
												}

											});

									mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_see_request_button
											.addClickHandler(new ClickHandler() {

												@Override
												public void onClick(ClickEvent event) {

													/*
													 * Show search request url
													 * in an other window
													 */

													Window.alert(xml);
												}

											});
								}

								/*
								 * ***************** Display the Feasibility
								 * result in a table in the
								 * FeasiblityResultPanel ****************
								 */

								/*
								 * List of the results - FeasibilityResult - to
								 * place in the table
								 */
								final ArrayList<FeasibilityResult> result_list = new ArrayList<FeasibilityResult>();

								AsyncDataProvider<FeasibilityResult> provider = new AsyncDataProvider<FeasibilityResult>() {
									@SuppressWarnings("unchecked")
									@Override
									protected void onRangeChanged(HasData<FeasibilityResult> display) {
										/*
										 * Get start and end of the list to
										 * display in the table
										 */
										int start = display.getVisibleRange().getStart();
										int end = start + display.getVisibleRange().getLength();
										end = end >= feasibilityResult.size() ? feasibilityResult.size() : end;
										/*
										 * Iterator on the server response : we
										 * want to put all the objects contained
										 * in feasibilityResult in the list
										 * result_list
										 */
										Iterator<String> iterator = feasibilityResult.keySet().iterator();

										while (iterator.hasNext()) {

											String key = (String) iterator.next();
											/*
											 * Get each object of the response
											 */
											FeasibilityResult value = (FeasibilityResult) feasibilityResult.get(key);
											/*
											 * Put them in the result_list list
											 */
											result_list.add(value);

											/*
											 * Display the mesh(s) for each
											 * result on the map
											 */
											Vector polygon_layer = new Vector("polygon_layer" + value.identifier);

											if (value.upperLeft != null && value.lowerLeft != null && value.lowerRight != null && value.upperRight != null) {
												/*
												 * There can be several segment
												 * for one result
												 */
												for (int i = 0; i < value.upperLeft.size(); i++) {
													/*
													 * Points creation to build
													 * the mesh polygon
													 */
													Point p1 = new Point(value.upperLeft.get(i).longitude, value.upperLeft.get(i).latitude);
													Point p2 = new Point(value.lowerLeft.get(i).longitude, value.lowerLeft.get(i).latitude);
													Point p3 = new Point(value.lowerRight.get(i).longitude, value.lowerRight.get(i).latitude);
													Point p4 = new Point(value.upperRight.get(i).longitude, value.upperRight.get(i).latitude);

													/*
													 * The points have to
													 * respect the default
													 * project of the map
													 */
													p1.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
													p2.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
													p3.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));
													p4.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));

													Point[] points = { p1, p2, p3, p4 };

													/*
													 * Polygon feature building
													 * thanks to the points
													 * defined above
													 */
													LinearRing linear_ring = new LinearRing(points);
													VectorFeature polygon = new VectorFeature(linear_ring);
													/*
													 * Add the feature to the
													 * vector layer
													 */
													polygon_layer.addFeature(polygon);
													/*
													 * Make it visible
													 */
													polygon_layer.setIsVisible(true);
													/*
													 * Add the layer to the map
													 */
													((org.gwtopenmaps.openlayers.client.Map) map).addLayer(polygon_layer);

													/*
													 * Zoom and center on the
													 * last feature built
													 */
													LonLat center = new LonLat(value.upperLeft.get(i).longitude, value.upperLeft.get(i).latitude);
													center.transform("EPSG:4326", map.getProjection());
													map.setCenter(center, 6);
												}

											}

											/*
											 * Remove current object
											 */
											iterator.remove();
										}
										updateRowData(start, result_list);
									}
								};
								/*
								 * Display the list in the cell table in the
								 * FeasibilityResult panel
								 */
								provider.addDataDisplay(mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_cellTable);
								provider.updateRowCount(result_list.size(), true);

								/*
								 * Display or not the mesh on the map ->
								 * FieldUpdater
								 */
								mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_showColumn
										.setFieldUpdater(new FieldUpdater<FeasibilityResult, String>() {
											@Override
											public void update(int index, FeasibilityResult object, String value) {

												/*
												 * if the button value is Hide
												 * when the user click on it,
												 * the mesh disappear from the
												 * map
												 */
												if (value.equals("Hide")) {
													/*
													 * Get the good layer thanks
													 * to the object identifier
													 */
													Layer vector = map.getLayerByName("polygon_layer" + object.identifier);
													/*
													 * The layer become
													 * invisible
													 */
													vector.setIsVisible(false);
													/*
													 * We change the value of
													 * the button by setting the
													 * boolean
													 * "hideButtonClicked" to
													 * true -> the value become
													 * "show"
													 */
													mainPanel.feasibilityPanel.feasibilityResultPanel.hideButtonClicked = true;
													/*
													 * Redraw the row with the
													 * new Button value
													 */
													mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_cellTable.redrawRow(index);

													/*
													 * if the button value is
													 * Show when the user click
													 * on it, the mesh is
													 * displayed on the map
													 */
												} else if (value.equals("Show")) {
													/*
													 * Get the good layer thanks
													 * to the object identifier
													 */
													Layer vector = map.getLayerByName("polygon_layer" + object.identifier);
													/*
													 * The layer become visible
													 */
													vector.setIsVisible(true);
													/*
													 * We change the value of
													 * the button by setting the
													 * boolean
													 * "hideButtonClicked" to
													 * false -> the value become
													 * "hide"
													 */
													mainPanel.feasibilityPanel.feasibilityResultPanel.hideButtonClicked = false;
													/*
													 * Redraw the row with the
													 * new Button value
													 */
													mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_cellTable.redrawRow(index);

												}
											}
										});

								/*
								 * ********** Details displayed according to the
								 * selected product -> FeasibilityDetailsPanel
								 * **********
								 */
								Handler tableHandler = new SelectionChangeEvent.Handler() {
									@Override
									/*
									 * According to the selection, the details
									 * panel change
									 */
									public void onSelectionChange(SelectionChangeEvent event) {
										/*
										 * Get the selected object
										 */
										FeasibilityResult selectedScene = mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_selectionModelDetails
												.getLastSelectedObject();
										/*
										 * Fill all the panel informations with
										 * the object attributes, if there are
										 * no null
										 */
										mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_id.setText(selectedScene.identifier);

										if (selectedScene.acquisitionDate != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_acquisitionDate
													.setText(selectedScene.acquisitionDate);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_acquisitionDate
													.setText("not informed");

										}
										if (selectedScene.acrossTrack != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_accrossTrack
													.setText(selectedScene.acrossTrack);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_accrossTrack.setText("not informed");

										}
										if (selectedScene.alongTrack != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_alongTrack
													.setText(selectedScene.alongTrack);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_alongTrack.setText("not informed");

										}
										if (selectedScene.azimuth != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_azimuth.setText(selectedScene.azimuth);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_azimuth.setText("not informed");

										}
										if (selectedScene.cloudCover != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_cloud
													.setText(selectedScene.cloudCover);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_cloud.setText("not informed");

										}
										if (selectedScene.compositeType != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_compositeType
													.setText(selectedScene.compositeType);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_compositeType.setText("not informed");

										}
										if (selectedScene.elevation != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_elevation
													.setText(selectedScene.elevation);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_elevation.setText("not informed");

										}
										if (selectedScene.haze != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_haze.setText(selectedScene.haze);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_haze.setText("not informed");

										}
										if (selectedScene.instrument != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_instrument
													.setText(selectedScene.instrument);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_instrument.setText("not informed");

										}
										if (selectedScene.instrumentMode != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_sensorMode
													.setText(selectedScene.instrumentMode);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_sensorMode.setText("not informed");

										}
										if (selectedScene.maxNoiseLevel != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_noiseLevel
													.setText(selectedScene.maxNoiseLevel);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_noiseLevel.setText("not informed");

										}
										if (selectedScene.maxSunGlint != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_sunGlint
													.setText(selectedScene.maxSunGlint);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_sunGlint.setText("not informed");

										}
										if (selectedScene.minLuminosity != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_minLuminosity
													.setText(selectedScene.minLuminosity);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_minLuminosity.setText("not informed");

										}
										if (selectedScene.orbitType != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_orbitType
													.setText(selectedScene.orbitType);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_orbitType.setText("not informed");

										}
										if (selectedScene.platform != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_platform
													.setText(selectedScene.platform);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_platform.setText("not informed");

										}
										if (selectedScene.polarizationMode != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_polarisationMode
													.setText(selectedScene.polarizationMode);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_polarisationMode
													.setText("not informed");

										}
										if (selectedScene.resolution != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_resolution
													.setText(selectedScene.resolution);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_resolution.setText("not informed");

										}
										if (selectedScene.sandWind != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_sandWind
													.setText(selectedScene.sandWind);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_sandWind.setText("not informed");

										}
										if (selectedScene.sensor != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_sensorType
													.setText(selectedScene.sensor);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_sensorType.setText("not informed");

										}
										if (selectedScene.snowCover != null) {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_snow.setText(selectedScene.snowCover);

										} else {

											mainPanel.feasibilityPanel.feasibilityDetailsPanel.feasibility_details_panel_snow.setText("not informed");

										}

									}
								};

								/*
								 * Add the handler to the selection model
								 */
								mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_selectionModelDetails
										.addSelectionChangeHandler(tableHandler);
								/*
								 * Add the selection model to the table
								 */
								mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_cellTable
										.setSelectionModel(mainPanel.feasibilityPanel.feasibilityResultPanel.feasibility_result_panel_selectionModelDetails);
							}
						});

			}
		});

		/**************** SHOPCART PANEL ***************/

		/*
		 * Download the selected product by clicking on the "start" button ->
		 * FieldUpdater
		 */
		mainPanel.shopcartPanel.shopcartListPanel.shopcart_list_panel_download_column.setFieldUpdater(new FieldUpdater<DownloadProduct, String>() {
			@Override
			public void update(final int index, DownloadProduct object, String value) {
				// The user clicked on the button for the passed auction.


						/*
						 * Open a dialog box to see the different available options for the product to choose
						 */
						final DialogBox dialogBox = new DialogBox();
						dialogBox.ensureDebugId("cwDialogBox");
						dialogBox.setText("Options");
						//dialogBox.setPixelSize(250, 250);
						
						/*
						 *  Create a table to layout the content
						 */
					    AbsolutePanel dialogContents = new AbsolutePanel();
					    dialogContents.setWidth("300px");
					    dialogContents.setHeight("300px");
					    dialogBox.setWidget(dialogContents);
					    
					    /*
					     * Add some text to the top of the dialog
					     */
					    HTML title = new HTML("Available options for this product :");
					    dialogContents.add(title);
					    dialogContents.setWidgetPosition(title, 50, 5);
					    
					    /*
					     * Button to submit the request, register the chosen options and 
					     */
					    Button closeDialogBox = new Button();
					    closeDialogBox.setText("Submit");
					    dialogContents.add(closeDialogBox);
					    dialogContents.setWidgetPosition(closeDialogBox, 230, 250);
					    
					    /*
					     * Add checkbox to choose the different options
					     */
					    HTML processingLevelLabel = new HTML("Processing Level");
					    CheckBox processingLevelOptions = new CheckBox("1B");
					    dialogContents.add(processingLevelOptions, 200,30);
					    dialogContents.add(processingLevelLabel,10,30);
					    
					    HTML productTypeLabel = new HTML("Product Type");
					    CheckBox productTypeOptions = new CheckBox("ASA_IMG_1P");
					    dialogContents.add(productTypeOptions, 200,50);
					    dialogContents.add(productTypeLabel,10,50);
					    
					    HTML qualityOfServiceLabel = new HTML("Quality of service");
					    final CheckBox qualityOfServiceOptions1 = new CheckBox("Standard");
					    final CheckBox qualityOfServiceOptions2 = new CheckBox("NRT");
					    final CheckBox qualityOfServiceOptions3 = new CheckBox("RUSH");
					    dialogContents.add(qualityOfServiceOptions1, 200,70);
					    dialogContents.add(qualityOfServiceOptions2, 200,90);
					    dialogContents.add(qualityOfServiceOptions3, 200,110);
					    dialogContents.add(qualityOfServiceLabel,10,70);
					    
					    
					    /*
					     * Add a lister on the button to close the dialog box
					     */
					    closeDialogBox.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								mainPanel.shopcartPanel.shopcartListPanel.isOptionsChosen = true;
								dialogBox.hide();
								System.out.println(qualityOfServiceOptions1.getValue());
								mainPanel.shopcartPanel.shopcartListPanel.shopcart_list_panel_cellTable.redrawRow(index);
							}
						});

						dialogBox.center();
						dialogBox.show();

					
			}
		});

		/**************** TEST DISPLAY IMAGE ***************/

		/**************** ROOT PANEL CONFIGURATION ***************/
		dockPanel.setBorderWidth(1);
		RootPanel rootpanel = RootPanel.get();
		rootpanel.clear();
		rootpanel.add(dockPanel);

	}
}