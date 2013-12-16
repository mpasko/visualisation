/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.structures;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class MeshTest {
	
	public MeshTest() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of transform method, of class Mesh.
	 */
	@Test
	public void testTransform() {
		System.out.println("transform");
		Mesh<String,String> instance = new Mesh<String,String>();
		instance.transform("a","1").set("jumper");
		instance.transform("b","2").set("king");
		String expResult1 = "jumper";
		String result1 = instance.transform("a","1").get();
		assertEquals(expResult1, result1);
		String expResult2 = "king";
		String result2 = instance.transform("b","2").get();
		assertEquals(expResult2, result2);
	}
	
	/**
	 * Test of transform method, of class Mesh.
	 */
	@Test
	public void testTransformInverse() {
		System.out.println("transformInverse");
		Mesh<String,String> instance = new Mesh<String,String>();
		instance.transform("a","1").set("jumper");
		instance.transform("b","2").set("king");
		String expResult1 = "jumper";
		String result1 = instance.transform("1","a").get();
		assertEquals(expResult1, result1);
		String expResult2 = "king";
		String result2 = instance.transform("2","b").get();
		assertEquals(expResult2, result2);
	}

	/**
	 * Test of clear method, of class Mesh.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		Mesh<String,String> instance = new Mesh<String,String>();
		instance.transform("b","2").set("king");
		instance.clear();
		String expResult2 = "king";
		String result2 = instance.transform("b","2").get();
		assertNotSame(expResult2, result2);
	}
}
