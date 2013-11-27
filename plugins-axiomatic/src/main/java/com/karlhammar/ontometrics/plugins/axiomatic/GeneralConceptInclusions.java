package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import com.karlhammar.ontometrics.plugins.*;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class GeneralConceptInclusions implements OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingletonOWLAPI ss;
	
	public String getName() {
		return "General Concept Inclusion count plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile);
	}

	public String getMetricAbbreviation() {
		return "GCICount";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		OWLOntology ontology = ss.getOntology();
		Set<OWLClassAxiom> gcas = ontology.getGeneralClassAxioms();
		Iterator<OWLClassAxiom> gcasIter = gcas.iterator();
		Integer GCIs = 0;
		while (gcasIter.hasNext()) {
			OWLClassAxiom gca = gcasIter.next();
			if ((gca.getAxiomType() == AxiomType.SUBCLASS_OF) || (gca.getAxiomType() == AxiomType.EQUIVALENT_CLASSES)) {
				GCIs++;
			}
		}
		return GCIs.toString();
	}
}
