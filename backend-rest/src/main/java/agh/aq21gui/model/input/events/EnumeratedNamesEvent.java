/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input.events;

import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Event;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class EnumeratedNamesEvent implements EventAdapter{
	
	private static Long enumerator;
	
	static{
		enumerator = new Long(0);
	}

	@Override
	public List<Event> loadEvents(List<Map<String, Object>> events, AttributesGroup attrs) {
		LinkedList<Event> product = new LinkedList<Event>();
		if(events==null){
			throw new RuntimeException("Error! events in EventsGroup.loadEvents is null");
			//Logger.getLogger("JAXB").severe();
			//return null;
		}
		for(Map<String, Object> event : events){
			product.add(new Event(mapToList(event, attrs)));
		}
		return product;
	}

	@Override
	public List<Map<String, Object>> formatEvents(AttributesGroup attributesGroup, List<Event> events) {
		LinkedList<Map<String, Object>> workingList = new LinkedList<Map<String, Object>>();
		for(Event event : events){
			workingList.add(formatEvent(attributesGroup,event.getValues()));
		}
		return workingList;
	}
	
	private String generateAttrName(int number){
		StringBuilder builder = new StringBuilder("attribute");
		builder.append(number);
		return builder.toString();
	}
	
	TreeMap<String, Object> formatEvent(AttributesGroup attributesGroup, List<String> values) {
		TreeMap<String, Object> workingMap = new TreeMap<String, Object>();
		workingMap.put("id", enumerator);
		enumerator++;
		int counter = 1;
		for (String value:values){
			workingMap.put(generateAttrName(counter), value);
			counter++;
		}
		return workingMap;
	}
	
	private List<String> mapToList(Map<String, Object> event, AttributesGroup attributes){
		LinkedList<String> values = new LinkedList<String>();
		for(Object value : event.values()){
			values.add(value.toString());
		}
		return values;
	}
}
