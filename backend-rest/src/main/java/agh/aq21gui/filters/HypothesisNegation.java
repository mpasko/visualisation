/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.Util;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class HypothesisNegation {

    private LinkedList<Rule> recursivelyMyltiply(LinkedList<Rule> little) {
        LinkedList<Rule> local_copy = new LinkedList<Rule>(little);
        LinkedList<Rule> totall_list = new LinkedList<Rule>();
        if (!local_copy.isEmpty()) {
            Rule last = local_copy.removeLast();
            LinkedList<Rule> obtained = recursivelyMyltiply(local_copy);
            for (Selector sel : last.getSelectors()) {
                for (Selector negatedSelector : this.negateSelector(sel)) {
                    if (obtained.isEmpty()){
                        Rule new_rule = new Rule();
                        List<Selector> selectors = new LinkedList<Selector>();
                        selectors.add(negatedSelector);
                        new_rule.setSelectors(selectors);
                        totall_list.add(new_rule);
                    }
                    for (Rule old_rule : obtained) {
                        Rule new_rule = new Rule();
                        List<Selector> selectors = new LinkedList<Selector>(old_rule.getSelectors());
                        selectors.add(negatedSelector);
                        new_rule.setSelectors(selectors);
                        totall_list.add(new_rule);
                    }
                }
            }
        }
        return totall_list;
    }

    public Hypothesis negateHypothesis(Hypothesis hypo) {
        LinkedList<Rule> little = new LinkedList<Rule>(hypo.getRules());
        LinkedList<Rule> totall_list = recursivelyMyltiply(little);
        /*
         ArrayList<Integer> iterator = new ArrayList<Integer>(hypo.rules.size());
         for (int i=0; i<iterator.size(); ++i){
         iterator.set(i, 0);
         }
         */
        Hypothesis result = new Hypothesis();
        result.setRules(totall_list);
        result.setClasses(hypo.getClasses());
        result.name = hypo.getName();
        return result;
    }

    public List<Rule> negateRule(Rule rule) {
        LinkedList<Rule> list = new LinkedList<Rule>();
        for (Selector sel : rule.getSelectors()) {
            List<Selector> negatedCases = negateSelector(sel);
            for (Selector negated : negatedCases) {
                list.add(new Rule(negated));
            }
        }
        return list;
    }

    public List<Selector> negateSelector(Selector sel) {
        if (sel.getRange_begin().isEmpty()) {
            String negatedComparator = Util.negatedComparator(sel.comparator);
            Selector result = new Selector(sel.name, negatedComparator, sel.getValue());
            LinkedList<Selector> list = new LinkedList<Selector>();
            list.add(result);
            return list;
        } else {
            Selector left = new Selector(sel.getName(), "<", sel.getRange_begin());
            Selector right = new Selector(sel.getName(), ">", sel.getRange_end());
            LinkedList<Selector> list = new LinkedList<Selector>();
            list.add(left);
            list.add(right);
            return list;
        }
    }
}
