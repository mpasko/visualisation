/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.utils.Util;
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
public class ClassDescriptorMatcherTest {
    
    public ClassDescriptorMatcherTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of matchesValue method, of class ClassDescriptor.
     */
    @Test
    public void shouldMatchContinuousValue() {
        System.out.println("shouldMatchContinuousValue");
        shouldMatch("a=0.4", "0.4", null);
        shouldMatch("a>=0.4", "0.4", null);
        shouldMatch("a>=0.4", "0.5", null);
        shouldMatch("a>0.4", "0.5", null);
        shouldMatch("a<=0.4", "0.4", null);
        shouldMatch("a<=0.4", "0.3", null);
        shouldMatch("a<0.4", "0.3", null);
    }
    
    @Test
    public void shouldNotMatchContinuousValue() {
        System.out.println("shouldNotMatchContinuousValue");
        shouldNotMatch("a!=0.4", "0.4", null);
        shouldNotMatch("a>0.4", "0.4", null);
        shouldNotMatch("a>0.4", "0.3", null);
        shouldNotMatch("a>=0.4", "0.3", null);
        shouldNotMatch("a<0.4", "0.4", null);
        shouldNotMatch("a<0.4", "0.5", null);
        shouldNotMatch("a<=0.4", "0.5", null);
    }
    
    @Test
    public void shouldMatchLinearValue() {
        System.out.println("shouldMatchLinearValue");
        List<String> order = Util.strings("a0","a1","a2","a3","a4");
        shouldMatch("a=a2", "a2", order);
        shouldMatch("a>=a2", "a2", order);
        shouldMatch("a>=a2", "a3", order);
        shouldMatch("a>a2", "a3", order);
        shouldMatch("a<=a2", "a2", order);
        shouldMatch("a<=a2", "a1", order);
        shouldMatch("a<a2", "a1", order);
    }
    
    @Test
    public void shouldNotMatchLinearValue() {
        System.out.println("shouldNotMatchLinearValue");
        List<String> order = Util.strings("a0","a1","a2","a3","a4");
        shouldNotMatch("a!=a2", "a2", order);
        shouldNotMatch("a>a2", "a2", order);
        shouldNotMatch("a>a2", "a1", order);
        shouldNotMatch("a>=a2", "a1", order);
        shouldNotMatch("a<a2", "a2", order);
        shouldNotMatch("a<a2", "a3", order);
        shouldNotMatch("a<=a2", "a3", order);
    }
    
    @Test
    public void shouldMatchOnlyInsideContinuousRange() {
        System.out.println("shouldMatchOnlyInsideContinuousRange");
        shouldMatch("a=0.4..0.6", "0.5", null);
        shouldMatch("a=0.4..0.6", "0.4", null);
        shouldMatch("a=0.4..0.6", "0.6", null);
        shouldNotMatch("a=0.4..0.6", "0.3", null);
        shouldNotMatch("a=0.4..0.6", "0.7", null);
    }
    
    @Test
    public void shouldMatchOnlyOutsideContinuousRange() {
        System.out.println("shouldMatchOnlyOutsideContinuousRange");
        shouldNotMatch("a!=0.4..0.6", "0.5", null);
        shouldNotMatch("a!=0.4..0.6", "0.4", null);
        shouldNotMatch("a!=0.4..0.6", "0.6", null);
        shouldMatch("a!=0.4..0.6", "0.3", null);
        shouldMatch("a!=0.4..0.6", "0.7", null);
    }
    
    @Test
    public void shouldMatchOnlyInsideLinearRange() {
        System.out.println("shouldMatchOnlyInsideLinearRange");
        List<String> order = Util.strings("a2","a3","a4","a5","a6","a7");
        shouldMatch("a=a4..a6", "a5", order);
        shouldMatch("a=a4..a6", "a4", order);
        shouldMatch("a=a4..a6", "a6", order);
        shouldNotMatch("a=a4..a6", "a3", order);
        shouldNotMatch("a=a4..a6", "a7", order);
    }
    
    @Test
    public void shouldMatchOnlyOutsideLinearRange() {
        System.out.println("shouldMatchOnlyOutsideLinearRange");
        List<String> order = Util.strings("a2","a3","a4","a5","a6","a7");
        shouldNotMatch("a!=a4..a6", "a5", order);
        shouldNotMatch("a!=a4..a6", "a4", order);
        shouldNotMatch("a!=a4..a6", "a6", order);
        shouldMatch("a!=a4..a6", "a3", order);
        shouldMatch("a!=a4..a6", "a7", order);
    }
    
    @Test
    public void shouldMatchOnlyInSetStrings() {
        System.out.println("shouldMatchOnlyInSetStrings");
        shouldMatch("a=a4,a5,a6", "a4", null);
        shouldMatch("a=a4,a5,a6", "a5", null);
        shouldMatch("a=a4,a5,a6", "a6", null);
        shouldNotMatch("a=a4,a5,a6", "a1", null);
    }
    
