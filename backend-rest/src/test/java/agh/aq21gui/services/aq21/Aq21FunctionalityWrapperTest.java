/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.aq21;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.NumericUtil;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class Aq21FunctionalityWrapperTest {
    public static final String ACLASS = "class";
    public static final int size = 10;
    
    public Aq21FunctionalityWrapperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void when_continuous_then_should_perform_discretization() {
        System.out.println("when_continuous_the_should_perform_discretization");
        Input in = generateInputWithStarRun(size);
        Aq21FunctionalityWrapper instance = new Aq21FunctionalityWrapper();
        Output result = instance.enhancedInvoke(in);
        List<String> items = result.getCollumnOfData(ACLASS);
        
        assertEquals(size, items.size());
        for (String item : items) {
            assertFalse(NumericUtil.isNumber(item));
        }
        List<String> discretizedSet = result.findDomainObjectRecursively("class").getRange();
        assertEquals(1, result.getRunsGroup().runs.size());
        assertEquals(discretizedSet.size(), result.getOutputHypotheses().size());
        assertTrue(discretizedSet.size()>2);
        assertTrue(result.getAggregatedClassDescriptor().isCustomValue());
    }

    @Test
    public void when_integer_then_should_perform_good_job() {
        System.out.println("when_integer_then_should_perform_good_job");
        Input in = generateInputWithStarRun(size);
        in.findDomainObjectRecursively(ACLASS).setdomain("integer");
        Aq21FunctionalityWrapper instance = new Aq21FunctionalityWrapper();
        Output result = instance.enhancedInvoke(in);
        List<String> items = result.getCollumnOfData(ACLASS);
        
        assertEquals(size, items.size());
        for (String item : items) {
            assertFalse(NumericUtil.isNumber(item));
        }
        List<String> discretizedSet = result.findDomainObjectRecursively("class").getRange();
        assertEquals(1, result.getRunsGroup().runs.size());
        assertEquals(discretizedSet.size(), result.getOutputHypotheses().size());
        assertTrue(discretizedSet.size()>2);
        assertTrue(result.getAggregatedClassDescriptor().isCustomValue());
    }

    public static Input generateInputWithStarRun(int size) {
        Input in = StubFactory.generatePolynomialInput(size, ACLASS);
        agh.aq21gui.model.input.Test run = new Run();
        run.enforceClass(new ClassDescriptor(ACLASS, "=", "*"));
        in.runsGroup.runs.add(run);
        return in;
    }
}
