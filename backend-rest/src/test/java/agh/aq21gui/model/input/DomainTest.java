/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.stubs.Utils;
import agh.aq21gui.utils.Util;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class DomainTest {
	
	public DomainTest() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}


	/**
	 * Test of getparameters method, of class Domain.
	 */
	@Test
	public void testGetparametersBrace() {
		System.out.println("getparameters_bra");
		Domain instance = new Domain();
		instance.setRange(Util.strings("ala","ma"));
		instance.setdomain("nominal");
		String expResult = "{ala, ma}";
		String result = instance.getparameters();
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of getparameters method, of class Domain.
	 */
	@Test
	public void testGetparametersNonbrace() {
		System.out.println("getparameters_nonbra");
		Domain instance = new Domain();
		instance.setRange(Util.strings("ala","ma"));
		instance.setdomain("continuous");
		String expResult = "ala, ma";
		String result = instance.getparameters();
		assertEquals(expResult, result);
	}

	/**
	 * Test of setparameters method, of class Domain.
	 */
	@Test
	public void testSetparameters() {
		System.out.println("setparameters");
		String name = "ala, ma";
		Domain instance = new Domain();
		instance.setdomain("nominal");
		instance.setparameters(name);
		String expResult = "{ala, ma}";
		String result = instance.getparameters();
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of setparameters method, of class Domain.
	 */
	@Test
	public void testSetparametersEmptyString() {
		System.out.println("setparameters_emptyString");
		String params = "";
		Domain instance = new Domain();
		instance.setdomain("nominal");
		instance.setparameters(params);
		String expResult = "";
		String result = instance.getparameters();
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of setparameters method, of class Domain.
	 */
	@Test
	public void testSetparameters_toString() {
		System.out.println("setparameters_toString");
		String params = "{ala, ma}";
		Domain instance = new Domain();
		instance.setname("x");
		instance.setdomain("nominal");
		instance.setparameters(params);
		String expResult = "x nominal {ala, ma}\n";
		String result = instance.toString();
		assertEquals(expResult, result);
	}

	/**
	 * Test of toString method, of class Domain.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		Domain instance = new Domain();
		instance.setname("x");
		instance.setdomain("y");
		String expResult = "x y\n";
		String result = instance.toString();
		assertEquals(true, instance.emptyParams());
		assertEquals("y", instance.getdomain());
		assertEquals(expResult, result);
	}

	/**
	 * Test of setRange method, of class Domain.
	 */
	@Test
	public void testSetRange_Double_Double() {
		System.out.println("setRange_Double_Double");
		Double min = 1.0;
		Double max = 2.0;
		Domain instance = new Domain();
		instance.setRange(min, max);
		String expResult = "{1.0, 2.0}";
		String result = instance.getparameters();
		assertEquals(expResult, result);
	}

	/**
	 * Test of setRange method, of class Domain.
	 */
	@Test
	public void testSetRange_List() {
		System.out.println("setRange_List");
		List<String> values = Util.strings("1","2","3");
		Domain instance = new Domain();
		instance.setRange(values);
		String expResult = "{1, 2, 3}";
		String result = instance.getparameters();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getRange method, of class Domain.
	 */
	@Test
	public void testGetRange() {
		System.out.println("getRange");
		Domain instance = new Domain();
		instance.setRange(Util.strings("1","2","3"));
		List<String> result = instance.getRange();
		boolean containsAll = true;
		for (String item : Util.strings("1","2","3")) {
			boolean contained = false;
			for (String res : result) {
				if (res.equalsIgnoreCase(item)) {
					contained = true;
				}
			}
			if (contained == false) {
				containsAll = false;
			}
		}
		assertTrue(containsAll);
	}
	
	@Test
	public void getDomainRecursiveTest() {
		System.out.println("getRecursive");
		Attribute attr = new Attribute("x","length");
		DomainsGroup dg = new DomainsGroup();
        dg.addDomain("length","continuous");
		String result = attr.getdomainNameRecursively(dg);
		assertEquals("continuous", result);
	}
    
    @Test
	public void getDomainRecursiveTest3Steps() {
		System.out.println("getRecursive");
		Attribute attr = new Attribute("x","dom1");
		DomainsGroup dg = new DomainsGroup();
        dg.addDomain("dom1","dom2");
        dg.addDomain("dom2","dom3");
        dg.addDomain("dom3","continuous");
		String result = attr.getdomainNameRecursively(dg);
		assertEquals("continuous", result);
	}
    
    @Test(timeout=1000)
	public void getDomainRecursiveStopCondition() {
		System.out.println("getRecursive");
		Attribute attr = new Attribute("x","length");
		DomainsGroup dg = new DomainsGroup();
        dg.addDomain("length","length");
		String result = attr.getdomainNameRecursively(dg);
		assertEquals("length", result);
	}

    @Test
    public void testGetRangeRecursively() {
        System.out.println("getRangeRecursively");
        Domain instance = new Attribute("x","length");
		DomainsGroup dg = new DomainsGroup();
        dg.addDomain("length", "continuous", "{a, b, c}");
        List expResult = null;
        List result = instance.getRangeRecursively(dg);
        assertTrue(Utils.containsAllStrings(result, "a","b","c"));
    }

    @Test
    public void testGetRangeRecursively3Steps() {
        System.out.println("getRangeRecursively");
		DomainsGroup dg = new DomainsGroup();
        Domain instance = new Attribute("x","length");
        dg.addDomain("length", "dom1");
        dg.addDomain("dom1", "dom2");
        dg.addDomain("dom2", "continuous","{a, b, c}");
        List result = instance.getRangeRecursively(dg);
        assertTrue(Utils.containsAllStrings(result, "a","b","c"));
    }

    @Test(timeout=1000)
    public void testGetRangeRecursivelyStopCondition() {
        System.out.println("getRangeRecursively");
		DomainsGroup dg = new DomainsGroup();
        dg.addDomain("length", "length","{a, b, c}");
        Domain instance = new Attribute("x","length");
        List result = instance.getRangeRecursively(dg);
        assertTrue(Utils.containsAllStrings(result, "a","b","c"));
    }

}
