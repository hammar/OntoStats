package com.karlhammar.ontometrics.plugins.api;

import java.io.File;
import java.util.Optional;
import com.karlhammar.ontometrics.plugins.*;

public abstract class OntoMetricsPlugin {
    protected ParserJena        jena;
    protected ParserOWLAPI      owlapi;
    protected LazyParserGremlin gremlin;

	abstract public String getName();
	public void init(ParserJena jena, ParserOWLAPI owlapi, LazyParserGremlin gremlin) {
	    this.jena    = jena;
	    this.owlapi  = owlapi;
	    this.gremlin = gremlin;
	}
	abstract public String getMetricAbbreviation();
	abstract public Optional<String> getMetricValue(File ontologyFile);
}
