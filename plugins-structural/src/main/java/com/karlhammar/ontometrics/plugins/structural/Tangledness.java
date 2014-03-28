package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.logging.Logger;

import java.util.Optional;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class Tangledness extends OntoMetricsPlugin {
	
	private Logger logger = Logger.getLogger(getClass().getName());

	public String getName() {
		return "Tangledness plugin";
	}

	public String getMetricAbbreviation() {
		return "Tangledness";
	}

	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == jena) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = jena.getOntology();
		Integer nrOfClasses = 0;
		Integer nrOfMultiParentClasses = 0;
		ExtendedIterator<OntClass> allClasses = ontology.listClasses();
		while (allClasses.hasNext()) {
			OntClass c = allClasses.next();
			nrOfClasses++;
			ExtendedIterator<OntClass> superClasses = c.listSuperClasses(true);
			Integer parentClasses = 0;
			while (superClasses.hasNext()) {
				superClasses.next();
				parentClasses++;
			}
			if (parentClasses > 1) {
				nrOfMultiParentClasses++;
			}
		}
		Float Tangledness = ((float)nrOfMultiParentClasses / nrOfClasses);
		return Optional.of(Tangledness.toString());
	}

}
