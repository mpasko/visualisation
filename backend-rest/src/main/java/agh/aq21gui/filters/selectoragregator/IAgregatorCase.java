/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters.selectoragregator;

import agh.aq21gui.model.output.Selector;

/**
 *
 * @author marcin
 */
public interface IAgregatorCase {
    public boolean matches(Selector next, Selector actual);
    public Selector agregate(Selector next, Selector actual);
}
