package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

public class SomeSubsetOnly extends DiagramMetric {

    public SomeSubsetOnly () {
        super("Count of Some Subsumes Only axioms in the ontology", "SomeSubsetOnly", "simple_somesubsetonly.sparql");
    }
}
