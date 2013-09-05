package com.astrium.hmas.client;

import java.util.List;
import java.util.Map;

import com.astrium.hmas.bean.CatalogueResult;
import com.astrium.hmas.bean.DownloadProduct;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("shopcart")
public interface ShopcartService extends RemoteService{
	public  Map<String, DownloadProduct> getShopcart(List<CatalogueResult> list_shopcart) throws IllegalArgumentException;

}
