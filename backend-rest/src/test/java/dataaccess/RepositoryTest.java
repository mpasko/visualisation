/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import agh.aq21gui.Invoker;
import agh.aq21gui.MyResource;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.management.Directory;
import agh.aq21gui.model.management.InputPair;
import agh.aq21gui.model.management.OutputPair;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.stubs.StubFactory;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
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
public class RepositoryTest {
	
	static Repository instance = null;
	
	public RepositoryTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
		try {
			instance = Repository.getRepository();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	@AfterClass
	public static void tearDownClass() {
		instance.onStop();
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	} 

	/**
	 * Test of getDirectory method, of class Repository.
	 */
	@Test
	public void testGetDirectory() throws Exception{
		System.out.println("getDirectory");
		
		Directory expResult = null;
		Directory result;
		result = instance.getDirectory();
		assertNotSame(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		//fail("The test case is a prototype.");
	}

	/**
	 * Test of saveExperiment method, of class Repository.
	 */
	@Test
	public void testSaveExperiment() {
		System.out.println("saveExperiment");
		Input input = StubFactory.getInput();
		InputPair experiment = new InputPair("component_test_case_01", input);
		instance.saveExperiment(experiment);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of saveResult method, of class Repository.
	 */
	@Test
	public void testSaveResult() {
		System.out.println("saveResult");
		Input input = StubFactory.getInput();
		Output output = new Invoker().invoke(input);
		OutputPair result = new OutputPair("component_test_case_02", output);
		instance.saveResult(result);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}
}
