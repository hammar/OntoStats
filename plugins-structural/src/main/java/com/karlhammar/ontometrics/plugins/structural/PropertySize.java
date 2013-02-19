package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class PropertySize implements OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingleton ss;
	
	public String getName() {
		return "Property size plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingleton.getSingletonObject(ontologyFile);
	}

	public String getMetricAbbreviation() {
		return "PropSize";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		OntModel ontology = ss.getOntology();
		return getPropertySize(ontology).toString();
	}
	
	protected static Integer getPropertySize(OntModel ontology) {
		Integer nrOfProperties = 0;
		List<OntProperty> properties = ontology.listAllOntProperties().toList();
		for (OntProperty op: properties)
			if (!op.isAnnotationProperty())
				nrOfProperties++;
		return nrOfProperties;
	}
}
