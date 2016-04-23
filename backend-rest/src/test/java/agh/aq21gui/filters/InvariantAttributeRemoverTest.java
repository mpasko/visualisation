/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import java.util.Map;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class InvariantAttributeRemoverTest {
    
    public InvariantAttributeRemoverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of filter method, of class InvariantAttributeRemover.
     */
    @Test
    public void testFilterRemovesAttributes() {
        System.out.println("testFilterRemovesAttributes");
        Input in = new Input();
        in.addAttribute("not_rem", "nominal", null);
        in.addAttribute("rem", "nominal", null);
        in.addAttribute("class", "nominal", null);
        in.addEvent("a","a","t");
        in.addEvent("b","a","f");
        in.addEvent("c","a","f");
        in.addEvent("d","a","t");
        InvariantAttributeRemover instance = new InvariantAttributeRemover();
        Output result = instance.filter(in);
        assertEquals(2, result.gAG().attributes.size());
        for (Map<String, Object> event: result.getEvents()) {
            assertFalse(event.containsKey("rem"));
        }
    }
    
    @Test
    public void testFilterRemovesDomainsToo() {
        System.out.println("testFilterRemovesDomainsToo");
        Input in = new Input();
        in.addDomain("domain1", "nominal", null);
        in.addDomain("domain2", "nominal", null);
        in.addDomain("domain3", "nominal", null);
        in.addAttribute("not_rem", "domain1", null);
        in.addAttribute("rem", "domain2", null);
        in.addAttribute("class", "domain3", null);
        in.addEvent("a","a","t");
        in.addEvent("b","a","f");
        in.addEvent("c","a","f");
        in.addEvent("d","a","t");
        InvariantAttributeRemover instance = new InvariantAttributeRemover();
        Output result = instance.filter(in);
        assertEquals(2, result.getDomains().size());
        for (Domain domain: result.getDomains()) {
            assertNotSame("domain2", domain);
        }
    }
    
    @Test
    public void testFilterRemovesDomainsInHierarchy() {
        System.out.println("testFilterRemovesDomainsInHierarchy");
        Input in = new Input();
        in.addDomain("parent1", "nominal", null);
        in.addDomain("parent2", "nominal", null);
        in.addDomain("parent3", "nominal", null);
        in.addDomain("domain1", "parent1", null);
        in.addDomain("domain2", "parent1", null);
        in.addDomain("domain3", "parent3", null);
        in.addDomain("domain4", "parent2", null);
        in.addAttribute("not_rem", "domain1", null);
        in.addAttribute("rem", "domain2", null);
        in.addAttribute("class", "domain3", null);
        in.addEvent("a","a","t");
        in.addEvent("b","a","f");
        in.addEvent("c","a","f");
        in.addEvent("d","a","t");
        InvariantAttributeRemover instance = new InvariantAttributeRemover();
        Output result = instance.filter(in);
        assertEquals(4, result.getDomains().size());
        for (Domain domain: result.getDomains()) {
            assertNotSame("domain2", domain);
            assertNotSame("parent2", domain);
        }
    }
}
