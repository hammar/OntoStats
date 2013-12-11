package org.ontologyengineering.ontometrics.plugins;

public class ConjSubsetOnly extends DiagramMetric {
    public ConjSubsetOnly() {
        super("Count of (Atom Conj Atom) Subsumes Only axioms", "ConjSubsetOnly", "simple_conjsubsetonly.sparql");
    }
}
