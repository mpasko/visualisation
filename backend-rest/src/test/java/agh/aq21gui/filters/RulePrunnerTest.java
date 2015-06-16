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
import java.util.LinkedList;
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
public class RulePrunnerTest {
    
    public RulePrunnerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void when_simple_prunnable_output_then_cut_1selector() {
        System.out.println("when_simple_prunnable_output_then_cut_1selector");
        Output out = generatePrunnableOutput();
        RulePrunner instance = new RulePrunner();
        Output result = instance.prune(out);
        
        List<Hypothesis> hypotheses = result.getOutputHypotheses();
        assertEquals(1, hypotheses.size());
        List<Rule> rules = hypotheses.get(0).rules;
        assertEquals(1, rules.size());
        List<Selector> selectors = rules.get(0).getSelectors();
        assertEquals(2, selectors.size());
        for (Selector selector : selectors) {
            boolean case1 = selector.toString().equalsIgnoreCase("[x=a]");
            boolean case2 = selector.toString().equalsIgnoreCase("[z=e]");
            assertTrue(case1||case2);
        }
    }

    private static Output generatePrunnableOutput() {
        try {
            Output out = new Output();
            out.addAttribute("x", "nominal", "{a, b}");
            out.addAttribute("y", "nominal", "{c, d}");
            out.addAttribute("z", "nominal", "{e, f}");
            out.addAttribute("class", "nominal", "{t, f}");
            out.addEvent("a", "c", "e", "t");
            out.addEvent("a", "c", "f", "f");
            out.addEvent("b", "c", "e", "f");
            out.addEvent("b", "c", "f", "f");
            out.addEvent("a", "d", "e", "t");
            out.addEvent("a", "d", "f", "f");
            out.addEvent("b", "d", "e", "f");
            out.addEvent("b", "d", "f", "f");
            List<Hypothesis> hypotheses = new LinkedList<Hypothesis>();
            Selector xa = Selector.parse("[x=a]");
            Selector xb = Selector.parse("[x=b]");
            Selector yc = Selector.parse("[y=c]");
            Selector yd = Selector.parse("[y=d]");
            Selector ze = Selector.parse("[z=e]");
            Selector zf = Selector.parse("[z=f]");
            Hypothesis hypothesis = new Hypothesis(new Rule(xa, yc, ze));
            hypothesis.addClass(ClassDescriptor.parse("[class=t]"));
            hypotheses.add(hypothesis);
            out.setOutputHypotheses(hypotheses);
            return out;
        } catch (RecognitionException ex) {
            Logger.getLogger(RulePrunnerTest.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
