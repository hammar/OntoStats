package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class PropertyRangeRestrictionRatio implements OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingleton ss;
	
	public String getName() {
		return "Property range restrictions ratio plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingleton.getSingletonObject(ontologyFile);

	}

	public String getMetricAbbreviation() {
		return "PropRangeRatio";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		OntModel ontology = ss.getOntology();
		return calculatePropertyRangeRestrictionRate(ontology).toString();
	}
	
	private static Double calculatePropertyRangeRestrictionRate(OntModel m) {
		int rangeRestrictions = 0;
		List<OntProperty> properties = m.listAllOntProperties().toList();
		for (OntProperty op: properties)
			if (!op.isAnnotationProperty() && op.getRange() != null)
				if(op.getRange().isAnon() || !op.getRange().getURI().equalsIgnoreCase("http://www.w3.org/2002/07/owl#Thing"))
					rangeRestrictions++;
		
		return ((double)rangeRestrictions / properties.size());
	}
}
