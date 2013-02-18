package com.karlhammar.ontometrics;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;
import com.karlhammar.ontometrics.plugins.api.PluginService;
import com.karlhammar.ontometrics.plugins.api.PluginServiceFactory;

public class OntoMetrics {
	
	private static Logger logger = Logger.getLogger("com.karlhammar.ontometrics.OntoMetrics");

	private static void printHelpAndQuit(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "OntoMetrics -f <PATH> [-h]", options);
		System.exit(1);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Configure CLI parameters
		Options options = new Options();
		options.addOption("f", true, "Mandatory. File name of OWL file to process.");
		options.addOption("h", false, "Optional. If present, do not include header row.");
		final Parser commandlineparser = new PosixParser();
		
		// Parse command line, displaying help message if needed
		CommandLine cl = null;
		try {
            cl = commandlineparser.parse(options, args, true);
        } 
		catch (final ParseException exp) {
        	logger.severe("Unexpected exception:" + exp.getMessage());
        }
		if (null == cl || !cl.hasOption("f")) {
			printHelpAndQuit(options);
		}
		
		// Check that input file actually exists
		File ontologyFile=new File(cl.getOptionValue("f"));
		if (!ontologyFile.exists()) {
			printHelpAndQuit(options);
		}

		// Initialize the plugins and results map
		PluginService pluginService = PluginServiceFactory.createPluginService();
        pluginService.initPlugins();
        Map<String,String> results = new HashMap<String, String>();
        
        // Execute each plugin in turn
        // TODO: What happens if two plugins share abbreviation?
        Iterator<OntoMetricsPlugin> plugins = pluginService.getPlugins();
        while (plugins.hasNext()) {
        	OntoMetricsPlugin plugin = plugins.next();
        	results.put(plugin.getMetricAbbreviation(), plugin.getMetricValue(ontologyFile));
        }
        
        // Iterate over results to generate output string
        String metricAbbreviations = "";
        String metricValues = "";
        for (String key: results.keySet()) {
        	if (metricAbbreviations.length() == 0) {
        		metricAbbreviations = key;
        		metricValues = results.get(key);
        	}
        	else {
        		metricAbbreviations += (";" + key);
        		metricValues += (";" + results.get(key));
        	}
        }
        
        // Print output string (headers only if parameter has not been set)
        if (!cl.hasOption("h")) {
        	System.out.println(metricAbbreviations);
        }
        System.out.println(metricValues);
	}
}
