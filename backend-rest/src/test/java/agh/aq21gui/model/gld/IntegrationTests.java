/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.gld;

import agh.aq21gui.Invoker;
import agh.aq21gui.algorithms.GLDConverter;
import agh.aq21gui.algorithms.GLDOptimizer;
import agh.aq21gui.algorithms.SimulatedAnnealing;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.OutputParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class IntegrationTests {
	private String out;
	
	public IntegrationTests() {
	}
	
	@Before
	public void setUp() {
		out = "domains\n"+
"{\n"+
"	domain1 nominal  { YELLOW, PURPLE }\n"+
"	domain2 nominal  { SMALL, LARGE }\n"+
"	domain3 nominal  { STRETCH, DIP }\n"+
"	domain4 nominal  { ADULT, CHILD }\n"+
"	domain5 nominal  { T, F }\n"+
"}\n"+
"attributes\n"+
"{\n"+
"	attribute1 domain1 epsilon = 0.5 cost = 1\n"+
"	attribute2 domain2 epsilon = 0.5 cost = 1\n"+
"	attribute3 domain3 epsilon = 0.5 cost = 1\n"+
"	attribute4 domain4 epsilon = 0.5 cost = 1\n"+
"	attribute5 domain5 epsilon = 0.5 cost = 1\n"+
"}\n"+
"runs\n"+
"{\n"+
"   c1\n"+
"{\n"+
"      mode = tf\n"+
"      consequent = [attribute5=*]\n"+
"      ambiguity = includeinpos\n"+
"      trim = optimal\n"+
"      compute_alternative_covers = true\n"+
"      maxstar = 1\n"+
"      maxrule = 10\n"+
"}\n"+
"}\n"+
"Output_Hypotheses c1_000\n"+
"{\n"+
"  positive_events           = 12\n"+
"  negative_events           = 8\n"+
"[attribute5=T] \n"+
"       # Rule 1\n"+
"   <-- [attribute4=ADULT : 8,0,100%,8,0,100%]\n"+
"        : p=8,np=4,u=4,cx=7,c=1,s=8 # 15\n"+
"\n"+
"       # Rule 2\n"+
"   <-- [attribute3=STRETCH : 8,0,100%,8,0,100%]\n"+
"        : p=8,np=8,enp=8,n=0,en=0,u=4,cx=7,c=1,s=8 # 16\n"+
"\n"+
"}\n"+
"Output_Hypotheses c1_001\n"+
"{\n"+
"  positive_events           = 8\n"+
"  negative_events           = 12\n"+
"[attribute5=F] \n"+
"       # Rule 1\n"+
"   <-- [attribute3=DIP : 8,4,66%,8,4,66%]\n"+
"       [attribute4=CHILD : 8,4,66%,8,0,100%]\n"+
"        : p=8,np=8,u=8,cx=14,c=1,s=8 # 27\n"+
"}";
	}
	
	@After
	public void tearDown() {
	}
	// TODO add test methods here.
	// The methods must be annotated with annotation @Test. For example:
	//
	@Test
	public void testAll() {
		System.out.println("testAll");
		OutputParser parser = new OutputParser();
		Output output = parser.parse(out);
		GLDInput gld_in = new GLDInput();
		gld_in.setData(output);
		GLDOptimizer optimizer = new GLDOptimizer(gld_in, new SimulatedAnnealing());
		GLDOutput gld_output = optimizer.optimize();
		gld_output.print();
		GLDOutput.print(gld_output);
		fail("Test method is a prototype!");
	}
	
	@Test
	public void testConversion() {
		System.out.println("testConversion");
		OutputParser parser = new OutputParser();
		Output output = parser.parse(out);
		GLDInput gld_in = new GLDInput();
		gld_in.setData(output);
		GLDOptimizer optimizer = new GLDOptimizer(gld_in, new SimulatedAnnealing());
		GLDOutput data = optimizer.getInitialData();
		data.print();
		GLDOutput.print(data);
		fail("Test method is a prototype!");
	}
}
