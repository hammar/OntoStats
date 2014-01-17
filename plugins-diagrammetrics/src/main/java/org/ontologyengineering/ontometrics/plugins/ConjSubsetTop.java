package org.ontologyengineering.ontometrics.plugins;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class ConjSubsetTop extends DiagramMetric {
    public ConjSubsetTop() {
        super("Count of (Atom Conj Atom) Subsumes Top axioms", "ConjSubsetTop", "simple_conjsubsettop.sparql");
    }
}
