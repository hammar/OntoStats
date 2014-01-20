package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.karlhammar.ontometrics.plugins.LazyParserGremlin;
import com.karlhammar.ontometrics.plugins.ParserConfiguration;
import com.karlhammar.ontometrics.plugins.ParserJena;
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

    public static List<File> getTestDataFiles() {
        String basename = dirname + File.separator;
        return Arrays.asList(new File(basename + "14672877-e396-415e-b8e8-891c072bd662_test15.owl"), new File(basename + "BuildingsAndPlaces.rdf"), new File(basename + "ssn.owl"));
    }

    public static String runSimpleTest(File owl, DiagramMetric dm) {
        ParserJena    jena = ParserJena.resetSingletonObject(owl, new ParserConfiguration());
        dm.init(jena, null, null);
        return dm.getMetricValue(owl);
    }

    public static String runJenaTestDataFileComparison(File testFile, DiagramMetric dm) {
        ParserJena    jena = ParserJena.resetSingletonObject(testFile, new ParserConfiguration());
        dm.init(jena, null, null);
        return dm.getMetricValue(testFile);
    }

    public static String runGremlinTestDataComparison(File testFile, Filter.FilterType lhs, Filter.FilterType rhs) {
        LazyParserGremlin lpg = LazyParserGremlin.resetSingletonObject(testFile, new ParserConfiguration());
        SailGraph          sg = lpg.getOntology();
        GremlinPipeline    gp = new GremlinPipeline(sg.getEdges()).has("label", rdfsnsSubClassOf).and(
                new GremlinPipeline().outV().add((new Filter(sg, lhs)).getPipeline())
                , new GremlinPipeline().inV().add((new Filter(sg, rhs)).getPipeline())
        ).dedup();

        GremlinPipeline    eq = new GremlinPipeline(sg.getEdges()).has("label", owlnsEquivalentClass).and(
                new GremlinPipeline().outV().add((new Filter(sg, lhs)).getPipeline())
                , new GremlinPipeline().inV().add((new Filter(sg, rhs)).getPipeline())
        ).dedup();

        // Each A \equiv B counts as A \sqsubset B and B \sqsubseeq A, so double the total of equivs found.
        return Double.toString(gp.count() + (eq.count() * 2));
    }
}
