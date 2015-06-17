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
    
    public Hypothesis negateHypothesis(Hypothesis hypo) {
        LinkedList<Rule> totall_list = new LinkedList<Rule>();
        LinkedList<Rule> little = new LinkedList<Rule>(hypo.getRules());
        Rule last = little.removeLast();
        for (Selector sel : last.getSelectors()){
            LinkedList<Rule> obtained;
            
        }
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
        }
        return null;
    }
}
