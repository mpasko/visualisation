/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.adidata;

import agh.aq21gui.Aq21Resource;
import agh.aq21gui.IResource;
import agh.aq21gui.J48Resource;
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
public class PropertyRecognitionChemical {
    private static final String WYTRZYM_ZMECZ_MPA = "wytrzym_zmecz_mpa";
    private static final String RE_MPA = "re_mpa";
    private static final String RM_MPA = "rm_mpa";
    private static final String STOP = "stop";
    public static final String K1C_M_PA_VM = "K1c_MPaVm";
    public static final String A5_PERCENT = "A5_percent";
    public static final String HC_R = "HCR";
    
    public static void main(String[] args) {
        final ADIExperiment adiExperiment = new ADIExperiment();
        adiExperiment.setInput(StubFactory.loadAdiChemicalData());
        LinkedList<Entry<IResource, String>> algSet = new LinkedList<Map.Entry<IResource, String>>();
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new J48Resource(), ""));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "pd"));
        //// Error:
        //algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "atf"));
        algSet.add(new AbstractMap.SimpleEntry<IResource, String>(new Aq21Resource(), "tf"));
        adiExperiment.setAlgList(algSet);
		adiExperiment.runAllPossibilities(RM_MPA, "1257", allWithout(RM_MPA));
        adiExperiment.runAllPossibilities(RE_MPA, "1097", allWithout(RE_MPA));
        adiExperiment.runAllPossibilities(WYTRZYM_ZMECZ_MPA, "282", allWithout(WYTRZYM_ZMECZ_MPA));
        //TODO Dyskretyzacja
    }

    private static List<String> allWithout(String item) {
        List<String> strings = Util.strings(STOP, RE_MPA, WYTRZYM_ZMECZ_MPA, K1C_M_PA_VM, A5_PERCENT, HC_R);
        strings.remove(item);
        return strings;
    }
}