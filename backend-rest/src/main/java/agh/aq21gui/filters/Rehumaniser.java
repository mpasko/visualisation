/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.EventsGroup;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import agh.aq21gui.model.output.Output;
import agh.aq21gui.model.output.Rule;
import agh.aq21gui.model.output.Selector;
import java.util.LinkedList;

/**
 *
 * @author marcin
 */
public class Rehumaniser {
    public Output rehumaniseOutput(Output out) {
        for (Hypothesis hypo :  out.getOutputHypotheses()) {
            rehumaniseHypothesis(hypo);
        }
        EventsGroup eg = out.obtainEventsGroup();
        rehumaniseEvents(eg);
        return out;
    }

    public void rehumaniseHypothesis(Hypothesis hypo) {
        hypo.name = rehumaniseString(hypo.name);
        for (Rule rule : hypo.rules) {
            rehumaniseRule(rule);
        }
        for (ClassDescriptor claz : hypo.getClasses()) {
            rehumaniseSelector(claz);
        }
    }

    public void rehumaniseRule(Rule rule) {
        for (Selector selector : rule.getSelectors()) {
            rehumaniseSelector(selector);
        }
    }

    public String rehumaniseString(String name) {
        String result = name.replaceAll("([0-9]*?)dot([0-9]*?)", "$1.$2");
        result = result.replaceAll("above_equal_(.*?)_and_less_", "$1..");
        result = skipMiddleRanges(result);
        result = skipMiddleRanges(result);
        result = result.replaceAll("above_", ">");
        result = result.replaceAll("less_", "<");
        result = result.replaceAll("equal_", "=");
//        result = result.replaceAll("(.*?)\\.\\.>=(.*?)", ">=$1");
///        result = result.replaceAll("<(.*?)\\.\\.(.*?)", "<$2");
        return result;
    }

    private String skipMiddleRanges(String rangify) {
        return rangify.replaceAll("\\.\\.(.*?)\\.\\.", "..");
    }

    public void rehumaniseSelector(ClassDescriptor descriptor) {
        descriptor.name = rehumaniseString(descriptor.name);
        String rehumanisedValue = rehumaniseString(descriptor.getValue());
        if (descriptor.hasRange()) {
            descriptor.range_begin = rehumaniseString(descriptor.range_begin);
            descriptor.range_begin = descriptor.range_begin.replaceAll("(.*?)\\.\\.(.*+)", "$1");
            descriptor.range_end = rehumaniseString(descriptor.range_end);
            descriptor.range_end = descriptor.range_end.replaceAll("(.*?)\\.\\.(.*?)", "$2");
            if (descriptor.range_begin.startsWith("<")) {
                descriptor.comparator = "<";
                rehumanisedValue = descriptor.range_end;
                descriptor.setValue(rehumanisedValue);
            }
            if (descriptor.range_end.startsWith(">")) {
                descriptor.comparator = ">=";
                rehumanisedValue = descriptor.range_begin;
                descriptor.setValue(rehumanisedValue);
            }
        }
        if (descriptor.hasSet()) {
            LinkedList<String> newSet = new LinkedList<String>();
            for (String elem : descriptor.set_elements) {
                newSet.add(rehumaniseString(elem));
            }
            descriptor.set_elements = newSet;
        }
        if (!(descriptor.hasRange() || descriptor.hasSet())) {
            rehumanisedValue = unpackComparator(rehumanisedValue, "<=", descriptor);
            rehumanisedValue = unpackComparator(rehumanisedValue, ">=", descriptor);
            rehumanisedValue = unpackComparator(rehumanisedValue, "<", descriptor);
            rehumanisedValue = unpackComparator(rehumanisedValue, ">", descriptor);
            descriptor.setValue(rehumanisedValue);
        }
    }

    private String unpackComparator(String rehumanisedValue, String testedAgainst, ClassDescriptor descriptor) {
        if (rehumanisedValue.startsWith(testedAgainst)) {
            descriptor.comparator = testedAgainst;
            rehumanisedValue = rehumanisedValue.replaceAll(testedAgainst, "");
        }
        return rehumanisedValue;
    }

    private void rehumaniseEvent(Event event) {
        LinkedList<String> newValues = new LinkedList<String>();
        for (String val : event.getValues()) {
            newValues.add(rehumaniseString(val));
        }
        event.setValues(newValues);
    }

    public void rehumaniseEvents(EventsGroup eg) {
        for (Event event : eg.events) {
            rehumaniseEvent(event);
        }
    }
}
