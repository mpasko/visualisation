/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class ValueEvaluatorTest {
	
	public ValueEvaluatorTest() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of eval method, of class ValueEvaluator.
	 */
	@Test
	public void testEval() {
		System.out.println("eval");
		Coordinate row = new Coordinate();
		Value vx = new Value("vx");
		row.put("x", vx);
		Value vy = new Value("vy");
		row.put("y", vy);
		Coordinate col = new Coordinate();
		Value va = new Value("va");
		col.put("a", va);
		Value vb = new Value("vb");
		col.put("b", vb);
		Output out = new Output();
		out.addAttribute("x", "nominal", "{vx, zx}");
		out.addAttribute("y", "nominal", "{vy, zy}");
		out.addAttribute("a", "nominal", "{va, za}");
		out.addAttribute("b", "nominal", "{vb, zb}");
		Hypothesis hypo = new Hypothesis();
		out.getOutputHypotheses().add(hypo);
		Rule rule1 = new Rule();
		Selector sel1 = new Selector();
		sel1.name = "x";
		sel1.comparator="=";
		sel1.setValue("vx");
		ClassDescriptor desc = new ClassDescriptor();
		desc.name = "w";
		desc.comparator="=";
		desc.setValue("w1");
		hypo.addClass(desc);
		rule1.getSelectors().add(sel1);
		hypo.getRules().add(rule1);
		ValueEvaluator instance = new ValueEvaluator(out);
		CellValue expResult = new CellValue(desc);
		CellValue result = instance.eval(row, col);
		assertEquals(true, result.compare(expResult));
	}
}
