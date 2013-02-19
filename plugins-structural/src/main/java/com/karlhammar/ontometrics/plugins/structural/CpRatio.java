package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntModel;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class CpRatio implements OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingletonJena ss;
	
	public String getName() {
		return "Class to property ratio plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingletonJena.getSingletonObject(ontologyFile);
	}

	public String getMetricAbbreviation() {
		return "CtoPRatio";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		OntModel ontology = ss.getOntology();
		Integer classSize = ClassSize.getClassSize(ontology);
		Integer propertySize = PropertySize.getPropertySize(ontology);
		Double ratio = ((double)classSize / propertySize);
		return ratio.toString();
	}
}
