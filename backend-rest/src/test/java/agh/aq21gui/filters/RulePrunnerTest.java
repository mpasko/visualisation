/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.evaluator.Classifier;
import agh.aq21gui.evaluator.Statistics;
import agh.aq21gui.model.input.Event;
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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class RulePrunnerTest {

    private static Selector xa;
    private static Selector xb;
    private static Selector yc;
    private static Selector yd;
    private static Selector ze;
    private static Selector zf;
    private static ClassDescriptor classT;
    private static ClassDescriptor classF;

    public RulePrunnerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            xa = Selector.parse("[x=a]");
            xb = Selector.parse("[x=b]");
            yc = Selector.parse("[y=c]");
            yd = Selector.parse("[y=d]");
            ze = Selector.parse("[z=e]");
            zf = Selector.parse("[z=f]");
            classT = ClassDescriptor.parse("[class=t]");
            classF = ClassDescriptor.parse("[class=f]");
        } catch (RecognitionException ex) {
            Logger.getLogger(RulePrunnerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        verifyPrunnableResult(hypotheses);
    }

    @Test
    public void when_simple_prunnable_last_output_then_cut_last_selector() {
        System.out.println("when_simple_prunnable_last_output_then_cut_last_selector");
        Output out = generatePrunnableLastOutput();
        RulePrunner instance = new RulePrunner();
        Output result = instance.prune(out);

        List<Hypothesis> hypotheses = result.getOutputHypotheses();
        verifyPrunnableLastResult(hypotheses);
    }

    @Test
    public void when_simple_prunnabletwice_output_then_cut_2selectors() {
        System.out.println("when_simple_prunnabletwice_output_then_cut_2selectors");
        Output out = generateTwicePrunnableOutput();
        RulePrunner instance = new RulePrunner();
        Output result = instance.prune(out);

        List<Hypothesis> hypotheses = result.getOutputHypotheses();
        verifyTwicePrunnableResult(hypotheses);
    }

    @Test
    public void when_simple_nonprunnable_output_then_do_nothing() {
        System.out.println("when_simple_nonprunnable_output_then_do_nothing");
        Output out = generateNonPrunnableOutput();
        RulePrunner instance = new RulePrunner();
        Output result = instance.prune(out);

        List<Hypothesis> hypotheses = result.getOutputHypotheses();
        List<Hypothesis> originalHypos = out.getOutputHypotheses();
        assertEquals(originalHypos.size(), hypotheses.size());
        for (int i = 0; i < hypotheses.size(); ++i) {
            assertEquals(originalHypos.get(i), hypotheses.get(i));
        }
    }
    
    @Test
    public void when_sortable_then_sort() {
        System.out.println("when_sortable_then_sort");
        Output out = generateIfElseOutput();
        RulePrunner instance = new RulePrunner();
        Output result = instance.sortByAccuracyAndApplyElse(out);
        System.out.println(result.toString());
        List<Hypothesis> hypotheses = result.getOutputHypotheses();
        assertEquals(2, hypotheses.size());
        Hypothesis best = hypotheses.get(0);
        Hypothesis worse = hypotheses.get(1);
        Classifier classifier = new Classifier(out);
        assertEquals("f", best.getClasses().get(0).getValue());
        assertEquals("t", worse.getClasses().get(0).getValue()); //sorted
        assertEquals(1, best.getRules().size());
        assertEquals(2, worse.getRules().size()); //else applied
        for (Event e : out.obtainEventsGroup().events) {
            Statistics best_stats = new Statistics();
            classifier.analyzeEvent(e, best, best_stats);
            Statistics worse_stats = new Statistics();
            classifier.analyzeEvent(e, worse, worse_stats);
            assertEquals(0, worse_stats.getFalsePositive());
            assertEquals(0, best_stats.getFalsePositive());
            boolean best_applied = best_stats.getTruePositive()==1;
            boolean worse_applied = worse_stats.getTruePositive()==1;
            boolean if_best_then_not_worse = !(best_applied && worse_applied);
            assertTrue(if_best_then_not_worse);
        }
    }

    public static Output generatePrunnableOutput() {
        Output out = generateOutputWithDefaultAttributes();
        out.addEvent("a", "c", "e", "t");
        out.addEvent("a", "c", "f", "f");
        out.addEvent("b", "c", "e", "f");
        out.addEvent("b", "c", "f", "f");
        out.addEvent("a", "d", "e", "t");
        out.addEvent("a", "d", "f", "f");
        out.addEvent("b", "d", "e", "f");
        out.addEvent("b", "d", "f", "f");
        return addDefaultHypothesis(out);
    }

    public static Output generatePrunnableLastOutput() {
        Output out = generateOutputWithDefaultAttributes();
        out.addEvent("a", "c", "e", "t");
        out.addEvent("a", "c", "f", "t");
        out.addEvent("b", "c", "e", "f");
        out.addEvent("b", "c", "f", "f");
        out.addEvent("a", "d", "e", "f");
        out.addEvent("a", "d", "f", "f");
        out.addEvent("b", "d", "e", "f");
        out.addEvent("b", "d", "f", "f");
        return addDefaultHypothesis(out);
    }

    public static Output generateTwicePrunnableOutput() {
        Output out = generateOutputWithDefaultAttributes();
        out.addEvent("a", "c", "e", "t");
        out.addEvent("a", "c", "f", "t");
        out.addEvent("b", "c", "e", "f");
        out.addEvent("b", "c", "f", "f");
        out.addEvent("a", "d", "e", "t");
        out.addEvent("a", "d", "f", "t");
        out.addEvent("b", "d", "e", "f");
        out.addEvent("b", "d", "f", "f");
        return addDefaultHypothesis(out);
    }

    public static Output generateNonPrunnableOutput() {
        Output out = generateOutputWithDefaultAttributes();
        out.addEvent("a", "c", "e", "t");
        out.addEvent("a", "c", "f", "f");
        out.addEvent("b", "c", "e", "f");
        out.addEvent("b", "c", "f", "f");
        out.addEvent("a", "d", "e", "f");
        out.addEvent("a", "d", "f", "f");
        out.addEvent("b", "d", "e", "f");
        out.addEvent("b", "d", "f", "f");
        return addDefaultHypothesis(out);
    }

    private static Output addDefaultHypothesis(Output out) {
        List<Hypothesis> hypotheses = new LinkedList<Hypothesis>();
        Hypothesis hypothesis = new Hypothesis(new Rule(xa, yc, ze));
        hypothesis.addClass(classT);
        hypotheses.add(hypothesis);
        out.setOutputHypotheses(hypotheses);
        return out;
    }

    public static Output generateIfElseOutput() {
        Output out = generateOutputWithDefaultAttributes();
        out.addEvent("a", "c", "e", "t");
        out.addEvent("a", "c", "f", "t");
        out.addEvent("b", "c", "e", "f");
        out.addEvent("b", "c", "f", "t");
        out.addEvent("a", "d", "e", "f");
        out.addEvent("a", "d", "f", "f");
        out.addEvent("b", "d", "e", "f");
        out.addEvent("b", "d", "f", "f");
        List<Hypothesis> hypotheses = new LinkedList<Hypothesis>();
        Hypothesis best_hypothesis = new Hypothesis(new Rule(xb, ze));
        best_hypothesis.addClass(classF);
        Hypothesis worse_hypothesis = new Hypothesis(new Rule(yc));
        worse_hypothesis.addClass(classT);
        hypotheses.add(worse_hypothesis);
        hypotheses.add(best_hypothesis);
        out.setOutputHypotheses(hypotheses);
        return out;
    }

    private static Output generateOutputWithDefaultAttributes() {
        Output out = new Output();
        out.addAttribute("x", "nominal", "{a, b}");
        out.addAttribute("y", "nominal", "{c, d}");
        out.addAttribute("z", "nominal", "{e, f}");
        out.addAttribute("class", "nominal", "{t, f}");
        return out;
    }

    public static void verifyPrunnableResult(List<Hypothesis> hypotheses) {
        assertEquals(1, hypotheses.size());
        List<Rule> rules = hypotheses.get(0).rules;
        assertEquals(1, rules.size());
        List<Selector> selectors = rules.get(0).getSelectors();
        assertEquals(2, selectors.size());
        for (Selector selector : selectors) {
            boolean case1 = selector.toString().equalsIgnoreCase("[x=a]");
            boolean case2 = selector.toString().equalsIgnoreCase("[z=e]");
            boolean impossible = selector.toString().equalsIgnoreCase("[y=c]");
            assertTrue((case1 || case2) && !impossible);
        }
    }

    public static void verifyPrunnableLastResult(List<Hypothesis> hypotheses) {
        assertEquals(1, hypotheses.size());
        List<Rule> rules = hypotheses.get(0).rules;
        assertEquals(1, rules.size());
        List<Selector> selectors = rules.get(0).getSelectors();
        assertEquals(2, selectors.size());
        for (Selector selector : selectors) {
            boolean case1 = selector.toString().equalsIgnoreCase("[x=a]");
            boolean case2 = selector.toString().equalsIgnoreCase("[y=c]");
            boolean impossible = selector.toString().equalsIgnoreCase("[z=e]");
            assertTrue((case1 || case2) && !impossible);
        }
    }

    public static void verifyTwicePrunnableResult(List<Hypothesis> hypotheses) {
        assertEquals(1, hypotheses.size());
        List<Rule> rules = hypotheses.get(0).rules;
        assertEquals(1, rules.size());
        List<Selector> selectors = rules.get(0).getSelectors();
        assertEquals(1, selectors.size());
        assertEquals("[x=a]", selectors.get(0).toString());
    }
}
