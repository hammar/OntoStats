package org.ontologyengineering.ontometrics.plugins;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class TopSubsetAtom extends DiagramMetric {
    public TopSubsetAtom() {
        super("Count of Top Subsumes Atom axioms", "TopSubsetAtom", "simple_topsubsetatom.sparql");
    }
}