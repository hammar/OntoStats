package org.ontologyengineering.ontometrics.plugins;

public class AtomSubsetConj extends DiagramMetric {
    public AtomSubsetConj() {
        super("Count of Atom Subsumes (Atom Conj Atom) axioms to the TBox size", "AtomSubsetConj", "simple_atomsubsetconj.sparql");
    }
}
