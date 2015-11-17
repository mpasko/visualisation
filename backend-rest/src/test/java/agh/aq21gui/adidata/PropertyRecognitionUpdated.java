/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.adidata;

import agh.aq21gui.services.DiscretizerRanges;
import agh.aq21gui.Aq21Resource;
import agh.aq21gui.IResource;
import agh.aq21gui.J48Resource;
import agh.aq21gui.JRipResource;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.Util;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static agh.aq21gui.adidata.ADIExperiment.*;

/**
 *
 * @author marcin
 */
public class PropertyRecognitionUpdated {
    
    public static void main(String[] args) {
        final ADIExperiment adiExperiment = new ADIExperiment("Property_recognition_from_ingredients_and_forgery");
        adiExperiment.setInput(StubFactory.loadAdiUpdatedData());
        LinkedList<Entry<IResource, String>> algSet = new LinkedList<Map.Entry<IResource, String>>();
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new J48Resource(), "prune"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new JRipResource(), "strict"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "pd"));
        //// Error:
        //algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "atf"));
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
		adiExperiment.runAllPossibilities(W_ROZ, null, allPropertiesWithout(W_ROZ));
        adiExperiment.runAllPossibilities(WYDL, null, allPropertiesWithout(WYDL));
        adiExperiment.runAllPossibilities(PRZEW, null, allPropertiesWithout(PRZEW));
        adiExperiment.runAllPossibilities(HRC, null, allPropertiesWithout(HRC));
        adiExperiment.runAllPossibilities(UDAR, null, allPropertiesWithout(UDAR));
        adiExperiment.runAllPossibilities(G_PLAST, null, allPropertiesWithout(G_PLAST));
        adiExperiment.runAllPossibilities(W_ZME, null, allPropertiesWithout(W_ZME));
        adiExperiment.runAllPossibilities(FRAC, null, allPropertiesWithout(FRAC));
    }
}
