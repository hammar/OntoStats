package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.logging.Logger;

import com.google.common.base.Optional;

import com.hp.hpl.jena.ontology.OntModel;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class CpRatio extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	public String getName() {
		return "Class to property ratio plugin";
	}

	public String getMetricAbbreviation() {
		return "CtoPRatio";
	}

	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == jena) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = jena.getOntology();
		Integer classSize = ClassSize.getClassSize(ontology);
		Integer propertySize = PropertySize.getPropertySize(ontology);
		Double ratio = ((double)classSize / propertySize);
		return Optional.of(ratio.toString());
	}
}
