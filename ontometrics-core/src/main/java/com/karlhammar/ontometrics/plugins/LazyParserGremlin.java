package com.karlhammar.ontometrics.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.sail.SailGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.Pipeline;

import org.openrdf.sail.memory.MemoryStore;

/**
 * 
 * @author Aidan Delaney <aidan@phoric.eu>
 *
 * Allows running of GremlinPipelines against an in-memory model.  Within 
 * OntoStats we use Jena models, OWLAPI models and TinkerPop models.  This
 * class ensures that the TinkerPop model is only loaded when it is needed.
 * Such behaviour cuts down on the amount of memory consumed by models.
 */
public class LazyParserGremlin {
    private static Logger logger = Logger.getLogger("LazyParserGremlin");

    private static LazyParserGremlin ref = null;
    private SailGraph memGraph;

    private LazyParserGremlin(ParserConfiguration pc, FileInputStream fis, URL docUrl) throws IllegalArgumentException {
        // we can't get a Gremlin parser to resolve imports (yet).
        if(pc.getImportStrategy() == ParserConfiguration.ImportStrategy.ALLOW_IMPORTS) {
            throw new IllegalArgumentException();
        }
        memGraph = new SailGraph(new MemoryStore());
        memGraph.loadRDF(fis, docUrl.toString(), "rdf-xml", null);
    }

    public SailGraph getOntology() {
        return memGraph;
    }

    public static synchronized LazyParserGremlin resetSingletonObject(File ontologyFile, ParserConfiguration pc) {
        ref = null;
        return getSingletonObject(ontologyFile, pc);
    }

    public static synchronized LazyParserGremlin getSingletonObject(File ontologyFile) {
        return getSingletonObject(ontologyFile, new ParserConfiguration());
    }

    public static synchronized LazyParserGremlin getSingletonObject(File ontologyFile, ParserConfiguration pc) {
        if(null == ref) { // loead the in-memory graph structure
            URL          docUrl = null;
            try {
                docUrl = new URL("http://example.org/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            FileInputStream fis = null;
            try {
                 fis = new FileInputStream(ontologyFile);
            } catch (FileNotFoundException fnfe) {
                logger.severe("Cannot open file " + ontologyFile + System.lineSeparator() + fnfe.toString());
                System.exit(-1);
            }

            try {
                ref = new LazyParserGremlin(pc, fis, docUrl);
            } catch (IllegalArgumentException iae) {
                logger.severe("Cannot load RDF file with given configuration" + System.lineSeparator() + iae.toString());
                System.exit(-1);
            }
        }

        return ref;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
