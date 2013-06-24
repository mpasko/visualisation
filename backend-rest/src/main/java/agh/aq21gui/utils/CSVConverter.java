/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.utils;

import agh.aq21gui.model.input.EventsGroup;

/**
 *
 * @author marcin
 */
public class CSVConverter {
	
	public static String dewebify(String in){
		System.out.println(in);		
		String nls = in.replace("\\n", "\n");
		nls = nls.replace("\\r", "\r");
		nls = nls.replace("\"", "");
		System.out.println("After dewebify:");
		System.out.println(nls);		
		return nls;
	}
	
	public EventsGroup convert(String csv){

		String nls = dewebify(csv);
		nls = nls.replace(" ","");
		nls = nls.replaceAll("\t","");
		
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
