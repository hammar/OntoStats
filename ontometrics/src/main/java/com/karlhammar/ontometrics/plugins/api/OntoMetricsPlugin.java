package com.karlhammar.ontometrics.plugins.api;

import java.io.File;

public interface OntoMetricsPlugin {
	String getName();
	void init();
	String getMetricAbbreviation();
	String getMetricValue(File ontologyFile);
}
