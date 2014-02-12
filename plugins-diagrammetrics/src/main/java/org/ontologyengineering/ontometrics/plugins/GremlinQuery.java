package org.ontologyengineering.ontometrics.plugins;

import com.karlhammar.ontometrics.plugins.LazyParserGremlin;
import com.tinkerpop.blueprints.impls.sail.SailGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import org.apache.log4j.Logger;

import org.ontologyengineering.ontometrics.plugins.Filter.FilterType;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
public class GremlinQuery {
    private static final String
            owlns = "http://www.w3.org/2002/07/owl#",
            rdfns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
            rdfsns = "http://www.w3.org/2000/01/rdf-schema#",
            rdfsnsSubClassOf = rdfsns + "subClassOf",
            owlnsEquivalentClass = owlns + "equivalentClass";

    private Logger logger = Logger.getLogger(getClass().getName());
    private LazyParserGremlin gremlin;
    private FilterType lhs, rhs;

    /* package */ GremlinQuery(LazyParserGremlin gremlin, FilterType lhs, FilterType rhs) {
        this.gremlin = gremlin;
        this.lhs     = lhs;
        this.rhs     = rhs;
    }

    public String runQuery() {
        SailGraph       sg = gremlin.getOntology();
        GremlinPipeline gp = new GremlinPipeline(sg.getEdges()).has("label", rdfsnsSubClassOf).and(
                new GremlinPipeline().outV().add((new Filter(sg, lhs)).getPipeline())
                , new GremlinPipeline().inV().add((new Filter(sg, rhs)).getPipeline())
        ).dedup();

        GremlinPipeline eq = new GremlinPipeline(sg.getEdges()).has("label", owlnsEquivalentClass).and(
                new GremlinPipeline().outV().add((new Filter(sg, lhs)).getPipeline())
                , new GremlinPipeline().inV().add((new Filter(sg, rhs)).getPipeline())
        ).dedup();

        // Each A \equiv B counts as A \sqsubset B and B \sqsubseeq A, so double the total of equivs found.
        return Double.toString(gp.count() + (eq.count() * 2));
    }
}
