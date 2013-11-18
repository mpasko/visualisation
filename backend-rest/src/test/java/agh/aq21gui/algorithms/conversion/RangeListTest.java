/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

import agh.aq21gui.model.input.Attribute;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class RangeListTest {
	private Attribute attr;
	
	public RangeListTest() {
	}
	
	@Before
	public void setUp() {
		initSimpleAttribute();
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of addContinuous method, of class RangeList.
	 */
	@Test
	public void testSimpleAddContinuous() {
		System.out.println("addContinuous");
		RangeList instance = new RangeList(null);
		instance.addContinuous("4", "=");
		instance.addContinuous("5", "=");
		instance.addContinuous("6", ">");
		
		List<RangeElement> elems = instance.getElements();
		assertEquals(3, elems.size());
		assertEquals(true, elems.get(0).isSingleNumber());
	}
	
	/**
	 * Test of addContinuous method, of class RangeList.
	 */
	@Test
	public void testOrderAddContinuous() {
		System.out.println("addContinuous_order");
		RangeList instance = new RangeList(null);
		instance.addContinuous("4", "=");
		instance.addContinuous("6", ">");
		instance.addContinuous("1", "<");
		instance.addContinuous("5", "=");
		
		List<RangeElement> elems = instance.getElements();
		for (RangeElement el:elems) {
			System.out.print("Value: [");
			System.out.print(el.getValue());
			System.out.println("]");
		}
		assertEquals(4, elems.size());
		assertEquals("1.0", elems.get(0).getValue());
		assertEquals("4.0", elems.get(1).getValue());
		assertEquals("5.0", elems.get(2).getValue());
		assertEquals("6.0", elems.get(3).getValue());
	}

	/**
	 * Test of addLinear method, of class RangeList.
	 */
	@Test
	public void testSimpleAddLinear() {
		System.out.println("addLinear");
		String value = "b";
		String comparator = "=";
		RangeList instance = new RangeList(attr);
		instance.addLinear(value, comparator);
		
		List<RangeElement> elems = instance.getElements();
		assertEquals(1, elems.size());
		assertEquals(true, elems.get(0).isSingleNumber());
	}

	/**
	 * Test of genereteNames method, of class RangeList.
	 */
	@Test
	public void testGenereteNames() {
		System.out.println("genereteNames");
		RangeList instance = new RangeList(null);
		instance.addContinuous("1", ">");
		instance.addContinuous("2", ">");
		instance.addContinuous("2", "=");
		instance.addContinuous("1", "<=");
		instance.addContinuous("3", "<=");
		List<String> result = instance.genereteNames();
		for (String name:result) {
			System.out.print("Name: [");
			System.out.print(name);
			System.out.println("]");
		}
		assertEquals(5, result.size());
	}

	private void initSimpleAttribute() {
		attr = new Attribute();
		List<String> range = new LinkedList<String>();
		range.add("a");
		range.add("b");
		range.add("c");
		attr.setRange(range);
	}
}
