/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.splitter;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.utils.Util;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author marcin
 */
public class Splitter {
    public Collection<SingleRun> split(Input in) {
        LinkedList<SingleRun> splited = new LinkedList<SingleRun>();
        for (Test run : in.runsGroup.runs) {
            Input copy = Util.deepCopyInput(in);
            copy.runsGroup.runs.clear();
            copy.runsGroup.runs.add(run);
            splited.add(new SingleRun(copy, run));
        }
        return splited;
    }
}
