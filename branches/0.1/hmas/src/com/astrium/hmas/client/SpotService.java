package com.astrium.hmas.client;

import com.astrium.hmas.bean.SpotResult;
import com.astrium.hmas.bean.SpotSearch;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface SpotService extends RemoteService {
	SpotResult greetServer(SpotSearch spotSearch) throws IllegalArgumentException;
}
