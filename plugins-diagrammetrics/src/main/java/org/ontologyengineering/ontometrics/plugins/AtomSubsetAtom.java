package org.ontologyengineering.ontometrics.plugins;

/**
 *
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class AtomSubsetAtom extends DiagramMetric {
    public AtomSubsetAtom () {
        super("Count of Atom Subsumes Atom axioms to the TBox size", "AtomSubsetAtom", "simple_atomsubsetatom.sparql");
    }
}
