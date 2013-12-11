package org.ontologyengineering.ontometrics.plugins;

public class DisjSubsetNegAtom extends DiagramMetric {
    public DisjSubsetNegAtom () {
        super("Count of (Atom Disj Atom) Subsumes (Neg Atom) axioms", "DisjSubsetNegAtom", "simple_disjsubsetnegatom.sparql");
    }
}
