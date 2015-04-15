/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.RunsGroup;
import agh.aq21gui.model.output.Output;

/**
 *
 * @author marcin
 */
public interface IResource {
    public Output performExperiment(Input in);
    public RunsGroup generateConfig(Input in);
    public String getName();
}
