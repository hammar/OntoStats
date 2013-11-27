package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.util.Arrays;
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
        return calculatePrettyDiagramRatio(owlmodel, ontmodel);
    }

    final String PREFIX_STRING =
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";


    private class SimpleQuery {
        // this is almost documentation.
        public String laTeXString;
        public String queryString;
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

    private String calculatePrettyDiagramRatio(OWLOntology owl, OntModel jena) {
        // Get number of tbox axioms in ontology.
        double tboxes = 0.0;
        for(AxiomType<?> tbox: AxiomType.TBoxAxiomTypes) {
            tboxes += owl.getAxiomCount(tbox);
        }

        return runQuery(jena, s1, tboxes).toString();
    }

    private Double runQuery(OntModel jena, SimpleQuery sq, double tboxes) {
        Query qs1         = QueryFactory.create(sq.queryString);
        QueryExecution qe = QueryExecutionFactory.create(qs1, jena);
        ResultSet results =  qe.execSelect();
        
        return new Double(sumResultSet(results))/ tboxes;
    }

    // ResultSet is neither a Collection nor Iterable.
    private int sumResultSet(ResultSet rs) {
        int sum = 0;
        while (rs.hasNext()) {
            sum ++;
            rs.next();
        }
        return sum;
    }
}
