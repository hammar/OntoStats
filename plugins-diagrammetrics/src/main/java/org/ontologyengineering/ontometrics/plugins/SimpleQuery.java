package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.*;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.karlhammar.ontometrics.plugins.StructuralSingleton;
import com.karlhammar.ontometrics.plugins.StructuralSingletonOWLAPI;

public class SimpleQuery {
    private Logger logger = Logger.getLogger(getClass().getName());
    private StructuralSingletonOWLAPI sowl;
    private StructuralSingleton ss;

    // this is almost documentation.
    public String queryString;

    /* package */ SimpleQuery(File ontologyFile, String resourceId) throws IOException {
        sowl = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile);
        ss   = StructuralSingleton.getSingletonObject(ontologyFile, true);
        queryString = IOUtils.toString(getClass().getClassLoader().getResource(resourceId).openStream());
    }

    /* package */ String calculatePrettyDiagramRatio() {
        OWLOntology owlmodel = sowl.getOntology();
        OntModel    ontmodel = ss.getOntology();

        // Get number of tbox axioms in ontology.
        //double tboxes = 0.0;
        //for(AxiomType<?> tbox: AxiomType.TBoxAxiomTypes) {
        //    tboxes += owlmodel.getAxiomCount(tbox);
        //}

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
