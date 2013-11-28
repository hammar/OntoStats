package com.karlhammar.ontometrics.plugins.api;

import java.io.File;
import java.util.Iterator;

public interface PluginService {
	Iterator<OntoMetricsPlugin> getPlugins();
    void initPlugins(File ontologyFile);
}