    @Test
    public void shouldMatchOnlyInSetNumeric() {
        System.out.println("shouldMatchOnlyInSetStrings");
        shouldMatch("a=4,5,6", "4", null);
        shouldMatch("a=4,5,6", "5", null);
        shouldMatch("a=4,5,6", "6", null);
        shouldNotMatch("a=4,5,6", "1", null);
    }
    
    @Test
    public void shouldNotMatchOnlyInSetStrings() {
        System.out.println("shouldNotMatchOnlyInSetStrings");
        shouldNotMatch("a!=a4,a5,a6", "a4", null);
        shouldNotMatch("a!=a4,a5,a6", "a5", null);
        shouldNotMatch("a!=a4,a5,a6", "a6", null);
        shouldMatch("a!=a4,a5,a6", "a1", null);
    }
    
    @Test
    public void shouldNotMatchOnlyInSetNumeric() {
        System.out.println("shouldNotMatchOnlyInSetNumeric");
        shouldNotMatch("a!=4,5,6", "4", null);
        shouldNotMatch("a!=4,5,6", "5", null);
        shouldNotMatch("a!=4,5,6", "6", null);
        shouldMatch("a!=4,5,6", "1", null);
    }
    
    @Test
    public void shouldMatchMixedSelectorLinear() {
        System.out.println("shouldMatchMixedSelectorLinear");
        List<String> order = Util.strings("a2","a3","a4","a5","a6","a7");
        shouldMatch("a=a2,a4..a6", "a2", order);
        shouldMatch("a=a2,a4..a6", "a4", order);
        shouldMatch("a=a2,a4..a6", "a5", order);
        shouldMatch("a=a2,a4..a6", "a6", order);
        shouldNotMatch("a=a2,a4..a6", "a3", order);
        shouldNotMatch("a=a2,a4..a6", "a7", order);
    }
    
    @Test
    public void shouldMatchMixedSelectorContinuous() {
        System.out.println("shouldMatchMixedSelectorContinuous");
        shouldMatch("a=0.2,0.4..0.6", "0.2", null);
        shouldMatch("a=0.2,0.4..0.6", "0.4", null);
        shouldMatch("a=0.2,0.4..0.6", "0.5", null);
        shouldMatch("a=0.2,0.4..0.6", "0.6", null);
        shouldNotMatch("a=0.2,0.4..0.6", "0.3", null);
        shouldNotMatch("a=0.2,0.4..0.6", "0.7", null);
    }
    
    @Test
    public void shouldNotMatchMixedSelectorLinear() {
        System.out.println("shouldNotMatchMixedSelectorLinear");
        List<String> order = Util.strings("a2","a3","a4","a5","a6","a7");
        shouldNotMatch("a!=a2,a4..a6", "a2", order);
        shouldNotMatch("a!=a2,a4..a6", "a4", order);
        shouldNotMatch("a!=a2,a4..a6", "a5", order);
        shouldNotMatch("a!=a2,a4..a6", "a6", order);
        shouldMatch("a!=a2,a4..a6", "a3", order);
        shouldMatch("a!=a2,a4..a6", "a7", order);
    }
    
    @Test
    public void shouldNotMatchMixedSelectorContinuous() {
        System.out.println("shouldNotMatchMixedSelectorContinuous");
        shouldNotMatch("a!=0.2,0.4..0.6", "0.2", null);
        shouldNotMatch("a!=0.2,0.4..0.6", "0.4", null);
        shouldNotMatch("a!=0.2,0.4..0.6", "0.5", null);
        shouldNotMatch("a!=0.2,0.4..0.6", "0.6", null);
        shouldMatch("a!=0.2,0.4..0.6", "0.3", null);
        shouldMatch("a!=0.2,0.4..0.6", "0.7", null);
    }

    public static void shouldMatch(ClassDescriptor instance, String actualValue, List<String> linearOrder) {
        boolean result = instance.matchesValue(actualValue, linearOrder);
        assertEquals(true, result);
    }

    public static void shouldMatch(String descriptor, String actualValue, List<String> linearOrder) {
        try {
            ClassDescriptor instance = ClassDescriptor.parse(descriptor);
            shouldMatch(instance, actualValue, linearOrder);
        } catch (RecognitionException ex) {
            fail("Parsing exception: "+descriptor);
        }
    }

    public static void shouldNotMatch(ClassDescriptor instance, String actualValue, List<String> linearOrder) {
        boolean result = instance.matchesValue(actualValue, linearOrder);
        assertEquals(false, result);
    }

    public static void shouldNotMatch(String descriptor, String actualValue, List<String> linearOrder) {
        try {
            ClassDescriptor instance = ClassDescriptor.parse(descriptor);
            shouldNotMatch(instance, actualValue, linearOrder);
        } catch (RecognitionException ex) {
            fail("Parsing exception: "+descriptor);
        }
    }
}
