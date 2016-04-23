/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.adidata.archive;

import agh.adidata.scripts.ADIExperiment;
import agh.aq21gui.stubs.StubFactory;
import java.util.LinkedList;

/**
 *
 * @author marcin
 */
public class MaterialRecognition {
    public static void main(String[] args) {
        final ADIExperiment adiExperiment = new ADIExperiment("Material_recognition_classic");
		adiExperiment.runAllPossibilities("stop", null, new LinkedList<String>(), "");
    }
}
