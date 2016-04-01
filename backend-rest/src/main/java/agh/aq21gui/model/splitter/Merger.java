/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.splitter;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import java.util.Collection;

/**
 *
 * @author marcin
 */
public class Merger {
    public Output merge(Input source, Collection<Output> results) {
        Output globalResult = new Output(source);
        for (Output runResult : results) {
            globalResult.merge(runResult);
        }
        return globalResult;
    }
}
