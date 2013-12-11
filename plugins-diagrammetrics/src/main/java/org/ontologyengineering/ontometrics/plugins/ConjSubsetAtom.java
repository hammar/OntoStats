package org.ontologyengineering.ontometrics.plugins;

public class ConjSubsetAtom extends DiagramMetric {
    public ConjSubsetAtom () {
        super("Count of (Atom Conj Atom) Subsumes Atom axioms", "ConjSubsetAtom", "simple_conjsubsetatom.sparql");
    }
}
