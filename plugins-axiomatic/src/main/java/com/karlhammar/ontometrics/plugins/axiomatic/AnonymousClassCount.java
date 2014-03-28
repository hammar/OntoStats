package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.logging.Logger;
import java.util.Optional;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.karlhammar.ontometrics.plugins.*;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class AnonymousClassCount extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public String getName() {
		return "Anonymous class count plugin";
	}

	@Override
	public String getMetricAbbreviation() {
		return "Anonymous";
	}

	@Override
	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == jena) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = jena.getOntology();
		return Optional.of(calculateAnonymousClasses(ontology).toString());
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
