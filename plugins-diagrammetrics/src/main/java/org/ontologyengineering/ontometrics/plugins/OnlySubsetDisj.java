package org.ontologyengineering.ontometrics.plugins;

public class OnlySubsetDisj extends DiagramMetric {

    public OnlySubsetDisj () {
        super("Count of Only Subsumes (Atom Disj Atom) axioms", "OnlySubsetDisj", "simple_onlysubsetdisj.sparql");
    }
}
