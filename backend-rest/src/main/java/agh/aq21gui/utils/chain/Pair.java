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
@SuppressWarnings("Unused")
public class Pair<T> {
    public T next;
    public T actual;

    public Pair(T next, T actual) {
        this.next = next;
        this.actual = actual;
    }
    
    @Override public String toString() {
        return String.format("%s, %s", next.toString(), actual.toString());
    }
    public Pair<T> reverse() {
        Pair<T> pair = new Pair<T>(actual, next);
        return pair;
    }
}
