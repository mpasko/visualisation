/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import agh.aq21gui.exceptions.InputDataError;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Domain;
import agh.aq21gui.model.input.DomainsGroup;
import agh.aq21gui.model.input.Event;
import agh.aq21gui.model.input.EventsGroup;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Run;
import agh.aq21gui.model.input.Test;
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

    private Test generateRun(Attribute attr, PredictedDomain dom) {
        Run r = new Run();
        r.setName("c1");
        r.addParameter("mode", "tf");
        String condition;
        if (dom.type == DomainType.CONTINUOUS || dom.type == DomainType.INTEGER){
            condition = String.format(Locale.US,"[%s<=%f]", attr.getname(), dom.getMean());
        } else {
            condition = String.format("[%s=*]", attr.getname());
        }
        r.addParameter("consequent", condition);
        r.addParameter("ambiguity", "includeinpos");
        r.addParameter("trim", "optimal");
        r.addParameter("compute_alternative_covers", "true");
        r.addParameter("maxstar", "1");
        r.addParameter("maxrule", "10");
        return r;
    }

    private static boolean isWildcard(String value) {
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

    public static boolean isInteger(String value) {
        if (isWildcard(value)){
            return true;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isNumber(String value) {
        if (isWildcard(value)){
            return true;
        }
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean allItemsStrings(String[] events) {
        boolean allStrings=true;
        int i = 0;
        while ((i<events.length) && allStrings){
            allStrings &= !isNumber(events[i]);
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

    private enum DomainType {

        CONTINUOUS("continuous"),
        INTEGER("integer"),
        LINEAR("linear"),
        NOMINAL("nominal");
        public String value;

        private DomainType(String value) {
            this.value = value;
        }
    }

    private class PredictedDomain {

        public DomainType type;
        public List<String> values;
        public Double max;
        public Double min;
        public int number;

        public PredictedDomain(int id) {
            number = id;
            type = DomainType.INTEGER;
            values = new LinkedList<String>();
            max = Double.MIN_VALUE;
            min = Double.MAX_VALUE;
        }

        public void VerifyNew(String value) {
            switch (type) {
                case INTEGER:
                    if (!isInteger(value)) {
                        type = DomainType.CONTINUOUS;
                    } else if (!isNumber(value)) {
                        type = DomainType.NOMINAL;
                    }
                case CONTINUOUS:
                    if (!isNumber(value)) {
                        type = DomainType.NOMINAL;
                    }
                    break;
                case NOMINAL:
                    break;
            }
            if (!isWildcard(value)) {
                if (isNumber(value)) {
                    Double val = Double.parseDouble(value);
                    if (val > max) {
                        max = val;
                    }
                    if (val < min) {
                        min = val;
                    }
                }
                if (!values.contains(value)) {
                    values.add(value);
                }
            }
        }

        public Domain generate() {
            Domain dom = new Domain();
            dom.setdomain(type.value);
            dom.setname("domain" + number);
            switch (type) {
                case INTEGER:
                case CONTINUOUS:
                    dom.setRange(min, max);
                    break;
                case NOMINAL:
                    if (this.values.size()<2){
                        this.values.add("undefined");
                    }
                    dom.setRange(this.values);
                    break;
            }
            return dom;
        }
        
        public double getMean() {
            return (min + max)/2.0;
        }
    }

    public static String dewebify(String in) {
        System.out.println(in);
        String nls = in.replace("\\n", "\n");
        nls = nls.replace("\\r", "\r");
        nls = nls.replace("\"", "");
        System.out.println("After dewebify:");
        System.out.println(nls);
        return nls;
    }

    public LinkedList<PredictedDomain> predictDomains(EventsGroup events, List<String> offeredNames) {
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

        EventsGroup eventsGroup = new EventsGroup();
        List<String> offeredNames = new LinkedList<String>();
        String[] lines = nls.split("\\n");
        for (String line : lines) {
            String[] events = line.split(",");
            if (!events[0].isEmpty()) {
                for (int i = 0; i < events.length; i++) {
                    if (CSVConverter.isNumber(events[i]) && events[i].startsWith(".")) {
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
        input.sEG(eventsGroup);
        input.sDomainsGroup(domains);
        input.sAttributesGroup(attributes);
        List<Test> runs = new LinkedList<Test>();
        final Attribute classAttr = attributes.attributes.get(attributes.attributes.size() - 1);
        runs.add(generateRun(classAttr, columns.getLast()));
        input.runsGroup.setRuns(runs);
        return input;
    }
}
