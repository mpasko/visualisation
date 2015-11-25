/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils.chain;

import java.util.LinkedList;

/**
 *
 * @author marcin
 */
public class Chain<T, R> implements IAgregatorCase<T, R>{
    
    private LinkedList<IAgregatorCase> list = new LinkedList<IAgregatorCase>();
    
    public Chain() {}
    
    public Chain(IAgregatorCase ... incidents) {
        for (IAgregatorCase incident : incidents) {
            add(incident);
        }
    }
    
    @Override
    public R agregate(T item) {
        IAgregatorCase result = findOneThatMatches(item);
        if (result != null) {
            return (R) result.agregate(item);
        }
        if (item instanceof Pair<?>) {
            result = findOneThatMatches(reverse(item));
        }
        if (result != null) {
            return (R) result.agregate(reverse(item));
        }
        throw new CaseNotSupportedException(item);
    }
    
    public final void add(IAgregatorCase incident) {
        list.addLast(incident);
    }
    
    private T reverse(T item) {
        if (item instanceof Pair<?>) {
            Pair<?> pair = (Pair<?>) item;
            return (T) pair.reverse();
        } else {
            return item;
        }
    }

    private IAgregatorCase findOneThatMatches(T item) {
        for (IAgregatorCase agregator : list) {
            if (agregator.matches(item)) {
                return agregator;
            }
        }
        return null;
    }

    @Override
    public boolean matches(T item) {
        boolean matchesStrict = findOneThatMatches(item)!=null;
        return matchesStrict || findOneThatMatches(reverse(item))!=null;
    }
}
