package org.ontologyengineering.ontometrics.plugins;

public class DisjSubsetOnly extends DiagramMetric {
    public DisjSubsetOnly () {
        super("Count of (Atom Disj Atom) Subsumes Only axioms", "DisjSubsetOnly", "simple_disjsubsetonly.sparql");
    }
}
