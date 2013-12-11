package com.karlhammar.ontometrics.plugins;

import java.io.File;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.profiles.OWL2RLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

public class RLProfile extends OntoMetricsPlugin {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingletonOWLAPI ps;
	
	public String getName() {
		return "RL Profile Plugin";
	}

	/**
	 * Initialize the plugin. Required before metrics calculation.
	 */
	public void init(StructuralSingleton jena, StructuralSingletonOWLAPI owlapi) {
	    ps = owlapi;
	}

	public String getMetricAbbreviation() {
		return "RLProfile";
	}

	/**
	 * If plugin has been initiated (and profile singleton instantiated), 
	 * return report on whether this ontology is in the RL profile or not.
	 */
	public String getMetricValue(File ontologyFile) {
		if (null == ps) {
			logger.severe("getMetricValue called before init()!");
		}
		OWL2RLProfile o2rl = new OWL2RLProfile();
		OWLOntology ontology = ps.getOntology();
		OWLProfileReport report = o2rl.checkOntology(ontology);
		return new Boolean(report.isInProfile()).toString();
	}
}
