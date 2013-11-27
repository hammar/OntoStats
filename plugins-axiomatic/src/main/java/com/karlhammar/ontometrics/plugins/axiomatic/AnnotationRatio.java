package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.karlhammar.ontometrics.plugins.*;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class AnnotationRatio implements OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingleton ss;
	
	public String getName() {
		return "Annotation ratio plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingleton.getSingletonObject(ontologyFile);
	}

	public String getMetricAbbreviation() {
		return "AnnotationRatio";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		OntModel ontology = ss.getOntology();
		return calculateAnnotationRatio(ontology).toString();
	}

	private static Double calculateAnnotationRatio(OntModel m) {
		// Add all named classes, individuals, and non-annotation properties
		// to a set to use for calculating use of annotations.
		Set<OntResource> resources = new HashSet<OntResource>();
		resources.addAll(m.listNamedClasses().toSet());
		resources.addAll(m.listIndividuals().toSet());
		List<OntProperty> properties = m.listAllOntProperties().toList();
		for (OntProperty op: properties)
			if (!op.isAnnotationProperty())
				resources.add(op);
		
		AnnotationProperty commentProperty = m.getAnnotationProperty("http://www.w3.org/2000/01/rdf-schema#comment");
		int nrOfComments = 0;
		
		for (OntResource or: resources) {
			if (or.hasProperty(commentProperty))
				nrOfComments++;
		}
		
		// -1 to account for owl:Thing
		return ((double)nrOfComments / (resources.size() - 1));
	}
}
