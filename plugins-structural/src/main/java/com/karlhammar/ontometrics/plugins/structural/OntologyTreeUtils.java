package com.karlhammar.ontometrics.plugins.structural;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;

public class OntologyTreeUtils {
    List<Integer> heights;
    
    public List<Integer> getHeights() {
        return this.heights;
    }
    
    protected void calculateHeights(OWLOntology ontology)
    {
        this.heights = new ArrayList<Integer>();
        // First, find all leaf nodes
        Set<OWLClass> leaves = new HashSet<OWLClass>();
        Set<OWLClass> allClasses = ontology.getClassesInSignature();
        Iterator<OWLClass> iter = allClasses.iterator();
        while (iter.hasNext()) {
            OWLClass i = iter.next();
            if (i.getSubClasses(ontology).size() <= 0)
                leaves.add(i);
        }
        
        // Find longest path for each node to a top level class
        iter = leaves.iterator();
        while (iter.hasNext())
        {
            OWLClass leafNode = iter.next();
            // Ignore OWL thing if it for some reason shows up w/o children
            if (!leafNode.isOWLThing()) {
                heights.add(calculateTallestHeightFromNode(ontology,leafNode));
            }
        }
    }
    
    /*
     * A recursive method that returns the longest possible superclass path from
     * a given node to a top-level node (one that has no superclasses)
     * 
     * @param  o    The ontology that is walked through.
     * @param  c    The node that this recursion calculates.
     * @return      The cardinality of the longest found path.
     */
    private int calculateTallestHeightFromNode(OWLOntology o, OWLClass c)
    {
        
        Set<OWLClassExpression> superClasses = c.getSuperClasses(o);
        Set<OWLClass> namedSuperClasses = new HashSet<OWLClass>();
        
        // Get only named classes, i.e. non-anonymous
        for (OWLClassExpression oce: superClasses) {
            if (!oce.isAnonymous()) {
                namedSuperClasses.add(oce.asOWLClass());
            }
        }
        
        // If there are no superclasses, stop recursing.
        if (namedSuperClasses.size() <= 0)
        {
            if (c.isOWLThing()) {
                // Top has been reached
                return 0;
            }
            else {
                // Class has no asserted superclass and is not itself owl:Thing so we must 
                // infer that it is a direct child of owl:Thing 
                return 1;
            }
        }
        else
        {
            List<Integer> parentHeights = new ArrayList<Integer>();
            for(OWLClass namedParentClass: namedSuperClasses) {
                // Recurse one step up hierarchy
                parentHeights.add(calculateTallestHeightFromNode(o,namedParentClass));
            }
            
            // Get and return only the longest path
            Collections.sort(parentHeights, Collections.reverseOrder());
            int longestParentHeight = parentHeights.get(parentHeights.size()-1);
            return (longestParentHeight + 1);
        }
    }
}
