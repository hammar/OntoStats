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
                , new TestStruct[]{new TestStruct(new AtomSubsetNegAtom(), Filter.FilterType.ATOM_ONLY, Filter.FilterType.ATOM_COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new AtomSubsetConj(),    Filter.FilterType.ATOM_ONLY, Filter.FilterType.ATOM_CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new AtomSubsetDisj(),    Filter.FilterType.ATOM_ONLY, Filter.FilterType.ATOM_DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new AtomSubsetSome(),    Filter.FilterType.ATOM_ONLY, Filter.FilterType.ATOM_SOMEOF)}
                , new TestStruct[]{new TestStruct(new AtomSubsetOnly(),    Filter.FilterType.ATOM_ONLY, Filter.FilterType.ATOM_ALLOF)}
                , new TestStruct[]{new TestStruct(new AtomSubsetTop(),     Filter.FilterType.ATOM_ONLY, Filter.FilterType.TOP)}

                /*// Conj subset *
                , new TestStruct[]{new TestStruct(new ConjSubsetAtom(),    Filter.FilterType.ATOM_CONJUNCTION, Filter.FilterType.ATOM_ONLY)}
                , new TestStruct[]{new TestStruct(new ConjSubsetNegAtom(), Filter.FilterType.ATOM_CONJUNCTION, Filter.FilterType.ATOM_COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new ConjSubsetConj(),    Filter.FilterType.ATOM_CONJUNCTION, Filter.FilterType.ATOM_CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new ConjSubsetDisj(),    Filter.FilterType.ATOM_CONJUNCTION, Filter.FilterType.ATOM_DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new ConjSubsetSome(),    Filter.FilterType.ATOM_CONJUNCTION, Filter.FilterType.ATOM_SOMEOF)}
                , new TestStruct[]{new TestStruct(new ConjSubsetOnly(),    Filter.FilterType.ATOM_CONJUNCTION, Filter.FilterType.ATOM_ALLOF)}
                //, new TestStruct[]{new TestStruct(new ConjSubsetTop(),     Filter.FilterType.ATOM_CONJUNCTION, Filter.FilterType.TOP)}
                // Disj subset *
                , new TestStruct[]{new TestStruct(new DisjSubsetAtom(),    Filter.FilterType.ATOM_DISJUNCTION, Filter.FilterType.ATOM_ONLY)}
                , new TestStruct[]{new TestStruct(new DisjSubsetNegAtom(), Filter.FilterType.ATOM_DISJUNCTION, Filter.FilterType.ATOM_COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new DisjSubsetConj(),    Filter.FilterType.ATOM_DISJUNCTION, Filter.FilterType.ATOM_CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new DisjSubsetDisj(),    Filter.FilterType.ATOM_DISJUNCTION, Filter.FilterType.ATOM_DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new DisjSubsetSome(),    Filter.FilterType.ATOM_DISJUNCTION, Filter.FilterType.ATOM_SOMEOF)}
                , new TestStruct[]{new TestStruct(new DisjSubsetOnly(),    Filter.FilterType.ATOM_DISJUNCTION, Filter.FilterType.ATOM_ALLOF)}
                //, new TestStruct[]{new TestStruct(new DisjSubsetTop(),     Filter.FilterType.ATOM_DISJUNCTION, Filter.FilterType.TOP)}
                // Negation subset *
                , new TestStruct[]{new TestStruct(new NegAtomSubsetAtom(),    Filter.FilterType.ATOM_COMPLEMENT, Filter.FilterType.ATOM_ONLY)}
                , new TestStruct[]{new TestStruct(new NegAtomSubsetNegAtom(), Filter.FilterType.ATOM_COMPLEMENT, Filter.FilterType.ATOM_COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new NegAtomSubsetConj(),    Filter.FilterType.ATOM_COMPLEMENT, Filter.FilterType.ATOM_CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new NegAtomSubsetDisj(),    Filter.FilterType.ATOM_COMPLEMENT, Filter.FilterType.ATOM_DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new NegAtomSubsetSome(),    Filter.FilterType.ATOM_COMPLEMENT, Filter.FilterType.ATOM_SOMEOF)}
                , new TestStruct[]{new TestStruct(new NegAtomSubsetOnly(),    Filter.FilterType.ATOM_COMPLEMENT, Filter.FilterType.ATOM_ALLOF)}
                //, new TestStruct[]{new TestStruct(new NegAtomSubsetTop(),    Filter.FilterType.ATOM_COMPLEMENT, Filter.FilterType.TOP)}
                // Only subset *
                , new TestStruct[]{new TestStruct(new OnlySubsetAtom(),    Filter.FilterType.ATOM_ALLOF, Filter.FilterType.ATOM_ONLY)}
                , new TestStruct[]{new TestStruct(new OnlySubsetNegAtom(), Filter.FilterType.ATOM_ALLOF, Filter.FilterType.ATOM_COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new OnlySubsetConj(),    Filter.FilterType.ATOM_ALLOF, Filter.FilterType.ATOM_CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new OnlySubsetDisj(),    Filter.FilterType.ATOM_ALLOF, Filter.FilterType.ATOM_DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new OnlySubsetSome(),    Filter.FilterType.ATOM_ALLOF, Filter.FilterType.ATOM_SOMEOF)}
                , new TestStruct[]{new TestStruct(new OnlySubsetOnly(),    Filter.FilterType.ATOM_ALLOF, Filter.FilterType.ATOM_ALLOF)}
                //, new TestStruct[]{new TestStruct(new OnlySubsetTop(),    Filter.FilterType.ATOM_ALLOF, Filter.FilterType.TOP)}
                // Some subset *
                , new TestStruct[]{new TestStruct(new SomeSubsetAtom(),    Filter.FilterType.ATOM_SOMEOF, Filter.FilterType.ATOM_ONLY)}
                , new TestStruct[]{new TestStruct(new SomeSubsetNegAtom(), Filter.FilterType.ATOM_SOMEOF, Filter.FilterType.ATOM_COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new SomeSubsetConj(),    Filter.FilterType.ATOM_SOMEOF, Filter.FilterType.ATOM_CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new SomeSubsetDisj(),    Filter.FilterType.ATOM_SOMEOF, Filter.FilterType.ATOM_DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new SomeSubsetSome(),    Filter.FilterType.ATOM_SOMEOF, Filter.FilterType.ATOM_SOMEOF)}
                , new TestStruct[]{new TestStruct(new SomeSubsetOnly(),    Filter.FilterType.ATOM_SOMEOF, Filter.FilterType.ATOM_ALLOF)}
                //, new TestStruct[]{new TestStruct(new SomeSubsetTop(),    Filter.FilterType.ATOM_SOMEOF, Filter.FilterType.TOP)}
                // Top subset *
                , new TestStruct[]{new TestStruct(new TopSubsetAtom(),    Filter.FilterType.TOP, Filter.FilterType.ATOM_ONLY)}
                , new TestStruct[]{new TestStruct(new TopSubsetNegAtom(), Filter.FilterType.TOP, Filter.FilterType.ATOM_COMPLEMENT)}
                , new TestStruct[]{new TestStruct(new TopSubsetConj(),    Filter.FilterType.TOP, Filter.FilterType.ATOM_CONJUNCTION)}
                , new TestStruct[]{new TestStruct(new TopSubsetDisj(),    Filter.FilterType.TOP, Filter.FilterType.ATOM_DISJUNCTION)}
                , new TestStruct[]{new TestStruct(new TopSubsetSome(),    Filter.FilterType.TOP, Filter.FilterType.ATOM_SOMEOF)}
                , new TestStruct[]{new TestStruct(new TopSubsetOnly(),    Filter.FilterType.TOP, Filter.FilterType.ATOM_ALLOF)}
                , new TestStruct[]{new TestStruct(new TopSubsetTop(),     Filter.FilterType.TOP, Filter.FilterType.TOP)}*/
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
