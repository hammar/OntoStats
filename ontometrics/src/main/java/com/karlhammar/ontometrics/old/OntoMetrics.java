package com.karlhammar.ontometrics.old;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;*/

public class OntoMetrics {

	/**
	 * Simplistic tool for calculating some ontology metrics using the Jena APIs.
	 * Takes as input the path to a single ontology file and prints out data 
	 * formatted in CSV format (although without header row). Intended to be
	 * chained together with other UNIX command line tools.
	 * 
	 * In general, this tool ignores annotation properties when dealing with property-
	 * related metrics, for the reason that these can in most ontology engineering
	 * environments be hidden from view and not listed among other datatype or object
	 * properites in the properties pane. Therefore they are not believed to affect
	 * usability of resulting ontologies or patterns significantly (but of course
	 * their instantiations on concepts will likely do so, in particular rdfs:label
	 * and rdfs:comment!)
	 *  
	 * @param args Argument vector. Only a single argument is allowed, and this
	 * must be a path to an ontology file.
	 */
	public static void main(String[] args) {/*
		if (args.length != 1 || args[0] == null) {
			System.err.println("Expected usage: OntoMetrics.jar <PATH_TO_OWL_FILE>");
			System.exit(1);
		}
		
		File ontologyFile=new File(args[0]);
		if (!ontologyFile.exists()) {
			System.err.println(String.format("Provided file path (%s) does not exist.",args[0]));
			System.exit(1);
		}
		
		try {
			Profiles p = new Profiles(ontologyFile);
			System.out.println(String.format("%1$s;%2$s;%3$s;%4$s",
					ontologyFile.getName(),
					p.isInElProfile(),
					p.isInQlProfile(),
					p.isInRlProfile()));
		} 
		catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		
		/*
		 * Commented out to instead activate OWL 2 Profiles calculation above. 
		OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_DL_MEM);
		m.read(ontologyFile.toURI().toString());
		
		System.out.println(String.format("%1$s;%2$d;%3$d;%4$f;%5$d;%6$f;%7$f;%8$f",
				ontologyFile.getName(),
				calculateClassSize(m),
				calculatePropertySize(m),
				calculateClassToPropertyRatio(m),
				calculateAnonymousClasses(m),
				calculatePropertyDomainRestrictionRate(m),
				calculatePropertyRangeRestrictionRate(m),
				calculateAnnotationRatio(m)));
		*/
	}
	
	/**
	 * Calculates the size in number of named classes of an ontology.
	 * @param m The ontology to be calculated over.
	 * @return The number of named classes in said ontology.
	 *//*
	private static Integer calculateClassSize(OntModel m) {
		List<OntClass> classes = m.listNamedClasses().toList();
		// -1 to account for owl:Thing
		return classes.size() - 1;
	}
	*/
	/**
	 * Calculates the number of explicitly asserted properties in the provided model,
	 * excluding annotation properties.  
	 * @param m An ontology that is to be calculated over.
	 * @return Sum of object and datatype properties in ontology.
	 *//*
	private static Integer calculatePropertySize(OntModel m) {
		int nrOfProperties = 0;
		List<OntProperty> properties = m.listAllOntProperties().toList();
		for (OntProperty op: properties)
			if (!op.isAnnotationProperty())
				nrOfProperties++;	
		return nrOfProperties;
	}
	*/
	/**
	 * Calculates the ratio of defined named classes to defined properties
	 * in the ontology.
	 * @param m Ontology to be calculated on.
	 * @return Ratio of classes to properties.
	 *//*
	private static Double calculateClassToPropertyRatio(OntModel m) {
		int classSize = calculateClassSize(m);
		int propertySize = calculatePropertySize(m);
		return ((double)classSize / propertySize);
	}*/
	
	/**
	 * Returns the ratio of named classes, individuals and properties that
	 * are annotated with an rdfs:comment annotation.
	 * @param m Ontology to calculate over.
	 * @return Ratio of commented content.
	 */
	/*
	private static Double calculateAnnotationRatio(OntModel m) {
		// Add all named classes, individuals, and non-annotation properties
		// to a set to use for calculating use of annotations.
		Set<OntResource> resources = new HashSet<OntResource>();
		resources.addAll(m.listNamedClasses().toSet());
		resources.addAll(m.listIndividuals().toSet());
		List<OntProperty> properties = m.listAllOntProperties().toList();
		for (OntProperty op: properties)
			if (!op.isAnnotationProperty())
				resources.add(op);
		
		AnnotationProperty commentProperty = m.getAnnotationProperty("http://www.w3.org/2000/01/rdf-schema#comment");
		int nrOfComments = 0;
		
		for (OntResource or: resources) {
			if (or.hasProperty(commentProperty))
				nrOfComments++;
		}
		
		// -1 to account for owl:Thing
		return ((double)nrOfComments / (resources.size() - 1));
	}
	*/
	/**
	 * Returns the number of anonymous classes (i.e. restrictions and so on) asserted 
	 * in an ontology.
	 * @param m The ontology to calculate over.
	 * @return The number of anonymous classes.
	 */
	/*
	private static Integer calculateAnonymousClasses(OntModel m) {
		ExtendedIterator<OntClass> allClasses = m.listClasses();
		int nrOfAnonymousClasses = 0;
		while (allClasses.hasNext()) {
			OntClass c = allClasses.next();
			if (c.isAnon()) {
				nrOfAnonymousClasses++;
			}
		}
		return nrOfAnonymousClasses;
	}
	*/
	
	/** 
	 * Return the ratio of properties (excluding annotation properties) in the ontology
	 * that have a domain restriction set.
	 * @param m The ontology to calculate on.
	 * @return Ratio of domain restrictions to total properties.
	 */
	/*
	private static Double calculatePropertyDomainRestrictionRate(OntModel m) {
		int domainRestrictions = 0;
		List<OntProperty> properties = m.listAllOntProperties().toList();
		for (OntProperty op: properties)
			if (!op.isAnnotationProperty() && op.getDomain() != null)
				if(op.getDomain().isAnon() || !op.getDomain().getURI().equalsIgnoreCase("http://www.w3.org/2002/07/owl#Thing"))
					domainRestrictions++;
		
		return ((double)domainRestrictions / properties.size());
	}
	*/
	/** 
	 * Return the ratio of properties (excluding annotation properties) in the ontology
	 * that have a range restriction set.
	 * @param m The ontology to calculate on.
	 * @return Ratio of range restrictions to total properties.
	 */
	/*
	private static Double calculatePropertyRangeRestrictionRate(OntModel m) {
		int rangeRestrictions = 0;
		List<OntProperty> properties = m.listAllOntProperties().toList();
		for (OntProperty op: properties)
			if (!op.isAnnotationProperty() && op.getRange() != null)
				if(op.getRange().isAnon() || !op.getRange().getURI().equalsIgnoreCase("http://www.w3.org/2002/07/owl#Thing"))
					rangeRestrictions++;
		
		return ((double)rangeRestrictions / properties.size());
	}
	*/
}
