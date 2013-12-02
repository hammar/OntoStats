package org.ontologyengineering.ontometrics.plugins;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

public class SimpleQuery {
    final String PREFIX_STRING =
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";

    // this is almost documentation.
    public String laTeXString;
    public String queryString;

    /* package */ static String calculatePrettyDiagramRatio(OWLOntology owl, OntModel jena, SimpleQuery query) {
        // Get number of tbox axioms in ontology.
        double tboxes = 0.0;
        for(AxiomType<?> tbox: AxiomType.TBoxAxiomTypes) {
            tboxes += owl.getAxiomCount(tbox);
        }

        return SimpleQuery.runQuery(jena, query, tboxes).toString();
    }
    
    /* package */ static Double runQuery(OntModel jena, SimpleQuery sq, double tboxes) {
        Query qs1         = QueryFactory.create(sq.queryString);
        QueryExecution qe = QueryExecutionFactory.create(qs1, jena);
        ResultSet results =  qe.execSelect();
        
        return new Double(sumResultSet(results))/ tboxes;
    }

    // ResultSet is neither a Collection nor Iterable.
    /* package */ static int sumResultSet(ResultSet rs) {
        int sum = 0;
        while (rs.hasNext()) {
            sum ++;
            rs.next();
        }
        return sum;
    }
}
