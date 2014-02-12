package org.ontologyengineering.ontometrics.plugins;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class DiagramAxiomDefinitionTest {
    @Test
    public void testAxiomDefinition () {
        TestStruct ts = new TestStruct(new AtomSubsetAtom(), Filter.FilterType.ATOM, Filter.FilterType.ATOM);
        String atomSubsetAtomResult = TestUtils.runSimpleTest(TestUtils.getOWLFile(this.getClass().getName()), ts.dm);

        assertEquals("", "10.0", atomSubsetAtomResult);
    }
}
