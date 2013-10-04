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
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;

public class ShopcartListPanel extends Composite implements HasText {

	private static ShopcartListPanelUiBinder uiBinder = GWT.create(ShopcartListPanelUiBinder.class);
	@UiField(provided = true)
	/*
	 * Table in which the products to dowload will be displayed
	 */
	public CellTable<DownloadProduct> shopcart_list_panel_cellTable = new CellTable<DownloadProduct>();
	/*
	 * Button to submit the Order
	 */
	@UiField
	public Button shopcart_list_panel_order_submit_button;
	/*
	 * Button column to be able to pass the URI product to the Download panel
	 */
	public Column<DownloadProduct, String> shopcart_list_panel_download_column;
	/*
	 * Boolean to check if the options to order a produit have been chosen
	 */
	public boolean isOptionsChosen = false;

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
		 * The Order button is disable as long as the user hasn't choose the options for each produit in his shopcart
		 */
		shopcart_list_panel_order_submit_button.setEnabled(false);
		
		/*
		 * Column which tell the user if the options to order the product has been set or not
		 */
		Column<DownloadProduct, String> readyToOrderColumn = new Column<DownloadProduct, String>(new ClickableTextCell() {
			public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<img width=\"50\" src=\"" + value.asString() + "\">");
			}
		}) {

			@Override
			public String getValue(DownloadProduct object) {
				return isOptionsChosen ? "http://www.clker.com/cliparts/e/2/a/d/1206574733930851359Ryan_Taylor_Green_Tick.svg.hi.png" : "http://www.clker.com/cliparts/d/9/y/V/R/R/red-cross-hi.png";
			}
		};

		shopcart_list_panel_cellTable.addColumn(readyToOrderColumn, "Ready");
		shopcart_list_panel_cellTable.setColumnWidth(readyToOrderColumn, 40, Unit.PX);

		/*
		 * Button column to get the available options to order the product
		 */
		ButtonCell optionsButton = new ButtonCell();
		shopcart_list_panel_download_column = new Column<DownloadProduct, String>(optionsButton) {

			public String getValue(DownloadProduct object) {
				/*
				 * Get the available options
				 */
				return "Options";

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
