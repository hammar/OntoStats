package com.karlhammar.ontometrics;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginServiceFactory {
	public static PluginService createPluginService()
    {
        addPluginJarsToClasspath();
        return StandardPluginService.getInstance();
    }
 
    private static void addPluginJarsToClasspath()
    {
        try
        {
            //add the plugin directory to classpath
            ClasspathUtils.addDirToClasspath(new File("plugins"));
        } 
        catch (IOException ex)
        {
            Logger.getLogger(PluginServiceFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
