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
        if (!next.range_begin.isEmpty()){
            return aggregateRangeWithSel(next, actual);
        } else if (!actual.range_begin.isEmpty()) {
            return aggregateRangeWithSel(actual, next);
        }
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

    private static Selector aggregateRangeWithSel(Selector range, Selector sel) {
        if (!sel.getRange_begin().isEmpty()) {
            return aggregateTwoRanges(range, sel);
        }
        char c1 = sel.getComparator().charAt(0);
        Selector result = new Selector();
        result.setName(range.getName());
        result.setComparator("=");
        double begin = NumericUtil.tryParse(range.getRange_begin());
        double end = NumericUtil.tryParse(range.getRange_end());
        result.setRange_begin(range.getRange_begin());
        result.setRange_end(range.getRange_end());
        double val1 = NumericUtil.tryParse(sel.getValue());
        String error = String.format("Excluding selectors:%s and:%s", range.toString(), sel.toString());
        if (c1 == '<') {
            if(val1<begin) {
                throw new RuntimeException(error);
            }
            if(val1<end) {
                result.setRange_end(sel.getValue());
            }
        } else if (c1 == '>') {
            if(val1>end) {
                throw new RuntimeException(error);
            }
            if(val1>begin) {
                result.setRange_begin(sel.getValue());
            }
        } else if (c1 == '=') {
            if ((val1<begin)||(val1>end)) {
                throw new RuntimeException(error);
            } else {
                result = sel;
            }
        }
        return result;
    }

    private static Selector aggregateTwoRanges(Selector first, Selector second) {
        Selector result = new Selector();
        result.setName(first.getName());
        double begin1 = NumericUtil.tryParse(first.getRange_begin());
        double end1 = NumericUtil.tryParse(first.getRange_end());
        double begin2 = NumericUtil.tryParse(second.getRange_begin());
        double end2 = NumericUtil.tryParse(second.getRange_end());
        double newbegin = Math.max(begin1, begin2);
        double newend = Math.min(end1, end2);
        if (newend < newbegin) {
            String error = String.format("Excluding selectors:%s and:%s", first.toString(), second.toString());
            throw new RuntimeException(error);
        }
        result.setRange_begin(Double.valueOf(newbegin).toString());
        result.setRange_end(Double.valueOf(newend).toString());
        result.setComparator("=");
        return result;
    }
    
}
