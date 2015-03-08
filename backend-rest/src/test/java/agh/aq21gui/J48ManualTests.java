/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.services.j48.J48Service;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.services.csv.J48ArchetypeConfig;
import java.util.List;

/**
 *
 * @author marcin
 */
public class J48ManualTests {

    public static void main(String[] args) {
        final Input irisInput = StubFactory.getIrisInput();
        final List<Test> runs = irisInput.getTestsGroup().getRuns();
        new J48ArchetypeConfig().addAllJ48SpecificParameters(runs);
        
        //new J48Resource().convertAndRun(StubFactory.getInput());
        //new J48Resource().convertAndRun(StubFactory.getBaloonsOutput());
        new J48Service().convertAndRun(irisInput);
    }
    
}
