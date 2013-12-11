package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class PropertyRangeRestrictionRatio extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public String getName() {
		return "Property range restrictions ratio plugin";
	}

	@Override
	public String getMetricAbbreviation() {
		return "PropRangeRatio";
	}

	@Override
	public String getMetricValue(File ontologyFile) {
		if (null == jena) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = jena.getOntology();
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
