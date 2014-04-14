package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.Optional;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

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
        return "The count of logical axioms in the ontology as per OWL API.";
    }

    @Override
    public String getMetricAbbreviation() {
        return "LogicalAxiomCount";
    }

    @Override
    public Optional<String> getMetricValue(File ontologyFile) {
        if(null == owlapi) {
            logger.severe("getMetricValue called before init!");
        }

        return Optional.of(Integer.toString(owlapi.getOntology().getLogicalAxiomCount()));
    }
}