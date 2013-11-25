package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;

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
public class PrettyDiagramAxiomRatio implements OntoMetricsPlugin {

    private Logger logger = Logger.getLogger(getClass().getName());
    private StructuralSingletonOWLAPI sowl;
    
    public String getName() {
        return "Pretty diagram ratio plugin";
    }

    public void init(File ontologyFile) {
        sowl = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile);
    }

    public String getMetricAbbreviation() {
        return "PrettyDiagramRatio";
    }

    public String getMetricValue(File ontologyFile) {
        if (null == sowl) {
            logger.info("getMetricValue called before init()!");
            init(ontologyFile);
        }
        OWLOntology owlmodel = sowl.getOntology();
        return calculatePrettyDiagramRatio(owlmodel).toString();
    }


    private static Double calculatePrettyDiagramRatio(OWLOntology owl) {
        // Get number of tbox axioms in ontology.
        double tboxes = 0.0;
        for(AxiomType<?> tbox: AxiomType.TBoxAxiomTypes) {
            tboxes += owl.getAxiomCount(tbox);
        }

        // We can deal with DisjointUnion, EquivalentClasses, DisjointClasses 
        // and SubClassOf, count the number of tbox axioms of each of these 
        // types
        double present = 0.0;
        for(AxiomType<?> tbox: Arrays.asList(AxiomType.DISJOINT_UNION, AxiomType.EQUIVALENT_CLASSES, AxiomType.DISJOINT_CLASSES, AxiomType.SUBCLASS_OF)) {
            present += owl.getAxiomCount(tbox);
        }

        return present / tboxes;
    }
}
