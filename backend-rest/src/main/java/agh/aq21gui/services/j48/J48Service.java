/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.j48;

import agh.aq21gui.converters.Aq21InputToWeka;
import agh.aq21gui.converters.ContinuousClassFilter;
import agh.aq21gui.converters.J48TreeToRuleSet;
import agh.aq21gui.j48treegrammar.J48ParserUtil;
import agh.aq21gui.j48treegrammar.J48Tree;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Parameter;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.OutputHypotheses;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 *
 * @author marcin
 */
public class J48Service {

    public Output convertAndRun(Input input) {
        //System.out.println(input.toString());
        //Instances instances = new Aq21InputToWeka().aq21ToWeka(input);
        //System.out.println(instances.toString());
        Output out = new Output(input);
        List<Hypothesis> hypos = runAll(input);
        out.setOutputHypotheses(hypos);
        return out;
    }

    List<Hypothesis> runAll(Input input) {
        List<Hypothesis> hypotheses_all = new LinkedList<Hypothesis>();
        try {
            for (Test run : input.runsGroup.runs) {
                Integer number = 0;
                List<Hypothesis> hypotheses = prepareAndRunSingle(run, input);
                for (Hypothesis h: hypotheses) {
                    h.name = run.name + "_" + formatNumber(number++);
                }
                //System.out.println(hypotheses.toString());
                hypotheses_all.addAll(hypotheses);
            }
        } catch (Exception ex) {
 //           Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        return hypotheses_all;
    }

    public List<Hypothesis> runJ48(Test run, Instances instances) throws Exception {
        J48 j48 = new J48();
        //final String[] cmdline = new String[]{"-U", "-O", "-M", "2"};
        final String[] cmdline = paramsetToCmdline(run);
        j48.setOptions(cmdline);
        j48.buildClassifier(instances);
        System.out.println(j48.graph());
        J48ParserUtil parser = new J48ParserUtil();
        J48Tree tree = parser.parse(j48.graph());
//        System.out.println(tree.toString());
        List<Hypothesis> hypothesis = new J48TreeToRuleSet().treeToRules(tree, run.grepClassName());
        return hypothesis;
    }
    
    private String[] paramsetToCmdline(Test run){
        ArrayList<String> cmdline = new ArrayList<String>();
        boolean prune = false;
        boolean collapse = false;
        int minimum_instances = 2;
        for (Parameter param : run.getRunSpecificParameters()){
            if (param.name.equalsIgnoreCase("prune")){
                prune = isTrue(param);
            } else if (param.name.equalsIgnoreCase("collapse")) {
                collapse = isTrue(param);
            } else if (param.name.equalsIgnoreCase("minimum_instances")) {
                minimum_instances = parseInteger(param, minimum_instances);
            }
        }
        if (!prune) {
            cmdline.add("-U");
        }
        if (!collapse) {
            cmdline.add("-O");
        }
        cmdline.add("-M");
        cmdline.add(Integer.toString(minimum_instances));
        return cmdline.toArray(new String[]{});
    }

    private static void setClassificationClass(Input input, String className, Instances instances) {
        int classIndex = input.findAttributeNumber(className);
        instances.setClassIndex(classIndex);
    }

    private boolean isTrue(Parameter param) {
        return param.value.equalsIgnoreCase("true");
    }

    public int parseInteger(Parameter param, int defaultValue) {
        String val = param.value;
        val = val.replaceAll("\"", "");
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException _) {
            return defaultValue;
        }
    }

    private String formatNumber(Integer number) {
        final String text = number.toString();
        String prefix = "";
        for (int i=0; i<3-text.length(); ++i) {
            prefix = prefix.concat("0");
        }
        return prefix.concat(text);
    }

    private List<Hypothesis> prepareAndRunSingle(Test run, Input input) throws Exception {
        Instances instances = prepareDataForRun(run, input);
        List<Hypothesis> hypotheses = runJ48(run, instances);
        return hypotheses;
    }

    public static Instances prepareDataForRun(Test run, Input input) {
        String className = run.grepClassName();
        ClassDescriptor descriptor = run.grepClassDescriptor();
        Input filteredData = new ContinuousClassFilter().filter(input, descriptor);
        Instances instances = new Aq21InputToWeka().aq21ToWeka(filteredData);
        setClassificationClass(filteredData, className, instances);
        return instances;
    }
    
}
