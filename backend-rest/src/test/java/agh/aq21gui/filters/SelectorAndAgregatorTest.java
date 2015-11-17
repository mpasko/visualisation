/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.filters.selectoragregator.ExcludingSelectorsException;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Selector;
import java.util.Arrays;
import java.util.List;
import org.antlr.runtime.RecognitionException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class SelectorAndAgregatorTest {
    
    public SelectorAndAgregatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testAgregateTwoSelsEqualityNonequality() {
        System.out.println("testAgregateTwoSelsEqualityNonequality...");
        Input in = new Input();
        in.addDomain("c", "continuous", "");
        testTwoSelectorsAndExpectOther(in, "[c=260]", "[c!=360]", "[c=260]");
        testTwoSelectorsAndExpectExclusion(in, "[c=260]", "[c!=260]");
    }
    
    @Test
    public void testAgregateTwoSelsLessEqual() {
        System.out.println("testAgregateTwoSelsEqualityNonequality...");
        Input in = new Input();
        in.addDomain("c", "continuous", "");
        testTwoSelectorsAndExpectOther(in, "[c>260]", "[c<=360]", "[c=260..360]");
        testTwoSelectorsAndExpectExclusion(in, "[c<=260]", "[c>360]");
    }
    
    @Test
    public void testAgregateTwoSelsLessEqualSameDirection() {
        System.out.println("testAgregateTwoSelsEqualityNonequality...");
        Input in = new Input();
        in.addDomain("c", "continuous", "");
        testTwoSelectorsAndExpectOther(in, "[c>260]", "[c>=360]", "[c>=360]");
        testTwoSelectorsAndExpectOther(in, "[c<260]", "[c<=360]", "[c<260]");
    }
    
    @Test
    public void testAgregateTwoSelsEqualityNonequalityLitaralValues() {
        System.out.println("testAgregateTwoSelsEqualityNonequality...");
        Input in = new Input();
        in.addDomain("c", "nominal", "aa, bb");
        testTwoSelectorsAndExpectOther(in, "[c=aa]", "[c!=bb]", "[c=aa]");
        testTwoSelectorsAndExpectExclusion(in, "[c=aa]", "[c!=aa]");
    }
    
    @Test
    public void testAgregateTwoSelsTwoSets() {
        System.out.println("testAgregateTwoSelsTwoSets...");
        Input in = new Input();
        in.addDomain("c", "nominal", "a1, a2, a3, a4");
        testTwoSelectorsAndExpectOther(in, "[c=a1,a2,a3]", "[c=a2,a3,a4]", "[c=a2,a3]");
        testTwoSelectorsAndExpectExclusion(in, "[c=a1,a2]", "[c=a3,a4]");
    }
    
    
    @Test
    public void testAgregateTwoSelsLessEqualForLinear() {
        System.out.println("testAgregateTwoSelsLessEqualForLinear...");
        Input in = new Input();
        in.addAttribute("c", "c_domain", "");
        in.addDomain("c_domain", "linear", "a, b");
        testTwoSelectorsAndExpectOther(in, "[c>a]", "[c<=b]", "[c=a..b]");
        testTwoSelectorsAndExpectExclusion(in, "[c<=a]", "[c>b]");
    }
    
    @Test
    public void testAgregateTwoSelsLessEqualSameDirectionForLinear() {
        System.out.println("testAgregateTwoSelsLessEqualSameDirectionForLinear...");
        Input in = new Input();
        in.addAttribute("c", "c_domain", "");
        in.addDomain("c_domain", "linear", "a, b");
        testTwoSelectorsAndExpectOther(in, "[c>a]", "[c>=b]", "[c>=b]");
        testTwoSelectorsAndExpectOther(in, "[c<a]", "[c<=b]", "[c<a]");
    }
    
    public static void testTwoSelectorsAndExpectExclusion(Input in, String sel1, String sel2){
        try {
            System.out.println(String.format("testing: %s, %s, expecting exclusion", sel1, sel2));
            Selector c1 = Selector.parse(sel1);
            Selector c2 = Selector.parse(sel2);
            List<Selector> selectors = Arrays.asList(c1,c2);
            RuleAgregator instance = new RuleAgregator();
            try {
                instance.agregateSelectors(selectors, new SelectorAndAgregator(in));
            } catch (ExcludingSelectorsException ex) {
                return;
            }
            fail("Runtime exception expected here!");
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }
    
    public static void testTwoSelectorsAndExpectOther(Input in, String sel1, String sel2, String expected){
        try {
            System.out.println(String.format("testing: %s, %s, expecting: %s", sel1, sel2, expected));
            Selector c1 = Selector.parse(sel1);
            Selector c2 = Selector.parse(sel2);
            Selector cexp = Selector.parse(expected);
            List<Selector> selectors = Arrays.asList(c1,c2);
            RuleAgregator instance = new RuleAgregator();
            List<Selector> result = instance.agregateSelectors(selectors, new SelectorAndAgregator(in));
            assertEquals(1, result.size());
            Selector sel = result.get(0);
            verifySelector(sel, cexp);
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }

    public static void verifySelector(Selector sel, Selector cexp) {
        assertEquals(cexp.getValue().replaceAll("\\.0", ""), sel.getValue().replaceAll("\\.0", ""));
        assertEquals(cexp.comparator, sel.comparator);
        assertEquals(cexp.name, sel.name);
    }

    public static void verifySelector(Selector sel, String sexp) throws RecognitionException {
        Selector cexp = Selector.parse(sexp);
        assertEquals(cexp.getValue().replaceAll("\\.0", ""), sel.getValue().replaceAll("\\.0", ""));
        assertEquals(cexp.comparator, sel.comparator);
        assertEquals(cexp.name, sel.name);
    }
}
