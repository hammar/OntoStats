package org.ontologyengineering.ontometrics.plugins;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
@RunWith(Parameterized.class)
public class DiagramMetricTest {

    @Parameterized.Parameters
    public static Collection<TestStruct []> data() {
        return Arrays.asList(
                new TestStruct[]{new TestStruct(new AtomSubsetAtom(), Filter.FilterType.ATOM_ONLY, Filter.FilterType.ATOM_ONLY)}
                , new TestStruct[]{new TestStruct(new AtomSubsetNegAtom(), Filter.FilterType.ATOM_ONLY, Filter.FilterType.COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new AtomSubsetConj(), Filter.FilterType.ATOM_ONLY, Filter.FilterType.CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new AtomSubsetDisj(), Filter.FilterType.ATOM_ONLY, Filter.FilterType.DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new AtomSubsetSome(), Filter.FilterType.ATOM_ONLY, Filter.FilterType.SOMEOF)}
                , new TestStruct[]{new TestStruct(new AtomSubsetOnly(), Filter.FilterType.ATOM_ONLY, Filter.FilterType.ALLOF)}
                , new TestStruct[]{new TestStruct(new AtomSubsetTop(), Filter.FilterType.ATOM_ONLY, Filter.FilterType.TOP)}
        );
    }

    private TestStruct ts;
    public DiagramMetricTest(TestStruct ts) {
        this.ts = ts;
    }

    @Test
    public void simpleTest() {
        String res = TestUtils.runSimpleTest(TestUtils.getOWLFile(ts.dm.getClass().getName()), ts.dm);
        assertEquals("Failure on " + ts.toString() + " with simpleTest()", new String("1.0"), res);
    }

    @Test
    public void runComparisonTest() {
        for(File f: TestUtils.getTestDataFiles()) {
            String g = TestUtils.runGremlinTestDataComparison(f, ts.lhs, ts.rhs);
            String j = TestUtils.runJenaTestDataFileComparison(f, ts.dm);
            assertEquals("Failure on " + ts.toString() + " with " + f.toString(), j, g); // Compare the Jena result with the Gremlin result.
        }
    }
}
