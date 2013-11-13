/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.model.input.events.EnumeratedNamesEvent;
import agh.aq21gui.model.input.events.EventAdapter;
import agh.aq21gui.model.input.events.NameValueEvent;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.TreeNode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlTransient
public class EventsGroup implements IAQ21Serializable {
	public List<Event> events;
	String LABEL = "Events";
	private EventAdapter adapter;
	
	public EventsGroup(){
		adapter = new EnumeratedNamesEvent();
		events = new LinkedList<Event>();
	}
	
	@Override
	public String toString(){
		if(events.isEmpty()){
			return "";
		}
//		System.out.println("first event is");
//		System.out.println(events.get(0).toString());
		StringBuilder builder = FormatterUtil.begin(LABEL);
		FormatterUtil.appendAll(builder, events, 1);
		return FormatterUtil.terminate(builder);
	}

	public void addEvent(String... values) {
		Event e = new Event();
		e.getValues().addAll(Arrays.asList(values));
		events.add(e);
	}

	public void parseEvents(TreeNode treeNode) {
		for(TreeNode eventNode: treeNode.iterator(TParser.ROW)){
			Event event = new Event();
			event.parseRow(eventNode);
			events.add(event);
		}
	}

	public List<Map<String, Object>> formatEvents(AttributesGroup attributesGroup) {
		return adapter.formatEvents(attributesGroup, events);
	}

	public void loadEvents(List<Map<String, Object>> events, AttributesGroup attrs) {
		this.events = adapter.loadEvents(events, attrs);
	}

	void updateEvents(List<Event> events, AttributesGroup attrs) {
		this.events = new LinkedList<Event>(events);
	}

	List<Event> retrieveEvents(AttributesGroup attrs) {
		return this.events;
	}

	void traverse() {
		for(Event e : this.events){
			e.traverse();
		}
	}
}