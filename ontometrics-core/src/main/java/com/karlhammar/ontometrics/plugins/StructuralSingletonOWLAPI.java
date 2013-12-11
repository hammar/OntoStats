package com.karlhammar.ontometrics.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
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
	
	// If we get asked for an instance of this singleton that has a different
    // config, we will throw an exception.
    private ParserConfiguration config;

	private class NoImportLoader implements OWLOntologyIRIMapper {
	    // Copy and pasted from a blank Protégé document.
	    final String blankDocument = "<?xml version=\"1.0\"?>\n" + 
	            "\n" + 
	            "\n" + 
	            "<!DOCTYPE rdf:RDF [\n" + 
	            "    <!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >\n" + 
	            "    <!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >\n" + 
	            "    <!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >\n" + 
	            "    <!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >\n" + 
	            "]>\n" + 
	            "\n" + 
	            "\n" + 
	            "<rdf:RDF xmlns=\"http://www.semanticweb.org/aidan/ontologies/2013/11/untitled-ontology-10#\"\n" + 
	            "     xml:base=\"http://www.semanticweb.org/aidan/ontologies/2013/11/untitled-ontology-10\"\n" + 
	            "     xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n" + 
	            "     xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n" + 
	            "     xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"\n" + 
	            "     xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n" + 
	            "    <owl:Ontology rdf:about=\"http://www.semanticweb.org/aidan/ontologies/2013/11/untitled-ontology-10\"/>\n" + 
	            "</rdf:RDF>\n" + 
	            "";

        public IRI getDocumentIRI(IRI arg0) {
            File tmp = null;
            try {
                // Create temp file.
                tmp = File.createTempFile("blank", ".rdf");

                // Delete temp file when program exits.
                tmp.deleteOnExit();

                // Write to temp file
                BufferedWriter out = new BufferedWriter(new FileWriter(tmp));
                out.write(blankDocument);
                out.close();
            } catch (IOException e) {
            }
            return IRI.create(tmp); // create anonymous blank IRI
        }

	}
	/**
	 * Private constructor. Loads ontology document.
	 * @param ontologyFile
	 */
	private StructuralSingletonOWLAPI(File ontologyFile) {
	    this(ontologyFile, new ParserConfiguration());
	}

	private StructuralSingletonOWLAPI(File ontologyFile, ParserConfiguration pc) {
	    this.config = pc;

		try {
		    OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
		    config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			if(pc.getImportStrategy() == ParserConfiguration.ImportStrategy.IGNORE_IMPORTS) {
			    manager.addIRIMapper(new NoImportLoader());
			}
			this.ontology = manager.loadOntologyFromOntologyDocument(new FileDocumentSource(ontologyFile), config);
			/*if(ignoreImports) {
			    // remove all imports
			    for(OWLOntology imported: manager.getDirectImports(ontology)) {
			        manager.removeOntology(imported);
			    }
			}*/
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
		return getSingletonObject(ontologyFile, new ParserConfiguration());
	}

	public static synchronized StructuralSingletonOWLAPI getSingletonObject(File ontologyFile, ParserConfiguration pc) {
        if (ref == null) {
            ref = new StructuralSingletonOWLAPI(ontologyFile, pc);
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
