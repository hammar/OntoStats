package com.karlhammar.ontometrics.plugins;

import java.io.File;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWL2RLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

public class RLProfile implements OntoMetricsPlugin {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public String getName() {
		return "RL Profile Plugin";
	}

	public void init() {
		
	}

	public String getMetricAbbreviation() {
		return "RLProfile";
	}

	public String getMetricValue(File ontologyFile) {
		try {
			OWL2RLProfile o2rl = new OWL2RLProfile();
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);
			OWLProfileReport report = o2rl.checkOntology(ontology);
			return new Boolean(report.isInProfile()).toString();
		} 
		catch (OWLOntologyCreationException e) {
			logger.severe(e.getMessage());
			return null;
		}
	}

}
