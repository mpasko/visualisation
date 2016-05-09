/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.adidata.scripts;

import agh.aq21gui.IResource;
import agh.aq21gui.MetricsResource;
import agh.aq21gui.evaluator.StatsAgregator;
import agh.aq21gui.filters.AttributeRemover;
import agh.aq21gui.filters.ContinuousClassFilter;
import agh.aq21gui.filters.RuleAgregator;
import agh.aq21gui.filters.RulePrunner;
import agh.aq21gui.filters.RuleSorter;
import agh.aq21gui.filters.RuleVerticalAgregator;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.DiscretizerRanges;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Printer;
import agh.aq21gui.utils.Util;
import java.util.List;
import agh.adidata.scripts.testtools.MeasurmentResultFormatter;
import agh.aq21gui.filters.Rehumaniser;

/**
 *
 * @author marcin
 */
public class ExperimentCase {
    IResource resource;
    String mode;
    String className;
    String threshold;
    List<String> ignore;
    
    String algorithmName;
    String description;
    String keyword;
    private MeasurmentResultFormatter statTable;
    private String outputDirectory;
    //private String suiteName;
    private List<DiscretizerRanges> discretizerRanges;
    public String knowledge;

    public ExperimentCase(IResource key, String value, String className, String threshold, List<String> ignore, String suiteName) {
        this.resource = key;
        this.mode = value;
        this.className = className;
        this.threshold = threshold;
        //this.suiteName = suiteName;
        this.ignore = ignore;
        algorithmName = String.format("%s-%s", resource.getName().replace("Resource", ""), mode);
        description = String.format("Experiment, mode=%s%nclass=%s%nignoring=%s", algorithmName, className, ignore);
        keyword = String.format("%s-%s-%s", suiteName, algorithmName, className);
    }
    
    public void runExperiment(Input inputPattern) {
        Input input = Util.deepCopyInput(inputPattern);
        initializeProperConfiguration(resource, input, className, threshold, mode);
        input = inputPreProcessing(input, ignore);
        Output result = resource.performExperiment(input);
        Output processed = outputPostProcessing(result);
        MetricsResource metricsResource = new MetricsResource();
        if (mode.equalsIgnoreCase("strict")) {
            metricsResource.questionAsFalse = true;
        }
        StatsAgregator metrics = metricsResource.analyze(processed);
        //here we can maintain results in human readable format:
        processed = new Rehumaniser().rehumaniseOutput(processed);
        printRegularResults(description, result, processed, metrics);
        statTable.consumeResults(processed, algorithmName, metrics);
        
        saveResume(description, processed, input, algorithmName, className, keyword);
        saveMainKnowledge(keyword, processed);
    }
    

    public void initializeProperConfiguration(IResource resource, Input input, String className, String threshold, String mode) {
        RunsGroup runsGroup = resource.generateConfig(input);
        runsGroup.enforceClassForAll(className, threshold);
        runsGroup.enforceModeForAll(mode);
        if (mode.equalsIgnoreCase("prune")) {
            runsGroup.enforceParameter("prune", "true");
        }
        input.setRunsGroup(runsGroup);
    }
    

    public Output outputPostProcessing(Output result) {
        Output processed = result;
        System.out.println("Enabled filters: ");
        /* */
        System.out.println("   -RuleAgregator");
        processed = new RuleAgregator().agregate(processed);
        /* /
        System.out.println("   -RuleVerticalAgregator");
        processed = new RuleVerticalAgregator().agregate(processed);
        /* *x/
        //processed = new RulePrunner().doAll(processed);
        processed = new RulePrunner().prune(processed);
        /* */
        System.out.println("   -RuleSorter");
        processed = new RuleSorter().sort(processed);
        /* *x/
        System.out.println("   -Rehumaniser");
        processed = new Rehumaniser().rehumaniseOutput(processed);
        /* */
        return processed;
    }

    public Input inputPreProcessing(Input input, List<String> ignore) {
        input = applyRangeDiscretization(input);
        input = new AttributeRemover().dropAttributes(input, ignore);
        return input;
    }

    private Input applyRangeDiscretization(Input in) {
        Input result = in;
        for(DiscretizerRanges range : discretizerRanges) {
            ClassDescriptor cd = new ClassDescriptor(range.attribute, NumericUtil.dobleListToStringList(range.values));
            result = new ContinuousClassFilter().filter(result, cd, range.labels);
        }
        return result;
    }

    private void saveResume(String description, Output processed, Input input, String name, String className, String keyword) {
        StringBuilder resume = new StringBuilder(description);
        resume.append("\nKeyword: ").append(keyword).append("\n");
        resume.append("\nKnowledge:\n");
        //resume.append(processed.obtainOutputHypotheses().toString());
        resume.append(processed.toString());
        
        resume.append("\nKey parameters:\n");
        List<String> keyParameters = processed.obtainOutputHypotheses().getKeyParameters();
        new Rehumaniser().rehumaniseEvents(input.obtainEventsGroup());
        resume.append(Util.formatInputByKeyParams(input, keyParameters));
        
        //System.out.println(resume.toString());
        String full_name = String.format("%s%s.txt", outputDirectory, keyword);
        Util.saveFile(full_name, resume.toString());
    }

    private void printRegularResults(String description, Output result, Output processed, StatsAgregator metrics) {
        System.out.println(description);
        Printer.printLines(result.getRaw(), this.getClass());
        Printer.printLines(processed.obtainOutputHypotheses().toString(), this.getClass());
        System.out.println(metrics.toString());
        System.out.println(metrics.generateCounterMeasuresReport(processed));
    }

    private void saveMainKnowledge(String keyword, Output output) {
        StringBuilder resume = new StringBuilder();
        resume.append("\nKeyword: ").append(keyword).append("\n");
        resume.append("\nKnowledge:\n");
        resume.append(output.obtainOutputHypotheses().toString());
        //String full_name = String.format("%s%s_knowledge.txt", outputDirectory, suiteName);
        //Util.saveFile(full_name, resume.toString());
        knowledge = resume.toString();
    }

    void runExperiment() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void setStats(MeasurmentResultFormatter statTable) {
        this.statTable = statTable;
    }

    void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /*
    void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }*/

    void setDiscretizerRanges(List<DiscretizerRanges> ranges) {
        this.discretizerRanges = ranges;
    }
}
