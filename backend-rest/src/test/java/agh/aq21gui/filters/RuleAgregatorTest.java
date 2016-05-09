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
import java.util.LinkedList;
import java.util.List;
import org.antlr.runtime.RecognitionException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static agh.aq21gui.filters.SelectorAndAgregatorTest.*;
import agh.aq21gui.filters.selectoragregator.ExcludingSelectorsException;
import agh.aq21gui.model.input.Input;

/**
 *
 * @author marcin
 */
public class RuleAgregatorTest {
    private static SelectorAndAgregator agregator;
    
    public RuleAgregatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        Input input = new Input();
        input.addDomain("ad", "continuous", "");
        input.addDomain("bd", "continuous", "");
        input.addDomain("cd", "continuous", "");
        input.addDomain("od", "nominal", "");
        input.addAttribute("c", "cd", "");
        input.addAttribute("b", "bd", "");
        input.addAttribute("a", "ad", "");
        input.addAttribute("o", "od", "");
        input.addEvent("260", "1", "17", "none");
        input.addEvent("261", "2", "16", "none");
        input.addEvent("360", "3", "1", "none");
        input.addEvent("359", "20", "2", "none");
        input.addEvent("329", "19", "1", "cd");
        input.addEvent("366", "1", "1", "cd");
        input.addEvent("10", "1", "1", "cd");
        input.addEvent("9", "1", "1", "cd");
        input.addEvent("330", "1", "1", "cd");
        input.addEvent("300", "1", "1", "cd");
        agregator = new SelectorAndAgregator(input);
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testAgregateSelectors() throws RecognitionException {
        System.out.println("agregateSelectors");
        List<Selector> selectors = new LinkedList<Selector>();
        selectors.add(Selector.parse("[a<17]"));
        selectors.add(Selector.parse("[b>2]"));
        selectors.add(Selector.parse("[a>1]"));
        selectors.add(Selector.parse("[b<20]"));
        selectors.add(Selector.parse("[c<10]"));
        RuleAgregator instance = new RuleAgregator();
        List<Selector> result = instance.agregateSelectors(selectors, agregator);
        assertEquals(3, result.size());
        for (Selector sel : result) {
            if (sel.name.equalsIgnoreCase("a")) {
                assertEquals("2", sel.getRange_begin().replaceAll("\\.", ""));
                assertEquals("16", sel.getRange_end().replaceAll("\\.", ""));
            } else if (sel.name.equalsIgnoreCase("b")) {
                assertEquals("3", sel.getRange_begin().replaceAll("\\.", ""));
                assertEquals("19", sel.getRange_end().replaceAll("\\.", ""));
            } else if (sel.name.equalsIgnoreCase("c")) {
                assertEquals("10", sel.getValue().replaceAll("\\.", ""));
            } else {
                fail(String.format("Unexpected selector: %s", sel));
            }
        }
    }

    @Test
    public void testAgregateTwoSels() throws RecognitionException {
        System.out.println("agregateTwoSels");
        Selector next = Selector.parse("[a>1]");
        Selector actual = Selector.parse("[a<17]");
        Selector result = agregator.agregateTwoSels(next, actual);
        assertEquals("2", result.getRange_begin().replaceAll("\\.", ""));
        assertEquals("16", result.getRange_end().replaceAll("\\.", ""));
    }

