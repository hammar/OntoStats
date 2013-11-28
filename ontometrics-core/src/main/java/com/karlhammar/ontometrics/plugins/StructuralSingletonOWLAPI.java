package com.karlhammar.ontometrics.plugins;

import java.io.File;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;


/**
 * This is a singleton class used to perform OWLAPI opening operations in the
 * structural OntoMetrics plugin only once, as opposed to for every 
 * individual structural check.
 * @author Karl Hammar <karl@karlhammar.com>
 *
 */
public class StructuralSingletonOWLAPI {

	// Member variables.
	private Logger logger = Logger.getLogger(getClass().getName());
	private static StructuralSingletonOWLAPI ref;
	private OWLOntology ontology;
	
	/**
	 * Private constructor. Loads ontology document.
	 * @param ontologyFile
	 */
	private StructuralSingletonOWLAPI(File ontologyFile) {
		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			this.ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);
		}
		catch (OWLOntologyCreationException e) {
			logger.severe(e.getMessage());
		}
	}
	
	/**
	 * Getter for ontology member.
	 * @return
	 */
	public OWLOntology getOntology() {
		return this.ontology;
	}

	/**
	 * Getter for singleton. Instantiates if it does not already exist, otherwise
	 * returns existing instance.
	 * @param ontologyFile The File instance referring to ontology to calculate metrics over.
	 * @return New or existing Singleton instance.
	 */
	public static synchronized StructuralSingletonOWLAPI getSingletonObject(File ontologyFile) {
		if (ref == null) {
			ref = new StructuralSingletonOWLAPI(ontologyFile);
		}
		return ref;
	}

	/**
	 * Don't try this!
	 */
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
