/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.adidata.manualCustomTests;

import agh.adidata.scripts.ADIExperiment;
import static agh.adidata.scripts.ADIExperiment.*;
import agh.aq21gui.Aq21Resource;
import agh.aq21gui.IResource;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.services.DiscretizerRanges;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.Util;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class Aq21ATFRecreatorFromFile {
    
    public static void main(String[] args) {
        //fromReceipe();
        file();
    }

//    public static void fromReceipe() {
//        final ADIExperiment adiExperiment = new ADIExperiment("Property_recognition_from_ingredients_and_forgery");
//        adiExperiment.setInput(StubFactory.loadAdiUpdatedData());
//        LinkedList<Map.Entry<IResource, String>> algSet = new LinkedList<Map.Entry<IResource, String>>();
//        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "atf"));
//        adiExperiment.setAlgList(algSet);
//        LinkedList<DiscretizerRanges> ranges = new LinkedList<DiscretizerRanges>();
//        ranges.add(new DiscretizerRanges(W_ROZ, 820, 970, 1140, 1350, 1550));
//        ranges.add(new DiscretizerRanges(WYDL, 1, 3, 4, 5, 6, 8, 10));
//        ranges.add(new DiscretizerRanges(PRZEW, 0.5, 1., 2., 4., 6., 8.));
//        ranges.add(new DiscretizerRanges(HRC, 36.5, 45.));
//        ranges.add(new DiscretizerRanges(UDAR, 35, 60, 80, 100, 110));
//        ranges.add(new DiscretizerRanges(G_PLAST, 600, 700, 850, 850, 1100, 1350));
//        ranges.add(new DiscretizerRanges(W_ZME, 225, 240, 265, 275));
//        ranges.add(new DiscretizerRanges(FRAC, 50, 54, 59, 60, 62));
//        adiExperiment.setRanges(ranges);
//        String caption = "Zale\u017Cno\u015B\u0107 od sk\u0142adu i sposobu otrzymywania dla ";
//        adiExperiment.runAllPossibilities(W_ROZ, null, allPropertiesWithout(W_ROZ), caption + W_ROZ);
//    }


    private static void file() {
        /*
        final String data = Util.loadFile("test_inputs/atf_crush_reproduction.csv");
        CSVConverter converter = new CSVConverter();
        Input in = converter.convert(data);
        */
        Aq21Resource resource = new Aq21Resource();
        Input in = resource.fromAQ21(Util.loadFile("test_inputs/atf_crush_reproduction.aq21"));
        /*
        RunsGroup runsGroup = resource.generateConfig(in);
        in.setRunsGroup(runsGroup);
        //in.getAggregatedClassDescriptor().setName("");
        in.getRunsGroup().runs.get(0).addParameter("Mode", "ATF");
        */
        Output out = resource.performExperiment(in);
        System.out.println(out.toString());
    }
}
