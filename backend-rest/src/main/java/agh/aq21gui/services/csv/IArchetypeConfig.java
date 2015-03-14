/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.csv;

import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.EventsGroup;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Test;
import java.util.List;

/**
 *
 * @author marcin
 */
public abstract class IArchetypeConfig {
    public abstract List<Test> createConfig(
            AttributesGroup attributes, 
            DomainsGroup classDomain, 
            EventsGroup events
            );

    public List<Test> createConfig(Input in) {
        AttributesGroup attributes = in.gAG();
        DomainsGroup domains = in.gDG();
        EventsGroup eventsGroup = in.gEG();
        return createConfig(attributes, domains, eventsGroup);
    }

    protected String recalculateMean(EventsGroup events, int collumn) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Event e : events.events) {
            final String cell = e.getValues().get(collumn);
            double value = Double.parseDouble(cell);
            if (value > max) {
                max = value;
            }
            if (value < min) {
                min = value;
            }
        }
        return Double.valueOf((min + max) / 2).toString();
    }
}
