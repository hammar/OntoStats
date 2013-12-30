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
public class ParserJena {

	// Member variables.
	private static ParserJena ref;
	private OntModel ontology;

	// If we get asked for an instance of this singleton that has a different
	// config, we will throw an exception.
	private ParserConfiguration config;

	/**
	 * Private constructor. Loads ontology document.
	 * @param ontologyFile
	 */
	private ParserJena(File ontologyFile) {
	    this(ontologyFile, new ParserConfiguration());
	}
	
	/**
	 * Create a StructuralSingleton that may ignore imported ontologes.
	 * @param ontologyFile
	 * @param ignoreImports true to not import linked ontologies, false otherwise.
	 */
	private ParserJena(File ontologyFile, ParserConfiguration pc) {
	    this.config = pc;
	    this.ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
	    this.ontology.getDocumentManager().setProcessImports(pc.getImportStrategy() == ParserConfiguration.ImportStrategy.ALLOW_IMPORTS);
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
	 * This is an evil thing to do with a singleton, but at least make it functional.
	 * @param ontologyFile
	 * @param pc
	 * @return
	 */
	public static synchronized ParserJena resetSingletonObject(File ontologyFile, ParserConfiguration pc) {
	    ref = null;
	    return getSingletonObject(ontologyFile, pc);
	}

	/**
	 * Getter for singleton. Instantiates if it does not already exist, otherwise
	 * returns existing instance.
	 * @param ontologyFile The File instance referring to ontology to calculate metrics over.
	 * @return New or existing Singleton instance.
	 */
	public static synchronized ParserJena getSingletonObject(File ontologyFile) {
		return getSingletonObject(ontologyFile, new ParserConfiguration());
	}

	public static synchronized ParserJena getSingletonObject(File ontologyFile, ParserConfiguration pc) {
	    if (ref == null) {
	        ref = new ParserJena(ontologyFile, pc);
	    }

	    // if the caller is asking for a Singleton that has a different 
	    // configuration from the current, then admonish them.
	    if(!ref.config.equals(pc)) throw new IllegalStateException();
	    return ref;
	}

	/**
	 * Don't try this!
	 */
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
