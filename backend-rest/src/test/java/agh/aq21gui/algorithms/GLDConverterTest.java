/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import agh.aq21gui.model.gld.Argument;
import agh.aq21gui.model.gld.processing.CellValue;
import agh.aq21gui.model.gld.processing.Coordinate;
import agh.aq21gui.model.gld.GLDInput;
import agh.aq21gui.model.gld.GLDOutput;
import agh.aq21gui.model.gld.Value;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.stubs.Utils;
import agh.aq21gui.utils.Util;
import java.util.ArrayList;
import java.util.Arrays;
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
public class GLDConverterTest {
	private Output sample2nominal;
	
	public GLDConverterTest() {
	}
	
	@Before
	public void setUp() {
		sample2nominal=StubFactory.createSample2nominal();
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of convert method, of class GLDConverter.
	 */
	@Test
	public void testConvert2nominal() {
		System.out.println("convert2nominal");
		GLDInput input = new GLDInput();
		input.setData(sample2nominal);
		Hypothesis hypo = input.getData().getOutputHypotheses().get(0);
		GLDConverter instance = new GLDConverter(input);
		GLDOutput result = instance.convert();
		assertEquals(1, hypo.getRules().size());
		List<Argument> merged = new LinkedList<Argument>(result.getRows());
		merged.addAll(result.getColumns());
		assertEquals(2, merged.size());
		Argument arg1;
		Argument arg2;
		if (merged.get(0).name.equals("1")) {
			arg1 = merged.get(0);
			arg2 = merged.get(1);
		} else {
			arg2 = merged.get(0);
			arg1 = merged.get(1);
		}
		System.out.print("Arg1: ");
		for (Value v: arg1.getValues()) {
			System.out.print(v.getName());
			System.out.print(", ");
		}
		System.out.println();
		assertEquals(true,Utils.valuesContainStrings(arg1.getValues(),"b","c"));
		System.out.print("Arg2: ");
		for (Value v: arg2.getValues()) {
			System.out.print(v.getName());
			System.out.print(", ");
		}
		System.out.println();
		assertEquals(true,Utils.valuesContainStrings(arg2.getValues(),"d"));
	}
	
	/**
	 * Test of convert method, of class GLDConverter.
	 */
	@Test
	public void testConvertEmpty() {
		System.out.println("convertEmpty");
		GLDInput input = new GLDInput();
		input.setData(sample2nominal);
		GLDConverter instance = new GLDConverter(input);
		GLDOutput result = instance.convert();
		List<Coordinate> h = result.getHCoordSequence();
		List<Coordinate> v = result.getVCoordSequence();
		CellValue value1 = result.eval(v.get(0), h.get(0));
		CellValue value2 = result.eval(v.get(1), h.get(0));
		CellValue value3 = result.eval(v.get(0), h.get(1));
		CellValue value4 = result.eval(v.get(1), h.get(1));
		assertEquals(true,value1.compare(value2));
		assertEquals(true,value2.compare(value3));
		assertEquals(true,value3.compare(value4));
		assertEquals(true,value4.compare(value1));
	}

	@Test
	public void extractSelectorsTest() {
		System.out.println("extractSelectors");
		GLDInput input = new GLDInput();
		input.setData(sample2nominal);
		GLDConverter instance = new GLDConverter(input);
		List<Selector> selectors = instance.extractSelectors();
		assertEquals(2, selectors.size());
	}
}
