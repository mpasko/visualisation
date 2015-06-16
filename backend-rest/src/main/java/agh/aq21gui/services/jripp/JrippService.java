/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.jripp;

import agh.aq21gui.converters.Aq21InputToWeka;
import agh.aq21gui.filters.ContinuousClassFilter;
import agh.aq21gui.filters.Discretizer;
import agh.aq21gui.filters.RuleAgregator;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Parameter;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Util;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.JRip.Antd;
import weka.classifiers.rules.JRip.RipperRule;
import weka.core.Attribute;
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

    public static void setClassificationClass(Input input, String className, Instances instances) {
        int classIndex = input.findAttributeNumber(className);
        instances.setClassIndex(classIndex);
    }

    private static String[] paramsetToCmdline(Test run) {
         ArrayList<String> cmdline = new ArrayList<String>();
        int folds = 0;
        double minimal_weights = 0.0;
        int number_of_runs = 0;
        boolean debug = false;
        int seed = 0;
        boolean not_check_error_rate = false;
        boolean not_use_pruning = false;
        
        for (Parameter param : run.getRunSpecificParameters()) {
            if (param.name.equalsIgnoreCase("folds")) {
                folds = NumericUtil.parseIntegerDefault(param.value, folds);
            } else if (param.name.equalsIgnoreCase("minimal_weights")) {
                minimal_weights = NumericUtil.parseDoubleDefault(param.value, minimal_weights);
            } else if (param.name.equalsIgnoreCase("number_of_runs")) {
                number_of_runs = NumericUtil.parseIntegerDefault(param.value, number_of_runs);
            } else if (param.name.equalsIgnoreCase("debug")) {
                debug = param.isTrue();
            } else if (param.name.equalsIgnoreCase("seed")) {
                seed = NumericUtil.parseIntegerDefault(param.value, seed);
            } else if (param.name.equalsIgnoreCase("not_check_error_rate")) {
                not_check_error_rate = param.isTrue();
            } else if (param.name.equalsIgnoreCase("not_use_pruning")) {
                not_use_pruning = param.isTrue();
            } 
        }
        if (!debug) {
            cmdline.add("-D");
        }
        if (not_check_error_rate) {
            cmdline.add("-E");
        }
        
        if (not_use_pruning) {
            cmdline.add("-P");
        }
        
        cmdline.add("-S");
        cmdline.add(Integer.toString(seed));
        
        cmdline.add("-O");
        cmdline.add(Integer.toString(number_of_runs));
        
        
        cmdline.add("-N");
        cmdline.add(Double.toString(minimal_weights));
        
        cmdline.add("-F");
        cmdline.add(Integer.toString(folds));
        
        
        
        return cmdline.toArray(new String[cmdline.size()]);
    }

   
    
    public List<Hypothesis> prepareAndRunJripp(Input input, Test run) throws Exception {
        Instances instances = new Aq21InputToWeka().aq21ToWeka(input);
        String className = run.grepClassName();
        JrippService.setClassificationClass(input, className, instances);
        return runJripp(run, instances);
    }
    
    private static Selector convertFromJripSelector(Antd selector) {
         Selector s = new Selector();
        String[] tokens = selector.toString().split(" ");
        s.setName(tokens[0]);
        s.setComparator(tokens[1]);
        s.setValue(tokens[2]);
        return s;
    }
    
     private static Hypothesis getHypotheses(List<Hypothesis> hyps,Attribute attr, String claz) {
        for (Hypothesis h : hyps) {
            ClassDescriptor desc =  h.getClasses().get(0);
            if (desc.getValue().equalsIgnoreCase(claz)) {
                return h;
            }
        }
        
        // hypothesis not found
        Hypothesis hyp = new Hypothesis();
        ClassDescriptor clasDesc = prepareDescriptor(attr.name(), claz);
        hyp.setClasses(Util.singleElemList(clasDesc));
        hyps.add(hyp);
        
        return hyp;
    }
    
    public static List<Hypothesis> runJripp(Test run, Instances instances) throws Exception {
        Attribute attr = instances.classAttribute();
        JRip jripp = new JRip();
        final String[] cmdline = paramsetToCmdline(run);
        jripp.setOptions(cmdline);
        jripp.buildClassifier(instances);
        List<Hypothesis> hyps = new LinkedList<Hypothesis>();
        
        for (weka.classifiers.rules.Rule item : jripp.getRuleset()) {
            RipperRule ripperRule = (RipperRule) item;
            String consequent = ripperRule.toString(attr).split("=>")[1];
            String claz = consequent.split("=")[1].trim(); // get class value
            
            Hypothesis hypo = getHypotheses(hyps, attr, claz);
            System.out.println(ripperRule.toString(attr));
            Rule rule = new Rule();
            
            for (Antd a : ripperRule.getAntds()) {
                rule.addSelector(convertFromJripSelector(a));
            }
            
            hypo.rules.add(rule);
        }
        
        return hyps;
    }

    public static ClassDescriptor prepareDescriptor(String clasAttr, String claz) {
        final ClassDescriptor classDescriptor = new ClassDescriptor();
        classDescriptor.name = clasAttr;
        classDescriptor.comparator = "=";
        classDescriptor.setValue(claz);
        return classDescriptor;
    }
    
    public Output convertAndRun(Input input) {
        //System.out.println(Util.attachLines(input.toString()));
        //System.out.println("----------------------------------------------------------------------------");
        ClassDescriptor descriptor = input.getAggregatedClassDescriptor();
        Domain classDom = input.findDomainObjectRrecursively(descriptor.getName());
        Input filteredData;
        if (classDom.isContinuous() || classDom.isInteger()) {
            if (descriptor.isCustomValue()) {
                Discretizer discretizer = new Discretizer();
                Discretizer.Mode mode = Discretizer.Mode.SIMILAR_SIZE;
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
        Output agragated = new RuleAgregator().agregate(out);
        return agragated;
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
    private List<Hypothesis> runAll(Input input) {
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
    
    private static List<Hypothesis> prepareAndRunSingle(Test run, Input input) throws Exception {
        Instances instances = prepareDataForRun(run, input);
        List<Hypothesis> hypotheses = runJripp(run, instances);
        return hypotheses;
    }
    
    public static Instances prepareDataForRun(Test run, Input input) {
        String className = run.grepClassName();
        Instances instances = new Aq21InputToWeka().aq21ToWeka(input);
        setClassificationClass(input, className, instances);
        return instances;
    }
    
   
}
