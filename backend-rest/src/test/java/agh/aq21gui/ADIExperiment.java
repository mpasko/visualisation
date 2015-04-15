/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.converters.RuleSorter;
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
		adiExperiment.runExperiment(new J48Resource());
        adiExperiment.runExperiment(new Aq21Resource());
    }
    
    public void runExperiment(IResource resource) {
        System.out.println(String.format("%s Experiment:", resource.getName()));
        Input input = StubFactory.loadAdiData();
        RunsGroup runsGroup = resource.generateConfig(input);
        for (Test run : runsGroup.runs) {
            run.enforceClass("stop");
        }
        System.out.println("After setting correct class:");
        input.setRunsGroup(runsGroup);
        System.out.println(input.toString());
        Output result = resource.performExperiment(input);
        Output sortedResult = new RuleSorter().sort(result);
        System.out.println(sortedResult.toString());
    }
}
