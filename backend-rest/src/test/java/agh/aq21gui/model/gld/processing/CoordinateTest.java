/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld.processing;

import agh.aq21gui.model.gld.Value;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class CoordinateTest {
	
	public CoordinateTest() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of merge method, of class Coordinate.
	 */
	@Test
	public void testMerge() {
		System.out.println("merge");
		Coordinate col = new Coordinate();
		col.put("a", new Value("1"));
		Coordinate instance = new Coordinate();
		instance.put("b", new Value("2"));
		Coordinate result = instance.merge(col);
		List<Value> values = result.getValues();
		assertEquals(2, values.size());
		assertEquals("2", values.get(0).getName());
		assertEquals("1", values.get(1).getName());
	}

	/**
	 * Test of equals method, of class Coordinate.
	 */
	@Test
	public void testEquals() {
		System.out.println("equals");
		Coordinate other = new Coordinate();
		other.put("a", new Value("1"));
		Coordinate instance = new Coordinate();
		instance.put("a", new Value("1"));
		boolean expResult = true;
		boolean result = instance.equals(other);
		assertEquals(expResult, result);
		int expHash = instance.hashCode();
		int resHash = other.hashCode();
		assertEquals(expHash, resHash);
	}
}
