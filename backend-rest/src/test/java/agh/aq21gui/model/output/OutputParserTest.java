/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.services.aq21.OutputParser;
import agh.aq21gui.utils.Util;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class OutputParserTest {
	
	public OutputParserTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of parse method, of class OutputParser.
	 */
	@Test
	public void testParse_OutputHypothesis() throws IOException {
		OutputParser instance = new OutputParser();
        String filename = "test_inputs/output_hypotheses_test.txt";
        String out = Util.loadFile(filename);
		Output result = instance.parse(out);
		assertEquals(1, 1);
	}
	
	/**
	 * Test of parse method, of class OutputParser.
	 */
	@Test
	public void testParse_SIMPE_OutputHypothesis() throws IOException {
		OutputParser instance = new OutputParser();
        String filename = "test_inputs/output_hypotheses_simple_test.txt";
		String out = Util.loadFile(filename);
		Output result = instance.parse(out);
		assertEquals(1, 1);
	}
	
	/**
	 * Test of parse method, of class OutputParser.
	 */
	@Test
	public void testParse_template1() throws IOException {
		OutputParser instance = new OutputParser();
        String filename = "test_inputs/template1.txt";
		String out = Util.loadFile(filename);
		Output result = instance.parse(out);
		assertEquals(1, 1);
	}
	
	/**
	 * Test of parse method, of class OutputParser.
	 */
	@Test
	public void testParse_template1_in() throws IOException {
		OutputParser instance = new OutputParser();
        String filename = "test_inputs/template1_in.txt";
		String out = Util.loadFile(filename);
		Output result = instance.parse(out);
		assertEquals(1, 1);
	}
    //no_viable_bug_sono_ichi
    
	/**
	 * Test of parse method, of class OutputParser.
	 */
	@Test
	public void test_no_viable_bug_sono_ichi() throws IOException {
		OutputParser instance = new OutputParser();
        String filename = "test_inputs/no_viable_bug_sono_ichi";
		String out = Util.loadFile(filename);
		Output result = instance.parse(out);
        assertEquals(1, result.outHypo.hypotheses.size());
        assertEquals(1, result.outHypo.hypotheses.get(0).getRules().size());
        Rule rule = result.outHypo.hypotheses.get(0).getRules().get(0);
        assertEquals(1, rule.getSelectors().size());
        Selector sel = rule.getSelectors().get(0);
        System.out.println(sel.toString());
        assertEquals(2, sel.getSet_elements().size());
		assertEquals("a3", sel.getRange_begin());
        assertEquals("a10", sel.getRange_end());
	}
}
