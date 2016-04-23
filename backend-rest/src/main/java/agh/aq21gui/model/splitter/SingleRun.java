/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.splitter;

import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.Output;

/**
 *
 * @author marcin
 */
public class SingleRun {
    public Input in;
    public Test run;
    public Output out;
    
    public SingleRun(Input in, Test run) {
        this.in = in;
        this.run = run;
    }
    
    public String getClassName() {
        return run.grepClassName();
    }
    
    public Domain getClassDomain() {
        return in.findDomainObjectRecursively(getClassName());
    }
}
