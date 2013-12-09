/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.structures;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
		String expResult = "jumper";
		String result = instance.transform("a","1").get();
		assertEquals(expResult, result);
	}
}
