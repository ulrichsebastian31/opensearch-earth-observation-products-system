package com.astrium.hmas.client;

import com.astrium.hmas.bean.DownloadProduct;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("order")
public interface OrderService extends RemoteService{
	public String goDownload(DownloadProduct downloadProduct) throws IllegalArgumentException;

}
