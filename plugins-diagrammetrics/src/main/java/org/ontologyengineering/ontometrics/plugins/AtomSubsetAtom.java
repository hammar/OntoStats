package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

/**
 *
 * @author Aidan Delaney <aidan@ontologyengineering.org>
 *
 * If we have axioms of the form
 * <pre><code>
 * D  := \top |
 *       \bot |
 *       A (A atomic) |
 *       C \sqcap D |
 *       C \sqcup D |
 *       \neg C |
 *       \exists R. C |
 *       \forall R. C
 * </code></pre>
 * then there is a "pretty" mechanism to convert them into a Concept Diagram.
 * This metric calculates the amount of the ontology that can be represented as
 * Concept Diagrams using the "pretty" mechanism.
 */
public class AtomSubsetAtom implements OntoMetricsPlugin {

    private Logger logger = Logger.getLogger(getClass().getName());
    private SimpleQuery sq;

    public String getName() {
        return "Ratio of Atom Subsumes Atom axioms to the TBox size";
    }

    public void init(File ontologyFile) {
        try {
            sq = new SimpleQuery(ontologyFile, "simple_atomsubsetatom.sparql");
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public String getMetricAbbreviation() {
        return "AtomSubsetAtom";
    }

    public String getMetricValue(File ontologyFile) {
        return sq.calculatePrettyDiagramRatio();
    }
}
