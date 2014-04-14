package org.ontologyengineering.ontometrics.plugins;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.karlhammar.ontometrics.plugins.ParserOWLAPI;

import org.ontologyengineering.ontometrics.plugins.Filter.FilterType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
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

        switch(lhs) {
            // Filter LHS
        case ATOM:
            as = as.stream().filter(a -> isAtomic(a.getSubClass())).collect(Collectors.toSet());
            break;
        }

        switch(rhs) {
            // Filter RHS
        case ATOM:
            as = as.stream().filter(a -> isAtomic(a.getSuperClass())).collect(Collectors.toSet());
            break;
        }

        // Find all equality axioms
        Set<OWLEquivalentClassesAxiom> es = owlapi.getOntology().getAxioms(AxiomType.EQUIVALENT_CLASSES);
        Set<OWLEquivalentClassesAxiom> pairwise = es.stream()
                .flatMap(a -> a.asPairwiseAxioms().stream())
                .collect(Collectors.toSet());

        
        // Filter based on whether each pair is a pairing of atomic classes
        pairwise = pairwise.stream().filter(a -> filterPair(a.getClassExpressions())).collect(Collectors.toSet());

        // return size of filtered subsets + (2 * size of filtered equalities)
        return new Double(as.size() + (2.0 * pairwise.size())).toString();
    }

    private boolean filterPair(Set<OWLClassExpression> ocs) {
        // One of these must match lhs, the other rhs
        // TODO: make this code less fugly

        // we can't assume anything about the order of the class expressions, 
        // but the procedural order of the code checks an element for lhs match
        // first.
        OWLClassExpression lhsExpr = null;
        boolean lhsMatched = false, rhsMatched = false;
        for(OWLClassExpression ce: ocs) {
            if(!lhsMatched) {
                switch(lhs) {
                case ATOM:
                    if(isAtomic(ce)) {
                        lhsExpr=ce;
                        lhsMatched = true;
                    }
                    break;
                }
            }

            switch(rhs) {
            case ATOM:
                if(isAtomic(ce) && ce != lhsExpr) {
                    rhsMatched = true;
                }
                break;
            }
        }

        return lhsMatched && rhsMatched;
    }

    // for us, "atomic" asks whether a class is named or not.
    private boolean isAtomic(OWLClassExpression classExpression) {
        return !classExpression.isAnonymous();
    }
}
