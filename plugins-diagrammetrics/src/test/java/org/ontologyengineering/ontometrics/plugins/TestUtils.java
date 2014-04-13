package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.karlhammar.ontometrics.plugins.LazyParserGremlin;
import com.karlhammar.ontometrics.plugins.ParserConfiguration;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;

import org.ontologyengineering.ontometrics.plugins.DiagramMetric;
import org.ontologyengineering.ontometrics.plugins.Filter.FilterType;

import com.tinkerpop.blueprints.impls.sail.SailGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class TestUtils {

    public static final String  owlns = "http://www.w3.org/2002/07/owl#",
                                rdfns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                               rdfsns = "http://www.w3.org/2000/01/rdf-schema#",
                            rdfnsType = rdfns + "type",
                           rdfnsFirst = rdfns + "first",
                            rdfnsRest = rdfns + "rest",
                             rdfnsNil = rdfns + "nil",
                     rdfsnsSubClassOf = rdfsns + "subClassOf",
                 owlnsEquivalentClass = owlns + "equivalentClass",
                           owlnsClass = owlns + "Class",
                           owlnsThing = owlns + "Thing",
                    owlnsComplementOf = owlns + "complementOf",
                         owlnsUnionOf = owlns + "unionOf",
                  owlnsIntersectionOf = owlns + "intersectionOf",
                    owlSomeValuesFrom = owlns + "someValuesFrom",
                     owlAllValuesFrom = owlns + "allValuesFrom";

    static final String dirname = System.getProperty("diagrammetrics.test.resources");

    /**
     * 
     * @param basename of form foo.bar.ClassNameTest
     * @return
     */
    public static File getOWLFile(String basename) {
        // get the name of the OWL file
        File        owl = null;
        basename        = basename.substring(basename.lastIndexOf('.') + 1); // remove "foo.bar."
        basename        = basename.replaceFirst("Test", ""); // remove Test from ClassNameTest
        String    fname = dirname + File.separator + "simple_" + basename.toLowerCase() + ".owl"; // our files are called simple_classname.owl

        try {
            owl      = new File(fname);
            //combined = new File(dirname + File.separator + "combined.owl");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return owl;
    }

    public static List<File> getALCFiles() {
        String basename = dirname + File.separator;
        return Arrays.asList(new File(basename + "ALC-0"), new File(basename + "ALC-1")
                           , new File(basename + "ALC-2"), new File(basename + "ALC-3")
                           , new File(basename + "ALC-4"), new File(basename + "ALC-5")
                           , new File(basename + "ALC-6"));
    }

    public static File getSSNFile() {
        String basename = dirname + File.separator;
        return new File(basename + "ssn.owl");
    }

    public static List<File> getTestDataFiles() {
        String basename = dirname + File.separator;
        return Arrays.asList(new File(basename + "14672877-e396-415e-b8e8-891c072bd662_test15.owl"), new File(basename + "BuildingsAndPlaces.rdf"), new File(basename + "ssn.owl"));
    }

    public static String runSimpleTest(File owl, DiagramMetric dm) {
        ParserJena       jena = new ParserJena(owl);
        LazyParserGremlin lpg = new LazyParserGremlin(owl);
        ParserOWLAPI   owlapi = new ParserOWLAPI(owl);

        dm.init(jena, owlapi, lpg);
        return dm.getMetricValue(owl).get();
    }


    public static String runGremlinTest(File owl, DiagramMetric dm) {
        LazyParserGremlin lpg = new LazyParserGremlin(owl);
        GremlinQuery       gq = new GremlinQuery(lpg, dm.getLHS(), dm.getRHS());
        return gq.runQuery();
    }

    public static String runOWLAPITest(File owl, DiagramMetric dm) {
        ParserOWLAPI poa = new ParserOWLAPI(owl);
        OWLAPIQuery  oaq = new OWLAPIQuery(poa, dm.getLHS(), dm.getRHS());
        return oaq.runQuery();
    }

    public static String runJenaTest(File owl, DiagramMetric dm) throws IOException {
        ParserJena  pj = new ParserJena(owl);
        SparqlQuery jq = new SparqlQuery(pj, dm.getLHS(), dm.getRHS());
        return jq.runQuery();
    }
}
