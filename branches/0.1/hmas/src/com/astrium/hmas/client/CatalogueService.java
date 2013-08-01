package com.astrium.hmas.client;

import java.util.Map;

import com.astrium.hmas.bean.CatalogueResult;
import com.astrium.hmas.bean.CatalogueSearch;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("result")
public interface CatalogueService extends RemoteService{
	public Map<String, CatalogueResult> getResults(CatalogueSearch catalogueSearch) throws IllegalArgumentException;

}
