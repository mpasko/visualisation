/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.model.output.Hypothesis;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author marcin
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Input implements IAQ21Serializable {

    public RunsGroup runsGroup;
    private AttributesGroup attributesGroup;
    private DomainsGroup domainsGroup;
    private EventsGroup eventsGroup;
    private TestingEventsGroup testingEventsGroup;
    public TestsGroup testsGroup;
    private InputHypotheses inputHypotheses;
    private boolean attributesLoaded = false;
    private boolean eventsLoaded = false;
    private List<Map<String, Object>> eventsBackup;

    public Input() {
        attributesGroup = new AttributesGroup();
        domainsGroup = new DomainsGroup();
        eventsGroup = new EventsGroup();
        runsGroup = new RunsGroup();
        testsGroup = new TestsGroup();
        testingEventsGroup = new TestingEventsGroup();
        inputHypotheses = new InputHypotheses();
    }

    public Input(Input in) {
        attributesGroup = in.attributesGroup;
        domainsGroup = in.domainsGroup;
        eventsGroup = in.eventsGroup;
        runsGroup = in.runsGroup;
        testsGroup = in.testsGroup;
        testingEventsGroup = in.testingEventsGroup;
        inputHypotheses = in.inputHypotheses;
    }

    @XmlElement(name = "id")
    public long getId() {
        return 0;
    }

    public void setId(long id) {
        //this.dbid = id;
    }

    public AttributesGroup gAG() {
        return attributesGroup;
    }

    @XmlElement(name = "outputHypotheses")
    public void setOutputHypotheses(List<Hypothesis> in) {
        //simple do nothing
    }

    public List<Hypothesis> getOutputHypotheses() {
        return null;
    }

    @XmlElement(name = "attributes")
    public void setAttributes(List<Attribute> attributes) {
        attributesGroup.attributes = attributes;
        if (eventsLoaded) {
            eventsGroup.loadEvents(eventsBackup, this.attributesGroup);
            eventsBackup = null;
        } else {
            this.attributesLoaded = true;
        }
    }

    public List<Attribute> getAttributes() {
        return attributesGroup.attributes;
    }

    @XmlElement(name = "domains")
    public void setDomains(List<Domain> domains) {
        domainsGroup.domains = domains;
    }

    public List<Domain> getDomains() {
        return domainsGroup.domains;
    }

    @XmlElement(name = "events")
    public void setEvents(List<Map<String, Object>> events) {
        if (attributesLoaded) {
            eventsGroup.loadEvents(events, this.attributesGroup);
        } else {
            this.eventsBackup = events;
            this.eventsLoaded = true;
        }
    }

    public List<Map<String, Object>> getEvents() {
        return eventsGroup.formatEvents(this.attributesGroup);
    }
    
    public Object obtainCell(int row, String col) {
        String column = col.toLowerCase(Locale.US);
        return this.getEvents().get(row).get(column);
    }

    @XmlElement(name = "testingEvents")
    public void setTestingEvents(List<Map<String, Object>> events) {
        testingEventsGroup.loadEvents(events, this.attributesGroup);
    }

    public List<Map<String, Object>> getTestingEvents() {
        return testingEventsGroup.formatEvents(this.attributesGroup);
    }

    @XmlElement(name = "runsGroup")
    public void setRunsGroup(RunsGroup runs) {
        runsGroup = runs;
    }

    public RunsGroup getRunsGroup() {
        return runsGroup;
    }

    @XmlElement(name = "testsGroup")
    public void setTestsGroup(TestsGroup tests) {
        testsGroup = tests;
    }

    public TestsGroup getTestsGroup() {
        return testsGroup;
    }

    public void addDomain(String name, String subdomain, String parameters) {
        Domain d = new Domain();
        d.setname(name.toLowerCase());
        d.setdomain(subdomain.toLowerCase());
        d.setparameters(parameters.toLowerCase());
        this.domainsGroup.domains.add(d);
    }

    public void addAttribute(String name, String domain, String parameters) {
        Attribute a = new Attribute();
        a.setname(name.toLowerCase());
        a.setdomain(domain.toLowerCase());
        a.setparameters(parameters.toLowerCase());
        this.attributesGroup.attributes.add(a);
    }

    public void addEvent(String... values) {
        eventsGroup.addEvent(values);
    }

    public void replaceEventsGroup(EventsGroup group) {
        this.eventsGroup = group;
    }

    public EventsGroup obtainEventsGroup() {
        return this.eventsGroup;
    }

    public void replaceTestingEventsGroup(TestingEventsGroup group) {
        this.testingEventsGroup = group;
    }

    public void replaceDomainsGroup(DomainsGroup domains) {
        this.domainsGroup = domains;
    }

    public DomainsGroup obtainDomainsGroup() {
        return domainsGroup;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(domainsGroup.toString());
        builder.append(attributesGroup.toString());
        builder.append(runsGroup.toString());
        builder.append(testsGroup.toString());
        builder.append(inputHypotheses.toString());
        builder.append(eventsGroup.toString());
        builder.append(testingEventsGroup.toString());

        return builder.toString();
    }

    protected InputHypotheses obtainInputHypho() {
        return this.inputHypotheses;
    }

    protected void replaceOutputHypho(InputHypotheses inHypho) {
        this.inputHypotheses = inHypho;
    }

    public void traverse() {
        this.attributesGroup.traverse();
        this.domainsGroup.traverse();
        this.eventsGroup.traverse();
        this.runsGroup.traverse();
        this.testsGroup.traverse();
        this.inputHypotheses.traverse();
        this.testingEventsGroup.traverse();
    }

    public void replaceAttributesGroup(AttributesGroup attributes) {
        this.attributesGroup = attributes;
    }

    public int findAttributeNumber(String className) {
        int serial = 0;
        int found = -1;
        for (Attribute aq21_attr : this.getAttributes()) {
            if (aq21_attr.name.equalsIgnoreCase(className)) {
                found = serial;
            }
            serial++;
        }
        return found;
    }

    public Map<String, Object> generateKeyValue(Event event) {
        return eventsGroup.formatEvent(event, attributesGroup);
    }

    public int countEvents() {
        return this.eventsGroup.events.size();
    }

    @Deprecated
    @JsonIgnore
    public ClassDescriptor getAggregatedClassDescriptor() {
        //TODO do it for all class descriptors obtained
        Test run = this.runsGroup.getRuns().get(0);
        return run.grepClassDescriptor();
    }

    public Domain findDomainObjectRecursively(String name) {
        String currentName = name;
        Attribute attr = this.findAttribute(currentName);
        Domain found = null;
        if (attr!=null) {
            if (attr.isTerminal()) {
                found = attr;
            }
            currentName = attr.getdomain();
        }
        if (found==null){
            found = this.obtainDomainsGroup().findDomainObjectRecursively(currentName);
        }
        return found;
    }

    public Attribute findAttribute(String name) {
        return this.attributesGroup.getAttributeByName(name);
    }

    public LinkedList<String> getCollumnOfData(String claz) {
        LinkedList<String> items = new LinkedList<String>();
        for (int i = 0; i < getEvents().size(); ++i) {
            String stringValue = (String) this.obtainCell(i, claz);
            items.add(stringValue);
        }
        return items;
    }
}
