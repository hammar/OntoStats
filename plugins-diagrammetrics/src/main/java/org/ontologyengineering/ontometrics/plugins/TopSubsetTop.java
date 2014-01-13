package org.ontologyengineering.ontometrics.plugins;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class TopSubsetTop extends DiagramMetric {
    public TopSubsetTop() {
        super("Count of Top Subsumes Top axioms", "TopSubsetTop", "simple_topsubsettop.sparql");
    }
}