/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.c45;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;

/**
 *
 * @author marcin
 */
public class C45Service {

    public Output convertAndRun(Input input) {
        return new C45Invoker().invoke(input);
    }
    
}
