package com.karlhammar.ontometrics.plugins.api;

import java.util.Iterator;

public interface PluginService {
	Iterator<OntoMetricsPlugin> getPlugins();
    void initPlugins();
}
