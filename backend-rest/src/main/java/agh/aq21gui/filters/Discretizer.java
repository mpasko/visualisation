/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.utils.NumericUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author marcin
 */
public class Discretizer {

    public static enum Mode {
        HISTOGRAM, SIMILAR_SIZE
    }

    private void assertNumeric(double doublevalue) throws RuntimeException {
        if (Double.isNaN(doublevalue)) {
            throw new RuntimeException("Only numeric values supported for discretization");
        }
    }

    public Input discretize(Input in, String class_name, String buckets_number, Mode mode) {
        int events_size = countRealSize(in.getEvents(), class_name);
        int separators = getBucketsNumberFromString(buckets_number, events_size) - 1;
        if (separators < 1) {
            separators = 1;
        }
        LinkedList<String> values = new LinkedList<String>();
        if (mode == Mode.HISTOGRAM) {
            discretizeHistographically(in, class_name, separators, values);
        } else {
            discretizeSimillarBucketsSize(events_size, in, class_name, separators, values);
        }
        ClassDescriptor cd = new ClassDescriptor(class_name, values);
        return new ContinuousClassFilter().filter(in, cd);
    }

    public static int getBucketsNumberFromString(String buckets_number, int size) {
        int result;
        String str = buckets_number.toLowerCase();
        if (NumericUtil.isInteger(str)) {
            result = NumericUtil.parseIntegerDefault(str, 0);
        } else {
            if (str.contains("log")) {
                result = (int) Math.log(size) + 1;
            } else {
                result = (int) Math.sqrt(size);
            }
        }
        return result;
    }

    private void discretizeHistographically(Input in, String class_name, int separators, LinkedList<String> values) throws RuntimeException {
        Double min = Double.MAX_VALUE;
        Double max = Double.MIN_VALUE;
        for (Map<String, Object> event : in.getEvents()) {
            String stringvalue = event.get(class_name).toString();
            if (!NumericUtil.isWildcard(stringvalue)) {
                double doublevalue = NumericUtil.tryParse(stringvalue);
                assertNumeric(doublevalue);
                if (doublevalue < min) {
                    min = doublevalue;
                }
                if (doublevalue > max) {
                    max = doublevalue;
                }
            }
        }
        for (int i = 0; i < separators; ++i) {
            double proportional = (i + 1) * (max - min) / (separators + 1) + min;
            values.add(Double.valueOf(proportional).toString());
        }
    }

    private void discretizeSimillarBucketsSize(int events_size, Input in, String class_name, int separators, LinkedList<String> values) throws RuntimeException {
        ArrayList<Double> all_values = new ArrayList<Double>(events_size);
        for (Map<String, Object> event : in.getEvents()) {
            String stringvalue = event.get(class_name).toString();
            if (!NumericUtil.isWildcard(stringvalue)) {
                double doublevalue = NumericUtil.tryParse(stringvalue);
                assertNumeric(doublevalue);
                all_values.add(doublevalue);
            }
        }
        Collections.sort(all_values);
        for (int i = 0; i < separators; ++i) {
            double proportional = all_values.get((i + 1) * (events_size - 1) / (separators + 1)+1);
            values.add(Double.valueOf(proportional).toString());
        }
    }
    
    private int countRealSize(List<Map<String, Object>> events, String class_name) {
        int count = 0;
        for (Map<String, Object> event : events) {
            String stringvalue = event.get(class_name).toString();
            if (!NumericUtil.isWildcard(stringvalue)) {
                ++count;
            }
        }
        return count;
    }
}
