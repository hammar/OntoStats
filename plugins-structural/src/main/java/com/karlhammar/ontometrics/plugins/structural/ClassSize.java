package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.karlhammar.ontometrics.plugins.StructuralSingleton;
import com.karlhammar.ontometrics.plugins.StructuralSingletonOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class ClassSize extends OntoMetricsPlugin  {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingleton ss;
	
	public String getName() {
		return "Class size plugin";
	}

	public void init(StructuralSingleton jena, StructuralSingletonOWLAPI owlapi) {
		ss = jena;
	}

	public String getMetricAbbreviation() {
		return "ClassSize";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = ss.getOntology();
		return getClassSize(ontology).toString();
	}
	
	protected static Integer getClassSize(OntModel ontology) {
		List<OntClass> classes = ontology.listNamedClasses().toList();
		// -1 to account for owl:Thing
		Integer result = classes.size() - 1;
		return result;
	}
}
