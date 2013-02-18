package com.karlhammar.ontometrics.plugins;

import java.io.File;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class ELProfile implements OntoMetricsPlugin {
	
	public String getName() {
		return "EL Profile Plugin";
	}

	public void init() {
		
	}

	public String getMetricAbbreviation() {
		return "ELProfile";
	}

	public String getMetricValue(File ontologyFile) {
		// TODO Auto-generated method stub
		return "TRUE";
	}

}
