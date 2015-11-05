/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.j48;

import agh.aq21gui.converters.Aq21InputToWeka;
import agh.aq21gui.converters.J48TreeToRuleSet;
import agh.aq21gui.filters.ContinuousClassFilter;
import agh.aq21gui.filters.Discretizer;
import agh.aq21gui.filters.Discretizer.Mode;
import agh.aq21gui.j48treegrammar.J48ParserUtil;
import agh.aq21gui.j48treegrammar.J48Tree;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Parameter;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Printer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 *
 * @author marcin
 */
public class J48Service {
    private static String lastRaw = "";

    public Output convertAndRun(Input input) {
        //System.out.println(Util.attachLines(input.toString()));
        //System.out.println("----------------------------------------------------------------------------");
        ClassDescriptor descriptor = input.getAggregatedClassDescriptor();
        Domain classDom = input.findDomainObjectRrecursively(descriptor.getName());
        Input filteredData;
        if (classDom.isContinuous() || classDom.isInteger()) {
            if (descriptor.isCustomValue()) {
                Discretizer discretizer = new Discretizer();
                Mode mode = Discretizer.Mode.SIMILAR_SIZE;
                filteredData = discretizer.discretize(input, descriptor.name, "square root", mode);
                expandRunsForStar(filteredData, descriptor);
                //System.out.println(Util.attachLines(filteredData.toString()));
            } else {
                filteredData = new ContinuousClassFilter().filter(input, descriptor);
            }
        } else {
            filteredData = input;
        }
        Output out = new Output(filteredData);
        List<Hypothesis> hypos = runAll(filteredData);
        out.setOutputHypotheses(hypos);
        out.setRaw(lastRaw);
        return out;
    }

    public List<Hypothesis> runAll(Input input) {
        List<Hypothesis> hypotheses_all = new LinkedList<Hypothesis>();
        try {
            for (Test run : input.runsGroup.runs) {
                Integer number = 0;
                List<Hypothesis> hypotheses = prepareAndRunSingle(run, input);
                for (Hypothesis h : hypotheses) {
                    h.name = run.getName() + "_" + NumericUtil.formatNumber(number++);
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

    public static String[] paramsetToCmdline(Test run) {
        ArrayList<String> cmdline = new ArrayList<String>();
        boolean prune = false;
        boolean collapse = false;
        int minimum_instances = 2;
        for (Parameter param : run.getRunSpecificParameters()) {
            if (param.name.equalsIgnoreCase("prune")) {
                prune = param.isTrue();
            } else if (param.name.equalsIgnoreCase("collapse")) {
                collapse = param.isTrue();
            } else if (param.name.equalsIgnoreCase("minimum_instances")) {
                minimum_instances = NumericUtil.parseIntegerDefault(param.value, minimum_instances);
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
        return cmdline.toArray(new String[cmdline.size()]);
    }

    private static List<Hypothesis> prepareAndRunSingle(Test run, Input input) throws Exception {
        Instances instances = prepareDataForRun(run, input);
        List<Hypothesis> hypotheses = runJ48(run, instances);
        return hypotheses;
    }

    public static Instances prepareDataForRun(Test run, Input input) {
        String className = run.grepClassName();
        Instances instances = new Aq21InputToWeka().aq21ToWeka(input);
        setClassificationClass(input, className, instances);
        return instances;
    }

    public static void setClassificationClass(Input input, String className, Instances instances) {
        int classIndex = input.findAttributeNumber(className);
        instances.setClassIndex(classIndex);
    }

    public static List<Hypothesis> runJ48(Test run, Instances instances) throws Exception {
        J48 j48 = new J48();
        final String[] cmdline = paramsetToCmdline(run);
        j48.setOptions(cmdline);
        j48.buildClassifier(instances);
        //Printer.printLines(j48.graph(), J48Service.class);
        J48ParserUtil parser = new J48ParserUtil();
        String graph = j48.graph();
        J48Tree tree = parser.parse(graph);
        lastRaw = String.format("%s%n%n%s", j48.toString(), graph);
        List<Hypothesis> hypothesis = new J48TreeToRuleSet().treeToRules(tree, run.grepClassName());
        return hypothesis;
    }

    public static void expandRunsForStar(Input filteredData, ClassDescriptor descriptor) {
        List<Test> new_runs = new LinkedList<Test>();
        for (Test run : filteredData.runsGroup.runs) {
            if (run.grepClassDescriptor().isCustomValue()) {
                Domain domain = filteredData.findDomainObjectRrecursively(descriptor.name);
                if (domain.getRange() == null) {
                    throw new RuntimeException("If star (*) used for classifying continuous data, it should be discretized first");
                }
                for (String value : domain.getRange()) {
                    Test new_run = new Test(run);
                    new_run.setName(run.getName() + value);
                    new_run.enforceClass(new ClassDescriptor(descriptor.name, "=", value));
                    new_runs.add(new_run);
                }
            } else {
                new_runs.add(run);
            }
        }
        filteredData.getRunsGroup().setRuns(new_runs);
    }
}
