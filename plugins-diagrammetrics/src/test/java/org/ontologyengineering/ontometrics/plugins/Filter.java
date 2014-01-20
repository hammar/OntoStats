package org.ontologyengineering.ontometrics.plugins;

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
        ATOM_CONJUNCTION,
        ATOM_DISJUNCTION,
        ATOM_COMPLEMENT,
        ATOM_ALLOF,
        ATOM_SOMEOF,
        TOP,
        COMPLEX_CONJUNCTION,
        UNRESTRICTED};

    private static List<String> filters = Arrays.asList(TestUtils.owlnsComplementOf, TestUtils.owlnsIntersectionOf, TestUtils.owlnsUnionOf, TestUtils.owlSomeValuesFrom, TestUtils.owlAllValuesFrom);
    private GremlinPipeline pipeline;
    private Vertex owlClassVertex;

    public Filter(SailGraph sg, FilterType type) {
        owlClassVertex = sg.getVertex(TestUtils.owlnsClass);
        switch (type) {
            case ATOM_ONLY:
                this.pipeline = getAtomicPipeline();
                break;
            case ATOM_CONJUNCTION:
                this.pipeline = getConjunctionPipeline();
                break;
            case ATOM_DISJUNCTION:
                this.pipeline = getDisjunctionPipeline();
                break;
            case ATOM_SOMEOF:
                this.pipeline = getSomeValuesFromPipeline();
                break;
            case ATOM_ALLOF:
                this.pipeline = getAllValuesFromPipeline();
                break;
            case ATOM_COMPLEMENT:
                this.pipeline = getComplementPipeline();
                break;
            case TOP:
                this.pipeline = getTopPipeline();
                break;
            case COMPLEX_CONJUNCTION:
                this.pipeline = getComplexPipeline(getConjunctionPipeline());
        }
    }

    private static GremlinPipeline<Vertex, Vertex> getPipelineVerifyIsOfRDFType(Vertex v) {
        return new GremlinPipeline()._().as("ver").out(TestUtils.rdfnsType).retain(Arrays.asList(v)).back("ver");
    }

     GremlinPipeline<Vertex, Vertex> getAtomicPipeline() {
        return new GremlinPipeline<Vertex, Vertex>().or(
                new GremlinPipeline<Vertex, Vertex>().add(getPipelineVerifyIsOfRDFType(owlClassVertex))
                , new GremlinPipeline<Vertex, Vertex>().has("id", TestUtils.owlnsThing)
                /*, new GremlinPipeline<Vertex, Vertex>().filter(
                    new PipeFunction<Vertex, Boolean>() {
                        @Override
                        public Boolean compute(Vertex v) {
                            boolean containsBannedEdge = false;
                            for(Edge e: v.getEdges(Direction.OUT)) {
                                if(filters.contains(e.getLabel())) containsBannedEdge = true;
                            }
                            return !containsBannedEdge;
                        }
                    }
                )*/
        );
    }

    private GremlinPipeline getConjunctionPipeline() {
        return new GremlinPipeline().and(
                new GremlinPipeline().add(getPipelineVerifyIsOfRDFType(owlClassVertex))
                , new GremlinPipeline().outE(TestUtils.owlnsIntersectionOf).inV().outE(TestUtils.rdfnsFirst).inV().add(getAtomicPipeline())
                , new GremlinPipeline().outE(TestUtils.owlnsIntersectionOf).inV().outE(TestUtils.rdfnsRest).inV().outE(TestUtils.rdfnsFirst).inV().add(getAtomicPipeline())
                , new GremlinPipeline().outE(TestUtils.owlnsIntersectionOf).inV().outE(TestUtils.rdfnsRest).inV().outE(TestUtils.rdfnsRest).inV().has("id", TestUtils.rdfnsNil)
        );
    }

    private GremlinPipeline getDisjunctionPipeline() {
        return new GremlinPipeline().and(
                new GremlinPipeline().add(getPipelineVerifyIsOfRDFType(owlClassVertex))
                , new GremlinPipeline().outE(TestUtils.owlnsUnionOf).inV().outE(TestUtils.rdfnsFirst).inV().add(getAtomicPipeline())
                , new GremlinPipeline().outE(TestUtils.owlnsUnionOf).inV().outE(TestUtils.rdfnsRest).inV().outE(TestUtils.rdfnsFirst).inV().add(getAtomicPipeline())
                , new GremlinPipeline().outE(TestUtils.owlnsUnionOf).inV().outE(TestUtils.rdfnsRest).inV().outE(TestUtils.rdfnsRest).inV().has("id", TestUtils.rdfnsNil)
        );
    }

    private GremlinPipeline getSomeValuesFromPipeline() {
        return new GremlinPipeline().outE(TestUtils.owlSomeValuesFrom).inV().add(getAtomicPipeline());
    }

    private GremlinPipeline getAllValuesFromPipeline() {
        return new GremlinPipeline().outE(TestUtils.owlAllValuesFrom).inV().add(getAtomicPipeline());
    }

    private GremlinPipeline getComplementPipeline() {
        return new GremlinPipeline().outE(TestUtils.owlnsComplementOf).add(getAtomicPipeline());
    }

    private GremlinPipeline getTopPipeline() {
        return new GremlinPipeline().has("id", TestUtils.owlnsThing);
    }

    private GremlinPipeline getComplexPipeline(GremlinPipeline baseCase) {
        return baseCase.add(getAtomicLeavesPipeline());
    }

    private GremlinPipeline getAtomicLeavesPipeline() {
        return new GremlinPipeline().or(
                getTopPipeline()
                , getComplementPipeline()
                , getAllValuesFromPipeline()
                , getSomeValuesFromPipeline()
                , getDisjunctionPipeline()
                , getConjunctionPipeline()
        ).as("recurse").loop("recurse", new PipeFunction<Vertex, Boolean>() {
                    @Override
                    public Boolean compute(Vertex v) {
                        return true;
                    }
                }
        );
    }
    public GremlinPipeline getPipeline() {
        return pipeline;
    }
};