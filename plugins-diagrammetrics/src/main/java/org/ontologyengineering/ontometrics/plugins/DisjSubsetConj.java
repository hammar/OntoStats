package org.ontologyengineering.ontometrics.plugins;

public class DisjSubsetConj extends DiagramMetric {
    public DisjSubsetConj() {
        super("Count of (Atom Disj Atom) Subsumes (Atom Conj Atom) axioms", "DisjSubsetConj", "simple_disjsubsetconj.sparql");
    }
}
