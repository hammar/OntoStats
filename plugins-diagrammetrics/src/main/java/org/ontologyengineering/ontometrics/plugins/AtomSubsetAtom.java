package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.*;

import com.karlhammar.ontometrics.plugins.*;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

/**
 *
 * @author Aidan Delaney <aidan@ontologyengineering.org>
 *
 * If we have axioms of the form
 * <pre><code>
 * D  := \top |
 *       \bot |
 *       A (A atomic) |
 *       C \sqcap D |
 *       C \sqcup D |
 *       \neg C |
 *       \exists R. C |
 *       \forall R. C
 * </code></pre>
 * then there is a "pretty" mechanism to convert them into a Concept Diagram.
 * This metric calculates the amount of the ontology that can be represented as
 * Concept Diagrams using the "pretty" mechanism.
 */
public class AtomSubsetAtom implements OntoMetricsPlugin {

    private Logger logger = Logger.getLogger(getClass().getName());
    private StructuralSingletonOWLAPI sowl;
    private StructuralSingleton ss;

    public String getName() {
        return "Ratio of Atom Subsumes Atom axioms to the TBox size";
    }

    public void init(File ontologyFile) {
        // We need both OWL API and Jena for this trick.
        sowl = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile);
        ss   = StructuralSingleton.getSingletonObject(ontologyFile);
    }

    public String getMetricAbbreviation() {
        return "AtomSubsetAtom";
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
        laTeXString = "C_1 \\sqsubseteq C_3";
        queryString = PREFIX_STRING
                + "SELECT ?subject ?object "
                + "       WHERE { ?subject rdfs:subClassOf ?object ; "
                + "                        rdf:type              owl:Class . "
                + "               ?object  rdf:type              owl:Class . "
                + "               FILTER NOT EXISTS{?subject owl:unionOf        ?o1 } "
                + "               FILTER NOT EXISTS{?subject owl:intersectionOf ?o2 } "
                + "               FILTER NOT EXISTS{?subject owl:complementOf   ?o3 } "
                + "               FILTER NOT EXISTS{?object  owl:unionOf        ?o4 } "
                + "               FILTER NOT EXISTS{?object  owl:intersectionOf ?o5 } "
                + "               FILTER NOT EXISTS{?object  owl:complementOf   ?o6 } "
                + "               } "
                + "               GROUP BY ?subject ?object "
                ;
    }};
}
