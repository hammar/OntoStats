package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.StructuralSingleton;
import com.karlhammar.ontometrics.plugins.StructuralSingletonOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class AbsoluteDepth extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingletonOWLAPI ss;
	
	public String getName() {
		return "Absolute depth plugin";
	}

	public void init(StructuralSingleton jena, StructuralSingletonOWLAPI owlapi) {
		ss = owlapi;
	}

	public String getMetricAbbreviation() {
		return "AbsDepth";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.severe("getMetricValue called before init()!");
		}
		OntologyTreeUtils otu = new OntologyTreeUtils();
		
		if (null == otu.getHeights()) {
			otu.calculateHeights(ss.getOntology());
		}
		return getAbsoluteDepth(otu.getHeights()).toString();
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
