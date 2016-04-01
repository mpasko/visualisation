/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Input;
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
public class IllegalValueRemoverTest {
    public static final String ATTR1 = "attr1";
    public static final String ATTR2 = "attr2";
    public static final String ATTR3 = "attr3";
    public static final String Q = "?";
    
    public IllegalValueRemoverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of remove method, of class IllegalValueRemover.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        Input in = new Input();
        in.addAttribute(ATTR1, "nominal", "");
        in.addAttribute(ATTR2, "nominal", "");
        in.addAttribute(ATTR3, "nominal", "");
        in.addEvent("a", "b", "c");
        in.addEvent(Q, "b", "c");
        in.addEvent("a", Q, "c");
        in.addEvent("a", "b", Q);
        in.addEvent(Q, Q, "c");
        in.addEvent("a", Q, Q);
        in.addEvent(Q, "b", Q);
        in.addEvent(Q, Q, Q);
        IllegalValueRemover instance = new IllegalValueRemover();
        Input result = instance.remove(in, ATTR1, Q);
        List<Map<String, Object>> events = result.getEvents();
        assertEquals(4, events.size());
        int b_questions = 0;
        int c_questions = 0;
        for (Map<String, Object> event : events) {
            assertEquals("a", event.get(ATTR1));
            if (event.get(ATTR2).equals(Q)) {
                ++b_questions;
            }
            if (event.get(ATTR3).equals(Q)) {
                ++c_questions;
            }
        }
        assertEquals(2, b_questions); //attr1==a && attr2==? -> 1/4 of all
        assertEquals(2, c_questions); //attr1==a && attr3==? -> 1/4 of all
    }
}
