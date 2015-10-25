/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class NumericUtil {
    public static final double EPSILON = 1.0E-6;

    public static String stringValueOf(final double val) {
        return Double.valueOf(val).toString();
    }

    public static double tryParse(final String cell) {
        try {
            return Double.parseDouble(cell);
        } catch (NumberFormatException ex) {
            //Logger.getLogger(this.getClass().getName()).info(ex.getMessage());
        }
        return Double.NaN;
    }

    public static boolean isInteger(String value) {
        if (isWildcard(value)) {
            return true;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isWildcard(String value) {
        if (value.equalsIgnoreCase("?")) {
            return true;
        } else if (value.equalsIgnoreCase("*")) {
            return true;
        } else if (value.equalsIgnoreCase("NA")) {
            return true;
        } else if (value.equalsIgnoreCase("N/A")) {
            return true;
        }
        return false;
    }

    public static boolean isNumber(String value) {
        if (isWildcard(value)) {
            return true;
        }
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean compareDoubles(double value, double val1) {
        return Math.abs(value - val1) < NumericUtil.EPSILON;
    }

    public static String formatNumber(Integer number) {
        final String text = number.toString();
        int desiredLength = 3;
        String textToFill = "0";
        return FormatterUtil.alignString(desiredLength, textToFill, text);
    }

    public static int parseIntegerDefault(String param, int defaultValue) {
        String val = param;
        val = val.replaceAll("\"", "");
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException _) {
            return defaultValue;
        }
    }
    public static double parseDoubleDefault(String param, double defaultValue) {
        String val = param;
        val = val.replaceAll("\"", "");
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException _) {
            return defaultValue;
        }
    }

    public static List<String> dobleListToStringList(List<Double> values) {
        LinkedList<String> result = new LinkedList<String>();
        for (Double value : values) {
            result.add(value.toString());
        }
        return result;
    }
    
    public static List<Double> intListToDoubleList(List<Integer> values) {
        LinkedList<Double> result = new LinkedList<Double>();
        for (Integer value : values) {
            result.add(value.doubleValue());
        }
        return result;
    }

    public static List<Double> stringListToDoubleList(List<String> values) {
        LinkedList<Double> result = new LinkedList<Double>();
        for (String value : values) {
            result.add(tryParse(value));
        }
        return result;
    }
}
