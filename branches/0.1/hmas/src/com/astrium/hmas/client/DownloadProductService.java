package com.astrium.hmas.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Download")
public interface DownloadProductService extends RemoteService{
	public String getProducts(String url) throws IllegalArgumentException;

}
