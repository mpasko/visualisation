/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.filters;

import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.utils.Util;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class IllegalValueRemover {
    public Input remove(Input in, String attribute, String value) {
        Input result = Util.deepCopyInput(in);
        List<Event> current = result.obtainEventsGroup().events;
        LinkedList<Event> processedEvents = new LinkedList<Event>();
        for (Event event : current) {
            int num = in.findAttributeNumber(attribute);
            if (!event.getValues().get(num).equalsIgnoreCase(value)) {
                processedEvents.add(event);
            }
        }
        result.obtainEventsGroup().events = processedEvents;
        return result;
    }
}
