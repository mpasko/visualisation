/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import java.util.LinkedList;

/**
 *
 * @author marcin
 */
public class HypothesisAnd {

    public Hypothesis and(Hypothesis a, Hypothesis b) {
        try {
            LinkedList<Rule> rules = new LinkedList<Rule>();
            for (Rule rule_a : a.getRules()) {
                for (Rule rule_b : b.getRules()) {
                    Rule new_rule = and(rule_a, rule_b);
                    rules.add(new_rule);
                }
            }
            Hypothesis new_hypo = new Hypothesis();
            new_hypo.setClasses(b.getClasses());
            new_hypo.setName(b.getName());
            new_hypo.setRules(rules);
            return new_hypo;
        } catch (RuntimeException ex) {
            System.out.println(a.toString());
            System.out.println(b.toString());
            throw(new RuntimeException(ex));
        }
    }

    public Rule and(Rule rule_a, Rule rule_b) {
        LinkedList<Selector> selectors = new LinkedList<Selector>();
        selectors.addAll(rule_a.getSelectors());
        selectors.addAll(rule_b.getSelectors());
        Rule new_rule = new Rule();
        new_rule.setSelectors(selectors);
        return new_rule;
    }
}
