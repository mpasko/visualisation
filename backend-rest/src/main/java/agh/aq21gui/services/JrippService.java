/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services;

import agh.aq21gui.converters.J48TreeToRuleSet;
import agh.aq21gui.j48treegrammar.J48ParserUtil;
import agh.aq21gui.j48treegrammar.J48Tree;
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
        Test run = StubFactory.getIrisInput().testsGroup.runs.get(0);
        Instances instances = null;
        new JrippService().runJripp(run, instances);
    }
    
    public List<Hypothesis> runJripp(Test run, Instances instances) throws Exception {
        JRip jripp = new JRip();
        final String[] cmdline = J48Service.paramsetToCmdline(run);
        jripp.setOptions(cmdline);
        jripp.buildClassifier(instances);
        System.out.println(jripp.getRuleset());
        /*J48ParserUtil parser = new J48ParserUtil();
        J48Tree tree = parser.parse(j48.graph());
        List<Hypothesis> hypothesis = new J48TreeToRuleSet().treeToRules(tree, run.grepClassName());
        
        return hypothesis;
        */
        return null;
    }
}
