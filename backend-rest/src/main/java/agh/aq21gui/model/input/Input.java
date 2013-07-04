/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.model.output.Hypothesis;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marcin
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Input {
	private long id=0;
	
	public RunsGroup runsGroup;
	private AttributesGroup attributesGroup;
	private DomainsGroup domainsGroup;
	private EventsGroup eventsGroup;
	private TestingEventsGroup testingEventsGroup;
	public TestsGroup testsGroup;
	private InputHypotheses inputHypotheses;
	
	public Input(){
		attributesGroup = new AttributesGroup();
		domainsGroup = new DomainsGroup();
		eventsGroup = new EventsGroup();
		runsGroup = new RunsGroup();
		testsGroup = new TestsGroup();
		testingEventsGroup = new TestingEventsGroup();
		inputHypotheses = new InputHypotheses();
	}
	
	public long getid(){
		return id;
	}
	
	public void setid(long id){
		this.id = id;
	}
	
	public AttributesGroup gAG(){
		return attributesGroup;
	}
	
	@XmlElement(name="outputHypotheses")
	public void setOutputHypotheses(List<Hypothesis> in){
		//simple do nothing
	}
	
	public List<Hypothesis> getOutputHypotheses(){
		return null;
	}
	
	@XmlElement(name="attributes")
	public void setattributes(List<Attribute> attributes){
		attributesGroup.attributes=attributes;
	}
	
	public List<Attribute> getattributes(){
		return attributesGroup.attributes;
	}
	
	@XmlElement(name="domains")
	public void setdomains(List<Domain> domains){
		domainsGroup.domains=domains;
	}
	
	public List<Domain> getdomains(){
		return domainsGroup.domains;
	}
	
	@XmlElement(name="events")
	public void setevents(List<Event> events){
		eventsGroup.events=events;
	}
	
	public List<Event> getevents(){
		return eventsGroup.events;
	}
	
	@XmlElement(name="testingEvents")
	public void setTestingEvents(List<Event> events){
		testingEventsGroup.events=events;
	}
	
	public List<Event> getTestingEvents(){
		return testingEventsGroup.events;
	}
	
	@XmlElement(name="runsGroup")
	public void setRunsGroup(RunsGroup runs){
		runsGroup=runs;
	}
	
	public RunsGroup getRunsGroup(){
		return runsGroup;
	}
	
	@XmlElement(name="testsGroup")
	public void setTestsGroup(TestsGroup tests){
		testsGroup=tests;
	}
	
	public TestsGroup getTestsGroup(){
		return testsGroup;
	}
	
	public void addDomain(String name, String subdomain, String parameters){
		Domain d = new Domain();
		d.setname(name);
		d.setdomain(subdomain);
		d.setparameters(parameters);
		this.domainsGroup.domains.add(d);
	}
	
	public void addAttribute(String name, String domain, String parameters){
		Attribute a = new Attribute();
		a.setname(name);
		a.setdomain(domain);
		a.setparameters(parameters);
		this.attributesGroup.attributes.add(a);
	}
	
	public void addEvent(String... values){
		eventsGroup.addEvent(values);
	}

	public void sEG(EventsGroup group) {
		this.eventsGroup = group;
	}
	
	public void sTEG(TestingEventsGroup group) {
		this.testingEventsGroup = group;
	}
	
	public void sDomainsGroup(DomainsGroup domains){
		this.domainsGroup = domains;
	}

	protected void sAttributesGroup(AttributesGroup attributes) {
		this.attributesGroup = attributes;
	}
	
	@Override
	public String toString(){
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

	protected InputHypotheses gInputHypho() {
		return this.inputHypotheses;
	}

	protected void sOutputHypho(InputHypotheses inHypho) {
		this.inputHypotheses = inHypho;
	}
}
