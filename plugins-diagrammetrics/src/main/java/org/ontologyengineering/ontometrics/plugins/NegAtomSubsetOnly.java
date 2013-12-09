package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class NegAtomSubsetOnly implements OntoMetricsPlugin {
    private Logger logger = Logger.getLogger(getClass().getName());
    private SimpleQuery sq;

    public String getName() {
        return "Count of (Neg Atom) Subsumes (Only) axioms to the TBox size";
    }

    public void init(File ontologyFile) {
        try {
            sq = new SimpleQuery(ontologyFile, "simple_negatomsubsetonly.sparql");
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public String getMetricAbbreviation() {
        return "NegAtomSubsetOnly";
    }

    public String getMetricValue(File ontologyFile) {
        return sq.calculatePrettyDiagramRatio();
    }
}
