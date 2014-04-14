package org.ontologyengineering.ontometrics.plugins;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.log4j.Logger;

import com.karlhammar.ontometrics.plugins.ParserOWLAPI;

import org.ontologyengineering.ontometrics.plugins.Filter.FilterType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
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
        case ATOM_DISJUNCTION:
            as = as.stream().filter(a -> isDisjAtoms(a.getSubClass())).collect(Collectors.toSet());
            break;
        }

        switch(rhs) {
            // Filter RHS
        case ATOM:
            as = as.stream().filter(a -> isAtomic(a.getSuperClass())).collect(Collectors.toSet());
            break;
        case ATOM_DISJUNCTION:
            as = as.stream().filter(a -> isDisjAtoms(a.getSuperClass())).collect(Collectors.toSet());
            break;
        }

        // Find all equality axioms
        Set<OWLEquivalentClassesAxiom> es = owlapi.getOntology().getAxioms(AxiomType.EQUIVALENT_CLASSES);
        Set<OWLEquivalentClassesAxiom> pairwise = es.stream()
                .flatMap(a -> a.asPairwiseAxioms().stream())
                .collect(Collectors.toSet());

        // We now have pairs of EquivalentClasses, we're guaranteed (by the 
        // nature of our queries) that the lhs or rhs is an Atom.
        //  - Reorder the pairs such that they're in (Atom, x) order.  The
        //  - Filter the pairs such that only (Atom, x) are left where x is of
        //    type lhs or rhs (whichever one is not ATOM).
        //  - Finally count the number of expressions in x and record this as
        //    n `choose` 2 expressions.
        long count = pairwise.stream()
                // For a pair, we'll use the horrible pair[2] notation.
                .map(a -> new ArrayList(a.getClassExpressions()))
                .map(new Function<List<OWLClassExpression>, List<OWLClassExpression>>() {
                    @Override
                    public List<OWLClassExpression> apply(List<OWLClassExpression> ecas) {
                       if(!isAtomic(ecas.get(0))) {
                           Collections.swap(ecas, 0, 1);
                       }
                       return ecas;
                    }
                })
                .filter(a -> filterPair(a.get(0), a.get(1)))
                .mapToLong(a -> (a.get(1).getNestedClassExpressions().size() > 1)?ArithmeticUtils.binomialCoefficient(a.get(1).getNestedClassExpressions().size(), 2): a.get(1).getNestedClassExpressions().size())
                .sum();

        // return size of filtered subsets + (2 * size of filtered equalities)
        return new Double(as.size() + (2.0 * count)).toString();
    }

    private boolean filterPair(OWLClassExpression atom, OWLClassExpression rhsExpr) {
        // We're guaranteed by the calling code that the lhs is an ATOM.  But,
        // let's enforce this here.
        if(!isAtomic(atom)) {
            return false;
        }

        boolean rhsCheck = false;
        switch(rhs) {
        case ATOM:
            rhsCheck = isAtomic(rhsExpr);
            break;
        case ATOM_DISJUNCTION:
            rhsCheck = isDisjAtoms(rhsExpr);
            break;
        }

        return rhsCheck;
    }

    // for us, "atomic" asks whether a class is named or not.
    private boolean isAtomic(OWLClassExpression oce) {
        return !oce.isAnonymous();
    }

    // Is this OWLClassExpression a disjunction of atoms
    private boolean isDisjAtoms(OWLClassExpression oce) {
        // if this is not an Object Union, chuck it.
        if(! (oce instanceof OWLObjectUnionOf)) {
            return false;
        }

        Set<OWLClassExpression> disjuncts = oce.asDisjunctSet();
        System.out.println(disjuncts.size());
        return disjuncts.stream().allMatch(d -> isAtomic(d));
    }
}
