package org.ontologyengineering.ontometrics.plugins;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.karlhammar.ontometrics.plugins.LazyParserGremlin;
import com.karlhammar.ontometrics.plugins.ParserConfiguration;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.tinkerpop.blueprints.impls.sail.SailGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

import org.ontologyengineering.ontometrics.plugins.AtomSubsetAtom;
import org.junit.*;

import static org.junit.Assert.*;

public class AtomSubsetAtomTest {
    public static final String  owlns = "http://www.w3.org/2002/07/owl#",
            rdfns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
           rdfsns = "http://www.w3.org/2000/01/rdf-schema#";

    private File owl, ssn;
    private ParserJena jena;

    @Before
    public void setUp() {
        owl = TestUtils.getOWLFile(getClass().getName());
        ssn = TestUtils.getSSNFile();
    }

    @Test
    public void simpleTest() {
        AtomSubsetAtom asa = new AtomSubsetAtom();
                      jena = ParserJena.resetSingletonObject(owl, new ParserConfiguration());
        asa.init(jena, null, null);
        String res = asa.getMetricValue(owl);
        assertEquals(new String("1.0"), res);
    }

    // Need a combined.owl file that parses correctly.
//    @Test
//    public void combinedTest() {
//        AtomSubsetAtom asa = new AtomSubsetAtom();
//                      jena = ParserJena.resetSingletonObject(combined, new ParserConfiguration());
//        asa.init(jena, null, null);
//        String res = asa.getMetricValue(combined);
//        assertEquals(new String("1.0"), res);
//    }

    @Test
    public void gremlinComparision () {
        AtomSubsetAtom asa = new AtomSubsetAtom();
                      jena = ParserJena.resetSingletonObject(ssn, new ParserConfiguration());
        asa.init(jena, null, null);
        String res = asa.getMetricValue(ssn);

        LazyParserGremlin lpg = LazyParserGremlin.resetSingletonObject(ssn, new ParserConfiguration());
        SailGraph          sg = lpg.getOntology();
        List<String>  filters = new ArrayList<String>(); // Empty list
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

        assertEquals(res, new Double(count).toString()); // Compare the Jena result with the Gremlin result.
    }
}