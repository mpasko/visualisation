/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.filters.selectoragregator.Chain;
import agh.aq21gui.filters.selectoragregator.ExcludingSelectorsException;
import agh.aq21gui.filters.selectoragregator.IAgregatorCase;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Util;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class SelectorAndAgregator {
    private final Input input;

    public SelectorAndAgregator(Input input) {
        this.input = input;
    }
    
    public Selector agregateTwoSels(Selector next, Selector actual) {
        /*
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
        if (next.comparatorIsNonequality()&&!actual.comparatorIsNonequality()) {
            
        } else if (!next.comparatorIsNonequality()&&actual.comparatorIsNonequality()) {
            
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
        */
        List<String> stringsOrder = input.findDomainObjectRrecursively(next.name).getRange();
        Chain chain = new Chain();
        chain.add(new LessAndGreaterCase(stringsOrder));
        chain.add(new LessOrGreaterSameDirection(stringsOrder));
        chain.add(new RangeAndLessCase());
        chain.add(new TwoRangesCase());
        chain.add(new EqualityAndInequality());
        chain.add(new TwoSetsCase());
        return chain.agregate(next, actual);
    }
    
    private static Selector getPrototype(Selector next) {
        Selector result = new Selector();
        result.setName(next.getName());
        result.setComparator("=");
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
        if (c1 == '<') {
            if(val1<begin) {
                throw new ExcludingSelectorsException(range, sel);
            }
            if(val1<end) {
                result.setRange_end(sel.getValue());
            }
        } else if (c1 == '>') {
            if(val1>end) {
                throw new ExcludingSelectorsException(range, sel);
            }
            if(val1>begin) {
                result.setRange_begin(sel.getValue());
            }
        } else if (c1 == '=') {
            if ((val1<begin)||(val1>end)) {
                throw new ExcludingSelectorsException(range, sel);
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
                throw new ExcludingSelectorsException(first, second);
        }
        result.setRange_begin(Double.valueOf(newbegin).toString());
        result.setRange_end(Double.valueOf(newend).toString());
        result.setComparator("=");
        return result;
    }
    
    private static class TwoRangesCase implements IAgregatorCase {

        @Override
        public boolean matches(Selector next, Selector actual) {
            boolean r1 = next.hasRange();
            boolean r2 = actual.hasRange();
            return r1&&r2;
        }

        @Override
        public Selector agregate(Selector next, Selector actual) {
            return aggregateTwoRanges(next, actual);
        }
        
    }
    
    private static class RangeAndLessCase implements IAgregatorCase {

        @Override
        public boolean matches(Selector next, Selector actual) {
            char c1 = actual.comparator.charAt(0);
            boolean is_sharp = c1=='<' || c1 == '>';
            return next.hasRange()&&is_sharp;
        }

        @Override
        public Selector agregate(Selector next, Selector actual) {
            return aggregateRangeWithSel(next, actual);
        }
        
    }

    private static class LessAndGreaterCase implements IAgregatorCase {
        private final List<String> string_sequence;

        public LessAndGreaterCase(List<String> string_sequence) {
            this.string_sequence = string_sequence;
        }

        @Override
        public boolean matches(Selector next, Selector actual) {
            boolean symmetric_comparators = next.comparator.charAt(0) == '<' && actual.comparator.charAt(0) == '>';
            return symmetric_comparators;
        }

        @Override
        public Selector agregate(Selector next, Selector actual) {
            if (NumericUtil.compareDoubles(next.getDoubleValue(string_sequence), actual.getDoubleValue(string_sequence))) {
                Selector result = getPrototype(next);
                result.setValue(actual.getValue());
                return result;
            }
            boolean values_match = next.getDoubleValue(string_sequence) > actual.getDoubleValue(string_sequence);
            if (values_match) {
                Selector result = getPrototype(next);
                result.setRange_begin(actual.getValue());
                result.setRange_end(next.getValue());
                return result;
            } else {
                throw new ExcludingSelectorsException(next, actual);
            }
        }
    }

    private static class LessOrGreaterSameDirection implements IAgregatorCase {
        private final List<String> string_sequence;

        public LessOrGreaterSameDirection(List<String> string_sequence) {
            this.string_sequence = string_sequence;
        }

        @Override
        public boolean matches(Selector next, Selector actual) {
            boolean same_comparators = next.comparator.charAt(0) == actual.comparator.charAt(0);
            boolean inequality_comparators = next.comparator.charAt(0) == '<' || next.comparator.charAt(0) == '>';
            return same_comparators&&inequality_comparators;
        }

        @Override
        public Selector agregate(Selector next, Selector actual) {
            if (next.comparator.charAt(0)=='<') {
                if (next.getDoubleValue(string_sequence) < actual.getDoubleValue(string_sequence)) {
                    return next;
                } else {
                    return actual;
                }
            } else {
                if (next.getDoubleValue(string_sequence) > actual.getDoubleValue(string_sequence)) {
                    return next;
                } else {
                    return actual;
                }
            }
        }
    }

    private static class EqualityAndInequality implements IAgregatorCase {

        public EqualityAndInequality() {
        }

        @Override
        public boolean matches(Selector next, Selector actual) {
            boolean formal_assumption = next.comparator.equals("=") && actual.comparatorIsNonequality();
            boolean helper_assumption = !next.hasRange()&&!actual.hasRange()&&!next.hasSet()&&!actual.hasSet();
            return formal_assumption&& helper_assumption;
        }

        @Override
        public Selector agregate(Selector next, Selector actual) {
            if (next.getValue().equalsIgnoreCase(actual.getValue())) {
                throw new ExcludingSelectorsException(next, actual);
            } else {
                return next;
            }
        }
    }

    private static class TwoSetsCase implements IAgregatorCase {

        @Override
        public boolean matches(Selector next, Selector actual) {
            return next.hasSet()&&!actual.hasRange();
        }

        @Override
        public Selector agregate(Selector next, Selector actual) {
            LinkedList<String> items = new LinkedList<String>();
            if (actual.hasSet()) {
                for (String item : actual.getSet_elements()) {
                    if (Util.containsIgnoreCase(next.getSet_elements(), item)) {
                        items.add(item);
                    }
                }
            } else {
                String item = actual.getValue();
                if (Util.containsIgnoreCase(next.getSet_elements(), item)) {
                    items.add(item);
                }
            }
            if (items.size()<=0) {
                throw new ExcludingSelectorsException(next, actual);
            } else {
                Selector result = getPrototype(next);
                result.setSet_elements(items);
                return result;
            }
        }
    }
    
}
