package org.ontologyengineering.ontometrics.plugins;

public class NegAtomSubsetConj extends DiagramMetric {

    public NegAtomSubsetConj () {
        super("Count of (Neg Atom) Subsumes (Atom Conj Atom) axioms","NegAtomSubsetConj", "simple_negatomsubsetconj.sparql");
    }
}
