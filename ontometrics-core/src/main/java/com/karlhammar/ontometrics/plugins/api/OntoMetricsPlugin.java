package com.karlhammar.ontometrics.plugins.api;

import java.io.File;
import com.karlhammar.ontometrics.plugins.*;

public abstract class OntoMetricsPlugin {
    protected StructuralSingleton       jena;
    protected StructuralSingletonOWLAPI owlapi;

	abstract public String getName();
	public void init(StructuralSingleton jena, StructuralSingletonOWLAPI owlapi) {
	    this.jena   = jena;
	    this.owlapi = owlapi;
	}
	abstract public String getMetricAbbreviation();
	abstract public String getMetricValue(File ontologyFile);
}
