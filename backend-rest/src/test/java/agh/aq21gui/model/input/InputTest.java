/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.Util;
import java.util.LinkedList;
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
public class InputTest {
    private static Input iris_output;
    
    public InputTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        iris_output = StubFactory.getIrisOutput();
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testSerializeJsonAll() {
        System.out.println("testSerializeJsonAll");
        String result = Util.objectToJson(iris_output);
        assertTrue(result.length()>1);
    }
    
    @Test
    public void testSerializeJsonOutputHypotheses() {
        System.out.println("testSerializeJsonOutputHypotheses");
        String result = Util.objectToJson(iris_output.getOutputHypotheses());
        assertTrue(result.length()>1);
    }
    

    @Test
    public void testSerializeJsonAttributes() {
        System.out.println("testSerializeJsonAttributes");
        String result = Util.objectToJson(iris_output.getAttributes());
        assertTrue(result.length()>1);
    }

    @Test
    public void testSerializeJsonDomains() {
        System.out.println("testSerializeJsonDomains");
        String result = Util.objectToJson(iris_output.getDomains());
        assertTrue(result.length()>1);
    }

    @Test
    public void testSerializeJsonEvents() {
        System.out.println("testSerializeJsonEvents");
        String result = Util.objectToJson(iris_output.getEvents());
        assertTrue(result.length()>1);
    }

    @Test
    public void testSerializeJsonRunsGroup() {
        System.out.println("testSerializeJsonRunsGroup");
        String result = Util.objectToJson(iris_output.getRunsGroup());
        assertTrue(result.length()>1);
    }
}
