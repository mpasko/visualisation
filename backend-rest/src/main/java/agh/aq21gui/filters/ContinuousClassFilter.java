/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.utils.NumericUtil;
import agh.aq21gui.utils.Util;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class ContinuousClassFilter {

    private final static String DOMAIN_POSTFIX = "_triggered";
    public static final String STRING_UNSUPPORTED = "ClassDescriptor with list only supported for continuous valuses";

    private static void assertNotStringValue(double parsedValue) throws RuntimeException {
        if (Double.isNaN(parsedValue)) {
            throw new RuntimeException(STRING_UNSUPPORTED);
        }
    }

    public ContinuousClassFilter() {
    }

    public Input filter(Input in, ClassDescriptor cd) {
        Input result = Util.deepCopyInput(in);
        int index = in.gAG().getIndexOfAttribute(cd.getName());
        if (cd.getSet_elements().size() > 0) {
            LinkedList<String> labels = prepareLabels(cd.getSet_elements(), "<");
            result = filter(result, cd, labels);
        } else {
            LinkedList<String> labels = prepareLabels(Util.strings(cd.getValue()), cd.getComparator());
            setupNewDomain(labels, result, cd.getName());
            result = processEventsSingle(result, index, cd, labels.get(0), labels.get(1));
        }
        return result;
    }

    public Input filter(Input result, ClassDescriptor cd, List<String> labels) {
        if ((labels == null) || (labels.size() < cd.set_elements.size()-1)) {
            labels = prepareLabels(cd.getSet_elements(), "<");
        }
        setupNewDomain(labels, result, cd.getName());
        int index = result.gAG().getIndexOfAttribute(cd.getName());
        result = processEventsMultiple(result, index, cd, labels);
        return result;
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
        String to_return = "";
        if (comparator.equals("<")) {
            to_return = "less";
        } else if (comparator.equals(">")) {
            to_return = "above";
        } else if (comparator.equals("=")) {
            to_return = "equal";
        } else if (comparator.equals("!")) {
            to_return = "not";
        } else {
            //System.out.println("unrecognized: "+comparator);
            to_return = "unrelated_with";
        }
        return to_return;
    }

    private Input processEventsSingle(Input result, int index, ClassDescriptor cd, String match, String not_match) {
        for (Event event : result.obtainEventsGroup().events) {
            String currentValue = event.getValues().get(index);
            if (!NumericUtil.isWildcard(currentValue)) {
                if (cd.matchesValue(currentValue)) {
                    event.replaceValueAt(index, match);
                } else {
                    event.replaceValueAt(index, not_match);
                }
            }
        }
        return result;
    }

    private Input processEventsMultiple(Input result, int index, ClassDescriptor cd, List<String> labels) {
        for (Event event : result.obtainEventsGroup().events) {
            String currentValue = event.getValues().get(index);
            if (!NumericUtil.isWildcard(currentValue)) {
                double doubleValue = NumericUtil.tryParse(currentValue);
                assertNotStringValue(doubleValue);
                int number = determineWhichRangeMatches(doubleValue, cd.set_elements);
                event.replaceValueAt(index, labels.get(number));
            }
        }
        return result;
    }

    private String varbalizeValue(String value) {
        String val = value;
        val = val.replaceAll("\\.", "dot");
        val = val.replaceAll(",", "coma");
        val = val.replaceAll("-", "minus");
        val = val.replaceAll("\\{|\\}", "");
        return val;
    }

    private void appendToLastItem(LinkedList<String> labels, String match) {
        if (labels.isEmpty()){
            labels.offerLast(match);
        } else {
            StringBuilder new_last = new StringBuilder();
            new_last.append(labels.pollLast());
            new_last.append("_and_");
            new_last.append(match);
            labels.offerLast(new_last.toString());
        }
    }

    public static int determineWhichRangeMatches(double doubleValue, List<String> set_elements) {
        int cnt = 0;
        Iterator<String> iterator = set_elements.iterator();
        while(iterator.hasNext() && __helper_function_with_weird_name(doubleValue, iterator)){
            cnt++;
        }
        return cnt;
    }

    private static boolean __helper_function_with_weird_name(double doubleValue, Iterator<String> iterator) {
        double parsedValue = NumericUtil.tryParse(iterator.next());
        assertNotStringValue(parsedValue);
        return doubleValue>=parsedValue;
    }

    private LinkedList<String> prepareLabels(List<String> stringValues, String comparator) throws RuntimeException {
        LinkedList<String> labels = new LinkedList<String>();
        for (String stringValue : stringValues) {
            double doubleValue = NumericUtil.tryParse(stringValue);
            assertNotStringValue(doubleValue);
            final String negatedComparator = Util.negatedComparator(comparator);
            String match = verbalize(comparator) + varbalizeValue(stringValue);
            String not_match = verbalize(negatedComparator) + varbalizeValue(stringValue);
            appendToLastItem(labels, match);
            labels.offerLast(not_match);
        }
        return labels;
    }

    private void setupNewDomain(List<String> labels, Input result, String name) {
        final Domain triggered = new Domain(name+DOMAIN_POSTFIX, "linear");
        triggered.set_elements = labels;
        result.obtainDomainsGroup().domains.add(triggered);
        result.gAG().replaceAttributeDomain(name, triggered);
    }
}
