	/**
	 *
	 *   Copyright 2013 sourceforge.
	 *
	 *   Licensed under the Apache License, Version 2.0 (the "License");
	 *   you may not use this file except in compliance with the License.
	 *   You may obtain a copy of the License at
	 *
	 *        http://www.apache.org/licenses/LICENSE-2.0
	 *
	 *   Unless required by applicable law or agreed to in writing, software
	 *   distributed under the License is distributed on an "AS IS" BASIS,
	 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 *   See the License for the specific language governing permissions and
	 *   limitations under the License.
	 */


package com.astrium.hmas.client;


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
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.handler.RegularPolygonHandler;
import org.gwtopenmaps.openlayers.client.handler.RegularPolygonHandlerOptions;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3MapType;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3Options;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public class MapPanel {
	
	 
	    private static final Projection DEFAULT_PROJECTION = new Projection(
	            "EPSG:4326");
	    public MapWidget mapWidget;
	    public DrawFeature drawRectangleFeatureControl = null;
	    public DrawFeature drawRectangleFeatureControlFeas = null;
	    public Vector vectorLayer = new Vector("Vector layer");
	    public Vector vectorLayerFeas = new Vector("Vector layer Feasibility");
	    public ModifyFeature modFeas = new ModifyFeature(vectorLayerFeas);
	    public ModifyFeature mod = new ModifyFeature(vectorLayer);
	 
	    public MapWidget getMapWidget() {
			return mapWidget;
		}

		public void setMapWidget(MapWidget mapWidget) {
			this.mapWidget = mapWidget;
		}

		@SuppressWarnings("deprecation")
		public void buildMap() {
	        //create some MapOptions
	        MapOptions defaultMapOptions = new MapOptions();
	        defaultMapOptions.setDisplayProjection(new Projection("EPSG:4326"));
	        defaultMapOptions.setNumZoomLevels(18);
	 
	        //Create a MapWidget
	        mapWidget = new MapWidget("100%", "100%", defaultMapOptions);
	 
	        //Create some Google Layers
	       GoogleV3Options gHybridOptions = new GoogleV3Options();
	        //gHybridOptions.setProjection("EPSG:4326");
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
	        
	        //Create some WMS Layers
	        
	        //WMS parameters
	        WMSParams wmsParams = new WMSParams();
	        wmsParams.setIsTransparent(false); // to make base layer remove transparency 
	        wmsParams.setFormat("image/png");
	        wmsParams.setLayers("topp:bmng_092004");
	        wmsParams.setStyles("");
	        //WMS Layer     
	        WMS wmsLayer = new WMS ("Global Imagery","http://localhost:8000/geoserver/wms",wmsParams);
	        wmsLayer.isBaseLayer();
	 
	        //And add them to the map
	        Map map = this.mapWidget.getMap();
	        map.addLayer(gHybrid);
	        map.addLayer(gNormal);
	        map.addLayer(gSatellite);
	        map.addLayer(gTerrain);
	        map.addLayer(wmsLayer);
	 
	        //Lets add some default controls to the map
	        map.addControl(new LayerSwitcher()); //+ sign in the upperright corner to display the layer switcher
	        map.addControl(new OverviewMap()); //+ sign in the lowerright to display the overviewmap
	        map.addControl(new ScaleLine()); //Display the scaleline
	 
	        //Center and zoom to a location
	        LonLat lonLat = new LonLat(0, 0);
	        lonLat.transform(DEFAULT_PROJECTION.getProjectionCode(),
	                         map.getProjection()); //transform lonlat to OSM coordinate system
	        map.setCenter(lonLat, 3);
	        
	        DeferredCommand.addCommand(new Command() {
	        	 
	            @Override
	       
	            public void execute() {
	       
	               mapWidget.getMap().updateSize();
	       
	            }
	       
	         });
	        
	        Style style = new Style();
	        style.setStrokeColor("#FFFFFF");
	        style.setStrokeWidth(3);
	        style.setFillOpacity(0);
	        
	        vectorLayer.setStyle(style);
	        vectorLayerFeas.setStyle(style);
	        map.addLayer(vectorLayer);
	        map.addLayer(vectorLayerFeas);
	        
	        
	        mod.setMode(mod.RESIZE, mod.DRAG);
	        
	        modFeas.setMode(modFeas.RESIZE, modFeas.DRAG);

	        DrawFeatureOptions drawFeatureOptions = new DrawFeatureOptions();
	        
	        RegularPolygonHandlerOptions rectangleOptions = new RegularPolygonHandlerOptions();
	        rectangleOptions.setRadius(90);
	        rectangleOptions.setIrregular(true);
	        rectangleOptions.setSides(4);
	        
	        drawFeatureOptions.setHandlerOptions(rectangleOptions);
	        drawRectangleFeatureControlFeas= new DrawFeature(vectorLayerFeas, new RegularPolygonHandler(), drawFeatureOptions);
	        drawRectangleFeatureControl = new DrawFeature(vectorLayer, new RegularPolygonHandler(), drawFeatureOptions);
	        
	        //DragFeature dragFeature = createDragFeature(vectorLayer);
	        //DragFeature dragFeatureFeas = createDragFeature(vectorLayerFeas);
	        
	        map.addControl(drawRectangleFeatureControlFeas);
	        map.addControl(drawRectangleFeatureControl);
	       /* map.addControl(dragFeature);
	        map.addControl(dragFeatureFeas);*/
	        map.addControl(mod);
	        map.addControl(modFeas);

	        this.mapWidget.getElement().getFirstChildElement().getStyle().setZIndex(0); //force the map to fall behind popups
	        
	    }
		
		public DrawFeature getDrawRectangleFeatureControl() {
			return drawRectangleFeatureControl;
		}

		public void setDrawRectangleFeatureControl(
				DrawFeature drawRectangleFeatureControl) {
			this.drawRectangleFeatureControl = drawRectangleFeatureControl;
		}

		public DrawFeature getDrawRectangleFeatureControlFeas() {
			return drawRectangleFeatureControlFeas;
		}

		public void setDrawRectangleFeatureControlFeas(
				DrawFeature drawRectangleFeatureControlFeas) {
			this.drawRectangleFeatureControlFeas = drawRectangleFeatureControlFeas;
		}

		//Maybe useful
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
                public void onDragEvent(VectorFeature vectorFeature,
                        Pixel pixel) {
                    //report(vectorFeature, type, pixel);
                }
            };
        }
        
	    
	    
	 
	

}
