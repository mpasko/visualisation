/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils.chain;

import agh.aq21gui.model.output.Selector;

/**
 *
 * @author marcin
 */
public interface IAgregatorCase<T, R> {
    public boolean matches(T input);
    public R agregate(T input);
}
