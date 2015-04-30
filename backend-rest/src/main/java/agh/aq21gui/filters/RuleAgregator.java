/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.NumericUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author marcin
 */
public class RuleAgregator {
    public Output agregate(Output result) {
        Output copy = result;//Util.deepCopyOutput(result);
        List<Hypothesis> outputHypotheses = copy.getOutputHypotheses();
        agregateHypotheses(outputHypotheses);
        return copy;
    }

    private void agregateHypotheses(List<Hypothesis> outputHypotheses) {
        for (Hypothesis hypo : outputHypotheses) {
            for (Rule rule : hypo.rules) {
                List<Selector> selectors = rule.getSelectors();
                rule.setSelectors(agregateSelectors(selectors));
            }
        }
    }

    public List<Selector> agregateSelectors(List<Selector> selectors) {
        Map<String, Selector> map = new TreeMap<String, Selector>();
        for (Selector next : selectors) {
            String name = next.getName();
            Selector actual = map.get(name);
            if (actual != null) {
                map.put(name, agregateTwoSels(next,actual));
            } else {
                map.put(name, next);
            }
        }
        return new LinkedList<Selector>(map.values());
    }

    public Selector agregateTwoSels(Selector next, Selector actual) {
        char c1 = next.getComparator().charAt(0);
        char c2 = actual.getComparator().charAt(0);
        Selector result = new Selector();
        result.setName(next.getName());
        double val1 = NumericUtil.tryParse(next.getValue());
        double val2 = NumericUtil.tryParse(actual.getValue());
        if (c1==c2) {
            if (c1=='<') {
                if (val1<val2) {
                    return next;
                } else {
                    return actual;
                }
            } else if (c1=='>') {
                if (val1>val2) {
                    return next;
                } else {
                    return actual;
                }
            }
        } else {
            if (c1=='<' && c2=='>') {
                if (val2<val1) {
                    result.setRange_begin(NumericUtil.stringValueOf(val2));
                    result.setRange_end(NumericUtil.stringValueOf(val1));
                    result.setComparator("=");
                } else {
                    String message = String.format("Excluding selectors:%s and:%s", next.toString(), actual.toString());
                    throw new RuntimeException(message);
                }
            } else if (c1=='>' && c2=='<') {
                if (val1<val2) {
                    result.setRange_begin(NumericUtil.stringValueOf(val1));
                    result.setRange_end(NumericUtil.stringValueOf(val2));
                    result.setComparator("=");
                } else {
                    String message = String.format("Excluding selectors:%s and:%s", next.toString(), actual.toString());
                    throw new RuntimeException(message);
                }
            }
        }
        return result;
    }
}
