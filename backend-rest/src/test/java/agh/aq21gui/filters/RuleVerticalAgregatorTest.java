/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class RuleVerticalAgregatorTest {
    
    public RuleVerticalAgregatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void when_agregable_then_agregate() {
        System.out.println("when_agregable_then_agregate");
        Output input = generateAgregableHypothesis();
        
        RuleVerticalAgregator instance = new RuleVerticalAgregator();
        Output result = instance.agregate(input);
        
        List<Hypothesis> outputHypotheses = result.getOutputHypotheses();
        verifyAssumedStructure(outputHypotheses);
    }
    
    @Test
    public void when_complex_agregable_then_agregate() {
        System.out.println("when_complex_agregable_then_agregate");
        Output input = generateComplexAgregableHypothesis();
        
        RuleVerticalAgregator instance = new RuleVerticalAgregator();
        Output result = instance.agregate(input);
        
        List<Hypothesis> outputHypotheses = result.getOutputHypotheses();
        verifyAssumedStructure(outputHypotheses);
    }
    
    @Test
    public void when_range_agregable_then_agregate() {
        System.out.println("when_range_agregable_then_agregate");
        Output input = generateAgregableRangeHypothesis();
        
        RuleVerticalAgregator instance = new RuleVerticalAgregator();
        Output result = instance.agregate(input);
        
        List<Hypothesis> outputHypotheses = result.getOutputHypotheses();
        verifyAssumedStructure(outputHypotheses);
    }

    public static Output generateAgregableHypothesis() {
        Output input = new Output();
        Selector a_over_90 = new Selector("a", ">", "90");
        Selector wc_over_30 = new Selector("wc", ">", "30");
        Selector wc_less_90 = new Selector("wc", "<=", "90");
        Selector wt_over_330 = new Selector("wt", ">", "330");
        Rule rule1 = new Rule(a_over_90, wc_over_30, wt_over_330);
        Rule rule2 = new Rule(a_over_90, wc_less_90, wt_over_330);
        Hypothesis hypothesis = new Hypothesis(rule1, rule2);
        input.setOutputHypotheses(Arrays.asList(hypothesis));
        return input;
    }
    
    public static Output generateAgregableRangeHypothesis() {
        Output input = new Output();
        Selector a_over_90 = new Selector("a", ">", "90");
        Selector wc_less_30 = new Selector("wc", "<", "30");
        Selector wc_30_60 = new Selector("wc", "=", "30..60");
        Selector wc_60_90 = new Selector("wc", "=", "60..90");
        Selector wc_over_90 = new Selector("wc", ">=", "90");
        Selector wt_over_330 = new Selector("wt", ">", "330");
        Rule rule1 = new Rule(a_over_90, wc_less_30, wt_over_330);
        Rule rule2 = new Rule(a_over_90, wc_30_60, wt_over_330);
        Rule rule3 = new Rule(a_over_90, wc_60_90, wt_over_330);
        Rule rule4 = new Rule(a_over_90, wc_over_90, wt_over_330);
        Hypothesis hypothesis = new Hypothesis(rule1, rule2, rule3, rule4);
        input.setOutputHypotheses(Arrays.asList(hypothesis));
        return input;
    }
    
    public static Output generateComplexAgregableHypothesis() {
        Output input = new Output();
        Selector a_over_90 = new Selector("a", ">", "90");
        Selector wc_over_90 = new Selector("wc", ">", "90");
        Selector wc_less_90 = new Selector("wc", "<", "90");
        Selector wc_equal_90 = new Selector("wc", "=", "90");
        Selector wt_over_330 = new Selector("wt", ">", "330");
        Selector wt_eq_340 = new Selector("wt", "=", "340");
        Selector wt_over_340 = new Selector("wt", ">", "340");
        
        Rule rule1 = new Rule(a_over_90, wc_over_90, wt_over_330);
        Rule rule2 = new Rule(a_over_90, wc_less_90, wt_over_330);
        Rule rule3 = new Rule(a_over_90, wc_equal_90, wt_over_330);
        Rule rule4 = new Rule(a_over_90, wc_equal_90, wt_over_330);
        Rule rule5 = new Rule(a_over_90, wc_equal_90, wt_eq_340);
        Rule rule6 = new Rule(a_over_90, wc_equal_90, wt_over_340);
        Hypothesis hypothesis = new Hypothesis(rule1, rule2, rule3, rule4, rule5, rule6);
        input.setOutputHypotheses(Arrays.asList(hypothesis));
        return input;
    }

    public void verifyAssumedStructure(List<Hypothesis> outputHypotheses) {
        assertEquals(1, outputHypotheses.size());
        List<Rule> rules = outputHypotheses.get(0).rules;
        assertEquals(1, rules.size());
        List<Selector> selectors = rules.get(0).getSelectors();
        assertEquals(2, selectors.size());
        boolean is_a = false;
        boolean is_wt = false;
        for (Selector sel : selectors) {
            if (sel.getName().equalsIgnoreCase("a")){
                is_a=true;
                assertEquals(">", sel.getComparator());
                assertEquals("90", sel.getValue());
            } else {
                assertEquals("wt", sel.getName());
                is_wt=true;
                assertEquals(">", sel.getComparator());
                assertEquals("330", sel.getValue());
            }
        }
        assertTrue(is_a);
        assertTrue(is_wt);
    }
}
