/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.splitter;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.utils.NumericUtil;
import java.util.Collection;

/**
 *
 * @author marcin
 */
public class Merger {
    public Output merge(Input source, Collection<SingleRun> results) {
        Output globalResult = new Output(source);
        for (SingleRun runResult : results) {
            globalResult.merge(runResult.out);
            fixupHypothesisNames(runResult.out, runResult.run);
        }
        return globalResult;
    }

    private void fixupHypothesisNames(Output result, Test run) {
        int number = 0;
        for (Hypothesis h : result.getOutputHypotheses()) {
            h.name = run.getName() + "_" + NumericUtil.formatNumber(number++);
        }
    }
}
