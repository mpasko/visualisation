/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.RecognitionException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class HypothesisNegationTest {
    public final Selector sel2;
    public final Selector sel1;
    public final Selector sel3;
    public final Selector sel4;
    public final Selector sel5;
    public final Selector sel6;
    
    public HypothesisNegationTest() {
        sel1 = new Selector("a", ">", "3.5");
        sel2 = new Selector("b", "<=", "1.0");
        sel3 = new Selector("c", ">=", "2.0");
        sel4 = new Selector("d", "<", "1.5");
        sel5 = new Selector("e", "<=", "2.5");
        sel6 = new Selector("f", ">=", "3.0");
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    // not( a and b ) == ( not a ) or ( not b )
    @Test
    public void testNegateHypothesisSimple() {
        System.out.println("testNegateHypothesisSimple");
        Rule rule1 = new Rule(sel1, sel2);
        Hypothesis hypo = new Hypothesis(rule1);
        HypothesisNegation instance = new HypothesisNegation();
        Hypothesis result = instance.negateHypothesis(hypo);
        //System.out.println(result.toString());
        assertEquals(2, result.rules.size());
        for (Rule negated : result.rules){
            assertEquals(1, negated.getSelectors().size());
            Selector sel = negated.getSelectors().get(0);
            if (sel.getName().equals("a")){
                assertEquals("<=", sel.getComparator());
                assertEquals("3.5", sel.getValue());
            } else if (sel.getName().equals("b")){
                assertEquals(">", sel.getComparator());
                assertEquals("1.0", sel.getValue());
            } else {
                fail(String.format("Unexpected selector: %s", sel.toString()));
            }
        }
    }

    // not ( a  or  b ) == ( not a ) and ( not b )
    @Test
    public void testNegateHypothesisSimple2() {
        System.out.println("testNegateHypothesisSimple2");
        Rule rule1 = new Rule(sel1);
        Rule rule2 = new Rule(sel2);
        Hypothesis hypo = new Hypothesis(rule1, rule2);
        HypothesisNegation instance = new HypothesisNegation();
        Hypothesis result = instance.negateHypothesis(hypo);
        //System.out.println(result.toString());
        assertEquals(1, result.rules.size());
        for (Selector sel : result.rules.get(0).getSelectors()) {
            if (sel.getName().equals("a")){
                assertEquals("<=", sel.getComparator());
                assertEquals("3.5", sel.getValue());
            } else if (sel.getName().equals("b")){
                assertEquals(">", sel.getComparator());
                assertEquals("1.0", sel.getValue());
            } else {
                fail(String.format("Unexpected selector: %s", sel.toString()));
            }
        }
    }
    
    // not( (a and b) or (c and d) ) == ( na and nc) or (na and nd) or ( nb and nc ) or (nb and nd)
    @Test
    public void testNegateHypothesisComplex2x2() {
        System.out.println("testNegateHypothesisComplex2x2");
        Rule rule1 = new Rule(sel1, sel2);
        Rule rule2 = new Rule(sel3, sel4);
        Hypothesis hypo = new Hypothesis(rule1, rule2);
        HypothesisNegation instance = new HypothesisNegation();
        Hypothesis result = instance.negateHypothesis(hypo);
        //System.out.println(result.toString());
        assertEquals(4, result.rules.size());
        for (Rule multiplication : result.rules){
            assertEquals(2, multiplication.getSelectors().size());
        }
    }    

    // not( (a and b) or (c and d) or (e and f) ) == ( na and nc and  nd) or ( ... ) ... or (...)
    @Test
    public void testNegateHypothesisComplex2x2x2() {
        System.out.println("testNegateHypothesisComplex2x2x2");
        Rule rule1 = new Rule(sel1, sel2);
        Rule rule2 = new Rule(sel3, sel4);
        Rule rule3 = new Rule(sel5, sel6);
        Hypothesis hypo = new Hypothesis(rule1, rule2, rule3);
        HypothesisNegation instance = new HypothesisNegation();
        Hypothesis result = instance.negateHypothesis(hypo);
        //System.out.println(result.toString());
        assertEquals(8, result.rules.size());
        for (Rule multiplication : result.rules){
            assertEquals(3, multiplication.getSelectors().size());
        }
    }    

    // not( a and b ) == ( not a ) or ( not b )
    @Test
    public void testNegateRule() {
        System.out.println("negateRule");
        Rule rule = new Rule(sel1, sel2);
        HypothesisNegation instance = new HypothesisNegation();
        List<Rule> result = instance.negateRule(rule);
        assertEquals(2, result.size());
        for (Rule negated : result){
            assertEquals(1, negated.getSelectors().size());
            Selector sel = negated.getSelectors().get(0);
            if (sel.getName().equals("a")){
                assertEquals("<=", sel.getComparator());
                assertEquals("3.5", sel.getValue());
            } else if (sel.getName().equals("b")){
                assertEquals(">", sel.getComparator());
                assertEquals("1.0", sel.getValue());
            } else {
                fail(String.format("Unexpected selector: %s", sel.toString()));
            }
        }
    }

    @Test
    public void testNegateSelector() {
        System.out.println("negateSelector");
        HypothesisNegation instance = new HypothesisNegation();
        List<Selector> result = instance.negateSelector(sel1);
        assertEquals(1, result.size());
        assertEquals("<=", result.get(0).getComparator());
        assertEquals("3.5", result.get(0).getValue());
    }
    
    
    @Test
    public void testNegateJrippSelector() {
        System.out.println("negateSelector");
        HypothesisNegation instance = new HypothesisNegation();
        instance.setAddQuestions(true);
        List<Selector> result = instance.negateSelector(sel1);
        assertEquals(2, result.size());
        assertEquals("<=", result.get(0).getComparator());
        assertEquals("3.5", result.get(0).getValue());
        assertEquals("=", result.get(1).getComparator());
        assertEquals("?", result.get(1).getValue());
    }

    @Test
    public void when_range_then_two_resultset() {
        try {
            System.out.println("when_range_then_two_resultset");
            HypothesisNegation instance = new HypothesisNegation();
            List<Selector> result = instance.negateSelector(Selector.parse("a=1..5"));
            assertEquals(2, result.size());
            Selector left = result.get(0);
            Selector right = result.get(1);
            assertEquals("<", left.getComparator());
            assertEquals("1", left.getValue());
            assertEquals(">", right.getComparator());
            assertEquals("5", right.getValue());
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }
}
