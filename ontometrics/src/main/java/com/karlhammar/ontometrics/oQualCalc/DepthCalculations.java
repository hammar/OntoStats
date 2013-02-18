package com.karlhammar.ontometrics.oQualCalc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//import org.semanticweb.owlapi.model.OWLClass;
//import org.semanticweb.owlapi.model.OWLClassExpression;
//import org.semanticweb.owlapi.model.OWLOntology;

public class DepthCalculations { 
	
	/*
	 * Holds the set of path heights/depths within the ontology, counting the 
	 * longest path from each node to a root node.
	 */
	List<Integer> heights;
	
	// The ontology to calculate metrics for.
	//OWLOntology ontology;
	
	/*
	 * Constructor, initializes the required parameters and kicks off the 
	 * calculations (since it makes little sense to instantiate this class 
	 * if not wanting to perform the calculations).
	 *//*
	public DepthCalculations(OWLOntology o)
	{
		heights = new ArrayList<Integer>();
		ontology = o;
		calculateHeights();
	}*/
	
	/* 
	 * This method performs all of the common height calculations that fills
	 * {@link heights} with values.
	 */
	
	private void calculateHeights()
	{
		/*
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
	    	heights.add(calculateTallestHeightFromNode(ontology,leafNode));
	    }*/
	}
	
	/*
	 * A recursive method that returns the longest possible superclass path from
	 * a given node to a top-level node (one that has no superclasses)
	 * 
	 * @param  o 	The ontology that is walked through.
	 * @param  c	The node that this recursion calculates.
	 * @return 		The cardinality of the longest found path.
	 *//*
	private int calculateTallestHeightFromNode(OWLOntology o, OWLClass c)
	{
		Set<OWLClassExpression> superClasses = c.getSuperClasses(o);
		
		// If there are no superclasses, stop recursing.
		if (superClasses.size() <= 0)
		{
			return 0;
		}
		else
		{
			List<Integer> parentHeights = new ArrayList<Integer>();
			Iterator<OWLClassExpression> iter = superClasses.iterator();
		    while (iter.hasNext()) {
		    	OWLClassExpression i = iter.next();
		    	
		    	// We don't want anonymous classes, only explicitly stated 
		    	// superclass relations
		    	if (!i.isAnonymous())
		    	{
		    		OWLClass superClass = i.asOWLClass();
		    		parentHeights.add(calculateTallestHeightFromNode(o,superClass));
		    	}
		    }
		    
		    // Get and return only the longest path
		    Collections.sort(parentHeights, Collections.reverseOrder());
		    int longestParentHeight = parentHeights.get(parentHeights.size()-1);
		    return (longestParentHeight + 1);
		}
	}
	
	// Summarize all of the heights into a total absolute height
	public Integer getAbsoluteDepth()
	{
	    int height = 0;
	    Iterator<Integer> iter = heights.iterator();
	    while (iter.hasNext())
	    {
	    	int heightOfCurrentIteration = iter.next();
	    	height += heightOfCurrentIteration;
	    }
		return height;
	}
	
	// Get the average height (i.e. absolute height divided by number of paths)
	public float getAverageDepth()
	{
		return getAbsoluteDepth().floatValue()/heights.size();
	}
	
	// Get the max depth (i.e. the tallest depth of any path)
	public int getMaxDepth()
	{
	    // Reverse-sort the results, so that the deepest path is listed first. 
	    Collections.sort(heights,Collections.reverseOrder());
		return heights.get(0);
	}*/
	
}
