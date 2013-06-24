/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.utils.OutputParser;
import agh.aq21gui.Invoker;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
		FileInputStream stream = new FileInputStream("test_inputs/output_hypotheses_test.txt");
		Output result = instance.parse(Invoker.streamToString(stream));
		assertEquals(1, 1);
	}
	
	/**
	 * Test of parse method, of class OutputParser.
	 */
	@Test
	public void testParse_SIMPE_OutputHypothesis() throws IOException {
		OutputParser instance = new OutputParser();
		FileInputStream stream = new FileInputStream("test_inputs/output_hypotheses_simple_test.txt");
		String out = Invoker.streamToString(stream);
		Output result = instance.parse(out);
		assertEquals(1, 1);
	}
	
	/**
	 * Test of parse method, of class OutputParser.
	 */
	@Test
	public void testParse_template1() throws IOException {
		OutputParser instance = new OutputParser();
		FileInputStream stream = new FileInputStream("test_inputs/template1.txt");
		String out = Invoker.streamToString(stream);
		Output result = instance.parse(out);
		assertEquals(1, 1);
	}
	
	/**
	 * Test of parse method, of class OutputParser.
	 */
	@Test
	public void testParse_template1_in() throws IOException {
		OutputParser instance = new OutputParser();
		FileInputStream stream = new FileInputStream("test_inputs/template1_in.txt");
		String out = Invoker.streamToString(stream);
		Output result = instance.parse(out);
		assertEquals(1, 1);
	}
}
