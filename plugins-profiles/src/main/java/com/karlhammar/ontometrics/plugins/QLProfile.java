package com.karlhammar.ontometrics.plugins;

import java.io.File;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class QLProfile implements OntoMetricsPlugin {

	private static Logger logger = Logger.getLogger(QLProfile.class.getName());
	
	public String getName() {
		return "QL Profile Plugin";
	}

	public void init() {
		logger.info(getName() + " initialized!");
	}

	public String getMetricAbbreviation() {
		return "QLProfile";
	}

	public String getMetricValue(File ontologyFile) {
		// TODO Auto-generated method stub
		return null;
	}

}
