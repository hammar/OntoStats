package org.ontologyengineering.ontometrics.plugins;

public class AtomSubsetDisj extends DiagramMetric {
    public AtomSubsetDisj() {
        super("Count of Atom Subsumes (Atom Disj Atom) axioms", "AtomSubsetDisj", "simple_atomsubsetdisj.sparql");
    }
}
