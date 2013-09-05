package com.astrium.hmas.client;

import com.astrium.hmas.bean.DownloadProduct;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OrderServiceAsync {

	void goDownload(DownloadProduct downloadProduct, AsyncCallback<String> callback);

}
