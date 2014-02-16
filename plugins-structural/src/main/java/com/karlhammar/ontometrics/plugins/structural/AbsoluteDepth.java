package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.base.Optional;

import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class AbsoluteDepth extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	public String getName() {
		return "Absolute depth plugin";
	}
	
	public String getMetricAbbreviation() {
		return "AbsDepth";
	}

	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == owlapi) {
			logger.severe("getMetricValue called before init()!");
		}
		OntologyTreeUtils otu = new OntologyTreeUtils();
		
		if (null == otu.getHeights()) {
			otu.calculateHeights(owlapi.getOntology());
		}
		return Optional.of(getAbsoluteDepth(otu.getHeights()).toString());
	}
	
	public static Integer getAbsoluteDepth(List<Integer> heights)
	{
	    int height = 0;
	    Iterator<Integer> iter = heights.iterator();
	    while (iter.hasNext())
	    {
	    	int heightOfCurrentIteration = iter.next();
	    	height += heightOfCurrentIteration;
	    }
		return height;
	}
}
