package com.karlhammar.ontometrics.plugins.api;

import java.io.File;
import com.karlhammar.ontometrics.plugins.*;

public abstract class OntoMetricsPlugin {
    protected ParserJena       jena;
    protected ParserOWLAPI owlapi;

	abstract public String getName();
	public void init(ParserJena jena, ParserOWLAPI owlapi) {
	    this.jena   = jena;
	    this.owlapi = owlapi;
	}
	abstract public String getMetricAbbreviation();
	abstract public String getMetricValue(File ontologyFile);
}
