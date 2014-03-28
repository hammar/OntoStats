package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import java.util.Optional;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class CommentsRatio extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	public String getName() {
		return "Comments ratio plugin";
	}

	public String getMetricAbbreviation() {
		return "CommentsRatio";
	}

	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == jena) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = jena.getOntology();
		return Optional.of(calculateCommentsRatio(ontology).toString());
	}

	private static Double calculateCommentsRatio(OntModel m) {
		// Add all named classes, individuals, and properties
		// to a set of "commentable" entities.
		Set<OntResource> commentableEntities = new HashSet<OntResource>();
		commentableEntities.addAll(m.listNamedClasses().toSet());
		commentableEntities.addAll(m.listIndividuals().toSet());
		commentableEntities.addAll(m.listAllOntProperties().toSet());
		
		// Count how many of those entities that are indeed commented.
		int nrOfComments = 0;
		for (OntResource or: commentableEntities) {
			if (or.hasProperty(RDFS.comment)) {
				nrOfComments++;
			}
		}
		
		// Calculate the ratio
		return ((double)nrOfComments / commentableEntities.size());
	}
}
