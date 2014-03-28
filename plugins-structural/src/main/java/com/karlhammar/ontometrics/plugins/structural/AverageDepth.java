package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import java.util.Optional;

import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class AverageDepth extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	public String getName() {
		return "Average depth plugin";
	}

	public String getMetricAbbreviation() {
		return "AvgDepth";
	}

	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == owlapi) {
			logger.severe("getMetricValue called before init()!");
		}

		OntologyTreeUtils otu = new OntologyTreeUtils();
		if (null == otu.getHeights()) {
			otu.calculateHeights(owlapi.getOntology());
		}
		List<Integer> heights = otu.getHeights();
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
		
		return Optional.of(avgHeight.toString());
	}
}
