package org.ontologyengineering.ontometrics.plugins;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class TopSubsetSome extends DiagramMetric {
    public TopSubsetSome() {
        super("Count of Top Subsumes Some axioms", "TopSubsetSome", "simple_topsubsetsome.sparql");
    }
}
