/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.adidata;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.IResource;
import agh.aq21gui.J48Resource;
import agh.aq21gui.MetricsResource;
import agh.aq21gui.filters.RuleSorter;
import agh.aq21gui.evaluator.StatsAgregator;
import agh.aq21gui.filters.AttributeRemover;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.stubs.StubFactory;
import java.util.List;

/**
 *
 * @author marcin
 */
public class ADIExperiment {
    
    public void runAllPossibilities(String className, String threshold, List<String> ignore) {
		runExperiment(new J48Resource(), "", className, threshold, ignore);
        runExperiment(new Aq21Resource(), "pd", className, threshold, ignore);
        runExperiment(new Aq21Resource(), "atf", className, threshold, ignore);
        runExperiment(new Aq21Resource(), "tf", className, threshold, ignore);
    }
    
    public void runExperiment(IResource resource, String mode, String className, String threshold, List<String> ignore) {
        System.out.println(String.format("%s Experiment, mode=%s:", resource.getName(), mode));
        Input input = StubFactory.loadAdiData();
        RunsGroup runsGroup = resource.generateConfig(input);
        runsGroup.enforceClassForAll(className, threshold);
        runsGroup.enforceModeForAll(mode);
        input.setRunsGroup(runsGroup);
        input = new AttributeRemover().dropAttributes(input, ignore);
        //System.out.println("After setting correct class:");
        //System.out.println(input.toString());
        Output result = resource.performExperiment(input);
        Output sortedResult = new RuleSorter().sort(result);
        if (sortedResult.countEvents()==0){
            sortedResult.replaceEventsGroup(input.obtainEventsGroup());
        }
        System.out.println(sortedResult.obtainOutputHypotheses().toString());
        StatsAgregator metrics = new MetricsResource().analyze(sortedResult);
        System.out.println(metrics.getTotall());
    }
}
