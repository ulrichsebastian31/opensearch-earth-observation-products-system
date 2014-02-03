package com.astrium.hmas.roseo.capabilities;

import java.util.Map;

import javax.ws.rs.core.Response;

public abstract class GenericCapabilitiesService {
	
	Map<String, String> inputs;
    Response output;
    
    public Response getOutput() {
        return output;
    }
    
    public GenericCapabilitiesService() {
        
    }

	public abstract void getCapabilities(Map<String, String> inputs, String serverAdress) throws Exception;
}
