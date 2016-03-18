/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.j48;

import agh.aq21gui.converters.Aq21InputToWeka;
import agh.aq21gui.filters.RulePrunnerTest;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.aq21.Aq21FunctionalityWrapperTest;
import agh.aq21gui.services.csv.J48ArchetypeConfig;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.NumericUtil;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import weka.core.Instances;

/**
 *
 * @author marcin
 */
public class J48ServiceTest {
    public static final int size = 10;
    
    public J48ServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void when_star_then_discretize() {
        System.out.println("when_star_then_discretize");
        J48Service instance = new J48Service();
        Input in = Aq21FunctionalityWrapperTest.generateInputWithStarRun(size);
        Output result = instance.convertAndRun(in);
        List<String> items = result.getCollumnOfData("class");
        
        assertEquals(size, items.size());
        for (String item : items) {
            assertFalse(NumericUtil.isNumber(item));
        }
        List<String> discretizedSet = result.findDomainObjectRecursively("class").getRange();
        List<agh.aq21gui.model.input.Test> generatedRuns = result.getRunsGroup().runs;
        assertEquals(discretizedSet.size(), generatedRuns.size());
        assertTrue(discretizedSet.size()>2);
        assertFalse(result.getAggregatedClassDescriptor().isCustomValue());
    }

    /*
    @Test
    public void testRunAll() {
        System.out.println("runAll");
        Input input = null;
        J48Service instance = new J48Service();
        List expResult = null;
        List result = instance.runAll(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */

    @Test
    public void when_true_should_avoid_parameter() {
        System.out.println("when_true_should_avoid_parameter");
        agh.aq21gui.model.input.Test run = new Run();
        run.addParameter("minimum_instances", "1234");
        run.addParameter("prune", "true");
        run.addParameter("collapse", "true");
        String[] expResult = new String[]{"-M", "1234"};
        String[] result = J48Service.paramsetToCmdline(run);
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void when_false_should_add_parameter() {
        System.out.println("when_false_should_add_parameter");
        agh.aq21gui.model.input.Test run = new Run();
        run.addParameter("minimum_instances", "432");
        run.addParameter("prune", "false");
        run.addParameter("collapse", "false");
        String[] result = J48Service.paramsetToCmdline(run);
        assertEquals(4, result.length);
    }

    @Test
    public void testSetClassificationClass() {
        System.out.println("setClassificationClass");
        String className = "class";
        Input input = StubFactory.generatePolynomialInput(size, className);
        Instances instances = new Aq21InputToWeka().aq21ToWeka(input);
        J48Service.setClassificationClass(input, className, instances);
        assertEquals(1, instances.classIndex());
    }

    /*
    @Test
    public void testRunJ48() throws Exception {
        System.out.println("runJ48");
        agh.aq21gui.model.input.Test run = null;
        Instances instances = null;
        List expResult = null;
        List result = J48Service.runJ48(run, instances);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */

    @Test
    public void when_star_then_should_split_in_two() {
        System.out.println("expandRunsForStar");
        ClassDescriptor descriptor = new ClassDescriptor("class", "=", "*");
        Input filteredData = new Input();
        filteredData.addAttribute("marker", "integer", "");
        filteredData.addAttribute("class", "nominal", "{a, b}");
        filteredData.addEvent("1", "a");
        filteredData.addEvent("2", "b");
        filteredData.addEvent("3", "b");
        filteredData.addEvent("4", "a");
        agh.aq21gui.model.input.Test run = new Run();
        run.addParameter("customParameter", "customValue");
        run.enforceClass(descriptor);
        filteredData.runsGroup.runs.add(run);
        J48Service.expandRunsForStar(filteredData, descriptor);
        
        assertEquals(2, filteredData.runsGroup.runs.size());
        agh.aq21gui.model.input.Test run1 = filteredData.runsGroup.runs.get(0);
        agh.aq21gui.model.input.Test run2 = filteredData.runsGroup.runs.get(1);
        assertNotSame(run1, run2);
        List<ClassDescriptor> descriptors1 = run1.findConsequentParam().getDescriptors();
        List<ClassDescriptor> descriptors2 = run2.findConsequentParam().getDescriptors();
        assertEquals(1, descriptors1.size());
        assertEquals(1, descriptors2.size());
        String v1 = descriptors1.get(0).getValue();
        String v2 = descriptors2.get(0).getValue();
        boolean case1 = v1.equalsIgnoreCase("a")&&v2.equalsIgnoreCase("b");
        boolean case2 = v2.equalsIgnoreCase("a")&&v1.equalsIgnoreCase("b");
        assertTrue(case1 || case2);
        
        assertEquals("customvalue", run1.findParam("customparameter").value);
        assertEquals("customvalue", run2.findParam("customparameter").value);
    }

    @Test
    public void testConvertAndRun() {
        System.out.println("convertAndRun");
        J48Service instance = new J48Service();
        Output experiment = RulePrunnerTest.generatePrunnableOutput();
        List<agh.aq21gui.model.input.Test> runs = new J48ArchetypeConfig().createConfig(experiment);
        RunsGroup runsGroup = new RunsGroup();
        runsGroup.setRuns(runs);
        experiment.setRunsGroup(runsGroup);
        Output result = instance.convertAndRun(experiment);
        List<Hypothesis> to_verify = filterTrueClass(result.getOutputHypotheses());
        RulePrunnerTest.verifyPrunnableResult(to_verify);
    }

    @Test
    public void testRunAll() {
        System.out.println("runAll");
        J48Service instance = new J48Service();
        Output experiment = RulePrunnerTest.generateTwicePrunnableOutput();
        List<agh.aq21gui.model.input.Test> runs = new J48ArchetypeConfig().createConfig(experiment);
        RunsGroup runsGroup = new RunsGroup();
        runsGroup.setRuns(runs);
        experiment.setRunsGroup(runsGroup);
        List<Hypothesis> result = instance.runAll(experiment);
        List<Hypothesis> to_verify = filterTrueClass(result);
        RulePrunnerTest.verifyTwicePrunnableResult(to_verify);
    }

    public static List<Hypothesis> filterTrueClass(List<Hypothesis> result) {
        List<Hypothesis> to_verify = new LinkedList<Hypothesis>();
        for (Hypothesis hypo : result) {
            if (hypo.getClasses().get(0).getValue().equalsIgnoreCase("t")){
                to_verify.add(hypo);
            }
        }
        return to_verify;
    }

}
