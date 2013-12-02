package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.OWLOntology;

import com.hp.hpl.jena.ontology.OntModel;
import com.karlhammar.ontometrics.plugins.StructuralSingleton;
import com.karlhammar.ontometrics.plugins.StructuralSingletonOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class AtomSubsetConj implements OntoMetricsPlugin{

    private Logger logger = Logger.getLogger(getClass().getName());
    private StructuralSingletonOWLAPI sowl;
    private StructuralSingleton ss;
    
    public String getName() {
        return "Ratio of Atom Subsumes (Atom Conj Atom) axioms to the TBox size";
    }

    public void init(File ontologyFile) {
        // We need both OWL API and Jena for this trick.
        sowl = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile);
        ss   = StructuralSingleton.getSingletonObject(ontologyFile);
    }

    public String getMetricAbbreviation() {
        return "AtomSubsetConj";
    }

    public String getMetricValue(File ontologyFile) {
        if ((null == sowl) || (null == ss)) {
            logger.info("getMetricValue called before init()!");
            init(ontologyFile);
        }
        OWLOntology owlmodel = sowl.getOntology();
        OntModel    ontmodel = ss.getOntology();
        return SimpleQuery.calculatePrettyDiagramRatio(owlmodel, ontmodel, s1);
    }

    // Hacktastic double brace initialisation
    final SimpleQuery s1 = new SimpleQuery() {{
        laTeXString = "C_1 \\sqsubseteq C_2 \\sqcup C_3";
        queryString = PREFIX_STRING +
                "SELECT ?atoma ?atomb ?atomc\n" + 
                "WHERE { ?atoma     rdfs:subClassOf                          ?object\n" + 
                "                 ; rdf:type                                 owl:Class\n" + 
                "                 .\n" + 
                "        # FIXME: would like to write ?object owl:intersectionOf/owl:Class/rdf:first ?atomb here, but it doesn't work.\n" + 
                "        ?object     owl:intersectionOf                      ?conj\n" + 
                "                 ;  rdf:type                                owl:Class \n" + 
                "                 .\n" + 
                "        ?conj       rdf:first                               ?atomb\n" + 
                "                 ;  rdf:rest/rdf:first                      ?atomc\n" + 
                "                    # Ensure this is a binary intersectionOf\n" + 
                "                 ;  rdf:rest/rdf:rest                       rdf:nil\n" + 
                "                 .\n" + 
                "        ?atomb     rdf:type                                 owl:Class \n" + 
                "                 .\n" + 
                "        ?atomc     rdf:type                                 owl:Class \n" + 
                "                 .\n" + 
                "        # Ensure each atom is atomic\n" + 
                "        FILTER NOT EXISTS{?atoma owl:unionOf        ?ox }\n" + 
                "        FILTER NOT EXISTS{?atoma owl:intersectionOf ?oy } \n" + 
                "        FILTER NOT EXISTS{?atoma owl:complementOf   ?oz } \n" + 
                "        FILTER NOT EXISTS{?atomb owl:unionOf        ?o1 } \n" + 
                "        FILTER NOT EXISTS{?atomb owl:intersectionOf ?o2 } \n" + 
                "        FILTER NOT EXISTS{?atomb owl:complementOf   ?o3 } \n" + 
                "        FILTER NOT EXISTS{?atomc owl:unionOf        ?o4 } \n" + 
                "        FILTER NOT EXISTS{?atomc owl:intersectionOf ?o5 } \n" + 
                "        FILTER NOT EXISTS{?atomc owl:complementOf   ?o6 } \n" + 
                "       } \n" + 
                "GROUP BY ?atoma ?atomb ?atomc"
                ;
    }};
}
