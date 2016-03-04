/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Parameter;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.RunsGroup;
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
    public void when_consequent_present_should_change_parameter() {
        System.out.println("when_consequent_present_should_change_parameter");
        Input in = new Input();
        in.addAttribute("marker", "numeric", "");
        in.addAttribute("class", "continuous", "");
        in.addEvent("1", "2.3");
        in.addEvent("2", "4.9");
        in.addEvent("3", "5.01");
        in.addEvent("4", "9.9");
        RunsGroup runs = new RunsGroup();
		Run run_c1 = new Run();
		run_c1.setName("Run_c1");
        String inequality = ">=";
        String class_name = "class";
		run_c1.addParameter("Consequent", String.format("[%s%s%s]", class_name, inequality, 5.01));
        runs.runs.add(run_c1);
        in.runsGroup = runs;
        ClassDescriptor cd = new ClassDescriptor(class_name, ">", "5");
        verifyCorrectParameterChange(in, cd, inequality, class_name);
    }

    @Test
    public void when_linear_attr_and_consequent_present_should_change_parameter() {
        System.out.println("when_linear_attr_and_consequent_present_should_change_parameter");
        Input in = new Input();
        in.addAttribute("marker", "numeric", "");
        in.addAttribute("class", "linear", "a, b, c, d, e");
        in.addEvent("1", "a");
        in.addEvent("2", "b");
        in.addEvent("3", "c");
        in.addEvent("4", "d");
        in.addEvent("4", "e");
        RunsGroup runs = new RunsGroup();
		Run run_c1 = new Run();
		run_c1.setName("Run_c1");
        String inequality = ">=";
        String class_name = "class";
		run_c1.addParameter("Consequent", String.format("[%s%s%s]", class_name, inequality, "c"));
        runs.runs.add(run_c1);
        in.runsGroup = runs;
        ClassDescriptor cd = new ClassDescriptor(class_name, ">", "c");
        verifyCorrectParameterChange(in, cd, inequality, class_name);
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

    private void verifyCorrectParameterChange(Input in, ClassDescriptor cd, String inequality, String class_name) {
        ContinuousClassFilter instance = new ContinuousClassFilter();
        Input result = instance.filter(in, cd);
        agh.aq21gui.model.input.Test run_result = result.runsGroup.runs.get(0);
        Parameter desired_paramater = run_result.runSpecificParameters.parameters.get(0);
        assertEquals(1, desired_paramater.getDescriptors().size());
        ClassDescriptor desired_descriptor = desired_paramater.getDescriptors().get(0);
        assertEquals(inequality, desired_descriptor.comparator);
        assertEquals(result.getEvents().get(2).get(class_name), desired_descriptor.getValue());
    }
}
