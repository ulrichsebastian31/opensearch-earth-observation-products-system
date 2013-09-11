package com.astrium.hmas.client.DownloadGUI;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DownloadProductPanel.java
 *   File Type                                          :               Source Code
 *   Description                                        :               This class describes the interface of
 *   																	the panel where the user can enter 
 *   																	a product URI and download it
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

import com.astrium.hmas.client.DownloadService.DownloadProductService;
import com.astrium.hmas.client.DownloadService.DownloadProductServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;

public class DownloadProductPanel extends Composite implements HasText {

	private static DownloadProductPanelUiBinder uiBinder = GWT.create(DownloadProductPanelUiBinder.class);
	/*
	 * The field where the user enters the URI of the product to download
	 */
	@UiField
	public TextBox download_product_panel_product_uri_area;

	/*
	 * Submit button
	 */
	@UiField
	public Button download_product_panel_download_button;

	/*
	 * Label which displays the response of the server in the case where the
	 * product cannot be downloaded directly
	 */
	@UiField
	public Label download_product_panel_response;

	/*
	 * Download Product service
	 */
	private final DownloadProductServiceAsync downloadProductServiceAsync = GWT.create(DownloadProductService.class);

	interface DownloadProductPanelUiBinder extends UiBinder<Widget, DownloadProductPanel> {
	}

	public DownloadProductPanel() {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public DownloadProductPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

	@UiHandler("download_product_panel_download_button")
	/*
	 * Download Product service call
	 */
	void onDownload_product_panel_download_buttonClick(ClickEvent event) {

		/*
		 * Check if the user have put an URI in the field
		 */
		String productURI = download_product_panel_product_uri_area.getValue();
		if (productURI.equals("product URI...")) {

			Window.alert("ERROR : would you please put a valid URI in the field!");

		} else {

			downloadProductServiceAsync.getProducts(productURI, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					System.out.println("fail");
				}

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
					System.out.println(result);
					/*
					 * If the server sends back an download url, the client
					 * opens it and download the product
					 */
					String http = "http";
					if (result.startsWith(http)) {

						Window.open(result, "dw", "");

					} else {
						/*
						 * Otherwise, the client displays the server message
						 */
						download_product_panel_response.setVisible(true);
						download_product_panel_response.setText(result);
					}

				}
			});
		}

	}
}
