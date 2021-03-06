package com.karlhammar.ontometrics.plugins.api;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class StandardPluginService implements PluginService {
	private static StandardPluginService pluginService;
    private ServiceLoader<OntoMetricsPlugin> serviceLoader;
    private Logger logger = Logger.getLogger(getClass().getName());
 
    private StandardPluginService()
    {
        //load all the classes in the classpath that have implemented the interface
        serviceLoader = ServiceLoader.load(OntoMetricsPlugin.class);
    }
 
    public static StandardPluginService getInstance()
    {
        if(pluginService == null)
        {
            pluginService = new StandardPluginService();
        }
        return pluginService;
    }
 
    public Iterator<OntoMetricsPlugin> getPlugins()
    {
        return serviceLoader.iterator();
    }
}
