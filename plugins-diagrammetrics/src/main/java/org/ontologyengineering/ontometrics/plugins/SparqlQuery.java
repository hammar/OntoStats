package org.ontologyengineering.ontometrics.plugins;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.commons.io.*;
import org.ontologyengineering.ontometrics.plugins.Filter.FilterType;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.karlhammar.ontometrics.plugins.ParserJena;

public class SparqlQuery {
    private Logger logger = Logger.getLogger(getClass().getName());
    private ParserJena jena;

    // this is almost documentation.
    private String queryString;


    /* package */ SparqlQuery(ParserJena jena, FilterType lhs, FilterType rhs) throws IOException {
        this.jena   = jena;
        // hackity, hack, hack, hack.
        String resource = "simple_" + lhs.toString().toLowerCase() + "subset" + rhs.toString().toLowerCase() + ".sparql";
        queryString = IOUtils.toString(getClass().getClassLoader().getResource(resource).openStream());
    }

    /* package */ String runQuery() {
        String subset     = queryString.replace("$(TERM)", "rdfs:subClassOf");
        String equiv      = queryString.replace("$(TERM)", "owl:equivalentClass");

        // Run subset query
        Query qs1         = QueryFactory.create(subset);
        QueryExecution qe = QueryExecutionFactory.create(qs1, jena.getOntology());
        ResultSet results =  qe.execSelect();
        ResultSetRewindable rsrw = ResultSetFactory.copyResults(results);
        int subsetR = rsrw.size();

        // Run equivalence query
        qs1 = QueryFactory.create(equiv);
        qe  = QueryExecutionFactory.create(qs1, jena.getOntology());
        results =  qe.execSelect();
        rsrw = ResultSetFactory.copyResults(results);
        int equivR = rsrw.size() * 2;  // double this as \equiv is two \subseteq's

        return new Double(subsetR + equivR).toString();
    }
}
