package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.karlhammar.ontometrics.plugins.StructuralSingleton;
import com.karlhammar.ontometrics.plugins.StructuralSingletonOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class Tangledness extends OntoMetricsPlugin {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingleton ss;

	public String getName() {
		return "Tangledness plugin";
	}

	public void init(StructuralSingleton jena, StructuralSingletonOWLAPI owlapi) {
		ss = jena;
	}

	public String getMetricAbbreviation() {
		return "Tangledness";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = ss.getOntology();
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
		return Tangledness.toString();
	}

}
