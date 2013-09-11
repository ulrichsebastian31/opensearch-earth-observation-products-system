package com.astrium.hmas.client.ShopcartGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               ShopcartListPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the panel which
 *   																	handles the list of the products 
 *   																	placed in the shopcart
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
import com.astrium.hmas.bean.DownloadBean.DownloadProduct;
import com.astrium.hmas.client.ShopcartService.OrderService;
import com.astrium.hmas.client.ShopcartService.OrderServiceAsync;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

public class ShopcartListPanel extends Composite implements HasText {

	private static ShopcartListPanelUiBinder uiBinder = GWT.create(ShopcartListPanelUiBinder.class);
	@UiField(provided = true)
	/*
	 * Table in which the products to dowload will be displayed
	 */
	public CellTable<DownloadProduct> shopcart_list_panel_cellTable = new CellTable<DownloadProduct>();
	/*
	 * Button column to be able to pass the URI product to the Download panel
	 */
	public Column<DownloadProduct, String> shopcart_list_panel_download_column;

	/*
	 * Order Service
	 */
	private final OrderServiceAsync orderService = GWT.create(OrderService.class);

	/*
	 * and its Getter
	 */
	public OrderServiceAsync getOrderService() {
		return orderService;
	}

	interface ShopcartListPanelUiBinder extends UiBinder<Widget, ShopcartListPanel> {
	}

	/*
	 * ShopcartListPanel constructor : build the table
	 */
	public ShopcartListPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		shopcart_list_panel_cellTable.getElement().getStyle().setOverflow(Overflow.AUTO);

		/*
		 * Button column to send the URI product to the DownloadProduct panel
		 * and then download it
		 */
		ButtonCell dwButton = new ButtonCell();
		shopcart_list_panel_download_column = new Column<DownloadProduct, String>(dwButton) {

			public String getValue(DownloadProduct object) {
				/*
				 * Start the download
				 */
				return "start";

			}
		};

		/*
		 * Name and size settings
		 */
		shopcart_list_panel_cellTable.addColumn(shopcart_list_panel_download_column, "Download");
		shopcart_list_panel_cellTable.setColumnWidth(shopcart_list_panel_download_column, 40, Unit.PX);

		/*
		 * Column which informs on the name of the platform's product to
		 * downlaod
		 */
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
		/*
		 * Name settings
		 */
		shopcart_list_panel_cellTable.addColumn(nameColumn, "Platform");

		/*
		 * Column which informs on the name of the sensor's product to downlaod
		 */
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
		/*
		 * Name settings
		 */
		shopcart_list_panel_cellTable.addColumn(sensorColumn, "Sensor");

		/*
		 * Column which informs on the date of the acquisition's product to
		 * downlaod
		 */
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
		/*
		 * Name settings
		 */
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
