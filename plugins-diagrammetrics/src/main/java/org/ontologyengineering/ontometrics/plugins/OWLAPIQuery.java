package org.ontologyengineering.ontometrics.plugins;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.karlhammar.ontometrics.plugins.ParserOWLAPI;

import org.ontologyengineering.ontometrics.plugins.Filter.FilterType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class OWLAPIQuery {

    private Logger logger = Logger.getLogger(getClass().getName());
    private ParserOWLAPI owlapi;
    private FilterType lhs, rhs;

    public OWLAPIQuery(ParserOWLAPI owlapi, FilterType lhs, FilterType rhs) {
        this.owlapi = owlapi;
        this.lhs    = lhs;
        this.rhs    = rhs;
    }

    public String runQuery() {
        // Find all subset axioms
        Set<OWLSubClassOfAxiom> as = owlapi.getOntology().getAxioms(AxiomType.SUBCLASS_OF);

        // Filter LHS
        as = as.stream().filter(a -> isAtomic(a.getSubClass())).collect(Collectors.toSet());

        // Filter RHS
        as = as.stream().filter(a -> isAtomic(a.getSuperClass())).collect(Collectors.toSet());

        // Find all equality axioms
        Set<OWLEquivalentClassesAxiom> es = owlapi.getOntology().getAxioms(AxiomType.EQUIVALENT_CLASSES);
        Set<OWLEquivalentClassesAxiom> pairwise = es.stream()
                .flatMap(a -> a.asPairwiseAxioms().stream())
                .collect(Collectors.toSet());

        // Filter based on whether each pair is a pairing of atomic classes
        pairwise = pairwise.stream().filter(a -> areAtomic(a.getNamedClasses())).collect(Collectors.toSet());

        // return size of filtered subsets + (2 * size of filtered equalities)
        return new Double(as.size() + (2.0 * pairwise.size())).toString();
    }

    private boolean areAtomic(Set<OWLClass> cs) {
        return cs.stream().allMatch(c -> isAtomic(c));
    }

    private boolean isAtomic(OWLClassExpression oce) {
        // if the oce is named then we can get it as a class
        if(!oce.isAnonymous()) {
            // return false if anything this is not an OWL_CLASS 
            return oce.asOWLClass().getEquivalentClasses(owlapi.getOntology())
                        .stream()
                        .allMatch(c -> c.getClassExpressionType() == ClassExpressionType.OWL_CLASS);
        }
        return false;
    }
}
