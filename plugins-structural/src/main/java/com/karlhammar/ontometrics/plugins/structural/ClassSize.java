package com.karlhammar.ontometrics.plugins.structural;

import java.io.File;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class ClassSize implements OntoMetricsPlugin  {

	private OntModel m;
	
	public String getName() {
		return "Class size plugin";
	}

	public void init(File ontologyFile) {
		m = ModelFactory.createOntologyModel( OntModelSpec.OWL_DL_MEM);
		m.read(ontologyFile.toURI().toString());		
	}

	public String getMetricAbbreviation() {
		return "CLSIZE";
	}

	public String getMetricValue(File ontologyFile) {
		List<OntClass> classes = m.listNamedClasses().toList();
		// -1 to account for owl:Thing
		Integer result = classes.size() - 1;
		return result.toString();
	}

}
