/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author marcin
 */
public class RuleSorter {
    public Output sort(Output random) {
        Output sorted = Util.deepCopyOutput(random);
        for (Hypothesis hyp: sorted.getOutputHypotheses()) {
            hyp.setRules(sortRules(hyp.getRules()));
        }
        List<Hypothesis> values = new ArrayList<Hypothesis>(sorted.getOutputHypotheses());
        Collections.sort(values, new HypothesisComparator());
        sorted.setOutputHypotheses(values);
        return sorted;
    }

    public List<Rule> sortRules(List<Rule> rules) {
        ArrayList<Rule> sortedRules = new ArrayList<Rule>(rules);
        for (Rule rule: rules) {
            rule.setSelectors(sortSelectors(rule.getSelectors()));
        }
        Collections.sort(sortedRules, new RuleComparator());
        return sortedRules;
    }

    public List<Selector> sortSelectors(List<Selector> selectors) {
        ArrayList<Selector> sortedSelectors = new ArrayList<Selector>(selectors);
        Collections.sort(sortedSelectors, new SelectorComparator());
        return sortedSelectors;
    }

    public class HypothesisComparator implements Comparator<Hypothesis>{
        @Override
        public int compare(Hypothesis t, Hypothesis t1) {
            return t.printClasses().compareTo(t1.printClasses());
        }
    }

    public class RuleComparator implements Comparator<Rule>{
        @Override
        public int compare(Rule t, Rule t1) {
            return t.toString().compareTo(t1.toString());
        }
    }

    public class SelectorComparator implements Comparator<Selector>{
        @Override
        public int compare(Selector t, Selector t1) {
            return t.toString().compareTo(t1.toString());
        }
    }
}
