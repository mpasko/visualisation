/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import java.util.Arrays;
import org.antlr.runtime.RecognitionException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class RehumaniserTest {
    
    public RehumaniserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of rehumaniseOutput method, of class Rehumaniser.
     */
    @Test
    public void testRehumaniseOutput() throws RecognitionException {
        System.out.println("rehumaniseOutput");
        Output out = new Output();
        out.addEvent("1dot0");
        Rehumaniser instance = new Rehumaniser();
        Rule rule = new Rule();
        rule.addSelector(Selector.parse("[frac_toughness=above_equal_54dot0_and_less_59dot0]"));
        Hypothesis hypo = new Hypothesis(rule);
        hypo.name = "1dot0";
        out.setOutputHypotheses(Arrays.asList(hypo));
        Output result = instance.rehumaniseOutput(out);
        String expected = "54.0..59.0";
        Hypothesis expectedHypothesis = result.getOutputHypotheses().get(0);
        assertEquals("1.0", expectedHypothesis.name);
        Rule expectedRule = expectedHypothesis.rules.get(0);
        Selector expectedSelector = expectedRule.getSelectors().get(0);
        assertEquals(expected, expectedSelector.getValue());
        String expectedEvent = result.obtainEventsGroup().events.get(0).getValues().get(0);
        assertEquals("1.0", expectedEvent);
    }

    /**
     * Test of rehumaniseString method, of class Rehumaniser.
     */
    @Test
    public void testRehumaniseString() {
        System.out.println("rehumaniseString");
        verifyString("123.005", "123dot005");
        verifyString("820.0..970.0", "above_equal_820dot0_and_less_970dot0");
        verifyString(">=820.0", "above_equal_820dot0");
        verifyString("<970.0", "less_970dot0");
        verifyString(">820.0", "above_820dot0");
        verifyString("<=970.0", "less_equal_970dot0");
    }
    
    @Test
    public void testRehumaniseSelector() throws RecognitionException {
        System.out.println("testRehumaniseSelector");
        String value = "above_equal_820dot0_and_less_970dot0..above_equal_1140dot0_and_less_1350dot0";
        verifySelector("=", "820.0..1350.0", Selector.parse(String.format("[name=%s]", value)));
        value = "above_equal_820dot0_and_less_970dot0";
        verifySelector("=", "820.0..970.0", Selector.parse(String.format("[name=%s]", value)));
        value = "above_equal_820dot0_and_less_970dot0..above_equal_1140dot0";
        verifySelector(">=", "820.0", Selector.parse(String.format("[name=%s]", value)));
    }

    /**
     * Test of rehumaniseRule method, of class Rehumaniser.
     */
    @Test
    public void testRehumaniseRule() {
        System.out.println("rehumaniseRule");
        String value = "above_equal_820dot0_and_less_970dot0..above_equal_1140dot0_and_less_1350dot0";
        Rule rule = new Rule(new Selector("wytrzym_rozciag_mpa", "=", value));
        Rehumaniser instance = new Rehumaniser();
        instance.rehumaniseRule(rule);
        String expected = "820.0..1350.0";
        assertEquals(expected, rule.getSelectors().get(0).getValue());
    }
    
    @Test
    public void test_when_nonequality_then_update_comparator() {
        System.out.println("test_when_nonequality_then_update_comparator");
        String value = "less_1350dot0";
        Rule rule = new Rule(new Selector("wytrzym_rozciag_mpa", "=", value));
        Rehumaniser instance = new Rehumaniser();
        instance.rehumaniseRule(rule);
        Selector expectedSelector = rule.getSelectors().get(0);
        assertEquals("1350.0", expectedSelector.getValue());
        assertEquals("<", expectedSelector.comparator);
    }

    /**
     * Test of rehumaniseHypothesis method, of class Rehumaniser.
     */
    @Test
    public void testRehumaniseHypothesis() throws RecognitionException {
        System.out.println("rehumaniseHypothesis");
        Rule rule = new Rule();
        rule.addSelector(Selector.parse("[frac_toughness=above_equal_54dot0_and_less_59dot0]"));
        Hypothesis hypo = new Hypothesis(rule);
        hypo.name = "1dot0";
        hypo.addClass(Selector.parse("[frac_toughness=less_1350dot0]"));
        Rehumaniser instance = new Rehumaniser();
        instance.rehumaniseHypothesis(hypo);
        assertEquals("1.0", hypo.name);
        String expected = "54.0..59.0";
        assertEquals(expected, rule.getSelectors().get(0).getValue());
        ClassDescriptor expectedClass = hypo.getClasses().get(0);
        assertEquals("<", expectedClass.comparator);
        assertEquals("1350.0", expectedClass.getValue());
    }

    private void verifyString(String expResult, String name) {
        Rehumaniser instance = new Rehumaniser();
        String result = instance.rehumaniseString(name);
        assertEquals(expResult, result);
    }

    private void verifySelector(String expComparator, String expected, Selector selector) {
        Rehumaniser instance = new Rehumaniser();
        instance.rehumaniseSelector(selector);
        assertEquals(expected, selector.getValue());
        assertEquals(expComparator, selector.getComparator());
    }
}
