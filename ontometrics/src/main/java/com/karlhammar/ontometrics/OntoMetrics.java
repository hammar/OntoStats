package com.karlhammar.ontometrics;

import java.io.File;

import com.karlhammar.ontometrics.plugins.api.PluginService;
import com.karlhammar.ontometrics.plugins.api.PluginServiceFactory;

public class OntoMetrics {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PluginService pluginService = PluginServiceFactory.createPluginService();
        pluginService.initPlugins();
		
        /*
		if (args.length != 1 || args[0] == null) {
			System.err.println("Expected usage: OntoMetrics.jar <PATH_TO_OWL_FILE>");
			System.exit(1);
		}
		
		File ontologyFile=new File(args[0]);
		if (!ontologyFile.exists()) {
			System.err.println(String.format("Provided file path (%s) does not exist.",args[0]));
			System.exit(1);
		}
		*/
		
	}

}
