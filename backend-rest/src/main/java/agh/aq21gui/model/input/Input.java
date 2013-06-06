/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Input {
	
	public RunsGroup runsGroup;
	private AttributesGroup attributesGroup;
	private DomainsGroup domainsGroup;
	private EventsGroup eventsGroup;
	
	public Input(){
		attributesGroup = new AttributesGroup();
		domainsGroup = new DomainsGroup();
		eventsGroup = new EventsGroup();
		runsGroup = new RunsGroup();
	}
	
	public AttributesGroup gAG(){
		return attributesGroup;
	}
	
	@XmlElement(name="attributes")
	public void setAttributes(List<Attribute> attributes){
		attributesGroup.attributes=attributes;
	}
	
	public List<Attribute> getAttributes(){
		return attributesGroup.attributes;
	}
	
	@XmlElement(name="domains")
	public void setDomains(List<Domain> domains){
		domainsGroup.domains=domains;
	}
	
	public List<Domain> getDomains(){
		return domainsGroup.domains;
	}
	
	@XmlElement(name="events")
	public void setEvents(List<Event> events){
		eventsGroup.events=events;
	}
	
	public List<Event> getEvents(){
		return eventsGroup.events;
	}
	
	@XmlElement(name="runsGroup")
	public void setRunsGroup(RunsGroup events){
		runsGroup=events;
	}
	
	public RunsGroup getRunsGroup(){
		return runsGroup;
	}
	
	public void addDomain(String name, String subdomain, String parameters){
		Domain d = new Domain();
		d.name = name;
		d.subdomain = subdomain;
		d.parameters = parameters;
		this.domainsGroup.domains.add(d);
	}
	
	public void addAttribute(String name, String domain, String parameters){
		Attribute a = new Attribute();
		a.name = name;
		a.domain = domain;
		a.parameters = parameters;
		this.attributesGroup.attributes.add(a);
	}
	
	public void addEvent(String... values){
		eventsGroup.addEvent(values);
	}

	public void sEG(EventsGroup group) {
		this.eventsGroup = group;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		
		builder.append(domainsGroup.toString());
		builder.append(attributesGroup.toString());
		builder.append(runsGroup.toString());
		builder.append(eventsGroup.toString());
		
		return builder.toString();
	}
}
