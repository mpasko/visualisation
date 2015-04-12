/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.csv.CSVConverter;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.Util;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class ADIExperiment {
    public static void main(String[] args) {
        final ADIExperiment adiExperiment = new ADIExperiment();
		adiExperiment.runJ48Experiment();
        adiExperiment.runAq21Experiment();
    }
    
    public void runJ48Experiment() {
        System.out.println("J48 Experiment:");
        Input input = StubFactory.loadAdiData();
        final J48Resource resource = new J48Resource();
        RunsGroup runsGroup = resource.generateConfig(input);
        for (Test run : runsGroup.runs) {
            run.enforceClass("stop");
        }
        System.out.println("After setting correct class:");
        System.out.println(runsGroup.toString());
        input.setRunsGroup(runsGroup);
        Output result = resource.postIt(input);
        System.out.println(result.toString());
    }
    
    public void runAq21Experiment() {
        System.out.println("AQ21 Experiment:");
        Input input = StubFactory.loadAdiData();
        final Aq21Resource resource = new Aq21Resource();
        RunsGroup runsGroup = resource.generateConfig(input);
        for (Test run : runsGroup.runs) {
            run.enforceClass("stop");
        }
        System.out.println("After setting correct class:");
        System.out.println(runsGroup.toString());
        input.setRunsGroup(runsGroup);
        Output result = resource.postIt(input);
        System.out.println(result.toString());
    }
}
