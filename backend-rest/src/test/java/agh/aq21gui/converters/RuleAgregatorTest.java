/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.converters;

import agh.aq21gui.filters.RuleAgregator;
import agh.aq21gui.model.output.Selector;
import java.util.LinkedList;
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
public class RuleAgregatorTest {
    
    public RuleAgregatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    /*
    @Test
    public void testAgregate() {
        System.out.println("agregate");
        Output result_2 = null;
        RuleAgregator instance = new RuleAgregator();
        Output expResult = null;
        Output result = instance.agregate(result_2);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    */

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
        List<Selector> result = instance.agregateSelectors(selectors);
        assertEquals(3, result.size());
        for (Selector sel : result) {
            if (sel.name.equalsIgnoreCase("a")) {
                assertEquals("1.0", sel.getRange_begin());
                assertEquals("17.0", sel.getRange_end());
            } else if (sel.name.equalsIgnoreCase("b")) {
                assertEquals("2.0", sel.getRange_begin());
                assertEquals("20.0", sel.getRange_end());
            } else if (sel.name.equalsIgnoreCase("c")) {
                assertEquals("10", sel.getValue());
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
        RuleAgregator instance = new RuleAgregator();
        Selector result = instance.agregateTwoSels(next, actual);
        assertEquals("1.0", result.getRange_begin());
        assertEquals("17.0", result.getRange_end());
    }
}
