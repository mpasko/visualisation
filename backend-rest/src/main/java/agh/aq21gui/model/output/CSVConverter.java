/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.model.output;

import agh.aq21gui.model.input.EventsGroup;

/**
 *
 * @author marcin
 */
public class CSVConverter {
	
	public EventsGroup convert(String csv){
		System.out.println(csv);
		String nls = csv.replace("\\n", "\n");
		nls = nls.replace("\"", "");
		nls = nls.replace(" ","");
		nls = nls.replaceAll("\t","");
		
		System.out.println("After:");
		System.out.println(nls);
		EventsGroup group = new EventsGroup();
		String[] lines = nls.split("\\n");
		for (String line : lines){
			String[] events = line.split(",");
			if(!events[0].isEmpty()){
				group.addEvent(events);
			}
		}
		return group;
	}
}
