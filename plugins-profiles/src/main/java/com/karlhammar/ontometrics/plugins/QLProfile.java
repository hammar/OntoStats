package com.karlhammar.ontometrics.plugins;

import java.io.File;
import java.util.logging.Logger;
import java.util.Optional;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.profiles.OWL2QLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

public class QLProfile extends OntoMetricsPlugin {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public String getName() {
		return "QL Profile Plugin";
	}

	public String getMetricAbbreviation() {
		return "QLProfile";
	}

	/**
	 * If plugin has been initiated (and profile singleton instantiated), 
	 * return report on whether this ontology is in the QL profile or not.
	 */
	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == owlapi) {
			logger.severe("getMetricValue called before init()!");
		}
		OWL2QLProfile o2ql = new OWL2QLProfile();
		OWLOntology ontology = owlapi.getOntology();
		OWLProfileReport report = o2ql.checkOntology(ontology);
		return Optional.of(Boolean.toString(report.isInProfile()));
	}
}
