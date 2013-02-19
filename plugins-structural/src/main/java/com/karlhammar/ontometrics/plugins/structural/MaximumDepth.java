package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class MaximumDepth implements OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingletonOWLAPI ss;
	
	public String getName() {
		return "Maximum depth plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile);
	}

	public String getMetricAbbreviation() {
		return "MaxDepth";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		if (null == ss.getHeights()) {
			ss.calculateHeights();
		}
		List<Integer> heights = ss.getHeights();
		Collections.sort(heights, Collections.reverseOrder());
		return heights.get(0).toString();
	}
}
