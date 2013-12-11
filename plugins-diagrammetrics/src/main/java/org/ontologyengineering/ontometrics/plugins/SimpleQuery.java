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
    public String queryString;


    /* package */ SimpleQuery(ParserJena jena, String resourceId) throws IOException {
        this.jena   = jena;
        queryString = IOUtils.toString(getClass().getClassLoader().getResource(resourceId).openStream());
    }

    /* package */ String calculatePrettyDiagramRatio() {
        OntModel    ontmodel = jena.getOntology();

        return runQuery(ontmodel).toString();
    }

    private Double runQuery(OntModel jena) {
        Query qs1         = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(qs1, jena);
        ResultSet results =  qe.execSelect();
        
        return new Double(sumResultSet(results));
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
