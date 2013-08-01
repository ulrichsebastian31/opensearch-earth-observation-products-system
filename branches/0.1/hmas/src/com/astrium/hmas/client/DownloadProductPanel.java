package com.astrium.hmas.client;

import java.util.Arrays;
import java.util.List;

import com.astrium.hmas.bean.DownloadProduct;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class DownloadProductPanel extends Composite implements HasText {

	private static DownloadProductPanelUiBinder uiBinder = GWT
			.create(DownloadProductPanelUiBinder.class);
	@UiField(provided=true) CellTable<DownloadProduct> cellTable = new CellTable<DownloadProduct>();
	
	private static List<DownloadProduct> PRODUCTS = Arrays.asList(
			new DownloadProduct(false, 65, "Sentinel-1 A", "C-SAR", "29.06.2013", 3541),
			new DownloadProduct(true, 12, "Sentinel-1 A", "C-SAR", "29.06.2013", 41354),
			new DownloadProduct(true, 45, "Sentinel-1 A", "C-SAR", "29.06.2013", 321),
			new DownloadProduct(true, 28, "Sentinel-1 A", "C-SAR", "29.06.2013", 88413),
			new DownloadProduct(true, 52, "Sentinel-1 A", "C-SAR", "29.06.2013", 10123)
			);

	interface DownloadProductPanelUiBinder extends UiBinder<Widget, DownloadProductPanel> {
	}

	public DownloadProductPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		cellTable.setPageSize(5);
		
		Column<DownloadProduct, Boolean> availibityColumn = new Column<DownloadProduct, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(DownloadProduct object) {
		        // Get the value from the selection model.
		        return object.isAvailibity();
		      }
		    };
		    
	   cellTable.addColumn(availibityColumn, "Availibity");
	   cellTable.setColumnWidth(availibityColumn, 40, Unit.PX);
	   
	   ButtonCell downloadButton = new ButtonCell();
	   Column<DownloadProduct,String> downloadColumn = new Column<DownloadProduct,String>(downloadButton) {
	     public String getValue(DownloadProduct object) {
	       return "Start";
	     }
	   };
			    
		cellTable.addColumn(downloadColumn, "Action");
		cellTable.setColumnWidth(downloadColumn, 40, Unit.PX);
		
	    TextColumn<DownloadProduct> statusColumn = new TextColumn<DownloadProduct>() {
	      @Override
	      public String getValue(DownloadProduct object) {
	        return Integer.toString(object.status);
	      }
	    };
	    cellTable.addColumn(statusColumn, "Status");
	    
	    TextColumn<DownloadProduct> platformColumn = new TextColumn<DownloadProduct>() {
		      @Override
		      public String getValue(DownloadProduct object) {
		        return object.platform;
		      }
		    };
		    cellTable.addColumn(platformColumn, "Platform");
	    
	 // Add a text column to show the sensor name.
	    TextColumn<DownloadProduct> sensorColumn = new TextColumn<DownloadProduct>() {
	      @Override
	      public String getValue(DownloadProduct object) {
	        return object.sensor;
	      }
	    };
	    cellTable.addColumn(sensorColumn, "Sensor");

	 // Add a text column to show the date.
	    TextColumn<DownloadProduct> dateColumn = new TextColumn<DownloadProduct>() {
	      @Override
	      public String getValue(DownloadProduct object) {
	        return object.date;
	      }
	    };
	    cellTable.addColumn(dateColumn, "Date");
	    
	    TextColumn<DownloadProduct> idColumn = new TextColumn<DownloadProduct>() {
		      @Override
		      public String getValue(DownloadProduct object) {
		        return Integer.toString(object.id);
		      }
		    };
		    cellTable.addColumn(idColumn, "ID");
	    
	    
	    AsyncDataProvider<DownloadProduct> provider = new AsyncDataProvider<DownloadProduct>() {
	        @Override
	        protected void onRangeChanged(HasData<DownloadProduct> display) {
	          int start = display.getVisibleRange().getStart();
	          int end = start + display.getVisibleRange().getLength();
	          end = end >= PRODUCTS.size() ? PRODUCTS.size() : end;
	          List<DownloadProduct> sub = PRODUCTS.subList(start, end);
	          updateRowData(start, sub);
	        }
	      };
	      provider.addDataDisplay(cellTable);
	      provider.updateRowCount(PRODUCTS.size(), true);
	}


	public DownloadProductPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}


	public void setText(String text) {

	}

	public String getText() {
		return null;
	}
}