    /**
     * Test of agregateSelectors method, of class RuleAgregator.
     */
    @Test
    public void builds_range_and_cuts_it(){
        try {
            System.out.println("builds_range_and_cuts_it");
            Selector c1 = Selector.parse("[c>260]");
            Selector c2 = Selector.parse("[c<=329]");
            Selector c3 = Selector.parse("[o=none]");
            Selector c4 = Selector.parse("[c<=366]");
            List<Selector> selectors = Arrays.asList(c1,c2,c3,c4);
            RuleAgregator instance = new RuleAgregator();
            List<Selector> result = instance.agregateSelectors(selectors, agregator);
            assertEquals(2, result.size());
            for (Selector sel : result) {
                if (sel.name.equals("c")){
                    assertEquals("261", sel.range_begin.replaceAll("\\.0", ""));
                    assertEquals("329", sel.range_end.replaceAll("\\.0", ""));
                    assertEquals("=", sel.comparator);
                } else {
                    assertEquals("none", sel.getValue().replaceAll("\\.0", ""));
                    assertEquals("=", sel.comparator);
                }
            }
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void builds_range_and_compares_with_another(){
        try {
            System.out.println("builds_range_and_compares_with_another");
            Selector c1 = Selector.parse("[c>260]");
            Selector c2 = Selector.parse("[c<=329]");
            Selector c4 = Selector.parse("[c=300..366]");
            List<Selector> selectors = Arrays.asList(c1,c2,c4);
            RuleAgregator instance = new RuleAgregator();
            List<Selector> result = instance.agregateSelectors(selectors, agregator);
            assertEquals(1, result.size());
            Selector sel = result.get(0);
            assertEquals("300", sel.range_begin.replaceAll("\\.0", ""));
            assertEquals("329", sel.range_end.replaceAll("\\.0", ""));
            assertEquals("=", sel.comparator);
            
            verifySelector(sel, "[c=300..329]");
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }
    
    //[wygrz_izoterm_temp_c<=330.0][wygrz_izoterm_temp_c<=366.0][wygrz_izoterm_temp_c>300.0]
    @Test
    public void bizarre_bug_reproduction(){
        try {
            System.out.println("builds_range_and_compares_with_another");
            Selector c1 = Selector.parse("[c<=330.0]");
            Selector c2 = Selector.parse("[c<=366.0]");
            Selector c4 = Selector.parse("[c>300.0]");
            List<Selector> selectors = Arrays.asList(c1,c2,c4);
            RuleAgregator instance = new RuleAgregator();
            List<Selector> result = instance.agregateSelectors(selectors, agregator);
            assertEquals(1, result.size());
            Selector sel = result.get(0);
            assertEquals("329", sel.range_begin.replaceAll("\\.0", ""));
            assertEquals("330", sel.range_end.replaceAll("\\.0", ""));
            assertEquals("=", sel.comparator);
            
            verifySelector(sel, "[c=329..330]");
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void builds_range_and_forces_exclusion(){
        try {
            System.out.println("builds_range_and_forces_exclusion");
            Selector c1 = Selector.parse("[c>260]");
            Selector c2 = Selector.parse("[c<=329]");
            Selector c4 = Selector.parse("[c>=366]");
            List<Selector> selectors = Arrays.asList(c1,c2,c4);
            RuleAgregator instance = new RuleAgregator();
            try {
                instance.agregateSelectors(selectors, agregator);
            } catch (ExcludingSelectorsException ex) {
                return;
            }
            fail("Runtime exception expected here!");
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void whenExcludingSelectorsThenShouldRemoveHypothesis(){
        try {
            System.out.println("whenExcludingSelectorsThenShouldRemoveHypothesis");
            Selector c1 = Selector.parse("[c>260]");
            Selector c2 = Selector.parse("[c<=300]");
            Selector c3 = Selector.parse("[c>=400]");
            Output input_manual = new Output();
            input_manual.addDomain("c", "continuous", "");
            List<Hypothesis> hypotheses = new LinkedList<Hypothesis>();
            hypotheses.add(new Hypothesis(new Rule(c1), new Rule(c2, c3)));
            input_manual.setOutputHypotheses(hypotheses);
            RuleAgregator instance = new RuleAgregator();
            Output out = instance.agregate(input_manual);
            List<Hypothesis> hypos = out.getOutputHypotheses();
            assertEquals(1, hypos.size());
            List<Rule> rules = hypos.get(0).rules;
            assertEquals(1, rules.size());
            List<Selector> selectors = rules.get(0).getSelectors();
            assertEquals(1, selectors.size());
            Selector lastSel = selectors.get(0);
            verifySelector(lastSel, "[c>260]");
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void when_equality_and_inequality_on_same_value_then_forces_exclusion(){
        try {
            System.out.println("when_equality_and_inequality_on_same_value_then_forces_exclusion");
            Selector c1 = Selector.parse("[c=260]");
            Selector c2 = Selector.parse("[c!=260]");
            List<Selector> selectors = Arrays.asList(c1,c2);
            RuleAgregator instance = new RuleAgregator();
            try {
                instance.agregateSelectors(selectors, agregator);
            } catch (ExcludingSelectorsException ex) {
                return;
            }
            fail("Runtime exception expected here!");
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void when_equality_and_inequality_on_different_value_then_leaves_equality(){
        try {
            System.out.println("when_equality_and_inequality_on_different_value_then_leaves_equality");
            Selector c1 = Selector.parse("[c=260]");
            Selector c2 = Selector.parse("[c!=360]");
            List<Selector> selectors = Arrays.asList(c1,c2);
            RuleAgregator instance = new RuleAgregator();
            List<Selector> result = instance.agregateSelectors(selectors, agregator);
            assertEquals(1, result.size());
            Selector sel = result.get(0);
            assertEquals("260", sel.getValue().replaceAll("\\.0", ""));
            assertEquals("=", sel.comparator);
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }
}
