/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.adidata;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.IResource;
import agh.aq21gui.J48Resource;
import agh.aq21gui.JRipResource;
import agh.aq21gui.MetricsResource;
import agh.aq21gui.evaluator.StatsAgregator;
import agh.aq21gui.filters.AttributeRemover;
import agh.aq21gui.filters.ContinuousClassFilter;
import agh.aq21gui.filters.RuleAgregator;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.DiscretizerRanges;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Printer;
import agh.aq21gui.utils.Util;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import testtools.MeasurmentResultFormatter;

/**
 *
 * @author marcin
 */
public class ADIExperiment {
    
    public static final String STOP = "stop";
    public static final String W_ROZ = "wytrzym_rozciag_mpa";
    public static final String WYDL = "wydluzenie";
    public static final String PRZEW = "przewezenie";
    public static final String HRC = "hrc";
    public static final String UDAR= "udarnosc";
    public static final String G_PLAST = "granica_plast_mpa";
    public static final String W_ZME = "wytrzym_zmecz_mpa";
    public static final String FRAC = "frac_toughness";

    public static List<String> allPropertiesWithout(String item) {
        List<String> strings = Util.strings(STOP, W_ROZ, WYDL, PRZEW, HRC, UDAR, G_PLAST, W_ZME, FRAC);
        strings.remove(item);
        return strings;
    }
    
    public static List<String> allElementsAndreceipe() {
        LinkedList<String> strings = new LinkedList<String>();
        strings.addAll(Util.strings("C", "Si", "Mn", "Mg", "Cu", "Ni", "Mo", "S", "P", "B", "V", "Cr", "Ti", "Sn", "Nb", "Al"));
        strings.addAll(Util.strings("aust_temp_C", "aust_czas_min", "wygrz_izoterm_temp_C", "wygrz_izoterm_czas_min", "osrodek"));
        strings.add(STOP);
        return strings;
    }
    
    private Input inputPattern;
    private List<Entry<IResource, String>> algSet;
    private List<DiscretizerRanges> ranges = new LinkedList<DiscretizerRanges>();
    private MeasurmentResultFormatter statTable;

    public ADIExperiment() {
        generateDefaultAlgorithmSet();
    }

    /**
     * @param input the input to set
     */
    public void setInput(Input input) {
        this.inputPattern = input;
    }

    public void setAlgList(List<Entry<IResource, String>> list) {
        this.algSet = list;
    }

    private void generateDefaultAlgorithmSet() {
        setInput(StubFactory.loadAdiData());
        algSet = new LinkedList<Entry<IResource, String>>();
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new J48Resource(), "prune"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new JRipResource(), "strict"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "pd"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "atf"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "tf"));
    }

    public void runAllPossibilities(String className, String threshold, List<String> ignore) {
        statTable = new MeasurmentResultFormatter();
        for (Entry<IResource, String> entry : algSet) {
            runExperiment(entry.getKey(), entry.getValue(), className, threshold, ignore);
        }
        String table = statTable.formatTextResults(this);
        System.out.println(table);
    }

    public void runExperiment(IResource resource, String mode, String className, String threshold, List<String> ignore) {
        String name = String.format("%s-%s", resource.getName().replace("Resource", ""), mode);
        System.out.println(String.format("Experiment, mode=%s:", name));
        Input input = Util.deepCopyInput(inputPattern);
        initializeProperConfiguration(resource, input, className, threshold, mode);
        input = inputPreProcessing(input, ignore);
        Output result = resource.performExperiment(input);
        Output processed = outputPostProcessing(result);
        Printer.printLines(result.getRaw(), this.getClass());
        Printer.printLines(processed.obtainOutputHypotheses().toString(), this.getClass());
        MetricsResource metricsResource = new MetricsResource();
        if (mode.equalsIgnoreCase("strict")) {
            metricsResource.questionAsFalse = true;
        }
        StatsAgregator metrics = metricsResource.analyze(processed);
        System.out.println(metrics.toString());
        statTable.consumeResults(processed, name, metrics);
    }

    public String alignNumber(int number) {
        String stringified = Integer.valueOf(number).toString();
        return FormatterUtil.alignString(5, " ", stringified);
    }

    /**
     * @param ranges the ranges to set
     */
    public void setRanges(List<DiscretizerRanges> ranges) {
        this.ranges = ranges;
    }

    private Input applyRangeDiscretization(Input in) {
        Input result = in;
        for(DiscretizerRanges range : ranges) {
            ClassDescriptor cd = new ClassDescriptor(range.attribute, NumericUtil.dobleListToStringList(range.values));
            result = new ContinuousClassFilter().filter(result, cd, range.labels);
        }
        return result;
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
        
        processed = new RuleAgregator().agregate(processed);
        /* *x/
        processed = new RuleVerticalAgregator().agregate(processed);
        /* *x/
        processed = new RulePrunner().doAll(processed);
        /* *x/
        processed = new RuleSorter().sort(processed);
        /* */
        return processed;
    }

    public Input inputPreProcessing(Input input, List<String> ignore) {
        input = applyRangeDiscretization(input);
        input = new AttributeRemover().dropAttributes(input, ignore);
        return input;
    }
}
