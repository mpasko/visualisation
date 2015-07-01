/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.adidata;

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

/**
 *
 * @author marcin
 */
public class PropertyRecognitionUpdated {
    private static final String STOP = "stop";
    private static final String W_ROZ = "wytrzym_rozciag_Mpa";
    private static final String WYDL = "wydluzenie";
    private static final String PRZEW = "przewezenie";
    private static final String UDAR= "udarnosc";
    private static final String G_PLAST = "granica_plast_Mpa";
    private static final String W_ZME = "wytrzym_zmecz_Mpa";
    private static final String FRAC = "frac_toughness";
    
    public static void main(String[] args) {
        final ADIExperiment adiExperiment = new ADIExperiment();
        adiExperiment.setInput(StubFactory.loadAdiUpdatedData());
        LinkedList<Entry<IResource, String>> algSet = new LinkedList<Map.Entry<IResource, String>>();
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new J48Resource(), "prune"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new JRipResource(), "strict"));
        //algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "pd"));
        //// Error:
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "atf"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "tf"));
        adiExperiment.setAlgList(algSet);
        
		adiExperiment.runAllPossibilities(W_ROZ, null, allWithout(W_ROZ));
        adiExperiment.runAllPossibilities(WYDL, null, allWithout(WYDL));
        adiExperiment.runAllPossibilities(PRZEW, null, allWithout(PRZEW));
        adiExperiment.runAllPossibilities(UDAR, null, allWithout(UDAR));
        adiExperiment.runAllPossibilities(G_PLAST, null, allWithout(G_PLAST));
        adiExperiment.runAllPossibilities(W_ZME, null, allWithout(W_ZME));
        adiExperiment.runAllPossibilities(FRAC, null, allWithout(FRAC));
        
        //adiExperiment.runAllPossibilities(FRAC, null, new LinkedList<String>());
    }

    private static List<String> allWithout(String item) {
        List<String> strings = Util.strings(STOP, W_ROZ, WYDL, PRZEW, UDAR, G_PLAST, W_ZME, FRAC);
        strings.remove(item);
        return strings;
    }
}
