package org.ontologyengineering.ontometrics.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.sail.SailGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.PipeFunction;

public class Filter {
    public enum FilterType {
    ATOM_ONLY,
    CONJUNCTION,
    DISJUNCTION,
    COMPLEMENT,
    ALLOF,
    SOMEOF,
    UNRESTRICTED};

    private static List<String> filters = Arrays.asList(TestUtils.owlnsComplementOf, TestUtils.owlnsIntersectionOf, TestUtils.owlnsUnionOf, TestUtils.owlSomeValuesFrom, TestUtils.owlAllValuesFrom);
    public static GremlinPipeline<Vertex, Vertex> getPipelineToVertex(Vertex v) {
        return new GremlinPipeline()._().as("ver").out(TestUtils.rdfnsType).retain(Arrays.asList(v)).back("ver");
    }

    public static GremlinPipeline getAtomicPipeline(Vertex owlClassVertex) {
        return new GremlinPipeline(). and(
                new GremlinPipeline().add(getPipelineToVertex(owlClassVertex))
                , new GremlinPipeline().filter(
                new PipeFunction<Vertex, Boolean>() {
                    @Override
                    public Boolean compute(Vertex v) {
                        boolean containsBannedEdge = false;
                        for(Edge e: v.getEdges(Direction.OUT)) {
                            if(filters.contains(e.getLabel())) containsBannedEdge = true;
                        }
                        return new Boolean(!containsBannedEdge);
                    }
                }
        )
        );
    }

    private GremlinPipeline pipeline;

    public Filter(SailGraph sg, FilterType type) {
        Vertex classVertex = sg.getVertex(TestUtils.owlnsClass);
        switch (type) {
            case ATOM_ONLY:
                this.pipeline = getAtomicPipeline(classVertex);
                break;
            case CONJUNCTION:
                //this.pipeline = getConjunctionPipeline(classVertex);
                break;
        }
    }

    public GremlinPipeline getPipeline() {
        return pipeline;
    }
};