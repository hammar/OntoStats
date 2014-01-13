package org.ontologyengineering.ontometrics.plugins;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class TopSubsetDisj extends DiagramMetric {
    public TopSubsetDisj() {
        super("Count of Top Subsumes (Atom Disj Atom) axioms", "TopSubsetDisj", "simple_topsubsetdisj.sparql");
    }
}