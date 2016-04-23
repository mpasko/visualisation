/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.c45;

import agh.aq21gui.filters.IllegalValueRemover;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.splitter.Merger;
import agh.aq21gui.model.splitter.SingleRun;
import agh.aq21gui.model.splitter.Splitter;
import java.util.Collection;

/**
 *
 * @author marcin
 */
public class C45FunctionalityWrapper {
    
    private IllegalValueRemover illegalValueRemover = new IllegalValueRemover();
    private Splitter splitter = new Splitter();
    private Merger merger = new Merger();
    
    public Output run(Input in) {
        C45Invoker invoker = new C45Invoker();
        Collection<SingleRun> splitted = splitter.split(in);
        for (SingleRun run : splitted) {
            Input input = illegalValueRemover.remove(run.in, run.getClassName(), "?");
            run.out = invoker.invoke(input);
        }
        return merger.merge(in, splitted);
    }
}
