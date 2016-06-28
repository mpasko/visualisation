/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.adidata.scripts.testtools;

import agh.aq21gui.evaluator.Statistics;
import agh.aq21gui.evaluator.StatsAgregator;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.Util;
import java.text.DecimalFormat;
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

    public String formatTextResults() {
        int max = computeFirstRowSize();
        StringBuilder build = new StringBuilder();
        build.append(FormatterUtil.alignStringForward(max, " ", "name"));
        build.append("||  TP |  TN |  FP |  FN |  C  |\n");
        for (Entry<String, Statistics> entry : statTable.entrySet()) {
            build.append(FormatterUtil.alignStringForward(max, "=", ""));
            build.append("++=====+=====+=====+=====+=====+\n");
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
            build.append("|");
            build.append(alignNumber((int)stat.getComplexity()));
            build.append("|\n");
        }
        String table = build.toString();
        return table;
    }

    public String formatLatexResults(String caption) {
        StringBuilder build = new StringBuilder();
        build.append(Util.loadFile("resources\\table_header.txt"));
        for (Entry<String, Statistics> entry : statTable.entrySet()) {
            if (entry.getValue().getComplexity() > 0.0) {
                build.append(entry.getKey().replaceAll("_", "-"));
                build.append(" & ");
                Statistics stat = entry.getValue();
                build.append(stat.getTruePositive());
                build.append(" & ");
                build.append(stat.getTrueNegative());
                build.append(" & ");
                build.append(stat.getFalsePositive());
                build.append(" & ");
                build.append(stat.getFalseNegative());
                build.append(" & ");
                double f1 = stat.getF1Score();
                String f1text = new DecimalFormat("0.0000").format(f1);
                if (Double.isNaN(f1)) {
                    f1text = "-";
                }
                build.append(f1text);
                build.append(" & ");
                build.append(stat.getComplexity());
                build.append("\\\\\n\\hline\n");
            }
        }
        build.append(String.format("\\caption{\\texttt{%s}}", caption).replaceAll("_", "\\\\_")); 
        build.append("\n");
        build.append("\\end{longtable}\n");
        //build.append("}\n");
        //build.append("\\end{table}\n");
        return build.toString();
    }

    public String alignNumber(int number) {
        String stringified = Integer.valueOf(number).toString();
        return FormatterUtil.alignString(5, " ", stringified);
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
