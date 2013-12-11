package org.ontologyengineering.ontometrics.plugins;

public class ConjSubsetConj extends DiagramMetric {
    public ConjSubsetConj () {
        super("Count of (Atom Conj Atom) Subsumes (Atom Conj Atom) axioms", "ConjSubsetConj", "simple_conjsubsetconj.sparql");
    }
}
