package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class TreeImpurity implements OntoMetricsPlugin {

	private Logger logger = Logger.getLogger(getClass().getName());
	private StructuralSingletonJena ss;
	
	public String getName() {
		return "Tree impurity plugin";
	}

	public void init(File ontologyFile) {
		ss = StructuralSingletonJena.getSingletonObject(ontologyFile);

	}

	public String getMetricAbbreviation() {
		return "TreeImp";
	}

	public String getMetricValue(File ontologyFile) {
		if (null == ss) {
			logger.info("getMetricValue called before init()!");
			init(ontologyFile);
		}
		OntModel ontology = ss.getOntology();
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
		return TIP.toString();
	}

}
