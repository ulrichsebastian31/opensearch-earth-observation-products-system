package com.astrium.hmas.client;

import java.util.Map;

import com.astrium.hmas.bean.FeasibilityResult;
import com.astrium.hmas.bean.FeasibilitySearch;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FeasibilityServiceAsync {

	void getResults(FeasibilitySearch feasibilitySearch,
			AsyncCallback<Map<String, FeasibilityResult>> callback);

}
