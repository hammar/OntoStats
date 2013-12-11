package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.ParserConfiguration;
import com.karlhammar.ontometrics.plugins.StructuralSingletonOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

/**
 *
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class DLExpressivity implements OntoMetricsPlugin {
    private Logger logger = Logger.getLogger(getClass().getName());
    private StructuralSingletonOWLAPI sowl;

    @Override
    public String getName() {
        return "The DL expressiveness of the ontology as per OWL API.";
    }
    @Override
    public void init(File ontologyFile) {
        sowl = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile, (new ParserConfiguration()).setImportStrategy(ParserConfiguration.ImportStrategy.IGNORE_IMPORTS));
    }
    @Override
    public String getMetricAbbreviation() {
        return "DLExpressivity";
    }
    @Override
    public String getMetricValue(File ontologyFile) {
        init(ontologyFile);

        org.semanticweb.owlapi.metrics.DLExpressivity de = new org.semanticweb.owlapi.metrics.DLExpressivity(sowl.getOntology().getOWLOntologyManager());
        de.setOntology(sowl.getOntology());
        de.setImportsClosureUsed(false);
        return de.getValue();
    }
}