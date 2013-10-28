package com.astrium.hmas.client;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               MapPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the panel which
 *   																	contains the application map
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

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.control.DragFeature;
import org.gwtopenmaps.openlayers.client.control.DragFeature.DragFeatureListener;
import org.gwtopenmaps.openlayers.client.control.DragFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.DrawFeature;
import org.gwtopenmaps.openlayers.client.control.DrawFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.LayerSwitcher;
import org.gwtopenmaps.openlayers.client.control.ModifyFeature;
import org.gwtopenmaps.openlayers.client.control.OverviewMap;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.handler.RegularPolygonHandler;
import org.gwtopenmaps.openlayers.client.handler.RegularPolygonHandlerOptions;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3MapType;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3Options;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public class MapPanel {

	/*
	 * Default map projection
	 */
	private static final Projection DEFAULT_PROJECTION = new Projection("EPSG:4326");
	public MapWidget mapWidget;
	/*
	 * DrawFeature controls : one for the feasibility and an other for the catalogue search
	 */
	public DrawFeature drawRectangleFeatureControl = null;
	public DrawFeature drawRectangleFeatureControlFeas = null;
	/*
	 * The two vectors controlled by the drawFeature controls above
	 */
	public Vector vectorLayer = new Vector("Vector layer");
	public Vector vectorLayerFeas = new Vector("Vector layer Feasibility");
	/*
	 * Two ModifyFeature controls for the two vectors above
	 */
	public ModifyFeature modFeas = new ModifyFeature(vectorLayerFeas);
	public ModifyFeature mod = new ModifyFeature(vectorLayer);

	/*
	 * Getter and setter for the map widget
	 */
	public MapWidget getMapWidget() {
		return mapWidget;
	}

	public void setMapWidget(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
	}

	@SuppressWarnings("deprecation")
	/*
	 * Method which handles the map building
	 */
	public void buildMap() {
		/*
		 * Create some map options
		 */
		MapOptions defaultMapOptions = new MapOptions();
		/*
		 * Set the projection
		 */
		defaultMapOptions.setDisplayProjection(new Projection("EPSG:4326"));
		/*
		 * Set the number of zoom levels
		 */
		defaultMapOptions.setNumZoomLevels(18);

		/*
		 * Create a map widget
		 */
		mapWidget = new MapWidget("100%", "100%", defaultMapOptions);

		/*
		 * Create some Google layers
		 */
		GoogleV3Options gHybridOptions = new GoogleV3Options();
		gHybridOptions.setIsBaseLayer(true);
		gHybridOptions.setType(GoogleV3MapType.G_HYBRID_MAP);
		GoogleV3 gHybrid = new GoogleV3("Google Hybrid", gHybridOptions);

		GoogleV3Options gNormalOptions = new GoogleV3Options();
		gNormalOptions.setIsBaseLayer(true);
		gNormalOptions.setType(GoogleV3MapType.G_NORMAL_MAP);
		GoogleV3 gNormal = new GoogleV3("Google Normal", gNormalOptions);

		GoogleV3Options gSatelliteOptions = new GoogleV3Options();
		gSatelliteOptions.setIsBaseLayer(true);
		gSatelliteOptions.setType(GoogleV3MapType.G_SATELLITE_MAP);
		GoogleV3 gSatellite = new GoogleV3("Google Satellite", gSatelliteOptions);

		GoogleV3Options gTerrainOptions = new GoogleV3Options();
		gTerrainOptions.setIsBaseLayer(true);
		gTerrainOptions.setType(GoogleV3MapType.G_TERRAIN_MAP);
		GoogleV3 gTerrain = new GoogleV3("Google Terrain", gTerrainOptions);

		/*
		 *  Create some WMS Layers
		 */

		/*
		 *  WMS parameters
		 */
		WMSParams wmsParams = new WMSParams();
		/*
		 * to make base layer remove transparency
		 */
		wmsParams.setIsTransparent(false);
		wmsParams.setFormat("image/png");
		wmsParams.setLayers("topp:bmng_092004");
		wmsParams.setStyles("");
		/*
		 *  WMS Layer
		 */
		WMS wmsLayer = new WMS("Global Imagery", "http://localhost:8000/geoserver/wms", wmsParams);
		/*
		 * It's a base layer
		 */
		wmsLayer.isBaseLayer();

		/*
		 * Add all the base layers to the map
		 */
		Map map = this.mapWidget.getMap();
		map.addLayer(gHybrid);
		map.addLayer(gNormal);
		map.addLayer(gSatellite);
		map.addLayer(gTerrain);
		map.addLayer(wmsLayer);

		/*
		 *  Lets add some default controls to the map
		 *  + sign in the upperright corner to display the layer switcher
		 */
		map.addControl(new LayerSwitcher()); 
		
		/*
		 * + sign in the lowerright to display the overviewmap
		 */
		map.addControl(new OverviewMap());
		
		/*
		 * Display the scaleline
		 */
		map.addControl(new ScaleLine()); // Display the scaleline

		/*
		 *  Center and zoom to a location
		 */
		LonLat lonLat = new LonLat(0, 0);
		/*
		 * transform lonlat to OSM coordinate system
		 */
		lonLat.transform(DEFAULT_PROJECTION.getProjectionCode(), map.getProjection());
		/*
		 * Set center and zoom
		 */
		map.setCenter(lonLat, 3);

		DeferredCommand.addCommand(new Command() {

			@Override
			public void execute() {

				mapWidget.getMap().updateSize();

			}

		});

		/*
		 * Style for the AOI vector layer
		 */
		Style style = new Style();
		style.setStrokeColor("#FFFFFF");
		style.setStrokeWidth(3);
		style.setFillOpacity(0);

		/*
		 * Add the style to the two layers which will handle the AOI rectangle
		 */
		vectorLayer.setStyle(style);
		vectorLayerFeas.setStyle(style);
		/*
		 * Add the two layers to the map
		 */
		map.addLayer(vectorLayer);
		map.addLayer(vectorLayerFeas);

		/*
		 * The two layers can be dragged and resized
		 */
		mod.setMode(ModifyFeature.RESIZE, ModifyFeature.DRAG);
		modFeas.setMode(ModifyFeature.RESIZE, ModifyFeature.DRAG);

		/*
		 * DrawFeature controls options
		 */
		DrawFeatureOptions drawFeatureOptions = new DrawFeatureOptions();

		/*
		 * The AOI vector will be a rectangle
		 */
		RegularPolygonHandlerOptions rectangleOptions = new RegularPolygonHandlerOptions();
		rectangleOptions.setRadius(90);
		rectangleOptions.setIrregular(true);
		rectangleOptions.setSides(4);

		/*
		 * Add the rectangle options to the drawFeature options
		 */
		drawFeatureOptions.setHandlerOptions(rectangleOptions);
		/*
		 * Create the two drawFeature controls with their options
		 */
		drawRectangleFeatureControlFeas = new DrawFeature(vectorLayerFeas, new RegularPolygonHandler(), drawFeatureOptions);
		drawRectangleFeatureControl = new DrawFeature(vectorLayer, new RegularPolygonHandler(), drawFeatureOptions);

		/*
		 * Add the two drawFeature controls to the map
		 */
		map.addControl(drawRectangleFeatureControlFeas);
		map.addControl(drawRectangleFeatureControl);

		/*
		 * Add the two modifyFeature controls to the map
		 */
		map.addControl(mod);
		map.addControl(modFeas);

		/*
		 * Force the map to fall behind popups
		 */
		this.mapWidget.getElement().getFirstChildElement().getStyle().setZIndex(0); 
	}

	/*
	 * Getters and setters
	 */
	public DrawFeature getDrawRectangleFeatureControl() {
		return drawRectangleFeatureControl;
	}

	public void setDrawRectangleFeatureControl(DrawFeature drawRectangleFeatureControl) {
		this.drawRectangleFeatureControl = drawRectangleFeatureControl;
	}

	public DrawFeature getDrawRectangleFeatureControlFeas() {
		return drawRectangleFeatureControlFeas;
	}

	public void setDrawRectangleFeatureControlFeas(DrawFeature drawRectangleFeatureControlFeas) {
		this.drawRectangleFeatureControlFeas = drawRectangleFeatureControlFeas;
	}

	// Method which create a drag Feature (not used)
	@SuppressWarnings("unused")
	private DragFeature createDragFeature(Vector layer) {
		DragFeatureOptions dragFeatureOptions = new DragFeatureOptions();
		dragFeatureOptions.onDrag(createDragFeatureListener("onDrag"));
		dragFeatureOptions.onStart(createDragFeatureListener("onStart"));
		dragFeatureOptions.onComplete(createDragFeatureListener("onComplete"));

		DragFeature dragFeature = new DragFeature(layer, dragFeatureOptions);
		return dragFeature;
	}

	DragFeatureListener createDragFeatureListener(final String type) {
		return new DragFeatureListener() {
			public void onDragEvent(VectorFeature vectorFeature, Pixel pixel) {
				// report(vectorFeature, type, pixel);
			}
		};
	}

}
