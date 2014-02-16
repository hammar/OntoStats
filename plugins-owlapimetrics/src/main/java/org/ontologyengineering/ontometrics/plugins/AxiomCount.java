package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.google.common.base.Optional;

import com.karlhammar.ontometrics.plugins.ParserConfiguration;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

/**
 *
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class AxiomCount extends OntoMetricsPlugin {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public String getName() {
        return "The count of axioms in the ontology as per OWL API.";
    }

    @Override
    public String getMetricAbbreviation() {
        return "AxiomCount";
    }

    @Override
    public Optional<String> getMetricValue(File ontologyFile) {
        if(null == owlapi) {
            logger.severe("getMetricValue called before init!");
        }

        org.semanticweb.owlapi.metrics.AxiomCount ac = new org.semanticweb.owlapi.metrics.AxiomCount(owlapi.getOntology().getOWLOntologyManager());
        ac.setOntology(owlapi.getOntology());
        ac.setImportsClosureUsed(false);
        return Optional.of(Integer.toString(ac.getValue()));
    }
}