package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.StructuralSingletonOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

/**
 *
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class AxiomCount implements OntoMetricsPlugin {
    private Logger logger = Logger.getLogger(getClass().getName());
    private StructuralSingletonOWLAPI sowl;

    @Override
    public String getName() {
        return "The count of axioms in the ontology as per OWL API.";
    }

    @Override
    public void init(File ontologyFile) {
        sowl = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile, true);
    }

    @Override
    public String getMetricAbbreviation() {
        return "AxiomCount";
    }

    @Override
    public String getMetricValue(File ontologyFile) {
        init(ontologyFile);

        org.semanticweb.owlapi.metrics.AxiomCount ac = new org.semanticweb.owlapi.metrics.AxiomCount(sowl.getOntology().getOWLOntologyManager());
        ac.setOntology(sowl.getOntology());
        ac.setImportsClosureUsed(false);
        return Integer.toString(ac.getValue());
    }
}