/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.model.output.ClassDescriptor;
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
public class CellValueTest {
	private ClassDescriptor desc1;
	private ClassDescriptor desc2;
	private final ClassDescriptor desc3;
	
	public CellValueTest() {
		desc2 = Descriptor("x", "=", "4");
		desc1 = Descriptor("x", "=", "4");
		desc3 = Descriptor("y", "=", "4");
	}
	
	public static ClassDescriptor Descriptor(String name, String comparator, String value){
		ClassDescriptor desc = new ClassDescriptor();
		desc.name=name;
		desc.comparator=comparator;
		desc.setValue(value);
		return desc;
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of matches method, of class CellValue.
	 */
	@Test
	public void testMatches() {
		System.out.println("matches");
		CellValue instance = new CellValue(desc2);
		boolean expResult = true;
		boolean result = instance.matches(desc1);
		assertEquals(expResult, result);
	}

	/**
	 * Test of compare method, of class CellValue.
	 */
	@Test
	public void testCompare() {
		System.out.println("compare");
		CellValue other = new CellValue(desc1);
		CellValue instance = new CellValue(desc2);
		boolean expResult = true;
		boolean result = instance.compare(other);
		assertEquals(expResult, result);
	}

	/**
	 * Test of compare method, of class CellValue.
	 */
	@Test
	public void testCompareEmpty() {
		System.out.println("compare");
		CellValue other = new CellValue();
		CellValue instance = new CellValue();
		boolean expResult = true;
		boolean result = instance.compare(other);
		assertEquals(expResult, result);
	}
	/**
	 * Test of addAll method, of class CellValue.
	 */
	@Test
	public void testAddAll() {
		System.out.println("addAll");
		List<ClassDescriptor> classes = new LinkedList<ClassDescriptor>();
		classes.add(desc1);
		classes.add(desc2);
		classes.add(desc3);
		CellValue instance = new CellValue();
		instance.addAll(classes);
		assertEquals(true,instance.matches(desc1));
		assertEquals(true,instance.matches(desc2));
		assertEquals(true,instance.matches(desc3));
	}
}
