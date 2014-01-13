package org.ontologyengineering.ontometrics.plugins;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class TopSubsetOnly extends DiagramMetric {
    public TopSubsetOnly() {
        super("Count of Top Subsumes Only axioms", "TopSubsetOnly", "simple_topsubsetonly.sparql");
    }
}