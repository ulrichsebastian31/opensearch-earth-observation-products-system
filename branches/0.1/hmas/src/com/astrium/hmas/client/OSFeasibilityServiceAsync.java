package com.astrium.hmas.client;

import java.util.Map;

import com.astrium.hmas.bean.Parameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OSFeasibilityServiceAsync {

	void getParameters(String url, AsyncCallback<Map<String, Parameter>> callback);

}
