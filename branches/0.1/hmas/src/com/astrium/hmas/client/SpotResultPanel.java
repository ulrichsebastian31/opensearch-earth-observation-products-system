package com.astrium.hmas.client;

import com.astrium.hmas.bean.DownloadProduct;
import com.astrium.hmas.bean.SpotScene;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.DefaultSelectionEventManager;

public class SpotResultPanel extends Composite implements HasText {

	private static ResultsSpotPanelUiBinder uiBinder = GWT
			.create(ResultsSpotPanelUiBinder.class);
	@UiField(provided=true) CellTable<SpotScene> spot_result_panel_cellTable = new CellTable<SpotScene>();
	@UiField AbsolutePanel spot_result_panel_absolutePanel;
	public MultiSelectionModel<SpotScene> spot_result_panel_selectionModelShopcart = new MultiSelectionModel<SpotScene>();
	public NoSelectionModel<SpotScene> spot_result_panel_selectionModelDetails = new NoSelectionModel<SpotScene>();
	public Column<SpotScene,String> showColumn;
	public boolean hideButtonClicked = false;
	
	interface ResultsSpotPanelUiBinder extends
			UiBinder<Widget, SpotResultPanel> {
	}
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final SpotServiceAsync greetingService = GWT
			.create(SpotService.class);

	@SuppressWarnings("unchecked")
	public SpotResultPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		spot_result_panel_absolutePanel.getElement().getStyle().setOverflow(Overflow.AUTO);
		Column<SpotScene, Boolean> shopcartColumn = new Column<SpotScene, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(SpotScene object) {
		        // Get the value from the selection model.
		        return false;
		      }
		    };
		    
	   spot_result_panel_cellTable.addColumn(shopcartColumn, "Shopcart");
	   spot_result_panel_cellTable.setColumnWidth(shopcartColumn, 40, Unit.PX);

	   //TODO: selection model shopcart
	   //DefaultSelectionEventManager selectionEventManager = DefaultSelectionEventManager.createCheckboxManager();
	   //spot_result_panel_cellTable.setSelectionModel(spot_result_panel_selectionModelShopcart, (com.google.gwt.view.client.CellPreviewEvent.Handler<SpotScene>) selectionEventManager);
			 
	   
		ButtonCell downloadButton = new ButtonCell();
		showColumn = new Column<SpotScene,String>(downloadButton) {
			     public String getValue(SpotScene object) {
			       return hideButtonClicked?"Show":"Hide";
			     }
		};
			 
	        
		spot_result_panel_cellTable.addColumn(showColumn, "Show");
		spot_result_panel_cellTable.setColumnWidth(showColumn, 40, Unit.PX);
		
		Column<SpotScene, String> quickLookColumn = new Column<SpotScene, String>(
					        new ClickableTextCell() 
					        {
					            public void render(Context context, 
					                               SafeHtml value, 
					                               SafeHtmlBuilder sb)
					            {
					                sb.appendHtmlConstant("<img width=\"50\" src=" 
					                                       + value.asString() + "\">");
					            }
					        })
					        {
					            @Override
					            public String getValue(SpotScene object) {
					                return object.image_url;
					            }
					        };
					 
			 
			    
		spot_result_panel_cellTable.addColumn(quickLookColumn, "Quicklook");
		spot_result_panel_cellTable.setColumnWidth(quickLookColumn, 40, Unit.PX);
	    
	    TextColumn<SpotScene> nameColumn = new TextColumn<SpotScene>() {
	      @Override
	      public String getValue(SpotScene object) {
	        return object.satellite;
	      }
	    };
	    spot_result_panel_cellTable.addColumn(nameColumn, "Satellite");
	    
	    TextColumn<SpotScene> stationColumn = new TextColumn<SpotScene>() {
	      @Override
	      public String getValue(SpotScene object) {
	        return object.archiving_station;
	      }
	    };
	    spot_result_panel_cellTable.addColumn(stationColumn, "Sensor");
	    
	    TextColumn<SpotScene> dateColumn = new TextColumn<SpotScene>() {
	      @Override
	      public String getValue(SpotScene object) {
	        return object.acquisition_date;
	      }
	    };
	    
	    spot_result_panel_cellTable.addColumn(dateColumn, "Date");
	    
	    TextColumn<SpotScene> cloudColumn = new TextColumn<SpotScene>() {
		      @Override
		      public String getValue(SpotScene object) {
		        return String.valueOf(object.cloud_cover);
		      }
		    };
		    spot_result_panel_cellTable.addColumn(cloudColumn, "Cloud coverage");
		    
	    
	}

	public SpotResultPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}



	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
