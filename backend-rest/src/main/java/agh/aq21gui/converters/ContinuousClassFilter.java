/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.converters;

import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.services.aq21.OutputParser;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Util;
import java.util.LinkedList;

/**
 *
 * @author marcin
 */
public class ContinuousClassFilter {
    private final static String DOMAIN_NAME = "triggered";
    
    public ContinuousClassFilter() {
    }
    
    public Input filter(Input in, ClassDescriptor cd) {
        Input result = Util.deepCopyInput(in);
        String name = cd.getName();
        final String stringValue = cd.getValue();
        double doubleValue = NumericUtil.tryParse(stringValue);
        if (Double.isNaN(doubleValue)) {
            return in;
        }
        String comparator = cd.getComparator();
        int index = in.gAG().getIndexOfAttribute(name);
        final String negatedComparator = Util.negatedComparator(comparator);
        String match = verbalize(comparator)+varbalizeValue(stringValue);
        String not_match = verbalize(negatedComparator)+varbalizeValue(stringValue);
        System.out.println(match);
        System.out.println(not_match);
        System.out.println(comparator);
        System.out.println(negatedComparator);
        Domain triggered = prepareDomain(match, not_match);
        result.gDG().domains.add(triggered);
        result.gAG().replaceAttributeDomain(name, triggered);
        result = processEvents(result, index, comparator, doubleValue, cd, match, not_match);
        return result;
    }

    private Domain prepareDomain(String match, String not_match) {
        final Domain triggered = new Domain(DOMAIN_NAME,"nominal");
        triggered.set_elements = Util.strings(match,not_match);
        return triggered;
    }

    private String verbalize(String comparator) {
        final char[] charArray = comparator.toCharArray();
        //int i = 0;
        StringBuilder builder = new StringBuilder();
        for (char ch : charArray) {
            builder.append(verbalizeSingle(new String(new char[]{ch})));
            builder.append("_");
            //++i;
        }
        return builder.toString();
    }

    private String verbalizeSingle(String comparator) {
        if (comparator.equals("<")) {
            return "less";
        } else if (comparator.equals(">")) {
            return "above";
        } else if (comparator.equals("=")) {
            return "equal";
        } else if (comparator.equals("!")) {
            return "not";
        } else {
            System.out.println("unrecognized: "+comparator);
            return "unrelated_with";
        }
    }

    private Input processEvents(Input result, int index, String comparator, double trigger, ClassDescriptor cd, String match, String not_match) {
        for (Event event : result.gEG().events) {
            String currentValue = event.getValues().get(index);
            if (!NumericUtil.isWildcard(currentValue)){
                if (cd.matchesValue(currentValue)) {
                    event.replaceValueAt(index, match);
                } else {
                    event.replaceValueAt(index, not_match);
                }
            }
        }
        return result;
    }

    private String varbalizeValue(String value) {
        String val = value.toString();
        val = val.replaceAll("\\.", "dot");
        val = val.replaceAll(",", "coma");
        val = val.replaceAll("-", "minus");
        val = val.replaceAll("\\{|\\}", "");
        return val;
    }
}
