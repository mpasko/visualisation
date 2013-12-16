/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import agh.aq21gui.model.gld.Argument;
import agh.aq21gui.model.gld.ArgumentsGroupTest;
import agh.aq21gui.model.gld.GLDOutput;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
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
public class GLDStateTest {
	private GLDOutput initialData;
	
	public GLDStateTest() {
		initialData = StubFactory.getGLDOutput();
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of build method, of class GLDState.
	 */
	@Test
	public void testBuild() {
		System.out.println("build");
		GLDState result = GLDState.build(initialData);
		assertEquals(initialData, result.getData());
	}

	/**
	 * Test of targetFunction method, of class GLDState.
	 */
	@Test
	public void testTargetFunction() {
		System.out.println("targetFunction");
		GLDState instance = StubFactory.getGLDState();
		instance.setRatioImportance(0);
		double expResult = instance.getClusters();
		double result = instance.targetFunction();
		assertEquals(expResult, result, 0.0);
	}

	/**
	 * Test of setRatioImportance method, of class GLDState.
	 */
	@Test
	public void testSetRatioImportance() {
		System.out.println("setRatioImportance");
		int newp = 0;
		GLDState instance = StubFactory.getGLDState();
		instance.setRatioImportance(1);
		double expResult = instance.getClusters()+instance.getGoldenRatioCloseness();
		double result = instance.targetFunction();
		assertEquals(expResult, result, 0.01);
	}

	/**
	 * Test of setRepartitionProb method, of class GLDState.
	 */
	@Test
	public void testSetRepartitionProb() {
		System.out.println("setRepartitionProb");
		double prob = 0.0;
		GLDState instance = StubFactory.getGLDState();
		instance.setRepartitionProb(prob);
		int h_before = instance.getData().getHeight();
		int w_before = instance.getData().getWidth();
		instance.modifyItself();
		assertEquals(h_before, instance.getData().getHeight());
		assertEquals(w_before, instance.getData().getWidth());
	}

	/**
	 * Test of getClusters method, of class GLDState.
	 */
	@Test
	public void testGetClusters() {
		System.out.println("getClusters");
		GLDState instance = StubFactory.getGLDState();
		double expResult = 1.0;
		double result = instance.getClusters();
		assertEquals(expResult, result, 0.0);
	}

	/**
	 * Test of getGoldenRatioCloseness method, of class GLDState.
	 */
	@Test
	public void testGetGoldenRatioCloseness() {
		System.out.println("getGoldenRatioCloseness");
		GLDState instance = StubFactory.getGLDState();
		double expResult = 0.61803;
		double result = instance.getGoldenRatioCloseness();
		assertEquals(expResult, result, 0.001);
	}

	/**
	 * Test of modifyItself method, of class GLDState.
	 */
	@Test
	public void testModifyItself() {
		System.out.println("modifyItself");
		GLDState instance = StubFactory.getGLDState();
		instance.setRepartitionProb(1.0);
		int h_before = instance.getData().getRows().size();
		int w_before = instance.getData().getColumns().size();
		instance.modifyItself();
		int h_after = instance.getData().getRows().size();
		int w_after = instance.getData().getColumns().size();
		assertNotSame(h_before, h_after);
		assertNotSame(w_before, w_after);
		assertEquals(1, Math.abs(w_after-w_before));
		assertEquals(1, Math.abs(h_after-h_before));
		assertEquals(w_before+h_before,w_after+h_after);
	}

	/**
	 * Test of moveItem method, of class GLDState.
	 */
	@Test
	public void testMoveItem() {
		System.out.println("moveItem");
		List<Argument> from = new LinkedList<Argument>();
		Argument arg1=new Argument();
		from.add(arg1);
		Argument arg2=new Argument();
		from.add(arg2);
		Argument arg3=new Argument();
		from.add(arg3);
		int from_at = 0;
		int to_at = 1;
		GLDState.moveItem(from, from_at, from, to_at);
		assertEquals(arg2,from.get(0));
		assertEquals(arg1,from.get(1));
		assertEquals(arg3,from.get(2));
	}

	/**
	 * Test of cloneItself method, of class GLDState.
	 */
	@Test
	public void testCloneItself() {
		System.out.println("cloneItself");
		Output out = StubFactory.getSimpleData();
		GLDOutput data = new GLDOutput(out);
		List<Argument> cols = new LinkedList<Argument>();
		data.setColumns(cols);
		List<Argument> rows=new LinkedList<Argument>();
		data.setRows(rows);
		GLDState instance = GLDState.build(data);
		State expResult = null;
		GLDState result = (GLDState) instance.cloneItself();
		assertNotSame(data, result.getData());
		assertNotSame(cols, result.getData().getColumns());
		assertNotSame(rows, result.getData().getRows());
	}

	/**
	 * Test of printIt method, of class GLDState.
	 */
	@Test
	public void testPrintIt() {
		System.out.println("printIt");
		GLDState instance = StubFactory.getGLDState();
		instance.printIt();
	}
}
