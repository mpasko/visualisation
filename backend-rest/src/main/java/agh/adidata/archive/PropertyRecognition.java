/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.adidata.archive;

import agh.adidata.scripts.ADIExperiment;
import agh.aq21gui.stubs.StubFactory;
import agh.aq21gui.utils.Util;
import java.util.List;

/**
 *
 * @author marcin
 */
public class PropertyRecognition {
    private static final String WYTRZYM_ZMECZ_MPA = "wytrzym_zmecz_mpa";
    private static final String RE_MPA = "re_mpa";
    private static final String RM_MPA = "rm_mpa";
    private static final String STOP = "stop";
    
    public static void main(String[] args) {
        final ADIExperiment adiExperiment = new ADIExperiment("PropertyRecognition_classic");
		adiExperiment.runAllPossibilities(RM_MPA, "1257", Util.strings(STOP, RE_MPA, WYTRZYM_ZMECZ_MPA), "");
        adiExperiment.runAllPossibilities(RE_MPA, "1097", Util.strings(STOP, RM_MPA, WYTRZYM_ZMECZ_MPA), "");
        adiExperiment.runAllPossibilities(WYTRZYM_ZMECZ_MPA, "282", Util.strings(STOP, RE_MPA, RM_MPA), "");
    }
}
