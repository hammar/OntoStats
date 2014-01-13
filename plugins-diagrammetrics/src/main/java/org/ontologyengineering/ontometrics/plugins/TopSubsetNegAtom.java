package org.ontologyengineering.ontometrics.plugins;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class TopSubsetNegAtom extends DiagramMetric {
    public TopSubsetNegAtom() {
        super("Count of Top Subsumes (Neg Atom) axioms", "TopSubsetNegAtom", "simple_topsubsetnegatom.sparql");
    }
}
