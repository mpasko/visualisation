/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

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
public class ArrayMergingSetsTest {
	private Object a;
	private Object b;
	private Object c;
	private Object d;
	private Object e;
	
	public ArrayMergingSetsTest() {
		e = new Object();
		d = new Object();
		c = new Object();
		b = new Object();
		a = new Object();
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of newElement method, of class ArrayMergingSets.
	 */
	@Test
	public void testNewElement() {
		System.out.println("newElement");
		Object obj = null;
		ArrayMergingSets instance = new ArrayMergingSets(1);
		instance.newElement(obj);
		assertEquals(1, instance.count());
	}

	/**
	 * Test of merge method, of class ArrayMergingSets.
	 */
	@Test
	public void testMerge() {
		System.out.println("merge");
		ArrayMergingSets instance = new ArrayMergingSets(5);
		instance.newElement(a);
		instance.newElement(b);
		instance.newElement(c);
		instance.newElement(d);
		instance.newElement(e);
		instance.merge(a, b);
		instance.merge(b, c);
		instance.merge(d, e);
		instance.merge(a, c);
		assertEquals(2, instance.count());
	}

	/**
	 * Test of count method, of class ArrayMergingSets.
	 */
	@Test
	public void testCount() {
		System.out.println("count");
		ArrayMergingSets instance = new ArrayMergingSets(3);
		instance.newElement(a);
		instance.newElement(b);
		instance.newElement(c);
		int expResult = 3;
		int result = instance.count();
		assertEquals(expResult, result);
	}

	/**
	 * Test of addAll method, of class ArrayMergingSets.
	 */
	@Test
	public void testAddAll() {
		System.out.println("addAll");
		List<Object> colSeq = new LinkedList<Object>();
		colSeq.add(a);
		colSeq.add(b);
		colSeq.add(c);
		ArrayMergingSets instance = new ArrayMergingSets(3);
		instance.addAll(colSeq);
		int expResult = 3;
		int result = instance.count();
		assertEquals(expResult, result);
	}
}
