package com.astrium.hmas.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.astrium.hmas.bean.DownloadProduct;
import com.astrium.hmas.shared.UrlValidator;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;

public class DownloadProductPanel extends Composite implements HasText {

	private static DownloadProductPanelUiBinder uiBinder = GWT
			.create(DownloadProductPanelUiBinder.class);
	@UiField TextBox download_product_panel_product_uri_area;
	@UiField Button download_product_panel_download_button;
	@UiField Label download_product_panel_response;
	
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
	void onDownload_product_panel_download_buttonClick(ClickEvent event) {

			String productURI = download_product_panel_product_uri_area.getValue();
			if(productURI.equals("product URI...")){
				Window.alert("ERROR : Opensearch URL not valid!");
			} else {
				downloadProductServiceAsync.getProducts(productURI,new AsyncCallback<String>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("fail");
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						System.out.println(result);
						String http = "http";
						if(result.startsWith(http)){
							Window.open(result, "dw", "");
						}else{
							download_product_panel_response.setVisible(true);
							download_product_panel_response.setText(result);
						}
						
						
					}});
			}
	   

	  
	}
}
