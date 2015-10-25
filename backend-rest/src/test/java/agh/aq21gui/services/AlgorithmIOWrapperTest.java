/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services;

import agh.aq21gui.filters.DiscretizerTest;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.stubs.StubFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class AlgorithmIOWrapperTest {
    
    public AlgorithmIOWrapperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of inputPreProcessing method, of class AlgorithmIOWrapper.
     */
    @Test
    public void when_discretizer_ranges_set_then_should_discretize() {
        final int size = 20;
        System.out.println("when_discretizer_ranges_set_then_should_discretize");
        String class_name = "Result";
        Input input = StubFactory.generatePolynomialInput(size, class_name);
        Run run = new Run();
        run.addParameter("discretize_ranges", "[Result=25,100,225]");
        input.runsGroup.runs.add(run);
        
        AlgorithmIOWrapper instance = new AlgorithmIOWrapper();
        //System.out.println(input.toString());
        Input result = instance.inputPreProcessing(input);
        //System.out.println(result.toString());
        
        Map<String, Integer> counters = new HashMap<String, Integer>();
        List<String> items = DiscretizerTest.performMeasure(input, result, size, class_name, counters);
        
        assertEquals((long)5, (long)counters.get(items.get(0)));
        assertEquals((long)5, (long)counters.get(items.get(5)));
        assertEquals((long)5, (long)counters.get(items.get(10)));
        assertEquals((long)5, (long)counters.get(items.get(15)));
    }
    
    @Test
    public void when_remove_params_set_then_should_remove_collumn() {
        System.out.println("when_remove_params_set_then_should_remove_collumn");
        Input input = StubFactory.getInput();
        input.runsGroup.addParameter("ignore_attributes", "[anything=number,length]");
        AlgorithmIOWrapper instance = new AlgorithmIOWrapper();
        Input result = instance.inputPreProcessing(input);
        assertEquals(2, result.getAttributes().size());
        for (Map<String, Object> event: result.getEvents()) {
            //It contains also generated identifier
            assertEquals(3, event.size());
        }
    }
}
