package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.karlhammar.ontometrics.plugins.LazyParserGremlin;
import com.karlhammar.ontometrics.plugins.ParserConfiguration;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.tinkerpop.blueprints.impls.sail.SailGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class TestUtils {

    public static final String  owlns = "http://www.w3.org/2002/07/owl#",
            rdfns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
           rdfsns = "http://www.w3.org/2000/01/rdf-schema#";
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

    public static File getSSNFile() {
        return new File(dirname + File.separator + "ssn.owl");
    }

    public static String runSimpleTest(File owl) {
        AtomSubsetAtom asa = new AtomSubsetAtom();
        ParserJena    jena = ParserJena.resetSingletonObject(owl, new ParserConfiguration());
        asa.init(jena, null, null);
        return asa.getMetricValue(owl);
    }

    public static String runJenaSSNComparision() {
        AtomSubsetAtom asa = new AtomSubsetAtom();
        ParserJena    jena = ParserJena.resetSingletonObject(getSSNFile(), new ParserConfiguration());
        asa.init(jena, null, null);
        return asa.getMetricValue(getSSNFile());
    }

    public static String runGremlinSSNComparision(List<String> filters) {
        LazyParserGremlin lpg = LazyParserGremlin.resetSingletonObject(getSSNFile(), new ParserConfiguration());
        SailGraph          sg = lpg.getOntology();
        double          count = 0.0;
        for(Object edge : new GremlinPipeline(sg.getEdges())
                                .has("label", rdfsns + "subClassOf")
                                .and(
                                        new GremlinPipeline().inV().filter(new FilterMaximalTree(filters))    // filter lhs
                                        , new GremlinPipeline().outV().filter(new FilterMaximalTree(filters)) // filter lrhs
                                     )
           ) {
            count++;
        }
        return new Double(count).toString();
    }
}
