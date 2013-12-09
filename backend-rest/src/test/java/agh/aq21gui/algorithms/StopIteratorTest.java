/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import agh.aq21gui.algorithms.structures.StopIterator;
import agh.aq21gui.model.gld.Value;
import java.util.LinkedList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class StopIteratorTest {
	private Value a;
	private Value b;
	private LinkedList<Value> list;
	
	public StopIteratorTest() {
	}
	
	@Before
	public void setUp() {
		a = new Value("a");
		b = new Value("b");
		list = new LinkedList<Value>();
		list.add(a);
		list.add(b);
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of shift method, of class StopIterator.
	 */
	@Test
	public void testShift() {
		System.out.println("shift");
		
		StopIterator instance = new StopIterator("list",list);
		instance.shift();
		assertEquals(instance.current(), b);
	}

	/**
	 * Test of hasNext method, of class StopIterator.
	 */
	@Test
	public void testHasNextReturnsFalseWhenFinish() {
		System.out.println("hasNext");
		StopIterator instance = new StopIterator("list",list);
		instance.shift();
		boolean expResult = false;
		boolean result = instance.hasNext();
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of hasNext method, of class StopIterator.
	 */
	@Test
	public void testHasNextReturnsTrueWhenMiddle() {
		System.out.println("hasNext");
		StopIterator instance = new StopIterator("list",list);
		boolean expResult = true;
		boolean result = instance.hasNext();
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of hasNext method, of class StopIterator.
	 */
	@Test
	public void testHasNextOverflows() {
		System.out.println("rollback");
		StopIterator instance = new StopIterator("list",list);
		instance.shift();
		instance.shift();
		
		Value expResult = null;
		Value result = instance.current();
		assertEquals(expResult, result);
	}

	/**
	 * Test of current method, of class StopIterator.
	 */
	@Test
	public void testCurrent() {
		System.out.println("current");
		StopIterator instance = new StopIterator("list",list);
		instance.shift();
		Value expResult = b;
		Value result = instance.current();
		assertEquals(expResult, result);
	}

	/**
	 * Test of rollback method, of class StopIterator.
	 */
	@Test
	public void testRollback() {
		System.out.println("rollback");
		StopIterator instance = new StopIterator("list",list);
		instance.shift();
		instance.rollback();
		
		Value expResult = a;
		Value result = instance.current();
		assertEquals(expResult, result);
	}
}
