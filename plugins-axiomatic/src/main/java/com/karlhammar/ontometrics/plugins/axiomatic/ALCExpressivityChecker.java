/*
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyrigth (C) 2014, Aidan Delaney <aidan@ontologyengineering.org>
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.karlhammar.ontometrics.plugins.axiomatic;

import java.util.Collections;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DLExpressivityChecker;

import static org.semanticweb.owlapi.util.DLExpressivityChecker.Construct;

/**
 * Based on DLExpressivityChecker by:
 * @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics
 *         Group, Date: 26-Feb-2007
 */
@SuppressWarnings({ "unused", "javadoc" })
public class ALCExpressivityChecker {

    public static int getALCAxiomCount (OWLOntology ontology) {
        // for each logical axiom in the ontology, figure out if it is in ALC
        // do this by creating a new ontology with only that fact.
        int count = 0;
        for(OWLAxiom a: ontology.getLogicalAxioms()) {
                OWLOntologyManager  m = OWLManager.createOWLOntologyManager();
                OWLOntology o = null;
                try {
                    o = m.createOntology();
                } catch (OWLOntologyCreationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                m.addAxiom(o, a);

                DLExpressivityChecker dec = new DLExpressivityChecker(Collections.singleton(o));
                try {
                    if(dec.getConstructs()
                            .stream()
                            .allMatch(c -> c.compareTo(DLExpressivityChecker.Construct.C) <= 0)) {
                        count++;
                    }
                } catch (OWLException oe) {
                    // this cannot be thrown
                }
        }
        return count;
    }
}
