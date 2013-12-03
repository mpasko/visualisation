/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

import agh.aq21gui.model.gld.Value;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class RangeRecognizerTest {
	
	public RangeRecognizerTest() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of accept method, of class RangeRecognizer.
	 */
	@Test
	public void testAccept_Value() {
		System.out.println("accept");
	//	Value v = new Value("a");
		Attribute attr = new Attribute();
		DomainsGroup dg = new DomainsGroup();
		attr.setname("a");
		attr.setdomain("continuous");
		attr.setRange(Util.strings("1","2","3"));
		Selector s = new Selector();
		s.name = "a";
		s.comparator = ">=";
		s.setValue("2");
		ContinuousElement left = new ContinuousElement("3", ">=");
		RangeElement right = new ContinuousElement("5", "<");
		RangeRecognizer instance = new RangeRecognizer(left,right);
		boolean expResult = true;
		boolean result = instance.accept(s,attr,dg);
		assertEquals(expResult, result);
	}

	/**
	 * Test of accept method, of class RangeRecognizer.
	 */
	@Test
	public void testAccept_3args() {
		System.out.println("accept_3args");
		Selector s = new Selector();
		s.name = "x";
		s.comparator = "=";
		s.setValue("a");
		Attribute attr = new Attribute();
		DomainsGroup dg = new DomainsGroup();
		attr.setname("x");
		attr.setdomain("nominal");
		attr.setparameters("{a, b}");
		RangeRecognizer instance = new RangeRecognizer("a");
		boolean expResult = true;
		boolean result = instance.accept(s, attr, dg);
		assertEquals(expResult, result);
	}
	
	@Test
	public void testAccept_range() {
		System.out.println("accept_range");
		Selector s = new Selector();
		s.name = "x";
		s.comparator = "=";
		s.setRange_begin("1.5");
		s.setRange_end("4.5");
		Attribute attr = new Attribute();
		DomainsGroup dg = new DomainsGroup();
		attr.setname("x");
		attr.setdomain("continuous");
		attr.setparameters("{1.0, 4.5}");
		ContinuousElement left = new ContinuousElement("2", ">");
		ContinuousElement right = new ContinuousElement("3.5", "<");
		RangeRecognizer instance = new RangeRecognizer(left, right);
		boolean expResult = true;
		boolean result = instance.accept(s, attr, dg);
		assertEquals(expResult, result);
	}
}
