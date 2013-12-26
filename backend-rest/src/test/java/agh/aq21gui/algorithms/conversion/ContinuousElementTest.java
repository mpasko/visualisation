/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class ContinuousElementTest {
	
	public ContinuousElementTest() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of minus method, of class ContinuousElement.
	 */
	@Test
	public void testMinus() {
		System.out.println("minus");
		RangeElement other = new ContinuousElement("2", "=");
		ContinuousElement instance = new ContinuousElement("3", ">");
		int expResult = 1;
		int result = instance.minus(other);
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of minus method, of class ContinuousElement.
	 */
	@Test
	public void testMinus2() {
		System.out.println("minus2");
		RangeElement other = new ContinuousElement("1", "<");
		ContinuousElement instance = new ContinuousElement("5", "=");
		int result = instance.minus(other);
		assertTrue(result>0);
	}
	
	/**
	 * Test of minus method, of class ContinuousElement.
	 */
	@Test
	public void testMinusComparator() {
		System.out.println("minus_comparator");
		RangeElement other = new ContinuousElement("2", "<");
		ContinuousElement instance = new ContinuousElement("2", "=");
		int result = instance.minus(other);
		assertTrue(result>0);
	}
	
	/**
	 * Test of minus method, of class ContinuousElement.
	 */
	@Test
	public void testMinusComparator2() {
		System.out.println("minus_comparator2");
		RangeElement other = new ContinuousElement("2", "=");
		ContinuousElement instance = new ContinuousElement("2", "<=");
		int result = instance.minus(other);
		assertTrue(result>0);
	}
	
	/**
	 * Test of minus method, of class ContinuousElement.
	 */
	@Test
	public void testMinusComparatorEquals() {
		System.out.println("minus_comparator_equals");
		RangeElement other = new ContinuousElement("1", "<");
		ContinuousElement instance = new ContinuousElement("1", ">=");
		int expResult = 0;
		int result = instance.minus(other);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getValue method, of class ContinuousElement.
	 */
	@Test
	public void testGetValue() {
		System.out.println("getValue");
		ContinuousElement instance = new ContinuousElement("4", "=");
		String expResult = "4.0";
		String result = instance.getValue();
		assertEquals(expResult, result);
	}
}
