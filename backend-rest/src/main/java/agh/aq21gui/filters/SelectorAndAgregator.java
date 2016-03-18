/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.filters.selectoragregator.ExcludingSelectorsException;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Util;
import agh.aq21gui.utils.chain.Chain;
import agh.aq21gui.utils.chain.IAgregatorCase;
import agh.aq21gui.utils.chain.Pair;
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
        List<String> stringsOrder = input.findDomainObjectRecursively(next.name).getRange();
        Chain<Pair<Selector>, Selector> chain = new Chain<Pair<Selector>, Selector>();
        chain.add(new LessAndGreaterCase(stringsOrder));
        chain.add(new LessOrGreaterSameDirection(stringsOrder));
        chain.add(new RangeAndLessCase());
        chain.add(new TwoRangesCase());
        chain.add(new EqualityAndInequality());
        chain.add(new TwoSetsCase());
        return chain.agregate(new Pair<Selector>(next, actual));
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
    
    private static class TwoRangesCase implements IAgregatorCase<Pair<Selector>, Selector> {

        @Override
        public boolean matches(Pair<Selector> item) {
            boolean r1 = item.next.hasRange();
            boolean r2 = item.actual.hasRange();
            return r1&&r2;
        }

        @Override
        public Selector agregate(Pair<Selector> item) {
            return aggregateTwoRanges(item.next, item.actual);
        }
        
    }
    
    private static class RangeAndLessCase implements IAgregatorCase<Pair<Selector>, Selector> {

        @Override
        public boolean matches(Pair<Selector> item) {
            char c1 = item.actual.comparator.charAt(0);
            boolean is_sharp = c1=='<' || c1 == '>';
            return item.next.hasRange()&&is_sharp;
        }

        @Override
        public Selector agregate(Pair<Selector> item) {
            return aggregateRangeWithSel(item.next, item.actual);
        }
        
    }

    private static class LessAndGreaterCase implements IAgregatorCase<Pair<Selector>, Selector> {
        private final List<String> string_sequence;

        public LessAndGreaterCase(List<String> string_sequence) {
            this.string_sequence = string_sequence;
        }

        @Override
        public boolean matches(Pair<Selector> item) {
            boolean symmetric_comparators = item.next.comparator.charAt(0) == '<' && item.actual.comparator.charAt(0) == '>';
            return symmetric_comparators;
        }

        @Override
        public Selector agregate(Pair<Selector> item) {
            if (NumericUtil.compareDoubles(item.next.getDoubleValue(string_sequence), item.actual.getDoubleValue(string_sequence))) {
                Selector result = getPrototype(item.next);
                result.setValue(item.actual.getValue());
                return result;
            }
            boolean values_match = item.next.getDoubleValue(string_sequence) > item.actual.getDoubleValue(string_sequence);
            if (values_match) {
                Selector result = getPrototype(item.next);
                result.setRange_begin(item.actual.getValue());
                result.setRange_end(item.next.getValue());
                return result;
            } else {
                throw new ExcludingSelectorsException(item.next, item.actual);
            }
        }
    }

    private static class LessOrGreaterSameDirection implements IAgregatorCase<Pair<Selector>, Selector> {
        private final List<String> string_sequence;

        public LessOrGreaterSameDirection(List<String> string_sequence) {
            this.string_sequence = string_sequence;
        }

        @Override
        public boolean matches(Pair<Selector> item) {
            boolean same_comparators = item.next.comparator.charAt(0) == item.actual.comparator.charAt(0);
            boolean inequality_comparators = item.next.comparator.charAt(0) == '<' || item.next.comparator.charAt(0) == '>';
            return same_comparators&&inequality_comparators;
        }

        @Override
        public Selector agregate(Pair<Selector> item) {
            if (item.next.comparator.charAt(0)=='<') {
                if (item.next.getDoubleValue(string_sequence) < item.actual.getDoubleValue(string_sequence)) {
                    return item.next;
                } else {
                    return item.actual;
                }
            } else {
                if (item.next.getDoubleValue(string_sequence) > item.actual.getDoubleValue(string_sequence)) {
                    return item.next;
                } else {
                    return item.actual;
                }
            }
        }
    }

    private static class EqualityAndInequality implements IAgregatorCase<Pair<Selector>, Selector> {

        public EqualityAndInequality() {
        }

        @Override
        public boolean matches(Pair<Selector> item) {
            boolean formal_assumption = item.next.comparator.equals("=") && item.actual.comparatorIsNonequality();
            boolean helper_assumption = !item.next.hasRange()&&!item.actual.hasRange()&&!item.next.hasSet()&&!item.actual.hasSet();
            return formal_assumption&& helper_assumption;
        }

        @Override
        public Selector agregate(Pair<Selector> item) {
            if (item.next.getValue().equalsIgnoreCase(item.actual.getValue())) {
                throw new ExcludingSelectorsException(item.next, item.actual);
            } else {
                return item.next;
            }
        }
    }

    private static class TwoSetsCase implements IAgregatorCase<Pair<Selector>, Selector> {

        @Override
        public boolean matches(Pair<Selector> item) {
            return item.next.hasSet()&&!item.actual.hasRange();
        }

        @Override
        public Selector agregate(Pair<Selector> item) {
            LinkedList<String> items = new LinkedList<String>();
            if (item.actual.hasSet()) {
                for (String elem : item.actual.getSet_elements()) {
                    if (Util.containsIgnoreCase(item.next.getSet_elements(), elem)) {
                        items.add(elem);
                    }
                }
            } else {
                String val = item.actual.getValue();
                if (Util.containsIgnoreCase(item.next.getSet_elements(), val)) {
                    items.add(val);
                }
            }
            if (items.size()<=0) {
                throw new ExcludingSelectorsException(item.next, item.actual);
            } else {
                Selector result = getPrototype(item.next);
                result.setSet_elements(items);
                return result;
            }
        }
    }
    
}
