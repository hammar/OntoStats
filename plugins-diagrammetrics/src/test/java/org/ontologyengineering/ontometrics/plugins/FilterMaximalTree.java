package org.ontologyengineering.ontometrics.plugins;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.ontologyengineering.ontometrics.plugins.MaybeBoolean.Option;

/**
 * 
 * @author Aidan Delaney <aidan@phoric.eu>
 *
 * Given an {@see Filter} this class will verify whether the tree rooted at the
 * vertex passes the filter.
 * 
 * A neater way of doing this might be to turn the RDF graph into an AST and use
 * a standard AST parser.  For the moment, this "works".
 */
public class FilterMaximalTree implements PipeFunction<Vertex, java.lang.Boolean> {

    private String                     typeClass = TestUtils.rdfns + "type";
    // keeping track of vistited nodes ensures we don't follow cycles.  Only used by isUnrestricted()
    private Map<Vertex, MaybeBoolean>    visited = new HashMap<Vertex, MaybeBoolean>();
    private Filter                        filter; 
    private final List<String>      atomicFilter = Arrays.asList(TestUtils.owlns + "complementOf"
                                             , TestUtils.owlns + "intersectionOf"
                                             , TestUtils.owlns + "unionOf"
                                             , TestUtils.owlns + "allValuesFrom"
                                             , TestUtils.owlns + "someValuesFrom"
                                             );

    public FilterMaximalTree (Filter filter) {
        this.filter = filter;
    }

    /**
     * Descend the graph starting at the given Vertex.  Ensure that the graph
     * expands to atomic node at its greatest extent.
     */
    @Override
    public Boolean compute(Vertex v) {
        return isUnrestricted(v);
    }

    /**
     * Return false if this vertex is 
     * @param v
     * @return
     */
    private Boolean isUnrestricted(Vertex v) {
        MaybeBoolean o = visited.get(v);
        if(null != o && o.isJust()) {
            // we've visited this and have results.
            return (Option.JUST_TRUE == o.get());
        }

        // if this vertex "atomic" and a Class, return true.
        if(isAtomic(v) || isComplex(v)) {
            return Boolean.TRUE;
        };

        return Boolean.FALSE;
    }

    private Boolean isConjunction(Vertex v) {
        // intersectionOf
        
        return false;
    }

    private Boolean isDisjunction(Vertex v) {
        return false;
    }

    private Boolean isComplex(Vertex v) {
        MaybeBoolean o = visited.get(v);
        if(null != o && o.isMaybe()) {
            // we've visited this, but have no results
            // don't want to cycle around, so return TRUE.
            return Boolean.TRUE;
        }
        visited.put(v,  new MaybeBoolean(Option.MAYBE));

        // otherwise, follow outlinks labelled :subClassOf, :unionOf,
        // :intersectionOf, :allValuesFrom, :someValuesFrom
        // these are not-necessarily mutually exclusive.
        // e is an edge from v to ev ie: v - e -> ev
        Boolean isComplex = Boolean.FALSE;
        for(Edge e: v.getEdges(Direction.OUT)) {
            Vertex ev = e.getVertex(Direction.OUT);

            //if(filter.getFilters().contains(e.getLabel())) {
            //    isComplex = isUnrestricted(ev);
            //}
        }

        if(!isComplex) {
            // finally, if we're not one of the above cases, print some debug info
            System.err.println(v.toString() + " is not cool!");
            for(Edge e: v.getEdges(Direction.OUT)) {
                System.err.println("\t" + e.toString());
            }
        }

        visited.put(v, new MaybeBoolean(isComplex));
        return isComplex;
    }

    private Boolean isTop(Vertex v) {
        if(v.getId().equals(TestUtils.owlns + "Thing")) return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean isBot(Vertex v) {
        if(v.getId().equals(TestUtils.rdfns + "nil")) return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean isAtomic(Vertex v) {
        boolean isTypeClass = false,
                isFiltered  = true;

        // Check that this vertex has rdf:type owl:Class
        for(Vertex i: v.getVertices(Direction.OUT, typeClass)) {
            if(i.getId().equals(TestUtils.owlns + "Class")) isTypeClass = true;
        }

        for(Edge e: v.getEdges(Direction.OUT)) {
            if(atomicFilter.contains(e.getLabel())) isFiltered = false;
        }

        return new Boolean(isTypeClass & isFiltered);
    }

    private Boolean runPipeline(GremlinPipeline pipeline, Vertex vertex) {
        return Boolean.TRUE;
    }
}
