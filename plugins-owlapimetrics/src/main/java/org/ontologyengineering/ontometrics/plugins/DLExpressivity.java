package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.ParserConfiguration;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

/**
 *
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class DLExpressivity extends OntoMetricsPlugin {
    private Logger logger = Logger.getLogger(getClass().getName());
    private ParserOWLAPI sowl;

    @Override
    public String getName() {
        return "The DL expressiveness of the ontology as per OWL API.";
    }
    @Override
    public void init(ParserJena jena, ParserOWLAPI owlapi) {
        sowl = owlapi;
    }
    @Override
    public String getMetricAbbreviation() {
        return "DLExpressivity";
    }
    @Override
    public String getMetricValue(File ontologyFile) {
        if(null == sowl) {
            logger.severe("getMetricValue called before init!");
        }

        org.semanticweb.owlapi.metrics.DLExpressivity de = new org.semanticweb.owlapi.metrics.DLExpressivity(sowl.getOntology().getOWLOntologyManager());
        de.setOntology(sowl.getOntology());
        de.setImportsClosureUsed(false);
        return de.getValue();
    }
}