package org.ontologyengineering.ontometrics.plugins;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

import static org.junit.Assert.*;

public class AtomSubsetAtomTest {
    @Test
    public void simpleTest() {
        AtomSubsetAtom cut = new AtomSubsetAtom();
        String res = TestUtils.runSimpleTest(TestUtils.getOWLFile(getClass().getName()), cut);
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
        String g = TestUtils.runGremlinSSNComparision(Filter.FilterType.ATOM_ONLY, Filter.FilterType.ATOM_ONLY);
        String j = TestUtils.runJenaSSNComparision(cut);

        assertEquals(j, g); // Compare the Jena result with the Gremlin result.
    }
}