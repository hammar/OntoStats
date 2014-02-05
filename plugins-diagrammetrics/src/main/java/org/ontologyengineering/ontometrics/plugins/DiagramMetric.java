package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.LazyParserGremlin;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

/**
 * 
 * @author Aidan Delaney <aidan@phoric.eu>
 *
 */
public class DiagramMetric extends OntoMetricsPlugin {
    private String name, abbreviation, resource;
    private Logger logger = Logger.getLogger(getClass().getName());
    private SparqlQuery sq = null;

    public DiagramMetric(String name, String abbreviation, String resource) {
        this.name         = name;
        this.abbreviation = abbreviation;
        this.resource     = resource;
    }

    @Override
    public void init(ParserJena jena, ParserOWLAPI owlapi, LazyParserGremlin gremlin) {
        super.init(jena, owlapi, gremlin);
        try {
            sq = new SparqlQuery(jena, resource);
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