package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import java.util.Optional;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;

public class ExistentialQuantification extends OntoMetricsPlugin {
	
	private Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public String getName() {
		return "Existential quantification count plugin";
	}

	@Override
	public String getMetricAbbreviation() {
		return "ExiQuants";
	}

	/**
	 * If plugin has been initiated (and profile singleton instantiated), 
	 * return report on whether this ontology is in the RL profile or not.
	 */
	@Override
	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == owlapi) {
			logger.severe("getMetricValue called before init()!");
		}
		OWLOntology ontology = owlapi.getOntology();
		Set<OWLClassExpression> ces = ontology.getNestedClassExpressions();
		Iterator<OWLClassExpression> cesIter = ces.iterator();
		Integer exiQuants = 0;
		while (cesIter.hasNext()) {
			OWLClassExpression ce = cesIter.next();
			if (ce.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
				exiQuants++;
			}
		}
		return Optional.of(exiQuants.toString());
	}
}
