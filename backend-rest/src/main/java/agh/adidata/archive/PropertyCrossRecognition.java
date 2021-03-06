/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.adidata.archive;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.IResource;
import agh.aq21gui.J48Resource;
import agh.aq21gui.JRipResource;
import agh.adidata.scripts.ADIExperiment;
import static agh.adidata.scripts.ADIExperiment.*;
import agh.aq21gui.services.DiscretizerRanges;
import agh.aq21gui.stubs.StubFactory;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class PropertyCrossRecognition {
    
    public static void main(String[] args) {
        final ADIExperiment adiExperiment = new ADIExperiment("PropertyCrossRecognition_classic");
        adiExperiment.setInput(StubFactory.loadAdiUpdatedData());
        LinkedList<Map.Entry<IResource, String>> algSet = new LinkedList<Map.Entry<IResource, String>>();
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new J48Resource(), "prune"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new JRipResource(), "strict"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "pd"));
        //// Error:
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "atf"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "tf"));
        adiExperiment.setAlgList(algSet);
        LinkedList<DiscretizerRanges> ranges = new LinkedList<DiscretizerRanges>();
        ranges.add(new DiscretizerRanges(W_ROZ, 820, 970, 1140, 1350, 1550));
        ranges.add(new DiscretizerRanges(WYDL, 1, 3, 4, 5, 6, 8, 10));
        ranges.add(new DiscretizerRanges(PRZEW, 0.5, 1., 2., 4., 6., 8.));
        ranges.add(new DiscretizerRanges(HRC, 36.5, 45.));
        ranges.add(new DiscretizerRanges(UDAR, 35, 60, 80, 100, 110));
        ranges.add(new DiscretizerRanges(G_PLAST, 600, 700, 850, 850, 1100, 1350));
        ranges.add(new DiscretizerRanges(W_ZME, 225, 240, 265, 275));
        ranges.add(new DiscretizerRanges(FRAC, 50, 54, 59, 60, 62));
        adiExperiment.setRanges(ranges);
		adiExperiment.runAllPossibilities(W_ROZ, null, allElementsAndreceipe(), "");
        adiExperiment.runAllPossibilities(WYDL, null, allElementsAndreceipe(), "");
        adiExperiment.runAllPossibilities(PRZEW, null, allElementsAndreceipe(), "");
        adiExperiment.runAllPossibilities(UDAR, null, allElementsAndreceipe(), "");
        adiExperiment.runAllPossibilities(G_PLAST, null, allElementsAndreceipe(), "");
        adiExperiment.runAllPossibilities(W_ZME, null, allElementsAndreceipe(), "");
        adiExperiment.runAllPossibilities(FRAC, null, allElementsAndreceipe(), "");
        adiExperiment.runAllPossibilities(HRC, null, allElementsAndreceipe(), "");
    }
}
