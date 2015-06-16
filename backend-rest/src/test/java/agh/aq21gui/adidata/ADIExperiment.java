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
import agh.aq21gui.filters.RuleVerticalAgregator;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.FormatterUtil;
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
    
    TreeMap<String, Statistics> statTable = new TreeMap<String, Statistics>();
    private Input inputPattern;
    private List<Entry<IResource, String>> algSet;
    
    public ADIExperiment() {
        generateDefaultConfig();
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

    private void generateDefaultConfig() {
        setInput(StubFactory.loadAdiData());
        algSet = new LinkedList<Entry<IResource, String>>();
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new J48Resource(), ""));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new JRipResource(), ""));
       // algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "pd"));
        //algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "atf"));
        //algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "tf"));
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
        String name = String.format("%s-%s",resource.getName().replace("Resource", ""), mode);
        System.out.println(String.format("Experiment, mode=%s:", name));
        Input input = Util.deepCopyInput(inputPattern);
        RunsGroup runsGroup = resource.generateConfig(input);
        runsGroup.enforceClassForAll(className, threshold);
        runsGroup.enforceModeForAll(mode);
        input.setRunsGroup(runsGroup);
        //System.out.println(input.toString());
        input = new AttributeRemover().dropAttributes(input, ignore);
        //System.out.println("After setting correct class:");
        //System.out.println(input.toString());
        Output result = resource.performExperiment(input);
        Output sortedResult = new RuleSorter().sort(result);
        Output processed = new RuleVerticalAgregator().agregate(sortedResult);
        
        //System.out.println(sortedResult);
        System.out.println(processed.obtainOutputHypotheses().toString());
        StatsAgregator metrics = new MetricsResource().analyze(processed);
        System.out.println(metrics.toString());
        for (Hypothesis hypo : processed.getOutputHypotheses()) {
            String hypoName = hypo.getName();
            statTable.put(name+hypoName, metrics.getParticular().get(hypoName));
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
}
