package com.astrium.hmas.client;

import java.util.Map;

import com.astrium.hmas.bean.CatalogueResult;
import com.astrium.hmas.bean.CatalogueSearch;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CatalogueServiceAsync {

	void getResults(CatalogueSearch catalogueSearch,
			AsyncCallback<Map<String, CatalogueResult>> callback);

}
