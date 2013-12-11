/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.Invoker;
import agh.aq21gui.algorithms.GLDOptimizer;
import agh.aq21gui.algorithms.SimulatedAnnealing;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.OutputParser;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author marcin
 */
public class IntegrationTests {
	private static Output baloons;
	private static Output iris;
	private static Output aq21example;
	private static Output alternateCovers;
	private boolean skip_iris_all = false;
	
	public IntegrationTests() {
	}
	
	@BeforeClass
	public static void setUp() {
		baloons = StubFactory.getBaloonsOutput();
		iris = StubFactory.getIrisOutput();
		aq21example = StubFactory.getOutput();
		alternateCovers = StubFactory.getACOutput();
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	public void testConversion() {
		System.out.println("testConversion");
		GLDInput gld_in = new GLDInput();
		gld_in.setData(baloons);
		GLDOptimizer optimizer = new GLDOptimizer(gld_in, new SimulatedAnnealing());
		GLDOutput gld_output = optimizer.getInitialData();
		gld_output.print();
		GLDOutput.printCellValues(gld_output);
		assertEquals(2, gld_output.width());
		assertEquals(2, gld_output.height());
		assertEquals(3, countMatches(gld_output,"t"));
		assertEquals(1, countMatches(gld_output,"f"));
	}
	@Test
	public void testAll() {
		System.out.println("testAll");
		GLDInput gld_in = new GLDInput();
		gld_in.setData(baloons);
		GLDOptimizer optimizer = new GLDOptimizer(gld_in, new SimulatedAnnealing());
		GLDOutput gld_output = optimizer.optimize();
		gld_output.print();
		GLDOutput.printCellValues(gld_output);
		assertEquals(2, gld_output.width());
		assertEquals(2, gld_output.height());
		assertEquals(3, countMatches(gld_output,"t"));
		assertEquals(1, countMatches(gld_output,"f"));
	}
	
	@Test
	public void testConversionIris() {
		System.out.println("testConversion_Iris");
		GLDInput gld_in = new GLDInput();
		gld_in.setData(iris);
		GLDOptimizer optimizer = new GLDOptimizer(gld_in, new SimulatedAnnealing());
		GLDOutput gld_output = optimizer.getInitialData();
		gld_output.print();
		GLDOutput.printCellValues(gld_output);
		int num_cells = gld_output.width()*gld_output.height();
		if(5384!=num_cells){
			skip_iris_all = true;
		}
		assertEquals(5184, num_cells);
	}
	
	@Test
	public void testAllIris() {
		System.out.println("testAll_Iris");
		if (skip_iris_all){
			System.out.println("Iris All -Skipped");
			return;
		}
		GLDInput gld_in = new GLDInput();
		gld_in.setData(iris);
		GLDOptimizer optimizer = new GLDOptimizer(gld_in, new SimulatedAnnealing());
		GLDOutput gld_output = optimizer.optimize();
		gld_output.print();
		GLDOutput.printCellValues(gld_output);
		assertEquals(5184, gld_output.width()*gld_output.height());
	}
	
	@Test
	public void testConversionAq21Example() {
		System.out.println("testConversion_Aq21Example");
		GLDInput gld_in = new GLDInput();
		gld_in.setData(aq21example);
		GLDOptimizer optimizer = new GLDOptimizer(gld_in, new SimulatedAnnealing());
		GLDOutput gld_output = optimizer.getInitialData();
		gld_output.print();
		GLDOutput.printCellValues(gld_output);
		assertEquals(1, gld_output.getColumns().size()+gld_output.getRows().size());
	}
	
	@Test
	public void testAllAq21Example() {
		System.out.println("testAll_Aq21Example");
		GLDInput gld_in = new GLDInput();
		gld_in.setData(aq21example);
		GLDOptimizer optimizer = new GLDOptimizer(gld_in, new SimulatedAnnealing());
		GLDOutput gld_output = optimizer.optimize();
		gld_output.print();
		GLDOutput.printCellValues(gld_output);
		assertEquals(1, gld_output.getColumns().size()+gld_output.getRows().size());
	}
	
	@Test
	public void testConversionAlternativeCovers() {
		System.out.println("testConversion_AlternativeCovers");
		GLDInput gld_in = new GLDInput();
		gld_in.setData(alternateCovers);
		GLDOptimizer optimizer = new GLDOptimizer(gld_in, new SimulatedAnnealing());
		GLDOutput gld_output = optimizer.getInitialData();
		gld_output.print();
		GLDOutput.printCellValues(gld_output);
		assertEquals(3, gld_output.getColumns().size()+gld_output.getRows().size());
	}
	
	@Test
	public void testAllAlternativeCovers() {
		System.out.println("testAll_AlternativeCovers");
		GLDInput gld_in = new GLDInput();
		gld_in.setData(alternateCovers);
		GLDOptimizer optimizer = new GLDOptimizer(gld_in, new SimulatedAnnealing());
		GLDOutput gld_output = optimizer.optimize();
		gld_output.print();
		GLDOutput.printCellValues(gld_output);
		assertEquals(3, gld_output.getColumns().size()+gld_output.getRows().size());
	}
	
	public static int countMatches(GLDOutput data, String value){
		int i = 0;
		ClassDescriptor desc = new ClassDescriptor();
		desc.name="attribute5";
		desc.comparator="=";
		desc.setValue(value);
		for (Coordinate x : data.getHCoordSequence()){
			for (Coordinate y : data.getVCoordSequence()){
				CellValue v = data.eval(x, y);
				if (v.matches(desc)){
					++i;
				}
			}
		}
		return i;
	}
}
