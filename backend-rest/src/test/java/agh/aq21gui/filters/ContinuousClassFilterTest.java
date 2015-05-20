/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.stubs.Utils;
import agh.aq21gui.utils.Util;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class ContinuousClassFilterTest {
    
    public ContinuousClassFilterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void when_single_value_should_divide_into_two_separate_parts() {
        System.out.println("when_single_value_should_divide_into_two_separate_parts");
        Input in = new Input();
        in.addAttribute("marker", "numeric", "");
        in.addAttribute("class", "continuous", "");
        in.addEvent("1", "2.3");
        in.addEvent("2", "4.9");
        in.addEvent("3", "5.01");
        in.addEvent("4", "9.9");
        ClassDescriptor cd = new ClassDescriptor("class", ">", "5");
        ContinuousClassFilter instance = new ContinuousClassFilter();
        Input result = instance.filter(in, cd);
        String item_1 = (String)result.getEvents().get(0).get("class");
        String item_2 = (String)result.getEvents().get(1).get("class");
        String item_3 = (String)result.getEvents().get(2).get("class");
        String item_4 = (String)result.getEvents().get(3).get("class");
        assertFalse(item_1.equalsIgnoreCase(item_3));
        assertFalse(item_2.equalsIgnoreCase(item_4));
        
        assertTrue(item_1.equalsIgnoreCase(item_2));
        assertTrue(item_3.equalsIgnoreCase(item_4));
    }
    
    @Test
    public void when_two_values_should_divide_into_three_separate_parts() {
        System.out.println("when_two_values_should_divide_into_three_separate_parts");
        Input in = new Input();
        in.addAttribute("marker", "numeric", "");
        in.addAttribute("class", "continuous", "");
        in.addEvent("0", "-10.0");
        in.addEvent("1", "2.3");
        in.addEvent("2", "4.9");
        in.addEvent("3", "5.0");
        in.addEvent("4", "5.01");
        in.addEvent("5", "9.9");
        ClassDescriptor cd = new ClassDescriptor("class", "=", "5");
        cd.setSet_elements(Util.strings("3","5.01"));
        ContinuousClassFilter instance = new ContinuousClassFilter();
        Input result = instance.filter(in, cd);
        
        List<String> items = new ArrayList<String>(10);
        for (int i = 0; i < 6; ++i){
            items.add((String)result.getEvents().get(i).get("class"));
        }
        //Same set
        assertTrue(items.get(0).equalsIgnoreCase(items.get(1)));
        assertTrue(items.get(2).equalsIgnoreCase(items.get(3)));
        assertTrue(items.get(4).equalsIgnoreCase(items.get(5)));
        
        //Neighbours
        assertFalse(items.get(1).equalsIgnoreCase(items.get(2)));
        assertFalse(items.get(3).equalsIgnoreCase(items.get(4)));
        
        //Some custom
        assertFalse(items.get(0).equalsIgnoreCase(items.get(5)));
        assertFalse(items.get(0).equalsIgnoreCase(items.get(2)));
        assertFalse(items.get(1).equalsIgnoreCase(items.get(4)));
    }

    @Test
    public void testDetermineWhichRangeMatches() {
        System.out.println("determineWhichRangeMatches");
        double a = -10.0;
        double b = 5.0;
        double c = 5.01;
        double d = 5.02;
        List<String> set_elements = Util.strings("5","5.01");
        assertEquals(0, ContinuousClassFilter.determineWhichRangeMatches(a, set_elements));
        assertEquals(1, ContinuousClassFilter.determineWhichRangeMatches(b, set_elements));
        assertEquals(2, ContinuousClassFilter.determineWhichRangeMatches(c, set_elements));
        assertEquals(2, ContinuousClassFilter.determineWhichRangeMatches(d, set_elements));
    }
}
