package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class AverageDepth implements OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingletonOWLAPI ss;
	
	public String getName() {
		return "Average depth plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile);
	}

	public String getMetricAbbreviation() {
		return "AvgDepth";
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
		Float avgHeight;
		
		// If no heights are recorded then no classes are asserted in the ontology. In that case, 
		// return zero average height.
		if (heights.size() == 0) {
			avgHeight = new Float(0.0); 
		}
		// Otherwise, return average height by calculating total height by number of height paths.
		else {
			avgHeight = AbsoluteDepth.getAbsoluteDepth(heights).floatValue() / heights.size();
		}
		
		return avgHeight.toString();
	}
}
