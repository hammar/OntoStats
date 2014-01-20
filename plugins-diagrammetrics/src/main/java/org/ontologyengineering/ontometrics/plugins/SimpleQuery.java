package org.ontologyengineering.ontometrics.plugins;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.*;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

import com.karlhammar.ontometrics.plugins.ParserJena;

public class SimpleQuery {
    private Logger logger = Logger.getLogger(getClass().getName());
    private ParserJena jena;

    // this is almost documentation.
    private String queryString;


    /* package */ SimpleQuery(ParserJena jena, String resourceId) throws IOException {
        this.jena   = jena;
        queryString = IOUtils.toString(getClass().getClassLoader().getResource(resourceId).openStream());
    }

    /* package */ String calculatePrettyDiagramRatio() {
        OntModel    ontmodel = jena.getOntology();

        return runQuery(ontmodel).toString();
    }

    private Double runQuery(OntModel jena) {
        String subset     = queryString.replace("$(TERM)", "rdfs:subClassOf");
        String equiv      = queryString.replace("$(TERM)", "owl:equivalentClass");

        // Run subset query
        Query qs1         = QueryFactory.create(subset);
        QueryExecution qe = QueryExecutionFactory.create(qs1, jena);
        ResultSet results =  qe.execSelect();
        int subsetR = sumResultSet(results);

        // Run equivalence query
        qs1 = QueryFactory.create(equiv);
        qe  = QueryExecutionFactory.create(qs1, jena);
        results =  qe.execSelect();
        int equivR = sumResultSet(results) * 2;  // double this as \equiv is two \subseteq's

        return new Double(subsetR + equivR);
    }

    // ResultSet is neither a Collection nor Iterable.
    /* package */ static int sumResultSet(ResultSet rs) {
        int sum = 0;
        while (rs.hasNext()) {
            sum ++;
            com.hp.hpl.jena.query.QuerySolution qs = rs.next();
            /*  Will print the ResultSet as it is summed
            java.util.Iterator<String> i = qs.varNames();
            while (i.hasNext()) {
                String key = i.next();
                System.out.print(key + "(" + qs.get(key).toString() + ") ");
            }
            System.out.println();*/
        }
        return sum;
    }
}
