package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class AtomSubsetConj implements OntoMetricsPlugin{

    private Logger logger = Logger.getLogger(getClass().getName());
    private SimpleQuery sq;

    public String getName() {
        return "Count of Atom Subsumes (Atom Conj Atom) axioms to the TBox size";
    }

    public void init(File ontologyFile) {
        // We need both OWL API and Jena for this trick.
        try {
            sq = new SimpleQuery(ontologyFile, "simple_atomsubsetconj.sparql");
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public String getMetricAbbreviation() {
        return "AtomSubsetConj";
    }

    public String getMetricValue(File ontologyFile) {
        return sq.calculatePrettyDiagramRatio();
    }
}
