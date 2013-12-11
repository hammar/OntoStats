package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntModel;
import com.karlhammar.ontometrics.plugins.StructuralSingleton;
import com.karlhammar.ontometrics.plugins.StructuralSingletonOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class CpRatio extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingleton ss;
	
	public String getName() {
		return "Class to property ratio plugin";
	}

	public void init(StructuralSingleton jena, StructuralSingletonOWLAPI owlapi) {
		ss = jena;
	}

	public String getMetricAbbreviation() {
		return "CtoPRatio";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = ss.getOntology();
		Integer classSize = ClassSize.getClassSize(ontology);
		Integer propertySize = PropertySize.getPropertySize(ontology);
		Double ratio = ((double)classSize / propertySize);
		return ratio.toString();
	}
}
