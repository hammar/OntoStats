package com.karlhammar.ontometrics.plugins;

import java.io.File;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * This is a singleton class used to perform Jena opening operations in the
 * structural OntoMetrics plugin only once, as opposed to for every 
 * individual structural check.
 * @author Karl Hammar <karl@karlhammar.com>
 *
 */
public class StructuralSingleton {

	// Member variables.
	private static StructuralSingleton ref;
	private OntModel ontology;

	/**
	 * Private constructor. Loads ontology document.
	 * @param ontologyFile
	 */
	private StructuralSingleton(File ontologyFile) {
	    this(ontologyFile, false);
	}
	
	/**
	 * Create a StructuralSingleton that may ignore imported ontologes.
	 * @param ontologyFile
	 * @param ignoreImports true to not import linked ontologies, false otherwise.
	 */
	private StructuralSingleton(File ontologyFile, boolean ignoreImports) {
	    this.ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
	    this.ontology.getDocumentManager().setProcessImports(!ignoreImports);
	    this.ontology.read(ontologyFile.toURI().toString());
	}
	
	/**
	 * Getter for ontology member.
	 * @return
	 */
	public OntModel getOntology() {
		return this.ontology;
	}

	/**
	 * Getter for singleton. Instantiates if it does not already exist, otherwise
	 * returns existing instance.
	 * @param ontologyFile The File instance referring to ontology to calculate metrics over.
	 * @return New or existing Singleton instance.
	 */
	public static synchronized StructuralSingleton getSingletonObject(File ontologyFile) {
		return getSingletonObject(ontologyFile, false);
	}

	public static synchronized StructuralSingleton getSingletonObject(File ontologyFile, boolean ignoreImports) {
	    if (ref == null) {
	        ref = new StructuralSingleton(ontologyFile, ignoreImports);
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
