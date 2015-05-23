/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.utils.TreeNode;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class DomainsGroupTest {
    
    public DomainsGroupTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testFindDomain() {
        System.out.println("findDomain");
        String name = "g";
        DomainsGroup instance = generateSampleDomainTree();
        Domain result = instance.findDomain(name);
        assertEquals(name, result.name);
    }

    @Test
    public void testAddDomain_String_String() {
        System.out.println("addDomain");
        String domainName = "costam";
        String value = "continuous";
        DomainsGroup instance = new DomainsGroup();
        instance.addDomain(domainName, value);
        assertEquals(1, instance.domains.size());
    }

    @Test
    public void when_different_object_should_find_it() {
        System.out.println("when_different_object_should_find_it");
        DomainsGroup instance = generateSampleDomainTree();
        Domain domain = instance.domains.get(instance.domains.size()-1);
        Domain expResult = instance.domains.get(0);
        Domain result = instance.getdomainObjectRecursively(domain);
        assertEquals(expResult, result);
    }
    
    @Test
    public void when_same_object_should_return_it() {
        System.out.println("when_same_object_should_return_it");
        DomainsGroup instance = generateSampleDomainTree();
        Domain domain = instance.domains.get(0);
        Domain expResult = instance.domains.get(0);
        Domain result = instance.getdomainObjectRecursively(domain);
        assertEquals(expResult, result);
    }

    @Test
    public void testFindDomainObjectRecursively() {
        System.out.println("findDomainObjectRecursively");
        DomainsGroup instance = generateSampleDomainTree();
        String currentName = "g";
        Domain expResult = instance.domains.get(0);
        Domain result = instance.findDomainObjectRecursively(currentName);
        assertEquals(expResult, result);
    }

    public DomainsGroup generateSampleDomainTree() {
        DomainsGroup instance = new DomainsGroup();
        instance.addDomain("a", "continuous");
        instance.addDomain("b", "integer");
        instance.addDomain("c", "nominal");
        instance.addDomain("d", "linear");
        instance.addDomain("e", "a");
        instance.addDomain("f", "a");
        instance.addDomain("g", "e");
        instance.addDomain("h", "g");
        return instance;
    }
}
