package com.astrium.hmas.client;

import java.util.Map;

import com.astrium.hmas.bean.Parameter;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("osfeasibility")
public interface OSFeasibilityService extends RemoteService{
	public Map<String, Parameter> getParameters(String url) throws IllegalArgumentException;

}
