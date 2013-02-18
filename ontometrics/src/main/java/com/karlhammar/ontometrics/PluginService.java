package com.karlhammar.ontometrics;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;
import java.util.Iterator;

public interface PluginService {
	Iterator<OntoMetricsPlugin> getPlugins();
    void initPlugins();
}
