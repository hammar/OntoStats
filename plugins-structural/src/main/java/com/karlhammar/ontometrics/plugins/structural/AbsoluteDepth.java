package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class AbsoluteDepth implements OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingletonOWLAPI ss;
	
	public String getName() {
		return "Absolute depth plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile);
	}

	public String getMetricAbbreviation() {
		return "AbsDepth";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		if (null == ss.getHeights()) {
			ss.calculateHeights();
		}
		return getAbsoluteDepth(ss.getHeights()).toString();
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
