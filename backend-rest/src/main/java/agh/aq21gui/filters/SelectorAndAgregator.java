/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.filters.selectoragregator.ExcludingSelectorsException;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.Selector;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Util;
import agh.aq21gui.utils.chain.Chain;
import agh.aq21gui.utils.chain.IAgregatorCase;
import agh.aq21gui.utils.chain.Pair;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        List<String> stringsOrder = prepareValueList(next);
        Chain<Pair<Selector>, Selector> chain = new Chain<Pair<Selector>, Selector>();
        chain.add(new LessAndGreaterCase(stringsOrder));
        chain.add(new LessOrGreaterSameDirection(stringsOrder));
        chain.add(new RangeAndLessCase());
        chain.add(new TwoRangesCase());
        chain.add(new EqualityAndInequality());
        chain.add(new TwoSetsCase());
        return chain.agregate(new Pair<Selector>(next, actual));
    }

    private List<String> prepareValueList(Selector next) {
        Domain domain = input.findDomainObjectRecursively(next.name);
        List<String> stringsOrder = new LinkedList<String>(domain.getRange());
        if (domain.isContinuous() || domain.isInteger()) {
            for (Map<String, Object> event : input.getEvents()) {
                String value = event.get(next.name).toString();
                if (NumericUtil.isNumber(value)) {
                    stringsOrder.add(normalize(value));
                }
            }
            Collections.sort(stringsOrder, new StringNumberComparator());
        }
        return stringsOrder;
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
                if ((item.next.comparator.length()<=1) || (item.actual.comparator.length()<=1)) {
                    throw new ExcludingSelectorsException(item.next, item.actual);
                }
                Selector result = getPrototype(item.next);
                result.setValue(item.actual.getValue());
                return result;
            }
            boolean values_match = item.next.getDoubleValue(string_sequence) > item.actual.getDoubleValue(string_sequence);
            if (values_match) {
                Selector result = getPrototype(item.next);
                String rangeStart = item.actual.getValue();
                String rangeEnd = item.next.getValue();
                try {
                if (item.actual.comparator.length()<=1) {
                    rangeStart = nextValueOf(rangeStart);
                }
                if (item.next.comparator.length()<=1) {
                    rangeEnd = prevValueOf(rangeEnd);
                }
                } catch (IndexOutOfBoundsException ex) {
                    throw new ExcludingSelectorsException(item.next, item.actual);
                }
                result.setRange_begin(rangeStart);
                result.setRange_end(rangeEnd);
                return result;
            } else {
                throw new ExcludingSelectorsException(item.next, item.actual);
            }
        }

        private String nextValueOf(String rangeStart) {
            int currentIndex = string_sequence.lastIndexOf(normalize(rangeStart));
            assertNotMinusOne(currentIndex, rangeStart);
            return string_sequence.get(currentIndex + 1);
        }

        private String prevValueOf(String rangeStart) {
            int currentIndex = string_sequence.indexOf(normalize(rangeStart));
            assertNotMinusOne(currentIndex, rangeStart);
            return string_sequence.get(currentIndex - 1);
        }

        private void assertNotMinusOne(int currentIndex, String rangeStart) throws RuntimeException {
            if (currentIndex == -1) {
                String message = String.format("Internal Error!, cannot find %s among: %s", rangeStart, string_sequence);
                throw new RuntimeException(message);
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
            double nextDouble = item.next.getDoubleValue(string_sequence);
            double actualDouble = item.actual.getDoubleValue(string_sequence);
            if (NumericUtil.compareDoubles(nextDouble, actualDouble)){
                return returnShorterComparator(item);
            }
            if (item.next.comparator.charAt(0)=='<') {
                if (nextDouble < actualDouble) {
                    return item.next;
                } else {
                    return item.actual;
                }
            } else {
                if (nextDouble > actualDouble) {
                    return item.next;
                } else {
                    return item.actual;
                }
            }
        }

        private Selector returnShorterComparator(Pair<Selector> item) {
            if (item.next.comparator.equalsIgnoreCase(item.actual.comparator)) {
                return item.next;
            } else if (item.next.comparator.length() <= 1) {
                return item.next;
            } else {
                return item.actual;
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

    private static class StringNumberComparator implements Comparator<String> {

        public StringNumberComparator() {
        }

        @Override
        public int compare(String t, String t1) {
            double a = NumericUtil.parseDoubleDefault(t, 0);
            double b = NumericUtil.parseDoubleDefault(t1, 0);
            return Double.compare(a, b);
        }
    }
    
    private static String normalize(String value) {
        if (value.endsWith(".0")) {
            return value.substring(0, value.length()-2);
        }
        return value;
    }
}
