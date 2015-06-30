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
        List<Hypothesis> aggregatedHypotheses = agregateHypotheses(outputHypotheses);
        copy.setOutputHypotheses(aggregatedHypotheses);
        return copy;
    }

    private List<Hypothesis> agregateHypotheses(List<Hypothesis> outputHypotheses) {
        List<Hypothesis> aggregatedHypotheses = new LinkedList<Hypothesis>();
        for (Hypothesis hypo : outputHypotheses) {
            LinkedList<Rule> rules = new LinkedList<Rule>();
            for (Rule rule : hypo.rules) {
                try {
                    List<Selector> selectors = rule.getSelectors();
                    rule.setSelectors(agregateSelectors(selectors));
                    rules.add(rule);
                }
                catch (Exception e) {
                    // System.out.println(e.getMessage());
                }
                
            }
            hypo.setRules(rules);
            aggregatedHypotheses.add(hypo);
        }
        
        return aggregatedHypotheses;
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
