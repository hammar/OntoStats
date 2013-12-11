package org.ontologyengineering.ontometrics.plugins;

public class ConjSubsetDisj extends DiagramMetric {
    public ConjSubsetDisj() {
        super("Count of (Atom Conj Atom) Subsumes (Atom Disj Atom) axioms", "ConjSubsetDisj", "simple_conjsubsetdisj.sparql");
    }
}
