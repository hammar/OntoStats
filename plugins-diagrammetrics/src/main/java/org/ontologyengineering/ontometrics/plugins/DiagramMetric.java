package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.StructuralSingleton;
import com.karlhammar.ontometrics.plugins.StructuralSingletonOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class DiagramMetric extends OntoMetricsPlugin {
    private String name, abbreviation, resource;
    private Logger logger = Logger.getLogger(getClass().getName());
    private SimpleQuery sq = null;

    public DiagramMetric(String name, String abbreviation, String resource) {
        this.name         = name;
        this.abbreviation = abbreviation;
        this.resource     = resource;
    }

    @Override
    public void init(StructuralSingleton jena, StructuralSingletonOWLAPI owlapi) {
        try {
            sq = new SimpleQuery(jena, resource);
        } catch (IOException ioe) {
            logger.severe("Cannot load SPARQL resource.");
            logger.severe(ioe.toString());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMetricAbbreviation() {
        return abbreviation;
    }

    @Override
    public String getMetricValue(File ontologyFile) {
        if(null == sq) {
            logger.severe("getMetricValue() called before init!");
        }
        return sq.calculatePrettyDiagramRatio();
    }
}