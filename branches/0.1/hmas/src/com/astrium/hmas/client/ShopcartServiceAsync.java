package com.astrium.hmas.client;

import java.util.List;
import java.util.Map;

import com.astrium.hmas.bean.CatalogueResult;
import com.astrium.hmas.bean.DownloadProduct;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ShopcartServiceAsync {

	void getShopcart(List<CatalogueResult> list_shopcart, AsyncCallback<Map<String, DownloadProduct>> callback);

}
