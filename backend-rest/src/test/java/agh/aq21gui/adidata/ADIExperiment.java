/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.adidata;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.IResource;
import agh.aq21gui.J48Resource;
import agh.aq21gui.JRipResource;
import agh.aq21gui.MetricsResource;
import agh.aq21gui.evaluator.Statistics;
import agh.aq21gui.filters.RuleSorter;
import agh.aq21gui.evaluator.StatsAgregator;
import agh.aq21gui.filters.AttributeRemover;
import agh.aq21gui.filters.ContinuousClassFilter;
import agh.aq21gui.filters.RuleAgregator;
import agh.aq21gui.filters.RulePrunner;
import agh.aq21gui.filters.RuleVerticalAgregator;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Util;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author marcin
 */
public class ADIExperiment {
    
    public static final String STOP = "stop";
    public static final String W_ROZ = "wytrzym_rozciag_mpa";
    public static final String WYDL = "wydluzenie";
    public static final String PRZEW = "przewezenie";
    public static final String UDAR= "udarnosc";
    public static final String G_PLAST = "granica_plast_mpa";
    public static final String W_ZME = "wytrzym_zmecz_mpa";
    public static final String FRAC = "frac_toughness";

    public static List<String> allPropertiesWithout(String item) {
        List<String> strings = Util.strings(STOP, W_ROZ, WYDL, PRZEW, UDAR, G_PLAST, W_ZME, FRAC);
        strings.remove(item);
        return strings;
    }

    TreeMap<String, Statistics> statTable = new TreeMap<String, Statistics>();
    private Input inputPattern;
    private List<Entry<IResource, String>> algSet;
    private List<DiscretizerRanges> ranges = new LinkedList<DiscretizerRanges>();

    public ADIExperiment() {
        generateDefaultAlgorithmSet();
    }

    /**
     * @param input the input to set
     */
    public void setInput(Input input) {
        this.inputPattern = input;
    }

    public void setAlgList(List<Entry<IResource, String>> list) {
        this.algSet = list;
    }

    private void generateDefaultAlgorithmSet() {
        setInput(StubFactory.loadAdiData());
        algSet = new LinkedList<Entry<IResource, String>>();
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new J48Resource(), "prune"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new JRipResource(), "strict"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "pd"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "atf"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "tf"));
    }

    public void runAllPossibilities(String className, String threshold, List<String> ignore) {
        statTable = new TreeMap<String, Statistics>();
        for (Entry<IResource, String> entry : algSet) {
            runExperiment(entry.getKey(), entry.getValue(), className, threshold, ignore);
        }
        String table = formatTextResults();
        System.out.println(table);
    }

    public void runExperiment(IResource resource, String mode, String className, String threshold, List<String> ignore) {
        String name = String.format("%s-%s", resource.getName().replace("Resource", ""), mode);
        System.out.println(String.format("Experiment, mode=%s:", name));
        Input input = Util.deepCopyInput(inputPattern);
        initializeProperConfiguration(resource, input, className, threshold, mode);
        input = inputPreProcessing(input, ignore);
        Output result = resource.performExperiment(input);
        Output processed = outputPostProcessing(result);

        System.out.println(processed.obtainOutputHypotheses().toString());
        MetricsResource metricsResource = new MetricsResource();
        if (mode.equalsIgnoreCase("strict")) {
            metricsResource.questionAsFalse = true;
        }
        StatsAgregator metrics = metricsResource.analyze(processed);
        System.out.println(metrics.toString());
        for (Hypothesis hypo : processed.getOutputHypotheses()) {
            String hypoName = hypo.getName();
            statTable.put(name + hypoName, metrics.getParticular().get(hypoName));
        }
    }

    private String formatTextResults() {
        int max = computeFirstRowSize();
        StringBuilder build = new StringBuilder();
        build.append(FormatterUtil.alignStringForward(max, " ", "name"));
        build.append("||  TP |  TN |  FP |  FN |\n");
        for (Entry<String, Statistics> entry : statTable.entrySet()) {
            build.append(FormatterUtil.alignStringForward(max, "=", ""));
            build.append("++=====+=====+=====+=====+\n");
            build.append(FormatterUtil.alignStringForward(max, " ", entry.getKey()));
            build.append("||");
            Statistics stat = entry.getValue();
            build.append(alignNumber(stat.getTruePositive()));
            build.append("|");
            build.append(alignNumber(stat.getTrueNegative()));
            build.append("|");
            build.append(alignNumber(stat.getFalsePositive()));
            build.append("|");
            build.append(alignNumber(stat.getFalseNegative()));
            build.append("|\n");
        }
        String table = build.toString();
        return table;
    }

    public String alignNumber(int truePositive1) {
        String truePositive = new Integer(truePositive1).toString();
        String alignString = FormatterUtil.alignString(5, " ", truePositive);
        return alignString;
    }

    private int computeFirstRowSize() {
        int max = 7;
        for (String key : statTable.keySet()) {
            if (key.length() > max) {
                max = key.length();
            }
        }
        max += 3;
        return max;
    }

    /**
     * @param ranges the ranges to set
     */
    public void setRanges(List<DiscretizerRanges> ranges) {
        this.ranges = ranges;
    }

    private Input applyRangeDiscretization(Input in) {
        Input result = in;
        for(DiscretizerRanges range : ranges) {
            ClassDescriptor cd = new ClassDescriptor(range.attribute, NumericUtil.dobleListToStringList(range.values));
            result = new ContinuousClassFilter().filter(result, cd, range.labels);
        }
        return result;
    }

    public void initializeProperConfiguration(IResource resource, Input input, String className, String threshold, String mode) {
        RunsGroup runsGroup = resource.generateConfig(input);
        runsGroup.enforceClassForAll(className, threshold);
        runsGroup.enforceModeForAll(mode);
        if (mode.equalsIgnoreCase("prune")) {
            runsGroup.enforceParameter("prune", "true");
        }
        input.setRunsGroup(runsGroup);
    }

    public Output outputPostProcessing(Output result) {
        Output processed = result;
        
        processed = new RuleAgregator().agregate(processed);
        /* *x/
        processed = new RuleVerticalAgregator().agregate(processed);
        /* *x/
        processed = new RulePrunner().doAll(processed);
        /* *x/
        processed = new RuleSorter().sort(processed);
        /* */
        return processed;
    }

    public Input inputPreProcessing(Input input, List<String> ignore) {
        input = applyRangeDiscretization(input);
        input = new AttributeRemover().dropAttributes(input, ignore);
        return input;
    }
}
