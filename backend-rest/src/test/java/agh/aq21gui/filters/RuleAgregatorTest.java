/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Selector;
import java.util.Arrays;
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
public class RuleAgregatorTest {
    
    public RuleAgregatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
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
            List<Selector> result = instance.agregateSelectors(selectors);
            assertEquals(2, result.size());
            for (Selector sel : result) {
                if (sel.name.equals("c")){
                    assertEquals("260.0", sel.range_begin);
                    assertEquals("329.0", sel.range_end);
                    assertEquals("=", sel.comparator);
                } else {
                    assertEquals("none", sel.getValue());
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
            List<Selector> result = instance.agregateSelectors(selectors);
            assertEquals(1, result.size());
            Selector sel = result.get(0);
            assertEquals("300.0", sel.range_begin);
            assertEquals("329.0", sel.range_end);
            assertEquals("=", sel.comparator);
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
                instance.agregateSelectors(selectors);
            } catch (RuntimeException ex) {
                assertTrue(ex.getMessage().contains("Excluding"));
                return;
            }
            fail("Runtime exception expected here!");
        } catch (RecognitionException ex) {
            fail(ex.getMessage());
        }
    }
}
