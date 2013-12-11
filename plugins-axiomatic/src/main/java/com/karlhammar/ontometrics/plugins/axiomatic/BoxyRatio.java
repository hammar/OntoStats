package com.karlhammar.ontometrics.plugins.axiomatic;

import com.karlhammar.ontometrics.plugins.*;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;

import java.io.File;
import java.util.logging.Logger;

/**
 *
 * @author Aidan Delaney <aidan@phoric.eu>
 *
 * The Boxyness Ratio calculates the ratio of TBox:ABox:RBox axioms in an
 * ontology.  The sum of the ratio components often does not equal 1 as there
 * are axioms that are neither TBox, ABox nor RBox.  An example of such an axiom
 * is OWL API's AxiomType.SWRL_RULE or, more informally, other examples are
 * those axioms that relate to annotations.
 */
public class BoxyRatio extends OntoMetricsPlugin {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public String getName() {
        return "Boxyness Ratio Plugin";
    }

    @Override
    public String getMetricAbbreviation() {
        return "BoxyRatio";
    }

    /**
     * The ratio of TBoxAxioms:ABoxAxioms:RBoxAxioms.  In the ratio we divide
     * the number of TBoxAxioms by the total number of axioms in the ontology.
     * Similarly, ABoxAxioms and RBoxAxioms are divided by the total number of
     * axioms in the ontology.
     *
     * @return The ratio of TBoxAxioms:ABoxAxioms:RBoxAxioms with respect to the
     * size of the ontology.
     */
    @Override
    public String getMetricValue(File ontologyFile) {
        if (null == owlapi) {
            logger.severe("getMetricValue called before init()!");
        }
        OWLOntology ontology = owlapi.getOntology();
        int total  = ontology.getAxiomCount();

        int tboxes = 0;
        for(AxiomType<?> tbox: AxiomType.TBoxAxiomTypes) {
            tboxes += ontology.getAxiomCount(tbox);
        }

        int aboxes = 0;
        for(AxiomType<?> abox: AxiomType.ABoxAxiomTypes) {
            aboxes += ontology.getAxiomCount(abox);
        }

        int rboxes = 0;
        for(AxiomType<?> rbox: AxiomType.RBoxAxiomTypes) {
            rboxes += ontology.getAxiomCount(rbox);
        }

        float tboxCount   = (float) tboxes/total;
        float aboxCount   = (float) aboxes/total;
        float rboxCount   = (float) rboxes/total;

        return new String(tboxCount + ":" + aboxCount + ":" + rboxCount);
    }
}
