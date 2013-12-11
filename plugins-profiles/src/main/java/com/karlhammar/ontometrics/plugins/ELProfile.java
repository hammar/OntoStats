package com.karlhammar.ontometrics.plugins;

import java.io.File;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.profiles.OWL2ELProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

public class ELProfile extends OntoMetricsPlugin {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingletonOWLAPI ps;
	
	public String getName() {
		return "EL Profile Plugin";
	}

	/**
	 * Initialize the plugin. Required before metrics calculation.
	 */
	public void init(StructuralSingleton jena, StructuralSingletonOWLAPI owlapi) {
		ps = owlapi;
	}

	public String getMetricAbbreviation() {
		return "ELProfile";
	}

	/**
	 * If plugin has been initiated (and profile singleton instantiated), 
	 * return report on whether this ontology is in the EL profile or not.
	 */
	public String getMetricValue(File ontologyFile) {
		if (null == ps) {
			logger.severe("getMetricValue called before init()!");
		}
		OWL2ELProfile o2el = new OWL2ELProfile();
		OWLOntology ontology = ps.getOntology();
		OWLProfileReport report = o2el.checkOntology(ontology);
		return new Boolean(report.isInProfile()).toString();
	}
}
