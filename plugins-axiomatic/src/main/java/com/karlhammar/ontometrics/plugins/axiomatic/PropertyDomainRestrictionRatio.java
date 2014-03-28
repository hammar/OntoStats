package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import java.util.Optional;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.karlhammar.ontometrics.plugins.*;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class PropertyDomainRestrictionRatio extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	public String getName() {
		return "Property domain restrictions ratio plugin";
	}

	@Override
	public String getMetricAbbreviation() {
		return "PropDomRatio";
	}

	@Override
	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == jena) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = jena.getOntology();
		return Optional.of(calculatePropertyDomainRestrictionRate(ontology).toString());
	}
	
	private static Double calculatePropertyDomainRestrictionRate(OntModel m) {
		int domainRestrictions = 0;
		List<OntProperty> properties = m.listAllOntProperties().toList();
		for (OntProperty op: properties)
			if (!op.isAnnotationProperty() && op.getDomain() != null)
				if(op.getDomain().isAnon() || !op.getDomain().getURI().equalsIgnoreCase("http://www.w3.org/2002/07/owl#Thing"))
					domainRestrictions++;
		
		return ((double)domainRestrictions / properties.size());
	}
}
