package org.ontologyengineering.ontometrics.plugins;

public class NegAtomSubsetOnly extends DiagramMetric {
    public NegAtomSubsetOnly () {
        super ("Count of (Neg Atom) Subsumes (Only) axioms", "NegAtomSubsetOnly", "simple_negatomsubsetonly.sparql");
    }
}
