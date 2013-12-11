package org.ontologyengineering.ontometrics.plugins;

public class DisjSubsetDisj extends DiagramMetric {
    public DisjSubsetDisj() {
        super("Count of (Atom Disj Atom) Subsumes (Atom Disj Atom) axioms", "DisjSubsetDisj", "simple_disjsubsetdisj.sparql");
    }
}
