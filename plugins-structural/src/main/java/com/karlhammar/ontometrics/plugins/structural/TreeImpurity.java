package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.logging.Logger;

import com.google.common.base.Optional;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class TreeImpurity extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	public String getName() {
		return "Tree impurity plugin";
	}

	public String getMetricAbbreviation() {
		return "TreeImp";
	}

	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == jena) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = jena.getOntology();
		Integer subClassEdges = 0;
		Integer classNodes = 0;
		
		Property subClassOfProperty = ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#subClassOf");
		Selector selector = new SimpleSelector(null, subClassOfProperty, (RDFNode)null);
		StmtIterator subClassIter = ontology.listStatements(selector);
		while (subClassIter.hasNext()) {
			subClassIter.next();
			subClassEdges++;
		}
		
		ExtendedIterator<OntClass> allClasses = ontology.listClasses();
		while (allClasses.hasNext()) {
			allClasses.next();
			classNodes++;
		}
		
		Integer TIP = subClassEdges - classNodes + 1;
		return Optional.of(TIP.toString());
	}

}
