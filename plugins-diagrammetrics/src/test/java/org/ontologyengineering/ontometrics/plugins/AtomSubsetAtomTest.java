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
    String xmlrdfEquivClass = 
            "<?xml version=\"1.0\"?>\n" + 
            "<!DOCTYPE rdf:RDF [\n" + 
            "	<!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >\n" + 
            "	<!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >\n" + 
            "	<!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >\n" + 
            "	<!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >\n" + 
            "]>\n" + 
            "\n" + 
            "<rdf:RDF xmlns=\"http://www.ontologyengineering.org/testAtomSubsetAtom#\"\n" + 
            "	xml:base=\"http://www.ontologyengineering.org/testAtomSubsetAtom\"\n" + 
            "	xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" + 
            "	xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n" + 
            "	xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"\n" + 
            "	xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\">\n" + 
            "    <owl:Ontology rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom\"/>\n" + 
            "<owl:Class rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom#A\"/>\n" + 
            "    <owl:Class rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom#B\" />\n" + 
            "    <owl:Class rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom#C\">\n" + 
            "        <owl:equivalentClass>\n" + 
            "            <owl:Class>\n" + 
            "                <owl:unionOf rdf:parseType=\"Collection\">\n" + 
            "                    <rdf:Description rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom#A\"/>\n" + 
            "                    <rdf:Description rdf:about=\"http://www.ontologyengineering.org/testAtomSubsetAtom#B\"/>\n" + 
            "                </owl:unionOf>\n" + 
            "            </owl:Class>\n" + 
            "        </owl:equivalentClass>\n" + 
            "    </owl:Class>\n" + 
            "</rdf:RDF>";

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void simpleOWLAPITest() throws IOException {
        AtomSubsetAtom cut = new AtomSubsetAtom();
        File tmp = Files.createTempFile("xmlrdfAtomSubsetAtom",".xml").toFile();
        FileUtils.write(tmp, xmlrdfAtomSubsetAtom);

        String res = TestUtils.runOWLAPITest(tmp, cut);
        assertEquals(new String("1.0"), res);
    }

    @Test
    public void simpleJenaTest() throws IOException {
        AtomSubsetAtom cut = new AtomSubsetAtom();
        File tmp = Files.createTempFile("xmlrdfAtomSubsetAtom",".xml").toFile();
        FileUtils.write(tmp, xmlrdfAtomSubsetAtom);

        String res = TestUtils.runJenaTest(tmp, cut);
        assertEquals(new String("1.0"), res);
    }

    @Test
    public void checkOWLAPINegation() throws IOException {
        AtomSubsetAtom cut = new AtomSubsetAtom();
        File tmp = Files.createTempFile("xmlrdfNegAtomSubsetAtom",".xml").toFile();
        FileUtils.write(tmp, xmlrdfNegAtomSubsetAtom);

        String res = TestUtils.runOWLAPITest(tmp, cut);
        assertEquals(new String("2.0"), res);
    }

    @Test
    public void checkComplexAgreement() throws IOException {
        AtomSubsetAtom cut = new AtomSubsetAtom();
        File tmp = Files.createTempFile("xmlrdfEquivClass",".xml").toFile();
        FileUtils.write(tmp, xmlrdfEquivClass);

        String  owl = TestUtils.runOWLAPITest(tmp, cut);
        String jena = TestUtils.runJenaTest(tmp, cut);
        assertEquals(owl, jena);
    }

    @Test
    public void checkJenaNegation() throws IOException {
        AtomSubsetAtom cut = new AtomSubsetAtom();
        File tmp = Files.createTempFile("xmlrdfNegAtomSubsetAtom",".xml").toFile();
        FileUtils.write(tmp, xmlrdfNegAtomSubsetAtom);

        String res = TestUtils.runJenaTest(tmp, cut);
        assertEquals(new String("2.0"), res);
    }

    @Test
    public void doALCTest() throws IOException {
        AtomSubsetAtom cut = new AtomSubsetAtom();
        for (File alc :TestUtils.getALCFiles()) {
            String owlapi = TestUtils.runOWLAPITest(alc, cut);
            String   jena = TestUtils.runJenaTest(alc, cut);

            assertEquals("Failed with file " + alc.toString(), owlapi, jena);
        }
    }

    @Test
    public void doSSNTest() throws IOException {
        AtomSubsetAtom cut = new AtomSubsetAtom();
        File ssn = TestUtils.getSSNFile();
        String owlapi = TestUtils.runOWLAPITest(ssn, cut);
        String   jena = TestUtils.runJenaTest(ssn, cut);

        assertEquals(owlapi, jena);
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