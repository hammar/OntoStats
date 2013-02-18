package com.karlhammar.ontometrics.plugins;

import java.io.File;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWL2ELProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

public class ELProfile implements OntoMetricsPlugin {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public String getName() {
		return "EL Profile Plugin";
	}

	public void init() {
		
	}

	public String getMetricAbbreviation() {
		return "ELProfile";
	}

	public String getMetricValue(File ontologyFile) {
		try {
			OWL2ELProfile o2el = new OWL2ELProfile();
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);
			OWLProfileReport report = o2el.checkOntology(ontology);
			return new Boolean(report.isInProfile()).toString();
		} 
		catch (OWLOntologyCreationException e) {
			logger.severe(e.getMessage());
			return null;
		}
	}

}
