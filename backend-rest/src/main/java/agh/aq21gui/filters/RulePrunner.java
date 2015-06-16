/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.evaluator.Classifier;
import agh.aq21gui.evaluator.Statistics;
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
    
    public Output prune(Output out){
        Output result = out; //deepcopy
        Classifier classifier = new Classifier(result);
        List<Hypothesis> new_hypotheses = new LinkedList<Hypothesis>();
        for (Hypothesis hypothesis : out.getOutputHypotheses()) {
            Statistics old_stats = classifier.performStatistics(hypothesis);
            double best_accuracy = old_stats.getAccuracy();
            Hypothesis best_modification = hypothesis;
            for (Rule rule : hypothesis.rules) {
                for (Selector sel : rule.getSelectors()) {
                    Hypothesis new_hypo = makeHypothesisWithoutSelector(hypothesis, rule, sel);
                    Statistics new_stats = classifier.performStatistics(new_hypo);
                    double accuracy = new_stats.getAccuracy();
                    if (accuracy>best_accuracy) {
                        best_accuracy = accuracy;
                        best_modification = new_hypo;
                    }
                }
            }
            new_hypotheses.add(best_modification);
        }
        result.setOutputHypotheses(new_hypotheses);
        return result;
    }

    private Hypothesis makeHypothesisWithoutSelector(Hypothesis old_hypothesis, Rule old_rule, Selector sel) {
        Rule new_rule = Util.shallowCopyRule(old_rule);
        List<Selector> selector_list = new_rule.getSelectors();
        selector_list.remove(sel);
        new_rule.setSelectors(selector_list);
        Hypothesis new_hypo = Util.shallowCopyHypothesis(old_hypothesis);
        List<Rule> rules_list = new_hypo.getRules();
        rules_list.remove(old_rule);
        rules_list.add(new_rule);
        return new_hypo;
    }
}
