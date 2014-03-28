package org.ontologyengineering.ontometrics.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.Optional;

import com.karlhammar.ontometrics.plugins.LazyParserGremlin;
import com.karlhammar.ontometrics.plugins.ParserJena;
import com.karlhammar.ontometrics.plugins.ParserOWLAPI;
import com.karlhammar.ontometrics.plugins.api.OntoMetricsPlugin;

import org.ontologyengineering.ontometrics.plugins.Filter.FilterType;

/**
 * 
 * @author Aidan Delaney <aidan@phoric.eu>
 *
 */
public abstract class DiagramMetric extends OntoMetricsPlugin {
    private String name, abbreviation, resource;
    private Logger   logger = Logger.getLogger(getClass().getName());
    private SparqlQuery  sq = null;
    private GremlinQuery gq = null;
    private FilterType   lhs, rhs;

    public DiagramMetric(String name, String abbreviation, String resource) {
        this.name         = name;
        // TODO: now that we get lhs and rhs from the classname, do the same for abbrev & resource
        this.abbreviation = abbreviation;
        this.resource     = resource;

        // Use the name of the class (which will be a subclass, as DiagramMetric is never directly instantiated).
        // This is horribly hacky, but it's cleaner than adding lhs & rhs to the constructor.
        // Assuming some standardisation to class names.
        String n         = getClass().getSimpleName();  // should return TokenSubsetToken
        String [] tokens = n.split("Subset");

        this.lhs         = getFilterType(tokens[0]);
        this.rhs         = getFilterType(tokens[1]);
    }

    private FilterType getFilterType(String token) {
        switch (token) {
            case "Atom":
                return FilterType.ATOM;
            case "Conj":
                return FilterType.ATOM_CONJUNCTION;
            case "Disj":
                return FilterType.ATOM_DISJUNCTION;
            case "NegAtom":
                return FilterType.ATOM_COMPLEMENT;
            case "Some":
                return FilterType.ATOM_SOMEOF;
            case "Only":
                return FilterType.ATOM_ALLOF;
            case "Top":
                return FilterType.TOP;
        }
        return null;
    }

    @Override
    public void init(ParserJena jena, ParserOWLAPI owlapi, LazyParserGremlin gremlin) {
        super.init(jena, owlapi, gremlin);
        try {
            sq = new SparqlQuery(jena, resource);
            gq = new GremlinQuery(gremlin, lhs, rhs);
        } catch (IOException ioe) {
            logger.severe("Cannot load SPARQL resource.");
            logger.severe(ioe.toString());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMetricAbbreviation() {
        return abbreviation;
    }

    @Override
    public Optional<String> getMetricValue(File ontologyFile) {
        if(null == sq) {
            logger.severe("getMetricValue() called before init!");
        }
        String sqr = sq.calculatePrettyDiagramRatio();
        String gqr = gq.runQuery();

        if(!sqr.equals(gqr)) {
            return Optional.empty();
        }
        return Optional.of(sqr);
    }
}