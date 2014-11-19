/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.converters.Aq21InputToWeka;
import agh.aq21gui.converters.J48TreeToRuleSet;
import agh.aq21gui.exceptions.IncorrectInputException;
import agh.aq21gui.j48treegrammar.J48ParserUtil;
import agh.aq21gui.j48treegrammar.J48Tree;
import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Parameter;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.OutputHypotheses;
import agh.aq21gui.stubs.StubFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Path;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Root resource for J48 (C4.5) Algorithm
 * @author marcin
 */
@Path("j48")
public class J48Resource {
    public void removeme(Input input) {
        Instances instances = new Aq21InputToWeka().aq21ToWeka(input);
        runAll(input,instances);
    }
    
    private void runAll(Input input, Instances instances) {
        try{
            for (Test run : input.runsGroup.runs){
                J48 j48 = new J48();
                //j48.setOptions(new String[]{"-C","0.25","-M","2"});
                j48.setOptions(new String[]{"-U","-O","-M","2"});
                String className = run.grepClassName();
                int classIndex = input.findAttributeNumber(className);
                instances.setClassIndex(classIndex);
                j48.buildClassifier(instances);
                System.out.println(j48.graph());
                J48ParserUtil parser = new J48ParserUtil();
                J48Tree tree = parser.parse(j48.graph());
                System.out.println(tree.toString());
                OutputHypotheses hypothesis = new J48TreeToRuleSet().treeToRules(tree, className);
                System.out.println(hypothesis.toString());
                
            }
        } catch (Exception ex) {
            Logger.getLogger(J48Resource.class.getName()).log(Level.SEVERE, null, ex);
        }
        instances.setClassIndex(-1);
    }
}
