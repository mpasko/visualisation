/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.csv;

import agh.aq21gui.exceptions.InputDataError;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.EventsGroup;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.utils.NumericUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author marcin
 */
public class CSVConverter {

    private boolean allItemsStrings(String[] events) {
        boolean allStrings=true;
        int i = 0;
        while ((i<events.length) && allStrings){
            allStrings &= !NumericUtil.isNumber(events[i]);
            ++i;
        }
        return allStrings;
    }

    private boolean allItemsUnique(String[] events) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Object obj = new Object();
        for (String evt : events) {
            if (map.containsKey(evt)){
                return false;
            }
            map.put(evt, obj);
        }
        return true;
    }

    private DomainsGroup generateDomains(LinkedList<PredictedDomain> columns) {
        DomainsGroup domains = new DomainsGroup();
        for (PredictedDomain col : columns) {
            domains.domains.add(col.generate());
        }
        return domains;
    }

    

    public static String dewebify(String in) {
//        System.out.println(in);
        String nls = in.replace("\\n", "\n");
        nls = nls.replace("\\r", "\r");
        nls = nls.replace("\"", "");
//        System.out.println("After dewebify:");
//        System.out.println(nls);
        return nls;
    }

    LinkedList<PredictedDomain> predictDomains(EventsGroup events, List<String> offeredNames) {
        Event event0 = events.events.get(0);
        int len = event0.getValues().size();
        LinkedList<PredictedDomain> columns = new LinkedList<PredictedDomain>();
        for (int i = 1; i <= len; ++i) {
            columns.add(new PredictedDomain(i));
        }
        for (Event e : events.events) {
            for (int i = 0; i < len; ++i) {
                columns.get(i).VerifyNew(e.getValues().get(i));
            }
        }
        return columns;
    }

    public AttributesGroup predictAttributes(DomainsGroup domains, List<String> offeredNames) {
        AttributesGroup attributes = new AttributesGroup();
        int i = 1;
        for (Domain d : domains.domains) {
            Attribute attr = new Attribute();
            if (offeredNames.isEmpty()){
                attr.setname("attribute" + i);
            } else {
                if (offeredNames.size()<domains.domains.size()) {
                    throw new InputDataError("CSV Header is too short!");
                }
                attr.setname(offeredNames.get(i-1));
            }
            attr.setdomain(d.getname());
            attributes.attributes.add(attr);
            ++i;
        }
        return attributes;
    }

    public Input convert(String csv) {
        String nls = dewebify(csv);
        nls = nls.replace(" ", "");
        nls = nls.replaceAll("\t", "");
        nls = nls.replaceAll("\r", "");
        nls = nls.toLowerCase(Locale.getDefault());

        EventsGroup eventsGroup = new EventsGroup();
        List<String> offeredNames = new LinkedList<String>();
        String[] lines = nls.split("\\n");
        for (String line : lines) {
            String[] events = line.split(",");
            if (!events[0].isEmpty()) {
                for (int i = 0; i < events.length; i++) {
                    if (NumericUtil.isNumber(events[i]) && events[i].startsWith(".")) {
                        events[i] = "0" + events[i];
                    }
                }
                if (eventsGroup.events.isEmpty() && offeredNames.isEmpty()) {
                    if (allItemsStrings(events)&&allItemsUnique(events)) {
                        offeredNames.addAll(Arrays.asList(events));
                    } else {
                        eventsGroup.addEvent(events);
                    }
                } else {
                    eventsGroup.addEvent(events);
                }
            }
        }
        LinkedList<PredictedDomain> columns = predictDomains(eventsGroup,offeredNames);
        DomainsGroup domains = generateDomains(columns);
        AttributesGroup attributes = predictAttributes(domains,offeredNames);
        Input input = new Input();
        input.replaceEventsGroup(eventsGroup);
        input.replaceDomainsGroup(domains);
        input.replaceAttributesGroup(attributes);
        return input;
    }
}
