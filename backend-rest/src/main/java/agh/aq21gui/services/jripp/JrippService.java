/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.jripp;

import agh.aq21gui.converters.Aq21InputToWeka;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.services.j48.J48Service;
import agh.aq21gui.stubs.StubFactory;
import java.util.List;
import weka.classifiers.rules.JRip;
import weka.core.Instances;

/**
 *
 * @author marcin
 */
public class JrippService {
    public static void main(String args[]) throws Exception {
        Input input = StubFactory.getInput();
        Test run = input.runsGroup.runs.get(0);
        new JrippService().prepareAndRunJripp(input, run);
    }

    public List<Hypothesis> prepareAndRunJripp(Input input, Test run) throws Exception {
        Instances instances = new Aq21InputToWeka().aq21ToWeka(input);
        String className = run.grepClassName();
        J48Service.setClassificationClass(input, className, instances);
        return runJripp(run, instances);
    }
    
    public List<Hypothesis> runJripp(Test run, Instances instances) throws Exception {
        JRip jripp = new JRip();
        //final String[] cmdline = J48Service.paramsetToCmdline(run);
        //jripp.setOptions(cmdline);
        jripp.buildClassifier(instances);
        System.out.println(jripp.getRuleset());
        for (weka.classifiers.rules.Rule item : jripp.getRuleset()) {
            System.out.println(item.toString());
        }
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
