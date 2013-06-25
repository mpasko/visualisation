/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input;

import agh.aq21gui.aq21grammar.TParser;
import agh.aq21gui.utils.FormatterUtil;
import agh.aq21gui.utils.TreeNode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marcin
 */
@XmlTransient
public class EventsGroup {
	public List<Event> events;
	String LABEL = "Events";
	
	public EventsGroup(){
		events = new LinkedList<Event>();
	}
	
	@Override
	public String toString(){
		if(events.isEmpty()){
			return "";
		}
		StringBuilder builder = FormatterUtil.begin(LABEL);
		FormatterUtil.appendAll(builder, events, 1);
		return FormatterUtil.terminate(builder);
	}

	public void addEvent(String... values) {
		Event e = new Event();
		e.values.addAll(Arrays.asList(values));
		events.add(e);
	}

	public void parseEvents(TreeNode treeNode) {
		for(TreeNode eventNode: treeNode.iterator(TParser.ROW)){
			Event event = new Event();
			event.parseRow(eventNode);
			events.add(event);
		}
	}
}