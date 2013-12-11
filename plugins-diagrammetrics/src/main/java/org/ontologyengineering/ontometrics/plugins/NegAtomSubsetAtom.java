package org.ontologyengineering.ontometrics.plugins;

/**
 *
 * @author Aidan Delaney <aidan@phoirc.eu>
 */
public class NegAtomSubsetAtom extends DiagramMetric {
    public NegAtomSubsetAtom () {
        super("Count of (Neg Atom) Subsumes Atom axioms", "NegAtomSubsetAtom", "simple_negatomsubsetatom.sparql");
    }
}
