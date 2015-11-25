/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Util;
import agh.aq21gui.utils.chain.Chain;
import agh.aq21gui.utils.chain.IAgregatorCase;
import java.util.List;

/**
 *
 * @author marcin
 */
class ValueMatcher {
    private final Chain<Token, Boolean> chain;

    public ValueMatcher(List<String> linearOrder) {
        chain = new Chain<Token, Boolean>();
        chain.add(new RangeCase(linearOrder));
        chain.add(new SetCase());
        chain.add(new MixedCase(linearOrder));
        chain.add(new LinearAndNumericCase(linearOrder));
    }

    boolean matchesValue(ClassDescriptor descriptor, String actualValue) {
        return chain.agregate(new Token(descriptor, actualValue));
    }
    
    public static class Token{
        public ClassDescriptor descriptor;
        public String actualValue;

        public Token(ClassDescriptor descriptor, String actualValue) {
            this.descriptor = descriptor;
            this.actualValue = actualValue;
        }
        
    }

    private static double getDoubleValueOf(String string_value, List<String> linearOrder) {
        if (NumericUtil.isNumber(string_value)) {  
            return NumericUtil.tryParse(string_value);
        } else {
            return Util.indexOfIgnoreCase(linearOrder, string_value);
        }
    }

    public static class RangeCase implements IAgregatorCase<Token, Boolean> {
        private final List<String> order;
        
        public RangeCase(List<String> linearOrder){
            this.order = linearOrder;
        }
                
        @Override
        public boolean matches(Token input) {
            return input.descriptor.hasRange()&&!input.descriptor.hasSet();
        }

        @Override
        public Boolean agregate(Token input) {
            double begin = getDoubleValueOf(input.descriptor.range_begin, order);
            double end = getDoubleValueOf(input.descriptor.range_end, order);
            double actual = getDoubleValueOf(input.actualValue, order);
            final boolean isBetween = ((begin <= actual) && (actual <= end)) || ((end <= actual) && (actual <= begin));
            boolean result = false;
            if (input.descriptor.comparator.equals("=")) {
                result = isBetween;
            } else if (input.descriptor.comparatorIsNonequality()) {
                result = !isBetween;
            }
            return result;
        }
    }

    private static class SetCase implements IAgregatorCase<Token, Boolean> {
                
        @Override
        public boolean matches(Token input) {
            return input.descriptor.hasSet() && !input.descriptor.hasRange();
        }

        @Override
        public Boolean agregate(Token input) {
            boolean matches_any = false;
            for (String elem : input.descriptor.set_elements) {
                boolean matches = elem.equalsIgnoreCase(input.actualValue);
                matches_any |= matches;
            }
            boolean result = false;
            if (input.descriptor.comparator.equals("=")) {
                result = matches_any;
            } else if (input.descriptor.comparatorIsNonequality()) {
                result = !matches_any;
            }
            return result;
        }
    }
    
    private static class MixedCase implements IAgregatorCase<Token, Boolean> {
        private final List<String> order;
        
        public MixedCase(List<String> linearOrder){
            this.order = linearOrder;
        }

        @Override
        public boolean matches(Token input) {
            return input.descriptor.hasSet() && input.descriptor.hasRange();
        }

        @Override
        public Boolean agregate(Token input) {
            if (input.descriptor.comparatorIsNonequality()) {
                return new SetCase().agregate(input) && new RangeCase(order).agregate(input);
            } else {
                return new SetCase().agregate(input) || new RangeCase(order).agregate(input);
            }
        }
    }

    private static class LinearAndNumericCase implements IAgregatorCase<Token, Boolean> {
        private final List<String> order;

        public LinearAndNumericCase(List<String> linearOrder) {
            this.order = linearOrder;
        }

        @Override
        public boolean matches(Token input) {
            double right = getDoubleValueOf(input.descriptor.getValue(), order);
            double left = getDoubleValueOf(input.actualValue, order);
            boolean noOrder = order==null || order.isEmpty();
            boolean notNumbers = Double.isNaN(left) || Double.isNaN(right);
            return !(notNumbers && noOrder);
        }

        @Override
        public Boolean agregate(Token input) {
            double right = getDoubleValueOf(input.descriptor.getValue(), order);
            double left = getDoubleValueOf(input.actualValue, order);
            return matchesDoubleValue(input.descriptor, left, right);
        }

        private boolean matchesDoubleValue(ClassDescriptor desc, double left, double right) {
            boolean result;
            result = false;
            if (desc.comparator.equals("<")) {
                result = left < right;
            } else if (desc.comparator.equals(">")) {
                result = left > right;
            } else if (desc.comparator.equals(">=")) {
                result = left >= right;
            } else if (desc.comparator.equals("<=")) {
                result = left <= right;
            } else if (desc.comparatorIsNonequality()) {
                result = Math.abs(right - left) > NumericUtil.EPSILON;
            } else if (desc.comparator.equals("=")) {
                result = Math.abs(right - left) < NumericUtil.EPSILON;
            }
            return result;
        }
    }
}
