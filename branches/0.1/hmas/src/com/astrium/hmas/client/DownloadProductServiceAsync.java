package com.astrium.hmas.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DownloadProductServiceAsync {

	void getProducts(String url, AsyncCallback<String> callback);

}
