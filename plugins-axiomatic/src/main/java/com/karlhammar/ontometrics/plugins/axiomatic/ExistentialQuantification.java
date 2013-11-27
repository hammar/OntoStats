package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.*;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;

public class ExistentialQuantification implements OntoMetricsPlugin {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingletonOWLAPI ss;
	
	public String getName() {
		return "Existential quantification count plugin";
	}

	/**
	 * Initialize the plugin. Required before metrics calculation.
	 */
	public void init(File ontologyFile) {
		ss = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile);
		
	}

	public String getMetricAbbreviation() {
		return "ExiQuants";
	}

	/**
	 * If plugin has been initiated (and profile singleton instantiated), 
	 * return report on whether this ontology is in the RL profile or not.
	 */
	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		OWLOntology ontology = ss.getOntology();
		Set<OWLClassExpression> ces = ontology.getNestedClassExpressions();
		Iterator<OWLClassExpression> cesIter = ces.iterator();
		Integer exiQuants = 0;
		while (cesIter.hasNext()) {
			OWLClassExpression ce = cesIter.next();
			if (ce.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
				exiQuants++;
			}
		}
		return exiQuants.toString();
	}
}
