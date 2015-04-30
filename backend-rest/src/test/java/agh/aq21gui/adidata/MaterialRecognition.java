/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.adidata;

import java.util.LinkedList;

/**
 *
 * @author marcin
 */
public class MaterialRecognition {
    public static void main(String[] args) {
        final ADIExperiment adiExperiment = new ADIExperiment();
		adiExperiment.runAllPossibilities("stop", null, new LinkedList<String>());
    }
}
