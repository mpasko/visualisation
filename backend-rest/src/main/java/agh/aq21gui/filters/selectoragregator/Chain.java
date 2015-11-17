/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters.selectoragregator;

import agh.aq21gui.model.output.Selector;
import java.util.LinkedList;
import java.util.Locale;

/**
 *
 * @author marcin
 */
public class Chain implements IAgregatorCase{
    
    private LinkedList<IAgregatorCase> list = new LinkedList<IAgregatorCase>();
    
    public Chain() {}
    
    public Chain(IAgregatorCase ... incidents) {
        for (IAgregatorCase incident : incidents) {
            add(incident);
        }
    }
    
    @Override
    public Selector agregate(Selector next, Selector actual) {
        IAgregatorCase result = findOneThatMatches(next, actual);
        if (result == null) {
            result = findOneThatMatchesReversed(next, actual);
            if (result == null) {
                throw new CaseNotSupportedException(next, actual);
            } else {
                return result.agregate(actual, next);
            }
        } else {
            return result.agregate(next, actual);
        }
    }
    
    public final void add(IAgregatorCase incident) {
        list.addLast(incident);
    }

    private IAgregatorCase findOneThatMatches(Selector next, Selector actual) {
        for (IAgregatorCase item : list) {
            if (item.matches(next, actual)) {
                return item;
            }
        }
        return null;
    }

    private IAgregatorCase findOneThatMatchesReversed(Selector next, Selector actual) {
        return findOneThatMatches(actual, next);
    }

    @Override
    public boolean matches(Selector next, Selector actual) {
        boolean matchesStrict = findOneThatMatches(next, actual)!=null;
        return matchesStrict || findOneThatMatchesReversed(next, actual)!=null;
    }
}
