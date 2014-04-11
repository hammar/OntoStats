package org.ontologyengineering.ontometrics.plugins;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class AtomSubsetAtomTest {

    String xmlrdfAtomSubsetAtom = 
        "<?xml version=\"1.0\"?>" + System.lineSeparator() +
        "<!DOCTYPE rdf:RDF [" + System.lineSeparator() +
            "<!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >" + System.lineSeparator() +
            "<!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >" + System.lineSeparator() +
            "<!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >" + System.lineSeparator() +
            "<!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >" + System.lineSeparator() +
            "]>" +
            "<rdf:RDF xmlns=\"http://www.ontologyengineering.org/testAtomSubsetAtom#\"" +
                      " xml:base=\"http://www.ontologyengineering.org/testAtomSubsetAtom\"" +
                      " xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" +
                      " xmlns:owl=\"http://www.w3.org/2002/07/owl#\"" +
                      " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"" +
                      " xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\">" +
                           "<owl:Ontology rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom\"/>" +
                           "<owl:Class rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom#A\"/>" +
                           "<owl:Class rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom#B\">" +
                               "<rdfs:subClassOf rdf:resource=\"http://www.ontologyengineering.org/testAtomSubsetAtom#A\"/>" +
                           "</owl:Class>" +
            "</rdf:RDF>";

    String xmlrdfNegAtomSubsetAtom = 
            "<?xml version=\"1.0\"?>" + System.lineSeparator() +
            "<!DOCTYPE rdf:RDF [" + System.lineSeparator() +
                "<!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >" + System.lineSeparator() +
                "<!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >" + System.lineSeparator() +
                "<!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >" + System.lineSeparator() +
                "<!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >" + System.lineSeparator() +
                "]>" +
                "<rdf:RDF xmlns=\"http://www.ontologyengineering.org/testAtomSubsetAtom#\"" +
                          " xml:base=\"http://www.ontologyengineering.org/testAtomSubsetAtom\"" +
                          " xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" +
                          " xmlns:owl=\"http://www.w3.org/2002/07/owl#\"" +
                          " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"" +
                          " xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\">" +
                               "<owl:Ontology rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom\"/>" +
                               "<owl:Class rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom#A\"/>" +
                               "<owl:Class rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom#B\">" +
                                   "<owl:complementOf rdf:resource=\"http://www.ontologyengineering.org/testAtomSubsetAtom#C\"/>" +
                                   "<rdfs:subClassOf rdf:resource=\"http://www.ontologyengineering.org/testAtomSubsetAtom#A\"/>" +
                               "</owl:Class>" +
                               "<owl:Class rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom#C\">" +
                                   "<rdfs:subClassOf rdf:resource=\"http://www.ontologyengineering.org/testAtomSubsetAtom#A\"/>" +
                               "</owl:Class>" +
                "</rdf:RDF>";
    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void simpleTest() throws IOException {
        AtomSubsetAtom cut = new AtomSubsetAtom();
        File tmp = Files.createTempFile("xmlrdfAtomSubsetAtom",".xml").toFile();
        FileUtils.write(tmp, xmlrdfAtomSubsetAtom);

        String res = TestUtils.runOWLAPITest(tmp, cut);
        assertEquals(new String("1.0"), res);
    }

    @Test
    public void checkNegation() throws IOException {
        AtomSubsetAtom cut = new AtomSubsetAtom();
        File tmp = Files.createTempFile("xmlrdfNegAtomSubsetAtom",".xml").toFile();
        FileUtils.write(tmp, xmlrdfNegAtomSubsetAtom);

        String res = TestUtils.runOWLAPITest(tmp, cut);
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
        AtomSubsetAtom cut = new AtomSubsetAtom();
        // we're only looking for atoms, so filter nothing.
        /*String g = TestUtils.runGremlinTestDataComparison(Filter.FilterType.ATOM, Filter.FilterType.ATOM);
        String j = TestUtils.runVotingTestDataFileComparison(cut);

        assertEquals(j, g); // Compare the Jena result with the Gremlin result.*/
    }
}