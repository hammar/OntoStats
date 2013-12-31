package org.ontologyengineering.ontometrics.plugins;

import java.io.File;

public class TestUtils {

    static final String dirname = System.getProperty("diagrammetrics.test.resources");
    /**
     * 
     * @param basename of form foo.bar.ClassNameTest
     * @return
     */
    public static File getOWLFile(String basename) {
        // get the name of the OWL file
        File        owl = null;
        basename        = basename.substring(basename.lastIndexOf('.') + 1); // remove "foo.bar."
        basename        = basename.replaceFirst("Test", ""); // remove Test from ClassNameTest
        String    fname = dirname + File.separator + "simple_" + basename.toLowerCase() + ".owl"; // our files are called simple_classname.owl

        try {
            owl      = new File(fname);
            //combined = new File(dirname + File.separator + "combined.owl");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return owl;
    }

    public static File getSSNFile() {
        return new File(dirname + File.separator + "ssn.owl");
    }
}
