package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import java.util.Optional;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class ClassSize extends OntoMetricsPlugin  {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	public String getName() {
		return "Class size plugin";
	}

	public String getMetricAbbreviation() {
		return "ClassSize";
	}

	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == jena) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = jena.getOntology();
		return Optional.of(getClassSize(ontology).toString());
	}
	
	protected static Integer getClassSize(OntModel ontology) {
		List<OntClass> classes = ontology.listNamedClasses().toList();
		// -1 to account for owl:Thing
		Integer result = classes.size() - 1;
		return result;
	}
}
