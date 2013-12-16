/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.algorithms.structures.MeshCell;
import agh.aq21gui.model.gld.processing.CellValue;
import agh.aq21gui.model.gld.processing.Coordinate;
import agh.aq21gui.stubs.StubFactory;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class GLDOutputTest {
	private static GLDOutput prototype;
	
	public GLDOutputTest() {
	}
	
	@BeforeClass
	public static void setTest(){
		prototype = StubFactory.getGLDOutputBaloons();
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of cloneItself method, of class GLDOutput.
	 */
	@Test
	public void testCloneItself() {
		System.out.println("cloneItself");
		GLDOutput instance = new GLDOutput(StubFactory.getBaloonsOutput());
		GLDOutput expResult = null;
		GLDOutput result = instance.cloneItself();
		assertNotSame(expResult, result);
		assertNotSame(instance, result);
		assertNotSame(instance.getElements(), result.getElements());
		assertNotSame(instance.getColumns(), result.getColumns());
		assertNotSame(instance.getRows(), result.getRows());
	}

	/**
	 * Test of getMeshCellValues method, of class GLDOutput.
	 */
	@Test
	public void testGetMeshCellValues() {
		System.out.println("getMeshCellValues");
		GLDOutput instance = prototype.cloneItself();
		Iterable<Coordinate> rowSeq = instance.getVCoordSequence();
		Iterable<Coordinate> colSeq = instance.getHCoordSequence();
		instance.resetMesh();
		Iterable<MeshCell<CellValue>> result = instance.getMeshCellValues(rowSeq, colSeq);
		int count=0;
		for (MeshCell<CellValue> meshcell : result) {
			assertNotSame(null,meshcell);
			assertNotSame(null,meshcell.get());
			count++;
		}
		assertNotSame(0, count);
	}

	/**
	 * Test of resetMesh method, of class GLDOutput.
	 */
	@Test
	public void testResetMesh() {
		System.out.println("resetMesh");
		GLDOutput instance = prototype.cloneItself();
		instance.resetMesh();
		Iterable<Coordinate> rowSeq = instance.getVCoordSequence();
		Iterable<Coordinate> colSeq = instance.getHCoordSequence();
		final Coordinate x = rowSeq.iterator().next();
		final Coordinate y = colSeq.iterator().next();
		MeshCell<CellValue> meshcell = instance.mesh.transform(x, y);
		CellValue value = meshcell.get();
		assertNotSame(null,value);
		assertNotSame(null,value.getDescriptors());
		assertEquals(1,value.getDescriptors().size());
	}
	
}
