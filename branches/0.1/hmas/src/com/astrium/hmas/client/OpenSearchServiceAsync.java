package com.astrium.hmas.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OpenSearchServiceAsync {

	void getDescriptionFile(String url,
			AsyncCallback<Map<String, String>> callback);

}
