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
                map.put(name, SelectorAgregator.agregateTwoSels(next,actual));
            } else {
                map.put(name, next);
            }
        }
        return new LinkedList<Selector>(map.values());
    }
}
