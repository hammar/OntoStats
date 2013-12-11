package org.ontologyengineering.ontometrics.plugins;

public class NegAtomSubsetDisj extends DiagramMetric {
    public NegAtomSubsetDisj () {
        super("Count of (Neg Atom) Subsumes (Atom Disj Atom) axioms", "NegAtomSubsetDisj", "simple_negatomsubsetdisj.sparql");
    }
}
