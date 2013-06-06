/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlTransient
public class EventsGroup {
	public List<Event> events;
	
	public EventsGroup(){
		events = new LinkedList<Event>();
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("Events\n{\n");
		for(Event attribute : events){
			builder.append(attribute.toString());
		}
		builder.append("}\n");
		return builder.toString();
	}

	public void addEvent(String... values) {
		Event e = new Event();
		for (String item : values) {
			e.values.add(item);
		}
		events.add(e);
	}
}