package com.karlhammar.ontometrics.oQualCalc;

import java.io.File;

/*import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import se.hj.jth.karlhammar.qfc.calculators.DepthCalculations;*/

public class QFCalculator {

	public static void main(String [ ] args) {
		if (args.length != 1 || !(new File(args[0])).isFile())
		{
			System.out.println("Please pass along the path to an OWL file.");
		}
		else
		{/*
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			try 
			{
				OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(args[0]));
				DepthCalculations dc = new DepthCalculations(ontology);
				System.out.println("Absolute depth:\t" + dc.getAbsoluteDepth());
				System.out.println("Average depth:\t" + dc.getAverageDepth());
				System.out.println("Max depth:\t" + dc.getMaxDepth());
			} 
			catch (OWLOntologyCreationException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
}
