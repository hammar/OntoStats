package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.*;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.karlhammar.ontometrics.plugins.ParserConfiguration;
import com.karlhammar.ontometrics.plugins.StructuralSingleton;

public class SimpleQuery {
    private Logger logger = Logger.getLogger(getClass().getName());
    private StructuralSingleton ss;

    // this is almost documentation.
    public String queryString;

    /* package */ SimpleQuery(File ontologyFile, String resourceId) throws IOException {
        ss   = StructuralSingleton.getSingletonObject(ontologyFile, (new ParserConfiguration()).setImportStrategy(ParserConfiguration.ImportStrategy.IGNORE_IMPORTS));
        queryString = IOUtils.toString(getClass().getClassLoader().getResource(resourceId).openStream());
    }

    /* package */ String calculatePrettyDiagramRatio() {
        OntModel    ontmodel = ss.getOntology();

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
