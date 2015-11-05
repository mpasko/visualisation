/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.utils.TreeNode;
import agh.aq21gui.utils.Util;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import testtools.Utils;

/**
 *
 * @author marcin
 */
public class ClassDescriptorTest {
    
    public ClassDescriptorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void constructor3p_test() {
        ClassDescriptor instance = new ClassDescriptor("name", "=", "value");
        assertEquals("name", instance.name);
        assertEquals("=", instance.comparator);
        assertEquals("value", instance.getValue());
    }
    
    /**
     * Test of setValue method, of class ClassDescriptor.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        String val = "a,b,c";
        ClassDescriptor instance = new ClassDescriptor();
        instance.setName("x");
        instance.setValue(val);
        Utils.assertAllContained(instance.getSet_elements(), "a", "b", "c");
    }

    /**
     * Test of getValue method, of class ClassDescriptor.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        ClassDescriptor instance = new ClassDescriptor();
        instance.set_elements.addAll(Util.strings("a1", "a2", "a3"));
        instance.range_begin = "a4";
        instance.range_end = "a10";
        String result = instance.getValue();
        assertEquals("a1,a2,a3,a4..a10", result);
    }

    /**
     * Test of toString method, of class ClassDescriptor.
     */
    @Test
    public void testToString() {
        System.out.println("setValue");
        ClassDescriptor instance = new ClassDescriptor();
        instance.setName("x");
        instance.setComparator("=");
        instance.setSet_elements(Util.strings("a","b","c"));
        assertEquals("[x=a,b,c]", instance.toString());
    }

    /**
     * Test of parse method, of class ClassDescriptor.
     */
    @Test
    public void testParse() throws Exception {
        System.out.println("parse");
        String string = "[x=a,b,c..z]";
        ClassDescriptor result = ClassDescriptor.parse(string);
        assertEquals("x", result.name);
        assertEquals("=", result.comparator);
        assertEquals("c", result.range_begin);
        assertEquals("z", result.range_end);
        Utils.assertAllContained(result.getSet_elements(), "a", "b");
    }
}
