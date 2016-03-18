/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Output;

/**
 *
 * @author marcin
 */

public interface AbstractInvoker {
    
    public String getAppPath();
    
    public Output invoke(Input in);
}
