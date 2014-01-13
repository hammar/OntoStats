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
                // Atom subset *
                new TestStruct[]{new TestStruct(new AtomSubsetAtom(),      Filter.FilterType.ATOM_ONLY, Filter.FilterType.ATOM_ONLY)}
                , new TestStruct[]{new TestStruct(new AtomSubsetNegAtom(), Filter.FilterType.ATOM_ONLY, Filter.FilterType.COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new AtomSubsetConj(),    Filter.FilterType.ATOM_ONLY, Filter.FilterType.CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new AtomSubsetDisj(),    Filter.FilterType.ATOM_ONLY, Filter.FilterType.DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new AtomSubsetSome(),    Filter.FilterType.ATOM_ONLY, Filter.FilterType.SOMEOF)}
                , new TestStruct[]{new TestStruct(new AtomSubsetOnly(),    Filter.FilterType.ATOM_ONLY, Filter.FilterType.ALLOF)}
                , new TestStruct[]{new TestStruct(new AtomSubsetTop(),     Filter.FilterType.ATOM_ONLY, Filter.FilterType.TOP)}
                // Conj subset *
                , new TestStruct[]{new TestStruct(new ConjSubsetAtom(),    Filter.FilterType.CONJUNCTION, Filter.FilterType.ATOM_ONLY)}
                , new TestStruct[]{new TestStruct(new ConjSubsetNegAtom(), Filter.FilterType.CONJUNCTION, Filter.FilterType.COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new ConjSubsetConj(),    Filter.FilterType.CONJUNCTION, Filter.FilterType.CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new ConjSubsetDisj(),    Filter.FilterType.CONJUNCTION, Filter.FilterType.DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new ConjSubsetSome(),    Filter.FilterType.CONJUNCTION, Filter.FilterType.SOMEOF)}
                , new TestStruct[]{new TestStruct(new ConjSubsetOnly(),    Filter.FilterType.CONJUNCTION, Filter.FilterType.ALLOF)}
                //, new TestStruct[]{new TestStruct(new ConjSubsetTop(),     Filter.FilterType.CONJUNCTION, Filter.FilterType.TOP)}
                // Disj subset *
                , new TestStruct[]{new TestStruct(new DisjSubsetAtom(),    Filter.FilterType.DISJUNCTION, Filter.FilterType.ATOM_ONLY)}
                , new TestStruct[]{new TestStruct(new DisjSubsetNegAtom(), Filter.FilterType.DISJUNCTION, Filter.FilterType.COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new DisjSubsetConj(),    Filter.FilterType.DISJUNCTION, Filter.FilterType.CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new DisjSubsetDisj(),    Filter.FilterType.DISJUNCTION, Filter.FilterType.DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new DisjSubsetSome(),    Filter.FilterType.DISJUNCTION, Filter.FilterType.SOMEOF)}
                , new TestStruct[]{new TestStruct(new DisjSubsetOnly(),    Filter.FilterType.DISJUNCTION, Filter.FilterType.ALLOF)}
                //, new TestStruct[]{new TestStruct(new ConjSubsetTop(),     Filter.FilterType.CONJUNCTION, Filter.FilterType.TOP)}
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
