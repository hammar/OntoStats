package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class MaximumDepth extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	public String getName() {
		return "Maximum depth plugin";
	}

	public String getMetricAbbreviation() {
		return "MaxDepth";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == owlapi) {
			logger.severe("getMetricValue called before init()!");
		}

		OntologyTreeUtils otu = new OntologyTreeUtils();
		if (null == otu.getHeights()) {
			otu.calculateHeights(owlapi.getOntology());
		}
		List<Integer> heights = otu.getHeights();
		
		// If no heights are recorded then no classes are asserted in the ontology. In that case, 
		// return zero maximum height.
		if (heights.size() == 0) {
			return "0"; 
		}
		// Otherwise, sort height paths and return highest.
		else {
			Collections.sort(heights, Collections.reverseOrder());
			return heights.get(0).toString();
		}
	}
}
