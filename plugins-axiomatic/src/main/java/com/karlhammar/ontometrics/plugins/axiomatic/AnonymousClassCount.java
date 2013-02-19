package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class AnonymousClassCount implements OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingleton ss;
	
	public String getName() {
		return "Anonymous class count plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingleton.getSingletonObject(ontologyFile);
	}

	public String getMetricAbbreviation() {
		return "AnonymousClasses";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		OntModel ontology = ss.getOntology();
		return calculateAnonymousClasses(ontology).toString();
	}

	private static Integer calculateAnonymousClasses(OntModel m) {
		ExtendedIterator<OntClass> allClasses = m.listClasses();
		int nrOfAnonymousClasses = 0;
		while (allClasses.hasNext()) {
			OntClass c = allClasses.next();
			if (c.isAnon()) {
				nrOfAnonymousClasses++;
			}
		}
		return nrOfAnonymousClasses;
	}
}
