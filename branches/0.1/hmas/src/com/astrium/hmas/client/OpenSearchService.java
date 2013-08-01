package com.astrium.hmas.client;

import java.util.List;
import java.util.Map;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("os")
public interface OpenSearchService extends RemoteService {
	Map<String, String> getDescriptionFile(String url) throws IllegalArgumentException;

}
