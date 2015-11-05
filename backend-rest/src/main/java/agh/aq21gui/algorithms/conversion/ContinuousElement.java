/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.algorithms.conversion;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class ContinuousElement extends RangeElement {

    private Double value;

    ContinuousElement(String value, String comparator) {
        try {
            this.value = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            String message = "Error creating range boundary for type continuous. Number format parse exception. v={0}";
            Logger.getLogger("GLD").log(Level.SEVERE, message, value);
        }

        this.setComparator(comparator);
    }

    @Override
    public int minus(RangeElement other) {
        if (other instanceof ContinuousElement) {
            ContinuousElement next = (ContinuousElement) other;
            int cmp = value.compareTo(next.value);
            if (cmp == 0) {
                return classify(this) - classify(next);
            } else {
                return cmp;
            }
        } else {
            throw new RuntimeException("Case is unsupported!");
        }
    }

    @Override
    public String getValue() {
        return value.toString();
    }
}
