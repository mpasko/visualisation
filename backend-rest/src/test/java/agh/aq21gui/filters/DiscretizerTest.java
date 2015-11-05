/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.filters.Discretizer.Mode;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.stubs.StubFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class DiscretizerTest {
    public static final String CLASS_NAME = "class";
    
    public DiscretizerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void when_histogram_then_should_divide_range() {
        System.out.println("when_histogram_then_should_divide_range");
        int size = 17;
        Input in = StubFactory.generatePolynomialInput(size, CLASS_NAME);
        Discretizer instance = new Discretizer();
        Input result = instance.discretize(in, CLASS_NAME, "4", Mode.HISTOGRAM);
        
        Map<String, Integer> counters = new HashMap<String, Integer>();
        List<String> items = performMeasure(in, result, size, CLASS_NAME, counters);
        //System.out.println();
        assertEquals((long)8, (long)counters.get(items.get(0)));
        assertEquals((long)4, (long)counters.get(items.get(10)));
        assertEquals((long)2, (long)counters.get(items.get(13)));
        assertEquals((long)3, (long)counters.get(items.get(16)));
    }
    
    @Test
    public void when_similar_size_then_should_divide_by_values() {
        System.out.println("when_similar_size_then_should_divide_by_values");
        int size = 17;
        Input in = StubFactory.generatePolynomialInput(size, CLASS_NAME);
        Discretizer instance = new Discretizer();
        Input result = instance.discretize(in, CLASS_NAME, "square root", Mode.SIMILAR_SIZE);
        
        Map<String, Integer> counters = new HashMap<String, Integer>();
        List<String> items = performMeasure(in, result, size, CLASS_NAME, counters);
        //System.out.println();
        assertEquals(4.5, (double)counters.get(items.get(1)), 0.51);
        assertEquals(4.5, (double)counters.get(items.get(5)), 0.51);
        assertEquals(4.5, (double)counters.get(items.get(10)), 0.51);
        assertEquals(4.5, (double)counters.get(items.get(14)), 0.51);
    }
    
    @Test
    public void when_similar_size_then_should_correctly_divide_small_sets() {
        System.out.println("when_similar_size_then_should_correctly_divide_small_sets");
        int size = 4;
        Input in = StubFactory.generatePolynomialInput(size, CLASS_NAME);
        Discretizer instance = new Discretizer();
        Input result = instance.discretize(in, CLASS_NAME, "square root", Mode.SIMILAR_SIZE);
        
        Map<String, Integer> counters = new HashMap<String, Integer>();
        List<String> items = performMeasure(in, result, size, CLASS_NAME, counters);
        //System.out.println();
        assertEquals((long)2, (long)counters.get(items.get(0)));
        assertEquals((long)2, (long)counters.get(items.get(3)));
    }
    
    @Test
    public void when_similar_size_then_should_correctly_divide_small_sets2() {
        System.out.println("when_similar_size_then_should_correctly_divide_small_sets2");
        int size = 6;
        Input in = StubFactory.generatePolynomialInput(size, CLASS_NAME);
        Discretizer instance = new Discretizer();
        Input result = instance.discretize(in, CLASS_NAME, "3", Mode.SIMILAR_SIZE);
        
        Map<String, Integer> counters = new HashMap<String, Integer>();
        List<String> items = performMeasure(in, result, size, CLASS_NAME, counters);
        //System.out.println();
        assertEquals((long)2, (long)counters.get(items.get(0)));
        assertEquals((long)2, (long)counters.get(items.get(2)));
        assertEquals((long)2, (long)counters.get(items.get(4)));
    }

    @Test
    public void testGetBucketsNumberFromString() {
        System.out.println("getBucketsNumberFromString");
        String buckets_number = "square root";
        int size = 16;
        int expResult = 4;
        int result = Discretizer.getBucketsNumberFromString(buckets_number, size);
        assertEquals(expResult, result);
    }
    
    public static void countValues(Input result, int size, String clazz, Map<String, Integer> counters) {
        String key = clazz.toLowerCase(Locale.US);
        for (int i = 0; i < size; ++i){
            String stringValue = (String)result.obtainCell(i, key);
            if (!counters.containsKey(stringValue)) {
                counters.put(stringValue, 1);
            } else {
                counters.put(stringValue, counters.get(stringValue)+1);
            }
        }
    }

    public static List<String> performMeasure(Input in, Input result, int size, String class_name, Map<String, Integer> counters) {
        List<String> items = result.getCollumnOfData(class_name);
        countValues(result, size, class_name, counters);
        for (int i = 0; i < size; ++i){
            String originalValue = (String)in.obtainCell(i, class_name);
            String stringValue = (String)result.obtainCell(i, class_name);
            /*
            System.out.print(originalValue);
            System.out.print(" in ");
            System.out.print(stringValue);
            System.out.println();
            */
            assertNotSame(originalValue, stringValue);
        }
        return items;
    }
}
