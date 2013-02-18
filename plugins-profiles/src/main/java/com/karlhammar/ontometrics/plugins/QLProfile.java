package com.karlhammar.ontometrics.plugins;

import java.io.File;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class QLProfile implements OntoMetricsPlugin {
	
	public String getName() {
		return "QL Profile Plugin";
	}

	public void init() {
		
	}

	public String getMetricAbbreviation() {
		return "QLProfile";
	}

	public String getMetricValue(File ontologyFile) {
		// TODO Auto-generated method stub
		return "FALSE";
	}

}
