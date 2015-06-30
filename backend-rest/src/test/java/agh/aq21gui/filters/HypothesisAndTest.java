/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.antlr.runtime.RecognitionException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import testtools.Utils;

/**
 *
 * @author marcin
 */
public class HypothesisAndTest{
    private static List<Selector> sel_pool;
    private static List<Rule> rule_pool;
    
    public HypothesisAndTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        final int TOTALL_RULES = 4;
        final int TOTALL_SELS = TOTALL_RULES*2;
        sel_pool = new ArrayList<Selector>(TOTALL_SELS);
        rule_pool = new ArrayList<Rule>(TOTALL_SELS/2);
        try{
        for (int i=0; i<TOTALL_SELS; ++i) {
            sel_pool.add(Selector.parse("[n"+i+"<5.5]"));
        }
        for (int i=0; i<TOTALL_RULES; ++i) {
            rule_pool.add(new Rule(sel_pool.get(i*2), sel_pool.get(i*2+1)));
        }
        }catch(RecognitionException ex) {
            //It will never happen ;)
        }
    }

    @Test
    public void testAndHypotheses() {
        System.out.println("testAndHypotheses");
        Hypothesis a = new Hypothesis(rule_pool.get(0), rule_pool.get(1));
        Hypothesis b = new Hypothesis(rule_pool.get(2), rule_pool.get(3));
        HypothesisAnd instance = new HypothesisAnd();
        Hypothesis result = instance.and(a, b);
        assertEquals(4, result.getRules().size());
        for (Rule rule : result.getRules()) {
            int expected_size = 4;
            List<Selector> selectors = rule.getSelectors();
            assertEquals(expected_size, selectors.size());
            Utils.assertAllDifferent(selectors);
        }
    }
    
    @Test
    public void testAndRules() {
        System.out.println("testAndRules");
        HypothesisAnd instance = new HypothesisAnd();
        Rule a = rule_pool.get(0);
        Rule b = rule_pool.get(1);
        Rule result = instance.and(a, b);
        List<Selector> selectors = result.getSelectors();
        int expected_size = 4;
        assertEquals(expected_size, selectors.size());
        Utils.assertAllDifferent(selectors);
    }
}
