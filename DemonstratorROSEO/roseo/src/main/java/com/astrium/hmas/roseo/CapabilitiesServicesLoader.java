package com.astrium.hmas.roseo;

import java.util.HashMap;
import java.util.Map;

import com.astrium.hmas.roseo.capabilities.GenericCapabilitiesService;

public class CapabilitiesServicesLoader {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Class<? extends GenericCapabilitiesService>> getProcesses() throws Exception {

		Map<String, Class<? extends GenericCapabilitiesService>> processes = null;

		String packageName = "com.astrium.hmas.roseo.capabilities";
		// Class abstractWPSProcessClass = Class.forName(packageName +
		// ".GenericCapabilitiesService");

		// Returning element
		processes = new HashMap<String, Class<? extends GenericCapabilitiesService>>();

		Class capabilitiesClass = Class.forName(packageName + ".GetCapabilities");
		Class capabilitiesServiceIDClass = Class.forName(packageName + ".GetCapabilitiesServiceIdentification");
		Class capabilitiesServiceProviderClass = Class.forName(packageName + ".GetCapabilitiesServiceProvider");
		Class capabilitiesContentsClass = Class.forName(packageName + ".GetCapabilitiesContents");

		processes.put("", capabilitiesClass);
		processes.put("ServiceIdentification", capabilitiesServiceIDClass);
		processes.put("ServiceProvider", capabilitiesServiceProviderClass);
		processes.put("Contents", capabilitiesContentsClass);

		return processes;
	}
}
