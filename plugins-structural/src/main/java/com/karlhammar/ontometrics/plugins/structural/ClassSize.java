package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class ClassSize implements OntoMetricsPlugin  {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingleton ss;
	
	public String getName() {
		return "Class size plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingleton.getSingletonObject(ontologyFile);
	}

	public String getMetricAbbreviation() {
		return "ClsSize";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		OntModel ontology = ss.getOntology();
		List<OntClass> classes = ontology.listNamedClasses().toList();
		// -1 to account for owl:Thing
		Integer result = classes.size() - 1;
		return result.toString();
	}
}
