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
import agh.aq21gui.utils.MapList;
import agh.aq21gui.utils.Util;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author marcin
 */
public class RulePrunner {

    public RulePrunner() {
    }
    
    public Output doAll(Output out) {
        return this.sortByAccuracyAndApplyElse(this.prune(out));
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
    
    public Output sortByAccuracyAndApplyElse(Output out) {
        Output result = out; //deepcopy
        List<Hypothesis> old_hypotheses = result.getOutputHypotheses();
        List<Hypothesis> med_hypotheses = sortHypothesesByAccuracy(result, old_hypotheses);
        List<Hypothesis> new_hypotheses = applyElseOnAll(med_hypotheses);
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

    private List<Hypothesis> sortHypothesesByAccuracy(Output result, List<Hypothesis> old_hypotheses) {
        Classifier classifier = new Classifier(result);
        List<Hypothesis> new_hypotheses = new LinkedList<Hypothesis>();
        HashMap<Hypothesis, Double> accuracies = new HashMap<Hypothesis, Double>();
        for (Hypothesis hypo : old_hypotheses) {
            accuracies.put(hypo, classifier.performStatistics(hypo).getAccuracy());
        }
        new_hypotheses.addAll(old_hypotheses);
        Collections.sort(new_hypotheses, new HypothesisCompartor(accuracies));
        return new_hypotheses;
    }

    private List<Hypothesis> applyElseOnAll(List<Hypothesis> med_hypotheses) {
        List<Hypothesis> new_hypotheses = new LinkedList<Hypothesis>();
        MapList<Hypothesis> map = new MapList<Hypothesis>();
        for (Hypothesis hypo : med_hypotheses) {
            String key = createClassNameSignatureKey(hypo.getClasses());
            map.add(key, hypo);
        }
        for (String key: map.keySet()){
            LinkedList<Hypothesis> list = map.get(key);
            new_hypotheses.addAll(applyElseCascadely(list));
        }
        return new_hypotheses;
    }

    private String createClassNameSignatureKey(List<ClassDescriptor> classes) {
        StringBuilder build = new StringBuilder();
        for (ClassDescriptor desc : classes) {
            build.append(desc.getName());
        }
        return build.toString();
    }
    
    private String createClassValueSignatureKey(List<ClassDescriptor> classes) {
        StringBuilder build = new StringBuilder();
        for (ClassDescriptor desc : classes) {
            build.append(desc.getValue());
        }
        return build.toString();
    }

    private Collection<? extends Hypothesis> applyElseCascadely(LinkedList<Hypothesis> list) {
        HypothesisAnd and = new HypothesisAnd();
        HypothesisNegation neg = new HypothesisNegation();
        LinkedList<Hypothesis> result = new LinkedList<Hypothesis>();
        HashMap<String, Hypothesis> elseStack = new HashMap<String, Hypothesis>();
        for (Hypothesis hypo : list) {
            Hypothesis new_hypo;
            String key = createClassValueSignatureKey(hypo.getClasses());
            Hypothesis found = null;
            for (Entry<String, Hypothesis> entry : elseStack.entrySet()) {
                if (!key.equalsIgnoreCase(entry.getKey())) {
                    found = entry.getValue();
                }
            }
            if (elseStack.get(key)==null) {
                elseStack.put(key, neg.negateHypothesis(hypo));
            }
            if (found==null) {
                new_hypo = hypo;
            } else {
                new_hypo = and.and(found, hypo);
            }
            result.add(new_hypo);
        }
        return result;
    }
    
    static class HypothesisCompartor implements Comparator<Hypothesis> {
        private final HashMap<Hypothesis, Double> accuracies;

        public HypothesisCompartor(HashMap<Hypothesis, Double> accuracies) {
            this.accuracies = accuracies;
        }
        
        @Override
        public int compare(Hypothesis t0, Hypothesis t1) {
            Double a0 = accuracies.get(t0);
            Double a1 = accuracies.get(t1);
            if (a0>a1) {
                return 1;
            } else if (a0<a1) {
                return -1;
            } else {
                return 0;
            }
        }
        
    }
}
