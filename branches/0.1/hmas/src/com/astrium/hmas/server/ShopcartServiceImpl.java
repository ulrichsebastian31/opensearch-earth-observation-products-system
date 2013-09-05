package com.astrium.hmas.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.astrium.hmas.bean.CatalogueResult;
import com.astrium.hmas.bean.DownloadProduct;
import com.astrium.hmas.client.ShopcartService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ShopcartServiceImpl extends RemoteServiceServlet implements ShopcartService{

	@Override
	public Map<String, DownloadProduct> getShopcart(List<CatalogueResult> list_shopcart) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		int nb = list_shopcart.size();
		Map<String, DownloadProduct> map = new HashMap<String, DownloadProduct>();
		if(nb>0){
			for(int i=0;i<nb;i++){
				CatalogueResult catalogueResult = new CatalogueResult();
				catalogueResult = list_shopcart.get(i);
				
				DownloadProduct downloadProduct = new DownloadProduct();
				downloadProduct.setId(catalogueResult.getIdentifier());
				downloadProduct.setDate(catalogueResult.getStart() + "-" + catalogueResult.getEnd());
				downloadProduct.setPlatform(catalogueResult.getPlatform());
				downloadProduct.setSensor(catalogueResult.getInstrument());
				
				map.put(catalogueResult.getIdentifier(), downloadProduct);
				
			}
		}
		
		return map;
	}

}
