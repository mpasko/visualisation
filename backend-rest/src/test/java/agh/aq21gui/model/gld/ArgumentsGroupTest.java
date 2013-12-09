/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.algorithms.structures.StopIterator;
import agh.aq21gui.stubs.StubFactory;
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
public class ArgumentsGroupTest {
	
	public ArgumentsGroupTest() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of getArguments method, of class ArgumentsGroup.
	 */
	@Test
	public void testGetArguments() {
		System.out.println("getArguments");
		List expResult = new LinkedList();
		ArgumentsGroup instance = new ArgumentsGroup(expResult);
		List result = instance.getArguments();
		assertEquals(expResult, result);
	}

	@Test
	public void testIncrementIterators() {
		Value a = new Value("a");
		Value b = new Value("b");
		LinkedList<Value> listA = new LinkedList<Value>();
		listA.add(a);
		listA.add(b);
		LinkedList<Value> listB = new LinkedList<Value>();
		listB.add(a);
		listB.add(b);
		LinkedList<StopIterator> iterators = new LinkedList<StopIterator>();
		iterators.add(new StopIterator("lista",listA));
		iterators.add(new StopIterator("listb",listB));
		
		for (int i = 0; i < 2; ++i){
			ArgumentsGroup.incrementIterators(iterators);
		}
		Value result = iterators.get(0).current;
		assertEquals(b, result);
		result = iterators.get(1).current;
		assertEquals(a, result);
	}
	
	@Test
	public void testIncrementIteratorsOverflow() {
		Value a = new Value("a");
		Value b = new Value("b");
		LinkedList<Value> listA = new LinkedList<Value>();
		listA.add(a);
		listA.add(b);
		LinkedList<Value> listB = new LinkedList<Value>();
		listB.add(a);
		listB.add(b);
		LinkedList<StopIterator> iterators = new LinkedList<StopIterator>();
		iterators.add(new StopIterator("lista",listA));
		iterators.add(new StopIterator("listb",listB));
		
		for (int i = 0; i < 4; ++i){
			ArgumentsGroup.incrementIterators(iterators);
		}
		Value result = iterators.get(0).current;
		assertEquals(null, result);
	}
	
	/**
	 * Test of getCoordSequence method, of class ArgumentsGroup.
	 */
	@Test(timeout=1000)
	public void testGetCoordSequence() {
		ArgumentsGroup instance = StubFactory.getArgumentsGroupSample("arg");
		int expResult = 27;
		List<Coordinate> result = instance.getCoordSequence();
		for (Coordinate cord:result){
			//System.out.println(cord.toString());
		}
		assertEquals(expResult, result.size());
	}
	
	@Test
	public void testGetEmptyCoordSequence() {
		ArgumentsGroup instance = new ArgumentsGroup(new LinkedList<Argument>());
		int expResult = 1;
		List<Coordinate> result = instance.getCoordSequence();
		assertEquals(expResult, result.size());
	}
}
