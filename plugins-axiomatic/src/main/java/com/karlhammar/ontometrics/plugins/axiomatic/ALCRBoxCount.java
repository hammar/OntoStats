package com.karlhammar.ontometrics.plugins.axiomatic;

import java.io.File;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class  ALCRBoxCount extends OntoMetricsPlugin {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public String getName() {
        return "The size of the subset of the RBox of this ontology that is in ALC.";
    }

    @Override
    public String getMetricAbbreviation() {
        return "ALCRBoxAxiomCount";
    }

    @Override
    public Optional<String> getMetricValue(File ontologyFile) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = null;

        try {
            ontology = manager.createOntology();
        } catch (OWLOntologyCreationException e) {
            logger.severe("Failed to create ontology");
            e.printStackTrace();
        }
        manager.addAxioms(ontology, owlapi.getOntology().getRBoxAxioms(false));
        return Optional.of(Integer.toString(ALCExpressivityChecker.getALCAxiomCount(ontology)));
    }

}
