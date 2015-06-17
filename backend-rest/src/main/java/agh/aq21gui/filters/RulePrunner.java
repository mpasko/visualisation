/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.evaluator.Classifier;
import agh.aq21gui.evaluator.Statistics;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.Util;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class RulePrunner {

    public RulePrunner() {
    }

    public Output prune(Output out) {
        Output result = out; //deepcopy
        Classifier classifier = new Classifier(result);
        List<Hypothesis> new_hypotheses = new LinkedList<Hypothesis>();
        for (Hypothesis hypothesis : out.getOutputHypotheses()) {
            Hypothesis best_modification = simplifyHypothesis(hypothesis, classifier);
            new_hypotheses.add(best_modification);
        }
        result.setOutputHypotheses(new_hypotheses);
        return result;
    }

    private Hypothesis simplifyHypothesis(Hypothesis hypothesis, Classifier classifier) {
        Hypothesis previous;
        Hypothesis best_modification = hypothesis;
        do {
            previous = best_modification;
            best_modification = findBestModification(previous, classifier);
        } while (previous != best_modification);
        return best_modification;
    }

    private Hypothesis findBestModification(Hypothesis current, Classifier classifier) {
        Statistics old_stats = classifier.performStatistics(current);
        double best_accuracy = old_stats.getAccuracy();
        Hypothesis best = current;
        for (Rule rule : current.rules) {
            for (Selector sel : rule.getSelectors()) {
                Hypothesis new_hypo = makeHypothesisWithoutSelector(current, rule, sel);
                Statistics new_stats = classifier.performStatistics(new_hypo);
                double accuracy = new_stats.getAccuracy();
                if (accuracy > best_accuracy) {
                    best_accuracy = accuracy;
                    best = new_hypo;
                }
            }
        }
        return best;
    }

    private Hypothesis makeHypothesisWithoutSelector(Hypothesis old_hypothesis, Rule old_rule, Selector sel) {
        Rule new_rule = makeRuleWithoutSelector(old_rule, sel);
        Hypothesis new_hypo = shallowCopyHypothesis(old_hypothesis);
        List<Rule> rules_list = new_hypo.getRules();
        rules_list.remove(old_rule);
        rules_list.add(new_rule);
        new_hypo.setRules(rules_list);
        return new_hypo;
    }

    private Rule makeRuleWithoutSelector(Rule old_rule, Selector sel) {
        Rule new_rule = new Rule();
        List<Selector> selector_list = new LinkedList<Selector>(old_rule.getSelectors());
        selector_list.remove(sel);
        new_rule.setSelectors(selector_list);
        return new_rule;
    }

    public static Hypothesis shallowCopyHypothesis(Hypothesis hypothesis) {
        List<Rule> rules = hypothesis.rules;
        Hypothesis new_hypo = new Hypothesis();
        new_hypo.rules = new LinkedList<Rule>(rules);
        new_hypo.name = hypothesis.name;
        new_hypo.setClasses(new LinkedList<ClassDescriptor>(hypothesis.getClasses()));
        return new_hypo;
    }
}
