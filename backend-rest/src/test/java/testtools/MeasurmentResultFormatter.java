/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testtools;

import agh.aq21gui.adidata.ADIExperiment;
import agh.aq21gui.evaluator.Statistics;
import agh.aq21gui.evaluator.StatsAgregator;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.FormatterUtil;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author marcin
 */
public class MeasurmentResultFormatter {
    
    
    TreeMap<String, Statistics> statTable;
    
    public MeasurmentResultFormatter() {
        statTable = new TreeMap<String, Statistics>();
    }

    public void consumeResults(Output processed, String name, StatsAgregator metrics) {
        for (Hypothesis hypo : processed.getOutputHypotheses()) {
            String hypoName = hypo.getName();
            statTable.put(name + hypoName, metrics.getParticular().get(hypoName));
        }
    }

    public String formatTextResults(ADIExperiment adiExperiment) {
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
            build.append(adiExperiment.alignNumber(stat.getTruePositive()));
            build.append("|");
            build.append(adiExperiment.alignNumber(stat.getTrueNegative()));
            build.append("|");
            build.append(adiExperiment.alignNumber(stat.getFalsePositive()));
            build.append("|");
            build.append(adiExperiment.alignNumber(stat.getFalseNegative()));
            build.append("|\n");
        }
        String table = build.toString();
        return table;
    }

    int computeFirstRowSize() {
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
