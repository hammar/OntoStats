package com.karlhammar.ontometrics.plugins.axiomatic;

import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;

import java.io.File;
import java.util.logging.Logger;

public class BoxyRatio implements OntoMetricsPlugin {
    private Logger logger = Logger.getLogger(getClass().getName());
    private StructuralSingletonOWLAPI ss;

    public String getName() {
        return "Boxyness Ratio Plugin";
    }

    /**
     * Initialize the plugin. Required before metrics calculation.
     */
    public void init(File ontologyFile) {
        ss = StructuralSingletonOWLAPI.getSingletonObject(ontologyFile);
    }

    public String getMetricAbbreviation() {
        return "BoxyRatio";
    }

    /**
     */
    public String getMetricValue(File ontologyFile) {
        if (null == ss) {
            logger.info("getMetricValue called before init()!");
            init(ontologyFile);
        }
        OWLOntology ontology = ss.getOntology();
        int total  = ontology.getAxiomCount();

        int tboxes = 0;
        for(AxiomType<?> tbox: AxiomType.TBoxAxiomTypes) {
            tboxes += ontology.getAxiomCount(tbox);
        }
        float tboxCount = (float) tboxes/total;

        int aboxes = 0;
        for(AxiomType<?> abox: AxiomType.ABoxAxiomTypes) {
            aboxes += ontology.getAxiomCount(abox);
        }
        float aboxCount = (float) aboxes/total;

        int rboxes = 0;
        for(AxiomType<?> rbox: AxiomType.RBoxAxiomTypes) {
            rboxes += ontology.getAxiomCount(rbox);
        }
        float rboxCount = (float) rboxes/total;

        return new String(tboxCount + ":" + aboxCount + ":" + rboxCount);
    }
}
