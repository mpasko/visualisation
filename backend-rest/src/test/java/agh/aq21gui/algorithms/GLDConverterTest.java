/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms;

import agh.aq21gui.model.gld.Argument;
import agh.aq21gui.model.gld.CellValue;
import agh.aq21gui.model.gld.Coordinate;
import agh.aq21gui.model.gld.GLDInput;
import agh.aq21gui.model.gld.GLDOutput;
import agh.aq21gui.model.gld.Value;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
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
		createSample2nominal();
	}
	
	@After
	public void tearDown() {
	}
	
	private void createSample2nominal() {
		sample2nominal = new Output();
		sample2nominal.addAttribute("1", "nominal", "a,b,c");
		sample2nominal.addAttribute("2", "nominal", "d,e");
		final Selector selector1 = listSelector("1",Util.strings("b","c"));
		final Selector selector2 = valueSelector("2","=","d");
		List<Hypothesis> hyps = new LinkedList<Hypothesis>();
		hyps.add(hypothesis(selector1, selector2));
		sample2nominal.setOutputHypotheses(hyps);
	}

	/**
	 * Test of convert method, of class GLDConverter.
	 */
	@Test
	public void testConvert2nominal() {
		System.out.println("convert2nominal");
		GLDInput input = new GLDInput();
		input.setData(sample2nominal);
		GLDConverter instance = new GLDConverter(input);
		GLDOutput result = instance.convert();
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
		assertEquals(true,containsString(arg1.getValues(),"b","c"));
		System.out.print("Arg2: ");
		for (Value v: arg2.getValues()) {
			System.out.print(v.getName());
			System.out.print(", ");
		}
		System.out.println();
		assertEquals(true,containsString(arg2.getValues(),"d"));
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

	private static boolean containsString(List<Value> list, String ...strings) {
		for (String str : strings) {
			boolean found = false;
			for (Value item : list) {
				if (item.getName().contains(str)) {
					found = true;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
	
	private static Selector listSelector(String name, List<String> elems) {
		Selector selector1 = new Selector();
		selector1.name=name;
		selector1.comparator="=";
		selector1.set_elements = elems;
		return selector1;
	}

	private Selector valueSelector(String name, String comparator, String value) {
		Selector selector2 = new Selector();
		selector2.name=name;
		selector2.comparator=comparator;
		selector2.setValue(value);
		return selector2;
	}

	private Hypothesis hypothesis(Selector ...sel) {
		Hypothesis hypo = new Hypothesis();
		List<Rule> rules = new LinkedList<Rule>();
		Rule rule1 = new Rule();
		List<Selector> selectors = new LinkedList<Selector>(Arrays.asList(sel));
		rule1.setSelectors(selectors);
		assertEquals(sel.length, rule1.getSelectors().size());
		rules.add(rule1);
		hypo.setRules(rules);
		assertEquals(1, hypo.getRules().size());
		return hypo;
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
