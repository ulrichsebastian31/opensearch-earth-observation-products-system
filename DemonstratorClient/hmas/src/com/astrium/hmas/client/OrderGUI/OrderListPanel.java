package com.astrium.hmas.client.OrderGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OrderListPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the panel which
 *   																	handles the list of all the order 
 *   																	submitted by the user and their status
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.astrium.hmas.bean.CatalogueBean.CatalogueResult;
import com.astrium.hmas.bean.OrderBean.Order;
import com.astrium.hmas.client.OrderService.DeleteOrderService;
import com.astrium.hmas.client.OrderService.DeleteOrderServiceAsync;
import com.astrium.hmas.client.OrderService.GetOrderByIDService;
import com.astrium.hmas.client.OrderService.GetOrderByIDServiceAsync;
import com.astrium.hmas.client.OrderService.GetOrderListService;
import com.astrium.hmas.client.OrderService.GetOrderListServiceAsync;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class OrderListPanel extends Composite implements HasText {

	private static OrderListPanelUiBinder uiBinder = GWT.create(OrderListPanelUiBinder.class);

	@UiField(provided = true)
	CellTable<Order> order_list_panel_cellTable = new CellTable<Order>();

	interface OrderListPanelUiBinder extends UiBinder<Widget, OrderListPanel> {
	}
	/*
	 * Click on this button to get the XML file describing the order
	 */
	public Column<Order, String> order_list_panel_get_order_column; 
	/*
	 * Click on this button to cancel the order
	 */
	public Column<Order, String> order_list_panel_delete_order_column;

	/*
	 * Get Order list Service
	 */
	private final GetOrderListServiceAsync getOrderListService = GWT.create(GetOrderListService.class);
	/*
	 * Get Order by ID service
	 */
	private final GetOrderByIDServiceAsync getOrderByIDService = GWT.create(GetOrderByIDService.class);
	/*
	 * Delete Order service
	 */
	private final DeleteOrderServiceAsync deleteOrderService = GWT.create(DeleteOrderService.class);

	public GetOrderListServiceAsync getGetOrderListService() {
		return getOrderListService;
	}

	public GetOrderByIDServiceAsync getGetOrderByIDService() {
		return getOrderByIDService;
	}

	public DeleteOrderServiceAsync getDeleteOrderService() {
		return deleteOrderService;
	}

	public OrderListPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		/*
		 * Column which informs on the ID of the Order
		 */
		TextColumn<Order> idColumn = new TextColumn<Order>() {
			@Override
			public String getValue(Order object) {

				return object.id;

			}
		};
		/*
		 * Name settings
		 */
		order_list_panel_cellTable.addColumn(idColumn, "Order ID");
		order_list_panel_cellTable.setColumnWidth(idColumn, 60, Unit.PX);

		/*
		 * Column which informs on the status of the Order
		 */
		TextColumn<Order> statusColumn = new TextColumn<Order>() {
			@Override
			public String getValue(Order object) {

				return object.status;

			}
		};
		/*
		 * Name settings
		 */
		order_list_panel_cellTable.addColumn(statusColumn, "Status");

		/*
		 * Get order by ID column
		 */
		ButtonCell getOrderButton = new ButtonCell();
		order_list_panel_get_order_column = new Column<Order, String>(getOrderButton) {

			public String getValue(Order object) {
				/*
				 * Get the XML file returning the Order info
				 */
				return "Get XML";

			}
		};

		/*
		 * Name and size settings
		 */
		order_list_panel_cellTable.addColumn(order_list_panel_get_order_column, "Get Order");
		order_list_panel_cellTable.setColumnWidth(order_list_panel_get_order_column, 60, Unit.PX);

		/*
		 * Delete order by ID column
		 */
		ButtonCell deleteOrderButton = new ButtonCell();
		order_list_panel_delete_order_column = new Column<Order, String>(deleteOrderButton) {

			public String getValue(Order object) {
				/*
				 * Delete the Order
				 */
				return "Delete";

			}
		};

		/*
		 * Name and size settings
		 */
		order_list_panel_cellTable.addColumn(order_list_panel_delete_order_column, "Delete");
		order_list_panel_cellTable.setColumnWidth(order_list_panel_delete_order_column, 60, Unit.PX);
		/*
		 * Fill the order celltable by calling all the registered orders
		 */
		
		updateOrderList();
		getOrderByID();
		deleteOrder();
	}

	public OrderListPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

	public void updateOrderList() {
		getOrderListService.getOrders(new AsyncCallback<Map<String, Order>>() {

			@Override
			public void onSuccess(final Map<String, Order> result) {
				// TODO Auto-generated method stub
				System.out.println("Success get Order List");

				/*
				 * List of the results - Order - to place in the table
				 */
				final ArrayList<Order> result_list = new ArrayList<Order>();

				/*
				 * Displays the products in a table
				 */
				AsyncDataProvider<Order> provider = new AsyncDataProvider<Order>() {
					@Override
					protected void onRangeChanged(HasData<Order> display) {
						/*
						 * Get start and end of the list to display in the table
						 */
						int start = display.getVisibleRange().getStart();
						int end = start + display.getVisibleRange().getLength();
						end = end >= result.size() ? result.size() : end;
						/*
						 * Iterator on the server response : we want to put all
						 * the objects contained in result in the list
						 * result_list
						 */
						Iterator<String> iterator = result.keySet().iterator();

						while (iterator.hasNext()) {

							String key = (String) iterator.next();
							/*
							 * Get each object of the response
							 */
							Order value = (Order) result.get(key);
							/*
							 * Put them in the result_list list
							 */
							result_list.add(value);

							/*
							 * Remove current object
							 */
							iterator.remove();
						}
						updateRowData(start, result_list);

					}
				};
				/*
				 * Display the list in the cell table in the OrderList panel
				 */
				provider.addDataDisplay(order_list_panel_cellTable);
				provider.updateRowCount(result_list.size(), true);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("Fail get Order List");
			}
		});

	}
	
	public void getOrderByID(){
		order_list_panel_get_order_column.setFieldUpdater(new FieldUpdater<Order, String>() {
			
			@Override
			public void update(int index, Order object, String value) {
				// TODO Auto-generated method stub
				getOrderByIDService.getOrderByID(object, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("Fail get order by ID");
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						System.out.println("Success get order by ID");
						Window.open(result, "Order", "Order");
					}
				});
			}
		});
	}
	
	public void deleteOrder(){
		order_list_panel_delete_order_column.setFieldUpdater(new FieldUpdater<Order, String>() {

			@Override
			public void update(int index, Order object, String value) {
				// TODO Auto-generated method stub
				deleteOrderService.deleteOrder(object, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("Fail delete Order");
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						System.out.println("Success delete Order");
						updateOrderList();
					}
				});
			}
		});
	}

}
