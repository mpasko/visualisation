/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.adidata.scripts;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.IResource;
import agh.aq21gui.J48Resource;
import agh.aq21gui.JRipResource;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.services.DiscretizerRanges;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.Util;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import agh.adidata.scripts.testtools.MeasurmentResultFormatter;

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
    public static final String UDAR = "udarnosc";
    public static final String G_PLAST = "granica_plast_mpa";
    public static final String W_ZME = "wytrzym_zmecz_mpa";
    public static final String FRAC = "frac_toughness";
    
    public static StringBuilder knowledge;

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

    public static List<String> stopOnly() {
        return Util.strings(STOP);
    }
    private Input inputPattern;
    private List<Entry<IResource, String>> algSet;
    private List<DiscretizerRanges> ranges = new LinkedList<DiscretizerRanges>();
    private MeasurmentResultFormatter statTable;
    private final String suiteName;
    public static final String outputDirectory = ScriptsConfiguration.getParam("results_path");
    private boolean humanize = false;

    public ADIExperiment(String suiteName) {
        this.suiteName = suiteName;
        this.knowledge = new StringBuilder();
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

    public void runAllPossibilities(String className, String threshold, List<String> ignore, String caption) {
        try {
            statTable = new MeasurmentResultFormatter();
            for (Entry<IResource, String> entry : algSet) {
                ExperimentCase exp = new ExperimentCase(entry.getKey(), entry.getValue(), className, threshold, ignore, suiteName);
                exp.setHumanize(this.humanize);
                exp.setStats(statTable);
                exp.setOutputDirectory(outputDirectory);
                exp.setDiscretizerRanges(ranges);
                exp.runExperiment(inputPattern);
                knowledge.append(exp.knowledge);
                knowledge.append("\n====================================================\n");
            }
        } catch (RuntimeException ex) {
            String message = String.format("%s (%s) caused an exception: %s", suiteName, caption, ex.getMessage());
            System.out.println(message);
        }
        String table = statTable.formatTextResults();
        System.out.println(table);
        formatAndSaveLatexTable(caption);
    }

    /**
     * @param ranges the ranges to set
     */
    public void setRanges(List<DiscretizerRanges> ranges) {
        this.ranges = ranges;
    }

    public void formatAndSaveLatexTable(String caption) {
        String latex = statTable.formatLatexResults(caption);
        String temporaryOutput = ScriptsConfiguration.getParam("latex_tables");
        //String temporaryOutput = outputDirectory;
        String latexFile = String.format("%slatex\\%s.tex", temporaryOutput, FormatterUtil.makeFilenameFromText(caption));
        Util.saveFile(latexFile, latex, "UTF-8");
    }

    public void saveKnowledge() {
            String full_name = String.format("%s%s_knowledge.txt", outputDirectory, suiteName);
            Util.saveFile(full_name, knowledge.toString());
    }

    void setHumanize(boolean humanize) {
        this.humanize=humanize;
    }
}
