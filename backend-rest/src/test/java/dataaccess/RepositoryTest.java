/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import agh.aq21gui.services.aq21.Invoker;
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
			instance = Repository.getRepository("testdb");
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
	@SuppressWarnings("all")
	public void testSaveExperiment() {
		System.out.println("saveExperiment");
		Input input = StubFactory.getInput();
		final String name = "component_test_case_01";
		InputPair experiment = new InputPair(name, input);
		instance.saveExperiment(experiment);
		experiment = null;
		input = null;
		Input experiment2 = instance.getExperiment(name);
		assertEquals(4,experiment2.getAttributes().size());
		assertEquals(4,experiment2.getEvents().size());
	}

	/**
	 * Test of saveResult method, of class Repository.
	 */
	@Test
	@SuppressWarnings("all")
	public void testSaveResult() {
		System.out.println("saveResult");
		Input input = StubFactory.getInput();
		Output output = new Invoker().invoke(input);
		final String name = "component_test_case_02";
		OutputPair result = new OutputPair(name, output);
		instance.saveResult(result);
		// TODO review the generated test code and remove the default call to fail.
		//		fail("The test case is a prototype.");
	}
	
	@Test
	@SuppressWarnings("all")
	public void testDescriptionSaved() {
		System.out.println("testDescriptionSaved");
		final String name = "testDescription";
		final String description = "Description for an instance";
		InputPair input = new InputPair(name, StubFactory.getInput());
		input.setDescription(description);
		instance.saveExperiment(input);
		int i = 0;
		try{
			for (InputPair ip : instance.getDirectory().getExperiments()){
				if (ip.getName().equalsIgnoreCase(name)){
					assertEquals(description, ip.getDescription());
					++i;
				}
			}
		}catch(Exception e){
			fail(String.format("Exception:%s caught", e.getMessage()));
		}
		assertEquals(1, i);
	}
}
