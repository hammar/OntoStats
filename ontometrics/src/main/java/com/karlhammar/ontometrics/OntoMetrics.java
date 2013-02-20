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
		formatter.printHelp( "OntoMetrics -f <PATH> [-h] [-n] | -l", options);
		System.exit(1);
	}
	
	private static void printPluginsAndQuit() {
		PluginService pluginService = PluginServiceFactory.createPluginService();
		Iterator<OntoMetricsPlugin> plugins = pluginService.getPlugins();
		while (plugins.hasNext()) {
			OntoMetricsPlugin plugin = plugins.next();
			System.out.println(String.format("%s\t\t%s", plugin.getMetricAbbreviation(), plugin.getName()));
		}
		System.exit(0);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Configure CLI parameters
		Options options = new Options();
		options.addOption("f", true, "File name of OWL file to process. Either this or the '-l' option is mandatory.");
		options.addOption("l", false, "If present, print listing of installed plugins and quit. Either this or the '-f' option is mandatory.");
		options.addOption("h", false, "Optional. If present, do not include header row.");
		options.addOption("n", false, "Optional. If present, return the input file name as first column. Useful for batch jobs including many files.");
		final Parser commandlineparser = new PosixParser();
		
		// Parse command line, displaying help message if needed
		CommandLine cl = null;
		try {
            cl = commandlineparser.parse(options, args, true);
        } 
		catch (final ParseException exp) {
        	logger.severe("Unexpected exception:" + exp.getMessage());
        }
		if (null == cl || (!cl.hasOption("f") && !cl.hasOption("l"))) {
			printHelpAndQuit(options);
		}
		
		if (cl.hasOption("l")) {
			printPluginsAndQuit();
		}
		
		// Initialize the plugins and results map
		PluginService pluginService = PluginServiceFactory.createPluginService();
        Map<String,String> results = new HashMap<String, String>();
		
		// Check that input file actually exists
		File ontologyFile=new File(cl.getOptionValue("f"));
		if (!ontologyFile.exists()) {
			printHelpAndQuit(options);
		}

        // Execute each plugin in turn
        // TODO: What happens if two plugins share abbreviation?
        Iterator<OntoMetricsPlugin> plugins = pluginService.getPlugins();
        while (plugins.hasNext()) {
        	OntoMetricsPlugin plugin = plugins.next();
        	plugin.init(ontologyFile);
        	
        	// An error in plugin execution can mean null values returned, so check
        	// for this.
        	String metricValue = plugin.getMetricValue(ontologyFile);
        	String metricAbbreviation = plugin.getMetricAbbreviation();
        	if (null != metricAbbreviation && null != metricValue) {
        		results.put(metricAbbreviation, metricValue);
        	}
        }
        
        // Iterate over results to generate output string
        String metricAbbreviations;
        String metricValues;
        if (cl.hasOption("n")) {
        	metricAbbreviations = "FileName";
        	metricValues = ontologyFile.getName();
        }
        else {
        	metricAbbreviations = "";
            metricValues = "";	
        }
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
