package com.astrium.hmas.client;

import com.astrium.hmas.bean.SpotResult;
import com.astrium.hmas.bean.SpotSearch;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>SpotService</code>.
 */
public interface SpotServiceAsync {
	void greetServer(SpotSearch spotSearch, AsyncCallback<SpotResult> callback);
}
