/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.input.events;

import agh.aq21gui.model.input.AttributesGroup;
import agh.aq21gui.model.input.Event;
import java.util.List;
import java.util.Map;

/**
 *
 * @author marcin
 */
public interface EventAdapter {
	List<Event> loadEvents(List<Map<String, Object>> events, AttributesGroup attrs);
	List<Map<String, Object>> formatEvents(AttributesGroup attributesGroup, List<Event> events);
}
