package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.logging.Logger;

import java.util.Optional;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class ClassOutDegree extends OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public String getName() {
		return "Class out-degree plugin";
	}

	@Override
	public String getMetricAbbreviation() {
		return "COutDegree";
	}

	@Override
	public Optional<String> getMetricValue(File ontologyFile) {
		if (null == jena) {
			logger.severe("getMetricValue called before init()!");
		}
		OntModel ontology = jena.getOntology();
		ExtendedIterator<OntClass> allClasses = ontology.listClasses();
		int nrClasses = 0;
		int outEdges = 0;
		while (allClasses.hasNext()) {
			OntClass c = allClasses.next();
			nrClasses++;
			Selector selector = new SimpleSelector(c, null, (RDFNode)null);
			StmtIterator iter = ontology.listStatements(selector);
			while (iter.hasNext()) {
				Statement s = iter.next();
				if (s.getObject().isResource()) {
					outEdges++;
				}
			}
		}
		Float outDegree = (float)outEdges / nrClasses;
		return Optional.of(outDegree.toString());
	}

}
