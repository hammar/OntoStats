package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class ConjSubsetNegAtom implements OntoMetricsPlugin {
    private Logger logger = Logger.getLogger(getClass().getName());
    private SimpleQuery sq;

    public String getName() {
        return "Ratio of (Atom Conj Atom) Subsumes (Neg Atom) axioms to the TBox size";
    }

    public void init(File ontologyFile) {
        try {
            sq = new SimpleQuery(ontologyFile, "simple_conjsubsetnegatom.sparql");
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public String getMetricAbbreviation() {
        return "ConjSubsetNegAtom";
    }

    public String getMetricValue(File ontologyFile) {
        return sq.calculatePrettyDiagramRatio();
    }
}
