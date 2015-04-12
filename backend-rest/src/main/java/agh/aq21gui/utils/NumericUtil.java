/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

/**
 *
 * @author marcin
 */
public class NumericUtil {

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
    
}
