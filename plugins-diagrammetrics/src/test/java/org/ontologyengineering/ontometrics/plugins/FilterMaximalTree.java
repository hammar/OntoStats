package org.ontologyengineering.ontometrics.plugins;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
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
 * Computes a maximal tree following the relationships:
 * <ul>
 * <li>owl:intersectionOf,</li>
 * <li>owl:unionOf, and</li>
 * <li>owl:complementOf.</li>
 * </ul>
 * Any intermediate node implied by these relationships are followed.
 */
public class FilterMaximalTree implements PipeFunction<Vertex, java.lang.Boolean> {

    private String                     typeClass = AtomSubsetAtomTest.rdfns + "type";
    // keeping track of vistited nodes ensures we don't follow cycles.
    private Map<Vertex, MaybeBoolean>    visited = new HashMap<Vertex, MaybeBoolean>();
    private List<String>               filterFor; /* = Arrays.asList(Slig.owlns + "complementOf"
                                             , Slig.owlns + "intersectionOf"
                                             , Slig.owlns + "unionOf"
                                             , Slig.owlns + "allValuesFrom"
                                             , Slig.owlns + "someValuesFrom"
                                             //, Slig.owlns + "onProperty" // may need to extend to owl:Restriction with owl:qualifiedCardinality
                                             );*/

    public FilterMaximalTree (List<String> filters) {
        filterFor = filters;
    }

    /**
     * Descend the graph starting at the given Vertex.  Ensure that the graph
     * expands to atomic node at its greatest extent.
     */
    @Override
    public Boolean compute(Vertex v) {
        return verifySubgraph(v);
    }

    /**
     * Return false if this vertex is 
     * @param v
     * @return
     */
    private Boolean verifySubgraph(Vertex v) {
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

            if(filterFor.contains(e.getLabel())) {
                isComplex = verifySubgraph(ev);
            }
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

    private Boolean isAtomic(Vertex v) {
        boolean isTypeClass = false,
                isFiltered  = true;

        // Special case for owl:Thing
        if(v.getId().equals(AtomSubsetAtomTest.owlns + "Thing")) return Boolean.TRUE;

        // Check that this vertex has rdf:type owl:Class
        for(Vertex i: v.getVertices(Direction.OUT, typeClass)) {
            if(i.getId().equals(AtomSubsetAtomTest.owlns + "Class")) isTypeClass = true;
        }

        for(Edge e: v.getEdges(Direction.OUT)) {
            if(filterFor.contains(e.getLabel())) isFiltered = false;
        }

        return new Boolean(isTypeClass & isFiltered);
    }
}
