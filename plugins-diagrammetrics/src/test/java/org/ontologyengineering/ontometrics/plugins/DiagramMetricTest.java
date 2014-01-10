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
                , new TestStruct[]{new TestStruct(new AtomSubsetConj(), Filter.FilterType.ATOM_ONLY, Filter.FilterType.CONJUNCTION)}
        );
    }

    private TestStruct ts;
    public DiagramMetricTest(TestStruct ts) {
        this.ts = ts;
    }

    @Test
    public void simpleTest() {
        String res = TestUtils.runSimpleTest(TestUtils.getOWLFile(ts.dm.getClass().getName()), ts.dm);
        assertEquals(new String("1.0"), res);
    }

    @Test
    public void runComparisonTest() {
        for(File f: TestUtils.getTestDataFiles()) {
            String g = TestUtils.runGremlinTestDataComparison(f, ts.lhs, ts.rhs);
            String j = TestUtils.runJenaTestDataFileComparison(f, ts.dm);
            assertEquals(j, g); // Compare the Jena result with the Gremlin result.
        }
    }
}
