/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.evaluator;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import java.util.Arrays;
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
public class ClassifierTest {
    private static Input xorInput = null;
    private static Hypothesis trueness = null;
    private static Hypothesis falseness = null;
    private static Hypothesis wrongX = null;
    private static Hypothesis wrongY = null;
    private static Input numericInput = null;
    private static Hypothesis num_trueness;
    private static Hypothesis num_falseness;
    private static Hypothesis num_wrongY;
    private static Hypothesis num_wrongX;
    
    @BeforeClass
    public static void setUpClass() {
        xorInput = generateXorSampleInput();
        numericInput = generateNumericInput();
        try {
            setupXorRules();
            setupKartesianRules();            
        } catch (RecognitionException ex) {
            Logger.getLogger(ClassifierTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Input generateXorSampleInput() {
        Input input = new Input();
        input.addAttribute("X", "nominal", "{a, b}");
        input.addAttribute("Y", "nominal", "{c, d}");
        input.addAttribute("Class", "nominal", "{T, F}");
        input.addEvent("a", "c", "T");
        input.addEvent("a", "d", "F");
        input.addEvent("b", "c", "F");
        input.addEvent("b", "d", "T");
        return input;
    }
    
    public static Input generateNumericInput() {
        Input input = new Input();
        input.addAttribute("X", "continuous", "{0.0, 1.0}");
        input.addAttribute("Y", "continuous", "{0.0, 1.0}");
        input.addAttribute("Class", "nominal", "{T, F}");
        input.addEvent("0.25", "0.25", "T");
        input.addEvent("0.25", "0.75", "F");
        input.addEvent("0.75", "0.25", "F");
        input.addEvent("0.75", "0.75", "T");
        return input;
    }

    public static void setupXorRules() throws RecognitionException {
        Rule only_a = new Rule(Selector.parse("X=a"));
        Rule only_d = new Rule(Selector.parse("Y=d"));
        Rule a_and_c = new Rule(Selector.parse("X=a"),Selector.parse("Y=c"));
        Rule b_and_d = new Rule(Selector.parse("X=b"),Selector.parse("Y=d"));
        Rule b_and_c = new Rule(Selector.parse("X=b"),Selector.parse("Y=c"));
        Rule a_and_d = new Rule(Selector.parse("X=a"),Selector.parse("Y=d"));
        trueness = new Hypothesis(a_and_c, b_and_d);
        trueness.setClasses(Arrays.asList(new ClassDescriptor("Class", "=", "T")));
        falseness = new Hypothesis(b_and_c, a_and_d);
        falseness.setClasses(Arrays.asList(ClassDescriptor.parse("[Class=F]")));
        wrongX = new Hypothesis(only_a);
        wrongX.setClasses(Arrays.asList(ClassDescriptor.parse("Class=T")));
        wrongY = new Hypothesis(only_d);
        wrongY.setClasses(Arrays.asList(ClassDescriptor.parse("Class=F")));
    }

    public static void setupKartesianRules() throws RecognitionException {
        Rule x_more = new Rule(Selector.parse("X>=0.5"));
        Rule y_more = new Rule(Selector.parse("Y>0.5"));
        Rule only_big = new Rule(Selector.parse("X>=0.5"),Selector.parse("Y>0.5"));
        Rule only_small = new Rule(Selector.parse("X<0.5"),Selector.parse("Y<=0.5"));
        Rule small_big = new Rule(Selector.parse("X<=0.5"),Selector.parse("Y>0.5"));
        num_trueness = new Hypothesis(only_big, only_small);
        num_trueness.setClasses(Arrays.asList(new ClassDescriptor("Class", "=", "T")));
        num_falseness = new Hypothesis(small_big);
        num_falseness.setClasses(Arrays.asList(ClassDescriptor.parse("[Class=F]")));
        num_wrongX = new Hypothesis(x_more);
        num_wrongX.setClasses(Arrays.asList(ClassDescriptor.parse("Class=T")));
        num_wrongY = new Hypothesis(y_more);
        num_wrongY.setClasses(Arrays.asList(ClassDescriptor.parse("Class=F")));
    }
    
    @Before
    public void setUp() {
    }

    //TODO below:
    /*
    @Test
    public void testPerformStatistics_Collection() {
        System.out.println("performStatistics");
        Collection<Hypothesis> hypos = null;
        Classifier instance = null;
        StatsAgregator expResult = null;
        StatsAgregator result = instance.performStatistics(hypos);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */

    @Test
    public void when_correct_hypothesis_then_true_positive() {
        System.out.println("when_correct_hypothesis_then_true_positive");
        Classifier instance = new Classifier(xorInput);
        Statistics result = instance.performStatistics(trueness);
        assertEquals(2, result.getTruePositive());
        assertEquals(2, result.getTrueNegative());
    }
    
    @Test
    public void when_correct_hypothesis_then_true_negative() {
        System.out.println("when_true_hypothesis_then_true_outcome2");
        Classifier instance = new Classifier(xorInput);
        Statistics result = instance.performStatistics(falseness);
        assertEquals(2, result.getTruePositive());
        assertEquals(2, result.getTrueNegative());
    }

    @Test
    public void when_correct_numeric_hypothesis_then_true_positive() {
        System.out.println("when_true_numeric_hypothesis_then_true_outcome1");
        Classifier instance = new Classifier(numericInput);
        Statistics result = instance.performStatistics(num_trueness);
        assertEquals(2, result.getTruePositive());
        assertEquals(2, result.getTrueNegative());
    }

    @Test
    public void when_missing_selector_then_false_negative() {
        System.out.println("when_missing_selector_then_false_negative");
        Classifier instance = new Classifier(numericInput);
        Statistics result = instance.performStatistics(num_falseness);
        assertEquals(1, result.getTruePositive());
        assertEquals(2, result.getTrueNegative());
        assertEquals(1, result.getFalseNegative());
    }

    @Test
    public void when_incorrect_hypothesis_then_false_outcome1() {
        System.out.println("when_incorrect_hypothesis_then_false_outcome1");
        Classifier instance = new Classifier(xorInput);
        Statistics result = instance.performStatistics(wrongX);
        assertEquals(1, result.getTruePositive());
        assertEquals(1, result.getTrueNegative());
        assertEquals(1, result.getFalsePositive());
        assertEquals(1, result.getFalseNegative());
    }

    @Test
    public void when_incorrect_hypothesis_then_false_outcome2() {
        System.out.println("when_incorrect_hypothesis_then_false_outcome2");
        Classifier instance = new Classifier(xorInput);
        Statistics result = instance.performStatistics(wrongY);
        assertEquals(1, result.getTruePositive());
        assertEquals(1, result.getTrueNegative());
        assertEquals(1, result.getFalsePositive());
        assertEquals(1, result.getFalseNegative());
    }

    @Test
    public void when_incorrect_numeric_hypothesis_then_false_outcome1() {
        System.out.println("when_incorrect_numeric_hypothesis_then_false_outcome1");
        Classifier instance = new Classifier(numericInput);
        Statistics result = instance.performStatistics(num_wrongX);
        assertEquals(1, result.getTruePositive());
        assertEquals(1, result.getTrueNegative());
        assertEquals(1, result.getFalsePositive());
        assertEquals(1, result.getFalseNegative());
    }

    @Test
    public void when_incorrect_numeric_hypothesis_then_false_outcome2() {
        System.out.println("when_incorrect_numeric_hypothesis_then_false_outcome2");
        Classifier instance = new Classifier(numericInput);
        Statistics result = instance.performStatistics(num_wrongY);
        assertEquals(1, result.getTruePositive());
        assertEquals(1, result.getTrueNegative());
        assertEquals(1, result.getFalsePositive());
        assertEquals(1, result.getFalseNegative());
    }
}
