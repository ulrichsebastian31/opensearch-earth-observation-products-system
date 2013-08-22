package com.astrium.hmas.client;

import java.util.Map;

import com.astrium.hmas.bean.FeasibilityResult;
import com.astrium.hmas.bean.FeasibilitySearch;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("feasibilityResult")
public interface FeasibilityService extends RemoteService{
	public Map<String, FeasibilityResult> getResults(FeasibilitySearch feasibilitySearch) throws IllegalArgumentException;

}
