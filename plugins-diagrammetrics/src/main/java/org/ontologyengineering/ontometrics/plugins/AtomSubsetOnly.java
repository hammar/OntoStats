package org.ontologyengineering.ontometrics.plugins;

public class AtomSubsetOnly extends DiagramMetric {
    public AtomSubsetOnly () {
        super("Count of Atom Subsumes (Only) axioms", "AtomSubsetOnly", "simple_atomsubsetonly.sparql");
    }
}
