/**
 * 
 */
package com.astrium.hmas.client;


import java.util.Arrays;
import java.util.List;

import com.astrium.hmas.bean.CatalogueResult;
import com.astrium.hmas.bean.SpotScene;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SimpleRadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.TreeViewModel.NodeInfo;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;

/**
 * @author re-cetienne
 *
 */
public class CatalogueResultPanel extends Composite implements HasText {

	private static CatalogueResultPanelUiBinder uiBinder = GWT
			.create(CatalogueResultPanelUiBinder.class);
	@UiField(provided=true) CellTable<CatalogueResult> catalogue_result_panel_celltable = new CellTable<CatalogueResult>();
	@UiField Button catalogue_result_panel_xml_show_button;
	//public MultiSelectionModel<SpotScene> catalogue_result_panel_selectionModelShopcart = new MultiSelectionModel<SpotScene>();
	public NoSelectionModel<CatalogueResult> catalogue_result_panel_selectionModelDetails = new NoSelectionModel<CatalogueResult>();
	public Column<CatalogueResult,String> catalogue_result_panel_showColumn;
	public boolean hideButtonClicked = false;
	
	

	interface CatalogueResultPanelUiBinder extends UiBinder<Widget, CatalogueResultPanel> {
	}

	public CatalogueResultPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		catalogue_result_panel_celltable.getElement().getStyle().setOverflow(Overflow.AUTO);
		Column<CatalogueResult, Boolean> shopcartColumn = new Column<CatalogueResult, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(CatalogueResult object) {
		        // Get the value from the selection model.
		        return false;
		      }
		    };

		    catalogue_result_panel_celltable.addColumn(shopcartColumn, "Shopcart");
		    catalogue_result_panel_celltable.setColumnWidth(shopcartColumn, 40, Unit.PX);
		    

			ButtonCell downloadButton = new ButtonCell();
			catalogue_result_panel_showColumn = new Column<CatalogueResult,String>(downloadButton) {
				     public String getValue(CatalogueResult object) {
				       return hideButtonClicked?"Show":"Hide";
				     }
			};
				 
		        
			catalogue_result_panel_celltable.addColumn(catalogue_result_panel_showColumn, "Show");
			catalogue_result_panel_celltable.setColumnWidth(catalogue_result_panel_showColumn, 40, Unit.PX);
			
			/*Column<CatalogueResult, String> quickLookColumn = new Column<CatalogueResult, String>(
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
						            public String getValue(CatalogueResult object) {
						                return object.image_url;
						            }
						        };
	    
			catalogue_result_panel_celltable.addColumn(quickLookColumn, "Quicklook");
			catalogue_result_panel_celltable.setColumnWidth(quickLookColumn, 40, Unit.PX);*/
							    
			TextColumn<CatalogueResult> nameColumn = new TextColumn<CatalogueResult>() {
							      @Override
					public String getValue(CatalogueResult object) {
							    	  if(object.platform != null){
							    		  return object.platform;
							    	  }else{
							    		  return "not informed";
							    	  }
							        
					}
			};
			catalogue_result_panel_celltable.addColumn(nameColumn, "Platform");
							    
			TextColumn<CatalogueResult> sensorColumn = new TextColumn<CatalogueResult>() {
							      @Override
					public String getValue(CatalogueResult object) {
							    	  if(object.instrument != null){
							    		  return object.instrument;
							    	  }else{
							    		  return "not informed";
							    	  }
					}
			};
			catalogue_result_panel_celltable.addColumn(sensorColumn, "Sensor");
							    
			TextColumn<CatalogueResult> dateColumn = new TextColumn<CatalogueResult>() {
							      @Override
					public String getValue(CatalogueResult object) {
							        return object.start + " - " + object.end;
					}
			};
							    
			catalogue_result_panel_celltable.addColumn(dateColumn, "Date");
							    
			TextColumn<CatalogueResult> cloudColumn = new TextColumn<CatalogueResult>() {
								      @Override
					public String getValue(CatalogueResult object) {
								    	  if(object.cloudCover != null){
								    		  return object.cloudCover;
								    	  }else{
								    		  return "not informed";
								    	  }
					}
			};
			catalogue_result_panel_celltable.addColumn(cloudColumn, "Cloud coverage");
		
	}



	public CatalogueResultPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

		// Can access @UiField after calling createAndBindUi
	}

	public void setText(String text) {

	}

	/**
	 * Gets invoked when the default constructor is called
	 * and a string is provided in the ui.xml file.
	 */
	public String getText() {
		return null;
	}

}
