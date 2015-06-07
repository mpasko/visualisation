/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.NumericUtil;

/**
 *
 * @author marcin
 */
public class SelectorAgregator {

    public static Selector agregateTwoSels(Selector next, Selector actual) {
        char c1 = next.getComparator().charAt(0);
        char c2 = actual.getComparator().charAt(0);
        Selector result = new Selector();
        result.setName(next.getName());
        double val1 = NumericUtil.tryParse(next.getValue());
        double val2 = NumericUtil.tryParse(actual.getValue());
        if (c1 == c2) {
            if (c1 == '<') {
                if (val1 < val2) {
                    return next;
                } else {
                    return actual;
                }
            } else if (c1 == '>') {
                if (val1 > val2) {
                    return next;
                } else {
                    return actual;
                }
            }
        } else {
            if (c1 == '<' && c2 == '>') {
                if (val2 < val1) {
                    result.setRange_begin(NumericUtil.stringValueOf(val2));
                    result.setRange_end(NumericUtil.stringValueOf(val1));
                    result.setComparator("=");
                } else {
                    String message = String.format("Excluding selectors:%s and:%s", next.toString(), actual.toString());
                    throw new RuntimeException(message);
                }
            } else if (c1 == '>' && c2 == '<') {
                if (val1 < val2) {
                    result.setRange_begin(NumericUtil.stringValueOf(val1));
                    result.setRange_end(NumericUtil.stringValueOf(val2));
                    result.setComparator("=");
                } else {
                    String message = String.format("Excluding selectors:%s and:%s", next.toString(), actual.toString());
                    throw new RuntimeException(message);
                }
            }
        }
        return result;
    }
    
}
