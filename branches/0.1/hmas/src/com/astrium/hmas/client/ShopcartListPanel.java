package com.astrium.hmas.client;

import com.astrium.hmas.bean.DownloadProduct;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
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

public class ShopcartListPanel extends Composite implements HasText {

	private static ShopcartListPanelUiBinder uiBinder = GWT.create(ShopcartListPanelUiBinder.class);
	@UiField(provided = true)
	CellTable<DownloadProduct> shopcart_list_panel_cellTable = new CellTable<DownloadProduct>();
	public Column<DownloadProduct, String> shopcart_list_panel_download_column;
	
	private final OrderServiceAsync orderService = GWT.create(OrderService.class);
	
	public OrderServiceAsync getOrderService() {
		return orderService;
	}

	interface ShopcartListPanelUiBinder extends UiBinder<Widget, ShopcartListPanel> {
	}

	public ShopcartListPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		shopcart_list_panel_cellTable.getElement().getStyle().setOverflow(Overflow.AUTO);
		
		
		ButtonCell dwButton = new ButtonCell();
		shopcart_list_panel_download_column = new Column<DownloadProduct, String>(dwButton) {
			public String getValue(DownloadProduct object) {
				return "start";

			}
		};
		

		shopcart_list_panel_cellTable.addColumn(shopcart_list_panel_download_column, "Download");
		shopcart_list_panel_cellTable.setColumnWidth(shopcart_list_panel_download_column, 40, Unit.PX);
		
		
		TextColumn<DownloadProduct> nameColumn = new TextColumn<DownloadProduct>() {
			@Override
			public String getValue(DownloadProduct object) {
				if (object.platform != null) {
					return object.platform;
				} else {
					return "not informed";
				}

			}
		};
		shopcart_list_panel_cellTable.addColumn(nameColumn, "Platform");
		
		TextColumn<DownloadProduct> sensorColumn = new TextColumn<DownloadProduct>() {
			@Override
			public String getValue(DownloadProduct object) {
				if (object.sensor != null) {
					return object.sensor;
				} else {
					return "not informed";
				}

			}
		};
		shopcart_list_panel_cellTable.addColumn(sensorColumn, "Sensor");
		
		TextColumn<DownloadProduct> dateColumn = new TextColumn<DownloadProduct>() {
			@Override
			public String getValue(DownloadProduct object) {
				if (object.date != null) {
					return object.date;
				} else {
					return "not informed";
				}

			}
		};
		shopcart_list_panel_cellTable.addColumn(dateColumn, "Date");
	}

	
	public ShopcartListPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {
	}

	public String getText() {
		return null;
	}

}
