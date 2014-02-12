package org.ontologyengineering.ontometrics.plugins;

import java.util.Arrays;
import java.util.List;

import com.tinkerpop.blueprints.impls.sail.SailGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.PipeFunction;

public class Filter {
    private static final String
            owlns = "http://www.w3.org/2002/07/owl#",
            rdfns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
            rdfsns = "http://www.w3.org/2000/01/rdf-schema#",
            rdfnsType = rdfns + "type",
            rdfnsFirst = rdfns + "first",
            rdfnsRest = rdfns + "rest",
            rdfnsNil = rdfns + "nil",
            rdfsnsSubClassOf = rdfsns + "subClassOf",
            owlnsEquivalentClass = owlns + "equivalentClass",
            owlnsClass = owlns + "Class",
            owlnsThing = owlns + "Thing",
            owlnsComplementOf = owlns + "complementOf",
            owlnsUnionOf = owlns + "unionOf",
            owlnsIntersectionOf = owlns + "intersectionOf",
            owlSomeValuesFrom = owlns + "someValuesFrom",
            owlAllValuesFrom = owlns + "allValuesFrom";

    public enum FilterType {
        ATOM,
        ATOM_CONJUNCTION,
        ATOM_DISJUNCTION,
        ATOM_COMPLEMENT,
        ATOM_ALLOF,
        ATOM_SOMEOF,
        TOP,
        COMPLEX_CONJUNCTION,
        UNRESTRICTED};

    private static List<String> filters = Arrays.asList(owlnsComplementOf, owlnsIntersectionOf, owlnsUnionOf, owlSomeValuesFrom, owlAllValuesFrom);
    private GremlinPipeline pipeline;
    private Vertex owlClassVertex;

    public Filter(SailGraph sg, FilterType type) {
        owlClassVertex = sg.getVertex(owlnsClass);
        switch (type) {
            case ATOM:
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
        return new GremlinPipeline()._().as("ver").out(rdfnsType).retain(Arrays.asList(v)).back("ver");
    }

     GremlinPipeline<Vertex, Vertex> getAtomicPipeline() {
        return new GremlinPipeline<Vertex, Vertex>().or(
                new GremlinPipeline<Vertex, Vertex>().add(getPipelineVerifyIsOfRDFType(owlClassVertex))
                , new GremlinPipeline<Vertex, Vertex>().has("id", owlnsThing)
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
                , new GremlinPipeline().outE(owlnsIntersectionOf).inV().outE(rdfnsFirst).inV().add(getAtomicPipeline())
                , new GremlinPipeline().outE(owlnsIntersectionOf).inV().outE(rdfnsRest).inV().outE(rdfnsFirst).inV().add(getAtomicPipeline())
                , new GremlinPipeline().outE(owlnsIntersectionOf).inV().outE(rdfnsRest).inV().outE(rdfnsRest).inV().has("id", rdfnsNil)
        );
    }

    private GremlinPipeline getDisjunctionPipeline() {
        return new GremlinPipeline().and(
                new GremlinPipeline().add(getPipelineVerifyIsOfRDFType(owlClassVertex))
                , new GremlinPipeline().outE(owlnsUnionOf).inV().outE(rdfnsFirst).inV().add(getAtomicPipeline())
                , new GremlinPipeline().outE(owlnsUnionOf).inV().outE(rdfnsRest).inV().outE(rdfnsFirst).inV().add(getAtomicPipeline())
                , new GremlinPipeline().outE(owlnsUnionOf).inV().outE(rdfnsRest).inV().outE(rdfnsRest).inV().has("id", rdfnsNil)
        );
    }

    private GremlinPipeline getSomeValuesFromPipeline() {
        return new GremlinPipeline().outE(owlSomeValuesFrom).inV().add(getAtomicPipeline());
    }

    private GremlinPipeline getAllValuesFromPipeline() {
        return new GremlinPipeline().outE(owlAllValuesFrom).inV().add(getAtomicPipeline());
    }

    private GremlinPipeline getComplementPipeline() {
        return new GremlinPipeline().outE(owlnsComplementOf).inV().add(getAtomicPipeline());
    }

    private GremlinPipeline getTopPipeline() {
        return new GremlinPipeline().has("id", owlnsThing);
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