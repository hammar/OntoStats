package org.ontologyengineering.ontometrics.plugins;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class TopSubsetConj extends DiagramMetric {
    public TopSubsetConj() {
        super("Count of Top Subsumes (Atom Conj Atom) axioms", "TopSubsetConj", "simple_topsubsetconj.sparql");
    }
}
