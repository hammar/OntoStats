package org.ontologyengineering.ontometrics.plugins;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AtomSubsetConjTest {
    @Test
    public void simpleTest() {
        AtomSubsetConj cut = new AtomSubsetConj();
        String res = TestUtils.runSimpleTest(TestUtils.getOWLFile(getClass().getName()), cut);
        assertEquals(new String("1.0"), res);
    }

    @Test
    public void gremlinComparision () {
        AtomSubsetConj cut = new AtomSubsetConj();
        // we're only looking for atoms, so filter nothing.
        String g = TestUtils.runGremlinTestDataComparison(Filter.FilterType.ATOM_ONLY, Filter.FilterType.CONJUNCTION);
        String j = TestUtils.runJenaTestDataFileComparison(cut);

        assertEquals(j, g); // Compare the Jena result with the Gremlin result.
    }
}
